package com.devbrackets.android.androidmarkup.parser;

/**
 * Holds the values associated with specific types of spans
 * for the {@link MarkupParser}.  If you need to implement custom
 * spans, use values after {@link #MAX_SPAN_TYPE}
 */
public interface SpanType {
    int BOLD = 1;
    int ITALIC = 2;
    int ORDERED_LIST = 3;
    int UNORDERED_LIST = 4;
    int MAX_SPAN_TYPE = UNORDERED_LIST;
}
