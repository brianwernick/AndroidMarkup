package com.devbrackets.android.androidmarkup.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.devbrackets.android.androidmarkup.parser.core.MarkupParser
import com.devbrackets.android.androidmarkup.parser.html.HtmlParser

class MarkupTextView : AppCompatTextView {
    var markupParser: MarkupParser = HtmlParser()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    fun setMarkup(markup: String) {
        text = markupParser.toSpanned(markup)
    }
}
