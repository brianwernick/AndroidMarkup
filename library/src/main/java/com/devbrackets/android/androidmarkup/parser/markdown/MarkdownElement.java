package com.devbrackets.android.androidmarkup.parser.markdown;

import android.support.annotation.Nullable;

/**
 *
 */
public class MarkdownElement {

    @Nullable
    protected MarkdownElement parent;
    protected MarkdownElement[] children;
    protected int spanType; //see SpanType.java? probably a different parsing type
    protected String text;

    public MarkdownElement(@Nullable MarkdownElement parent, int spanType, String text) {
        this.parent = parent;
        this.spanType = spanType;
        this.text = text;
    }
}
