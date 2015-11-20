package com.devbrackets.android.androidmarkup.parser;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * An abstract base class that supports the Markup editing in the
 * {@link com.devbrackets.android.androidmarkup.widget.MarkupEditText}
 *
 * For the simplification of examples the pipe character "|" will represent selection points,
 * the underscore character "_" will represent the current span, and a the asterisk character "*"
 * will represent any characters between the span endpoints and the selection points.
 */
public abstract class MarkupParser {
    public enum SpanType {
        BOLD,
        ITALIC,
        ORDERED_LIST,
        UNORDERED_LIST
    }

    /**
     * Converts the specified markup text in to a Spanned
     * for use in the {@link com.devbrackets.android.androidmarkup.widget.MarkupEditText}
     *
     * @param text The markup text to convert to a spanned
     * @return The resulting spanned
     */
    public abstract Spanned toSpanned(String text);

    /**
     * Converts the specified spanned in to the corresponding markup.  The outputs from
     * this and {@link #toSpanned(String)} should be interchangeable.
     *
     * @param spanned The Spanned to convert to markup
     * @return The markup representing the Spanned
     */
    public abstract String fromSpanned(Spanned spanned);

    public boolean updateSpan(Spannable spannable, SpanType spanType, int startIndex, int endIndex) {
        switch (spanType) {
            case BOLD:
                style(spannable, startIndex, endIndex, Typeface.BOLD);
                return true;

            case ITALIC:
                style(spannable, startIndex, endIndex, Typeface.ITALIC);
                return true;

            case ORDERED_LIST:
                orderedList(spannable, startIndex, endIndex);
                return true;

            case UNORDERED_LIST:
                unOrderedList(spannable, startIndex, endIndex);
                return true;
        }

        return false;
    }

    protected void style(Spannable spannable, int start, int end, int style) {
        List<StyleSpan> overlappingSpans = getOverlappingStyleSpans(spannable, start, end, style);

        boolean modifiedSpan = false;
        for (StyleSpan span : overlappingSpans) {
            int spanStart = spannable.getSpanStart(span);
            int spanEnd = spannable.getSpanEnd(span);

            if (spanStart == start && spanEnd == end) {
                modifiedSpan = true;
                spannable.removeSpan(span);
                continue;
            }

            //TODO: other cases (partial matches, etc.)
            modifiedSpan |= handleSpanStartBeforeSelection(spannable, span, start, end);
//            modifiedSpan |= updateSpanStart(spannable, span, start, end);
        }

        if (!modifiedSpan) {
            spannable.setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * If the specified Span starts before or equal to the selection, then we need to update the span end
     * only if the span ending is less than the <code>selectionEnd</code>.  If the span ending is
     * greater than or equal to the <code>selectionEnd</code> then the selected area will have the style
     * removed.
     * <p>
     * The cases that need to be handled below are:
     * <ol>
     *     <li>
     *          The selection start is contained within or equal to the span start and the selection end goes beyond the
     *          span end.  (e.g. __|___***| will result in __|______| or |___***| will result in |______|)
     *     </li>
     *     <li>
     *          The selection start is equal to the span start and the span end is contained within the
     *          span.  (e.g. |______|__ will result in |******|__)
     *     </li>
     *     <li>
     *          Both the selection start and end are contained within the span.
     *          (e.g. __|______|__ will result in __|******|__)
     *     </li>
     *     <li>
     *          The selection start is contained within the span and the selection end is equal to the
     *          span end.  (e.g. __|______| will result in __|******|)
     *     </li>
     * </ol>
     */
    protected boolean handleSpanStartBeforeSelection(Spannable spannable, Object span, int selectionStart, int selectionEnd) {
        int spanStart = spannable.getSpanStart(span);
        int spanEnd = spannable.getSpanEnd(span);
        if (spanStart > selectionStart) {
            return false;
        }

        //Handles the first case listed above
        if (spanEnd < selectionEnd) {
            spannable.removeSpan(span);
            spannable.setSpan(span, spanStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return true;
        }

        //Handles the second case listed above
        if (selectionStart == spanStart && spanEnd > selectionEnd) {
            spannable.removeSpan(span);
            spannable.setSpan(span, selectionEnd, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return true;
        }

        //Handles the third case listed above
        if (spanEnd > selectionEnd) {
            //TODO: we need to split the span
            return true;
        }

        //Handles the final case listed above
        spannable.removeSpan(span);
        spannable.setSpan(span, spanStart, selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return true;
    }

    /**
     * If the specified Span ends after the <code>end</code>, then we need to update the start
     * only if the span start is greater than <code>start</code>.  If the span start is
     * less than or equal to the <code>start</code> then the selected area will have the style
     * removed.
     */
    protected boolean updateSpanStart(Spannable spannable, Object span, int start, int end) {
        int spanStart = spannable.getSpanStart(span);
        int spanEnd = spannable.getSpanEnd(span);

        if (spanEnd >= end) {
            if (spanEnd > end) {
                //Update the end of the span (keeping the styling)
                spannable.removeSpan(span);
                spannable.setSpan(span, start, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                //Remove the styling
                spannable.removeSpan(span);
                spannable.setSpan(span, end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            return true;
        }

        return false;
    }

    protected void orderedList(Spannable spannable, int start, int end) {
        //todo
    }

    protected void unOrderedList(Spannable spannable, int start, int end) {
        //todo
    }

    protected List<StyleSpan> getOverlappingStyleSpans(Spannable spannable, int start, int end, int style) {
        List<StyleSpan> spans = new LinkedList<>(Arrays.asList(spannable.getSpans(start, end, StyleSpan.class)));

        //Filters out the non-matching types
        Iterator<StyleSpan> iterator = spans.iterator();
        while (iterator.hasNext()) {
            StyleSpan span = iterator.next();
            if (span.getStyle() != style) {
                iterator.remove();
            }
        }

        return spans;
    }
}
