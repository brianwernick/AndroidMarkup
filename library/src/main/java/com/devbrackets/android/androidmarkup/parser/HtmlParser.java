package com.devbrackets.android.androidmarkup.parser;

import android.text.Html;
import android.text.Spanned;

/**
 * A MarkupParser for the Html protocol
 */
public class HtmlParser extends MarkupParser {
    @Override
    public Spanned toSpanned(String text) {
        return Html.fromHtml(text);
    }

    @Override
    public String fromSpanned(Spanned spanned) {
        return Html.toHtml(spanned);
    }
}
