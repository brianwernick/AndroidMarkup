package com.devbrackets.android.androidmarkup.parser.markdown

/**
 * `text` is always null except when the element has no children.  If an element  has children
 * the un-styled text should be a child to simplify ordering
 */
class MarkdownElement(var parent: MarkdownElement?) {
    var text: String? = null
    var spanType: Int = MarkdownSpanType.UNKNOWN
    var children: MutableList<MarkdownElement> = mutableListOf()

    constructor(parent: MarkdownElement?, spanType: Int, text: String?) : this(parent) {
        this.spanType = spanType
        this.text = text
    }

    constructor(parent: MarkdownElement?, spanType: Int, text: String?, children : MutableList<MarkdownElement>) : this(parent, spanType, text) {
        this.children = children
    }

    fun addChild(child: MarkdownElement) {
        child.parent = this
        children.add(child)
    }
}
