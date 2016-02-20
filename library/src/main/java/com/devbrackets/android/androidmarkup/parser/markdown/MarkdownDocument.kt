package com.devbrackets.android.androidmarkup.parser.markdown

import android.graphics.Typeface
import android.text.Spanned
import android.text.SpannedString
import android.text.style.StyleSpan
import com.devbrackets.android.androidmarkup.text.style.ListSpan
import java.util.*

class MarkdownDocument {
    val rootElement: MarkdownElement = MarkdownElement(null)

    constructor(markdown: String) {
        //TODO
    }

    constructor(spanned: Spanned) {
        var spans = findRelevantSpans(spanned, ReverseIndexSpanComparator(spanned))
        parseSpanned(spanned, null, spans, 0, spanned.length -1, rootElement)
    }

    fun toMarkdown(): String {
        val builder: StringBuilder = StringBuilder()
        toMarkdown(rootElement, builder)

        return builder.reverse().toString()
    }

    fun toSpanned(): Spanned {
        return SpannedString("")
    }

    //Inclusive indexes, parse moving from start to end (e.g. 0 to 100)
    protected fun parseSpanned(spanned: Spanned, currentSpan: Any?, spans: List<Any>, startIndex: Int, endIndex: Int, parent: MarkdownElement) {
        var workingElement = MarkdownElement(parent)
        currentSpan?.let { workingElement.spanType = getSpanType(currentSpan) }
        parent.addChild(workingElement)

        //Iterate through the range looking for collisions
        for (i in startIndex .. endIndex) {
            var collisionSpans = findSpansForIndex(i, spans)
            if (collisionSpans.isEmpty()) {
                workingElement.text = "${workingElement.text.orEmpty()}${spanned.substring(i, i)}"
                continue
            }

            //TODO: if there is only 1 collision span then recurse down to add that
            if (collisionSpans.size == 1) {
                //TODO: we probably need to return the end index to update i (also, clear and add a new working element?)
                parseSpanned(spanned, collisionSpans[0], listOf(), i, spanned.getSpanEnd(collisionSpans[0]), parent)
            } else {

            }

            //TODO: since there was a collision, we will need to create a new workingElement
            //TODO: what do we do when there are multiple collisions (... find the parent container and recurse over that range)
        }
    }

    protected fun findSpansForIndex(index: Int, spans: List<Any>) : MutableList<Any> {
        //TODO
        return mutableListOf(spans)
    }

//    /**
//     * @return The last index in `spans` used.  If this is a leaf node, the return value will be the same as `position`
//     */
//    protected fun populateElements(spanned: Spanned, spans: List<Any>, position: Int, parent: MarkdownElement): Int {
//        var span: Any = spans[position]
//        var spanStart = spanned.getSpanStart(span)
//
//        //If this span is a leaf, then add the element and return
//        var hasChildren = position < spans.size - 1 && spanned.getSpanEnd(spans[position + 1]) > spanStart
//        if (!hasChildren) {
//            var element = MarkdownElement(parent, getSpanType(span), getSpanText(spanned, span))
//            parent.addChild(element)
//            return position
//        }
//
//        //If the span has children then we need to split it in to corresponding elements
//        var element = MarkdownElement(parent, getSpanType(span), null)
//        parent.addChild(element)
//
//        return populateChildrenElements(spanned, spans, position, element, spanStart)
//    }
//
//    //TODO: characters not contained in a child span are removed (this is also a problem at the top level)
//    protected fun populateChildrenElements(spanned: Spanned, spans: List<Any>, position: Int, parent: MarkdownElement, parentStart: Int): Int {
//        var workingPosition = position + 1
//
//        while (workingPosition < spans.size) {
//            var child = spans[workingPosition]
//            var childEnd = spanned.getSpanEnd(child)
//
//            //If the child ends before or at the same spot the span starts then we have reached the end of this pass
//            if (parentStart >= childEnd) {
//                break
//            }
//
//            workingPosition = populateElements(spanned, spans, workingPosition, parent) + 1
//        }
//
//        return workingPosition
//    }

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

    /**
     * Finds all spans that are used for Markdown processing, filtering out
     * unused spans such as those used by the [EditText] for marking selection
     */
    protected fun findRelevantSpans(spanned: Spanned, comparator: Comparator<Any>): List<Any> {
        val spans = LinkedList(Arrays.asList(*spanned.getSpans(0, spanned.length, Any::class.java)))
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

    protected fun getSpanTag(element: MarkdownElement): String {
        when (element.spanType) {
            MarkdownSpanType.BOLD -> return BOLD_TAG
            MarkdownSpanType.ITALIC -> return ITALICS_TAG
            //TODO: lists
        }

        return ""
    }

    protected fun getSpanType(span: Any): Int {
        when (span) {
            is StyleSpan -> return if (span.style == Typeface.BOLD) MarkdownSpanType.BOLD else MarkdownSpanType.ITALIC
            is ListSpan -> return if (span.type == ListSpan.Type.BULLET) MarkdownSpanType.UNORDERED_LIST else MarkdownSpanType.ORDERED_LIST
            else -> return MarkdownSpanType.TEXT
        }
    }

    protected fun getSpanText(spanned: Spanned, span: Any): String {
        var spanEnd = spanned.getSpanEnd(span)
        var spanStart = spanned.getSpanStart(span)

        return escapeString(spanned.substring(spanStart, spanEnd))
    }

    protected fun escapeString(unescapedString: String): String {
        //TODO: don't forget to escape non-MD characters (e.g. * in text should be \*)
        return unescapedString
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