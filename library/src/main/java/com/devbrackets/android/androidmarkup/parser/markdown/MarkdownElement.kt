package com.devbrackets.android.androidmarkup.parser.markdown

/**
 * `text` is always null except when the element has no children.  If an element  has children
 * the un-styled text should be a child to simplify ordering
 */
class MarkdownElement(var parent: MarkdownElement?, var spanType: Int, var text: String?) {
    lateinit var children: MutableList<MarkdownElement>

    init {
        children = mutableListOf();
    }

    constructor(parent: MarkdownElement?, spanType: Int, text: String?, children : MutableList<MarkdownElement>) : this(parent, spanType, text) {
        this.children = children
    }

    fun addChild(child: MarkdownElement) {
        child.parent = this
        children.add(child)
    }
}
