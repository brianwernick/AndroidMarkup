package com.devbrackets.android.androidmarkup.parser

import android.text.Html
import android.text.Spanned

/**
 * A MarkupParser for the Html protocol
 */
class HtmlParser : MarkupParser() {
    override fun toSpanned(text: String): Spanned {
        return Html.fromHtml(text)
    }

    override fun fromSpanned(spanned: Spanned): String {
        return Html.toHtml(spanned)
    }
}
