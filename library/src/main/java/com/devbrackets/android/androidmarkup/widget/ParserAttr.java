package com.devbrackets.android.androidmarkup.widget;

/**
 * An enum that matches the xml attribute values
 */
public enum ParserAttr {
    HTML,
    MARKDOWN;

    static ParserAttr get(int ordinal) {
        return values()[ordinal];
    }
}
