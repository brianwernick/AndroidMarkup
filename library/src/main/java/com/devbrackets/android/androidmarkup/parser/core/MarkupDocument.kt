package com.devbrackets.android.androidmarkup.parser.core

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import com.devbrackets.android.androidmarkup.text.style.ListSpan
import java.util.*

open class MarkupDocument() {
    protected val rootElement: MarkupElement = MarkupElement(null)

    constructor(spanned: Spanned) : this() {
        var spans = findRelevantSpans(spanned, IndexSpanComparator(spanned))
        parseSpanned(spanned, null, spans, 0, spanned.length -1, rootElement)
        Log.d("MarkupDocument", "end");
    }

    //Inclusive indexes, parse moving from start to end (e.g. 0 to 100)
    protected fun parseSpanned(spanned: Spanned, currentSpan: Any?, spans: List<Any>, startIndex: Int, endIndex: Int, parent: MarkupElement) : Int {
        var workingElement = MarkupElement(parent) //TODO always adding a workingElement isn't correct since it may never be used (e.g. **bold** won't use it)
        currentSpan?.let { workingElement.spanType = getSpanType(currentSpan) }
        parent.addChild(workingElement)

        //Iterate through the range looking for collisions
        var index = startIndex -1
        while (++index < endIndex) {
            //Finds any spans that contain the current index, if none exists append the character at the index
            var collisionSpans = findSpansForIndex(index, spanned, spans)
            if (collisionSpans.isEmpty()) {
                workingElement.text = "${workingElement.text.orEmpty()}${spanned.substring(index, index+1)}"
                continue
            }

            //If there is only a single span collision, use that span as the currentSpan and recurse
            if (collisionSpans.size == 1) {
                index = parseSpanned(spanned, collisionSpans[0], listOf(), index, spanned.getSpanEnd(collisionSpans[0]), parent)
                continue
            }

            //If there are multiple span collisions, find the containing span and use that as the 'currentSpan' argument
            var containingSpan = findContainingSpan(spanned, collisionSpans)
            collisionSpans.remove(containingSpan)
            index = parseSpanned(spanned, containingSpan, collisionSpans, index, spanned.getSpanEnd(containingSpan), parent)
        }

        return index
    }

    /**
     * Finds the span that contains all other spans in the `spans`
     */
    protected fun findContainingSpan(spanned: Spanned, spans: List<Any>) : Any {
        var currentContainer = spans[0]
        var currentSpanStart = spanned.getSpanStart(currentContainer)
        var currentSpanEnd = spanned.getSpanEnd(currentContainer)

        for (i in 1 .. spans.size -1) {
            if (spanned.getSpanStart(spans[i]) <= currentSpanStart && spanned.getSpanEnd(spans[i]) > currentSpanEnd) {
                currentContainer = spans[i]
                currentSpanStart = spanned.getSpanStart(currentContainer)
                currentSpanEnd = spanned.getSpanEnd(currentContainer)
            }
        }

        return currentContainer
    }

    protected fun findSpansForIndex(index: Int, spanned: Spanned, spans: List<Any>) : MutableList<Any> {
        var collisionSpans = mutableListOf<Any>()
        for (span in spans) {
            if (spanned.getSpanStart(span) <= index && spanned.getSpanEnd(span) >= index) {
                collisionSpans.add(span)
            }
        }

        return collisionSpans
    }

    /**
     * Finds all spans that are used for Markup processing, filtering out
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

    protected fun getSpanType(span: Any): Int {
        when (span) {
            is StyleSpan -> return if (span.style == Typeface.BOLD) SpanType.BOLD else SpanType.ITALIC
            is ListSpan -> return if (span.type == ListSpan.Type.BULLET) SpanType.UNORDERED_LIST else SpanType.ORDERED_LIST
            else -> return SpanType.TEXT
        }
    }

    /**
     * Used to sort the spans so that the span the ends last (greatest value)
     * will be listed first
     */
    protected class IndexSpanComparator(private val spanned: Spanned) : Comparator<Any> {
        override fun compare(lhs: Any, rhs: Any): Int {
            return spanned.getSpanEnd(lhs) - spanned.getSpanEnd(rhs)
        }
    }
}