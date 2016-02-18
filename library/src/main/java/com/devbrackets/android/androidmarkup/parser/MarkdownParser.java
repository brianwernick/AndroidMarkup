package com.devbrackets.android.androidmarkup.parser;

import android.graphics.Typeface;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.StyleSpan;

import com.devbrackets.android.androidmarkup.parser.markdown.MarkdownDocument;
import com.devbrackets.android.androidmarkup.text.style.ListSpan;

import java.util.Comparator;

/**
 * A MarkupParser for the Markdown protocol.
 * NOTE: currently this only supports a small subset of the format
 */
public class MarkdownParser extends MarkupParser {
    @Override
    public Spanned toSpanned(String text) {
        return new SpannedString(text);
    }

    @Override
    public String fromSpanned(Spanned spanned) {
        if (spanned == null) {
            return "";
        }

//        List<Object> spans = getAllSpans(spanned, new ReverseIndexSpanComparator(spanned));
//        if (spans.isEmpty()) {
//            return spanned.toString();
//        }

        //TODO
        MarkdownDocument document = new MarkdownDocument(spanned);

        return spanned.toString();
    }

    protected String foo() {
        StringBuilder out = new StringBuilder();

        return out.toString();
    }

    protected void handleSpanEnd(Spanned spanned, Object span) {
        switch (determineSpanType(span)) {
            case SpanType.BOLD:
                break;

            case SpanType.ITALIC:
                break;

            case SpanType.ORDERED_LIST:
                break;

            case SpanType.UNORDERED_LIST:
                break;

            default:
            case SpanType.UNKNOWN:
                //Do nothing
        }
    }

    protected int determineSpanType(Object span) {
        if (span instanceof StyleSpan) {
            int style = ((StyleSpan)span).getStyle();
            if (style == Typeface.BOLD) {
                return SpanType.BOLD;
            } else if (style == Typeface.ITALIC) {
                return SpanType.ITALIC;
            }
        } else if (span instanceof ListSpan) {
            ListSpan.Type type = ((ListSpan) span).getType();
            return  type == ListSpan.Type.BULLET ? SpanType.UNORDERED_LIST : SpanType.ORDERED_LIST;
        }

        return SpanType.UNKNOWN;
    }

    protected static class ReverseIndexSpanComparator implements Comparator<Object> {
        private Spanned spanned;

        public ReverseIndexSpanComparator(Spanned spanned) {
            this.spanned = spanned;
        }

        @Override
        public int compare(Object lhs, Object rhs) {
            return spanned.getSpanEnd(rhs) - spanned.getSpanEnd(lhs);
        }
    }
}
