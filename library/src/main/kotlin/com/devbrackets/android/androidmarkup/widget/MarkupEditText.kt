package com.devbrackets.android.androidmarkup.widget

import android.content.Context
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import com.devbrackets.android.androidmarkup.parser.core.MarkupParser
import com.devbrackets.android.androidmarkup.parser.core.SpanType
import com.devbrackets.android.androidmarkup.parser.html.HtmlParser

/**
 * A WYSIWYG EditText for Markup languages such as HTML or
 * Markdown.  This leaves the UI up to the implementing application.
 */
open class MarkupEditText : AppCompatEditText {
    var markupParser: MarkupParser = HtmlParser()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    open fun toggleBold() {
        markupParser.updateSpan(text, SpanType.BOLD, selectionStart, selectionEnd)
    }

    open fun toggleItalics() {
        markupParser.updateSpan(text, SpanType.ITALIC, selectionStart, selectionEnd)
    }

    open fun toggleOrderedList() {
        markupParser.updateSpan(text, SpanType.ORDERED_LIST, selectionStart, selectionEnd)
    }

    open fun toggleUnOrderedList() {
        markupParser.updateSpan(text, SpanType.UNORDERED_LIST, selectionStart, selectionEnd)
    }

    open fun getMarkup() : String {
        return markupParser.fromSpanned(text)
    }

    open fun setMarkup(markup: String) {
        setText(markupParser.toSpanned(markup))
    }
}
