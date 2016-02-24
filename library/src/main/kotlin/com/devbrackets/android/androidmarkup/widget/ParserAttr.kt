package com.devbrackets.android.androidmarkup.widget

/**
 * An enum that matches the xml attribute values
 * TODO: should we still have this?  Probably not
 */
enum class ParserAttr {
    HTML,
    MARKDOWN;

    companion object {
        fun get(ordinal: Int): ParserAttr {
            return values()[ordinal]
        }
    }
}
