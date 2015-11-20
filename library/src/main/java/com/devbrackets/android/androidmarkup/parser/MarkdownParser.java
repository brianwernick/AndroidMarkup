package com.devbrackets.android.androidmarkup.parser;

import android.text.Spanned;
import android.text.SpannedString;

/**
 * A MarkupParser for the Markdown protocol.
 * NOTE: currently this only supports a small subset of the format
 */
public class MarkdownParser extends MarkupParser {
    @Override
    public Spanned toSpanned(String text) {
        return new SpannedString(text);
    }

    @Override
    public String fromSpanned(Spanned spanned) {
        return spanned.toString();
    }
}
