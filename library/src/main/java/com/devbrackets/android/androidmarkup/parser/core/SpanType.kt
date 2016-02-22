package com.devbrackets.android.androidmarkup.parser.core

/**
 * Holds the values associated with specific types of spans
 * for the [MarkupParser].  If you need to implement custom
 * spans, use values after [.MAX_SPAN_TYPE]
 */
object SpanType {
    const val UNKNOWN = 0
    const val TEXT = 1
    const val BOLD = 2
    const val ITALIC = 3
    const val ORDERED_LIST = 4
    const val UNORDERED_LIST = 5
    const val MAX_SPAN_TYPE = UNORDERED_LIST
}
