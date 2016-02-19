package com.devbrackets.android.androidmarkup.parser.html

import android.text.Html
import android.text.Spanned
import com.devbrackets.android.androidmarkup.parser.core.MarkupParser

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
