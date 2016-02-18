package com.devbrackets.android.androidmarkup.parser.markdown

class MarkdownElement(protected var parent: MarkdownElement?, protected var spanType: Int, protected var text: String) {
    protected lateinit var children: Array<MarkdownElement>
}
