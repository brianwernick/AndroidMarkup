package com.devbrackets.android.androidmarkup.parser.markdown;

import android.text.Spanned;

/**
 *
 */
public class MarkdownDocument {

    protected MarkdownElement[] elements;

    public MarkdownDocument(String markdown) {
        //TODO
    }

    public MarkdownDocument(Spanned spanned) {
        //TODO
    }

    public int getChildCount() {
        return elements.length;
    }
}
