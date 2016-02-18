package com.devbrackets.android.androidmarkup.parser

import android.graphics.Typeface
import android.text.Spanned
import android.text.SpannedString
import android.text.style.StyleSpan
import com.devbrackets.android.androidmarkup.parser.markdown.MarkdownDocument
import com.devbrackets.android.androidmarkup.text.style.ListSpan
import java.util.*

/**
 * A MarkupParser for the Markdown protocol.
 * NOTE: currently this only supports a small subset of the format
 */
class MarkdownParser : MarkupParser() {
    override fun toSpanned(text: String): Spanned {
        return SpannedString(text)
    }

    override fun fromSpanned(spanned: Spanned): String {
        if (spanned == null) {
            return ""
        }

        //        List<Object> spans = getAllSpans(spanned, new ReverseIndexSpanComparator(spanned));
        //        if (spans.isEmpty()) {
        //            return spanned.toString();
        //        }

        //TODO
        val document = MarkdownDocument(spanned)

        return spanned.toString()
    }

    protected fun foo(): String {
        val out = StringBuilder()

        return out.toString()
    }

    protected fun handleSpanEnd(spanned: Spanned, span: Any) {
        when (determineSpanType(span)) {
            SpanType.BOLD -> {
            }

            SpanType.ITALIC -> {
            }

            SpanType.ORDERED_LIST -> {
            }

            SpanType.UNORDERED_LIST -> {
            }
        }//Do nothing
    }

    protected fun determineSpanType(span: Any): Int {
        if (span is StyleSpan) {
            val style = span.style
            if (style == Typeface.BOLD) {
                return SpanType.BOLD
            } else if (style == Typeface.ITALIC) {
                return SpanType.ITALIC
            }
        } else if (span is ListSpan) {
            val type = span.type
            return if (type === ListSpan.Type.BULLET) SpanType.UNORDERED_LIST else SpanType.ORDERED_LIST
        }

        return SpanType.UNKNOWN
    }

    protected class ReverseIndexSpanComparator(private val spanned: Spanned) : Comparator<Any> {

        override fun compare(lhs: Any, rhs: Any): Int {
            return spanned.getSpanEnd(rhs) - spanned.getSpanEnd(lhs)
        }
    }
}
