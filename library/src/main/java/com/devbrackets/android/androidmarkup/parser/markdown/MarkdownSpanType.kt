package com.devbrackets.android.androidmarkup.parser.markdown

import com.devbrackets.android.androidmarkup.parser.core.SpanType

object MarkdownSpanType {
    const val UNKNOWN = SpanType.UNKNOWN
    const val BOLD = SpanType.BOLD
    const val ITALIC = SpanType.ITALIC
    const val ORDERED_LIST = SpanType.ORDERED_LIST
    const val UNORDERED_LIST = SpanType.UNORDERED_LIST
    const val TEXT = SpanType.MAX_SPAN_TYPE +1
    const val MAX_MARKDOWN_SPAN_TYPE = TEXT
}
