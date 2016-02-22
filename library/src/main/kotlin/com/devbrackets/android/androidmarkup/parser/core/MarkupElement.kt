package com.devbrackets.android.androidmarkup.parser.core

/**
 *
 */
class MarkupElement (var parent: MarkupElement?) {
    var text: String? = null
    var spanType: Int = SpanType.UNKNOWN
    var children: MutableList<MarkupElement> = mutableListOf()

    constructor(parent: MarkupElement?, spanType: Int, text: String?) : this(parent) {
        this.spanType = spanType
        this.text = text
    }

    constructor(parent: MarkupElement?, spanType: Int, text: String?, children : MutableList<MarkupElement>) : this(parent, spanType, text) {
        this.children = children
    }

    fun addChild(child: MarkupElement) {
        child.parent = this
        children.add(child)
    }
}