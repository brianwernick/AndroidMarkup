package com.devbrackets.android.androidmarkup.parser.markdown

import com.devbrackets.android.androidmarkup.parser.core.SpanType

object MarkdownSpanType {
    const val TEXT = SpanType.MAX_SPAN_TYPE +1
    const val BOLD = SpanType.BOLD
    const val ITALIC = SpanType.ITALIC
    const val ORDERED_LIST = SpanType.ORDERED_LIST
    const val UNORDERED_LIST = SpanType.UNORDERED_LIST
}
