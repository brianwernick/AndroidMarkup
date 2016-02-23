package com.devbrackets.android.androidmarkup.parser.core

class MarkupElement (var parent: MarkupElement?) {
    var text: String? = null
    var spanType: Int = SpanType.UNKNOWN
    var children: MutableList<MarkupElement> = mutableListOf()

    fun addChild(child: MarkupElement) {
        child.parent = this
        children.add(child)
    }
}