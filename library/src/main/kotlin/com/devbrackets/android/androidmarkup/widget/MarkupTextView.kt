package com.devbrackets.android.androidmarkup.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.devbrackets.android.androidmarkup.R
import com.devbrackets.android.androidmarkup.parser.core.MarkupParser
import com.devbrackets.android.androidmarkup.parser.html.HtmlParser
import com.devbrackets.android.androidmarkup.parser.markdown.MarkdownParser

class MarkupTextView : AppCompatTextView {
    lateinit var markupParser: MarkupParser

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    protected fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null || !readAttributes(context, attrs)) {
            markupParser = HtmlParser()
        }
    }

    fun setMarkup(markup: String) {
        text = markupParser.toSpanned(markup)
    }

    /**
     * Reads the attributes associated with this view, setting any values found

     * @param context The context to retrieve the styled attributes with
     * *
     * @param attrs The [AttributeSet] to retrieve the values from
     * *
     * @return True if the attributes were read
     */
    protected fun readAttributes(context: Context, attrs: AttributeSet): Boolean {
        if (isInEditMode) {
            return false
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarkupTextView) ?: return false

        //Updates the Parser
        setParser(ParserAttr.get(typedArray.getInteger(R.styleable.MarkupTextView_parser, 0)))

        typedArray.recycle()
        return true
    }

    protected fun setParser(parserAttr: ParserAttr) {
        when (parserAttr) {
            ParserAttr.HTML -> markupParser = HtmlParser()
            ParserAttr.MARKDOWN -> markupParser = MarkdownParser()
        }
    }
}
