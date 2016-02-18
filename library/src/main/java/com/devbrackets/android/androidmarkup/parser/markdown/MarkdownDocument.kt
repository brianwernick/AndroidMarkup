package com.devbrackets.android.androidmarkup.parser.markdown

import android.text.Spanned

class MarkdownDocument {
    lateinit var elements: Array<MarkdownElement>
    val childCount: Int
        get() = elements.size

    constructor(markdown: String) {
        //TODO
    }

    constructor(spanned: Spanned) {
        //TODO
    }

}
