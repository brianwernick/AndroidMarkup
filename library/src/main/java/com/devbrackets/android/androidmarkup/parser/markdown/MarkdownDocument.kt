package com.devbrackets.android.androidmarkup.parser.markdown

import android.graphics.Typeface
import android.text.Spanned
import android.text.SpannedString
import android.text.style.StyleSpan
import com.devbrackets.android.androidmarkup.text.style.ListSpan
import java.util.*

class MarkdownDocument {
    lateinit var elements: MutableList<MarkdownElement>

    constructor(markdown: String) {
        //TODO
    }

    constructor(spanned: Spanned) {
        var spans: List<Any> = findAllSpans(spanned, ReverseIndexSpanComparator(spanned))
        if (spans.isEmpty()) {
            elements = mutableListOf(MarkdownElement(null, MarkdownSpanType.TEXT, spanned.toString()))
            return
        }

        elements = mutableListOf()
        var position = 0
        while (position < spans.size) {
            position = populateElements(spanned, spans, position, null) +1
        }
    }

    fun toMarkdown() : String {
        //TODO: don't forget to escape non-MD characters (e.g. * in text should be \*)

        val builder: StringBuilder = StringBuilder()
        for (element in elements) {
            toMarkdown(element, builder)
        }

        //see https://github.com/Uncodin/bypass/blob/master/platform/android/library/src/in/uncod/android/bypass/ReverseSpannableStringBuilder.java
        return builder.reverse().toString() //TODO: this isn't right, we need a custom reverse builder (this will result in **tset** instead of **test**)
    }

    fun toSpanned(): Spanned {
        return SpannedString("")
    }

    /**
     * @return The last index in `spans` used.  If this is a leaf node, the retun value will be the same as `position`
     */
    protected fun populateElements(spanned: Spanned, spans: List<Any>, position: Int, parent: MarkdownElement?) : Int {
        var span: Any = spans[position]
        var spanStart = spanned.getSpanStart(span)

        //If this span is a leaf, then add the element and return
        var hasChildren = position < spans.size -1 && spanned.getSpanEnd(spans.get(position +1)) > spanStart
        if (!hasChildren) {
            var element = MarkdownElement(parent, getSpanType(span), getSpanText(spanned, span))
            parent?.addChild(element) ?: elements.add(element)
            return position
        }

        //If the span has children then we need to split it in to corresponding elements
        var element = MarkdownElement(parent, getSpanType(span), null)
        parent?.addChild(element) ?: elements.add(element)
        var workingPosition = position +1

        while (workingPosition < spans.size) {
            var child = spans[workingPosition]
            var childEnd = spanned.getSpanEnd(child)

            //If the child ends before or at the same spot the span starts then we have reached the end of this pass
            if (spanStart >= childEnd) {
                break;
            }

            //TODO: add a child element... does this work. (we are missing the text that isn't in the child spans)
            workingPosition = populateElements(spanned, spans, workingPosition, element) +1
        }

        return workingPosition
    }

    /**
     * NOTE: builds the string in reverse
     */
    protected fun toMarkdown(element: MarkdownElement, builder: StringBuilder) {
        //Appends the closing tag
        builder.append(getSpanTag(element))
        builder.append(element.text.orEmpty().reversed())

        for (child in element.children) {
            toMarkdown(child, builder)
        }

        //Appends the opening tag
        builder.append(getSpanTag(element))
    }

    protected fun findAllSpans(spanned: Spanned): MutableList<Any> {
        return LinkedList(Arrays.asList(*spanned.getSpans(0, spanned.length, Any::class.java)))
    }

    protected fun findAllSpans(spanned: Spanned, comparator: Comparator<Any>): List<Any> {
        val spans = findAllSpans(spanned)
        if (spans.isEmpty()) {
            return spans
        }

        //remove irrelevant span types
        var iterator = spans.iterator()
        while (iterator.hasNext()) {
            if (!supportedSpan(iterator.next())) {
                iterator.remove()
            }
        }

        Collections.sort(spans, comparator)
        return spans
    }

    protected fun supportedSpan(span: Any): Boolean {
        when (span) {
            is StyleSpan -> return true
            is ListSpan -> return true
            else -> return false
        }
    }

    protected fun getSpanTag(element: MarkdownElement) : String {
        when (element.spanType) {
            MarkdownSpanType.BOLD -> return BOLD_TAG
            MarkdownSpanType.ITALIC -> return ITALICS_TAG
            //TODO: lists
        }

        return ""
    }

    protected fun getSpanType(span: Any) : Int {
        when (span) {
            is StyleSpan -> return if (span.style == Typeface.BOLD) MarkdownSpanType.BOLD else MarkdownSpanType.ITALIC
            is ListSpan -> return if (span.type == ListSpan.Type.BULLET) MarkdownSpanType.UNORDERED_LIST else MarkdownSpanType.ORDERED_LIST
            else -> return MarkdownSpanType.TEXT
        }
    }

    protected fun getSpanText(spanned: Spanned, span: Any) : String {
        var spanEnd = spanned.getSpanEnd(span)
        var spanStart = spanned.getSpanStart(span)

        return spanned.substring(spanStart, spanEnd)
    }

    /**
     * Used to sort the spans so that the span the ends last (greatest value)
     * will be listed first
     */
    protected class ReverseIndexSpanComparator(private val spanned: Spanned) : Comparator<Any> {
        override fun compare(lhs: Any, rhs: Any): Int {
            return spanned.getSpanEnd(rhs) - spanned.getSpanEnd(lhs)
        }
    }

    companion object {
        const val BOLD_TAG: String = "**"
        const val ITALICS_TAG: String = "_"
    }
}
