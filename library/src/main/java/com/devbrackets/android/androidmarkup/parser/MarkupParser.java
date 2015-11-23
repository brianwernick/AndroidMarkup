package com.devbrackets.android.androidmarkup.parser;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
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

    protected void style(Spannable spannable, int selectionStart, int selectionEnd, int style) {
        List<StyleSpan> overlappingSpans = getOverlappingStyleSpans(spannable, selectionStart, selectionEnd, style);

        boolean modifiedSpan = false;
        for (StyleSpan span : overlappingSpans) {
            int spanStart = spannable.getSpanStart(span);
            int spanEnd = spannable.getSpanEnd(span);

            if (spanStart == selectionStart && spanEnd == selectionEnd) {
                modifiedSpan = true;
                spannable.removeSpan(span);
                continue;
            }

            modifiedSpan |= handleSpanStartBeforeSelection(spannable, span,spanStart, spanEnd, selectionStart, selectionEnd);
            modifiedSpan |= handleSpanStartAfterSelection(spannable, span, spanStart, spanEnd, selectionStart, selectionEnd);

            //TODO: optimize spans
        }

        if (!modifiedSpan) {
            spannable.setSpan(new StyleSpan(style), selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    protected void orderedList(Spannable spannable, int selectionStart, int selectionEnd) {
        //todo
    }

    protected void unOrderedList(Spannable spannable, int selectionStart, int selectionEnd) {
        //todo
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
    protected boolean handleSpanStartBeforeSelection(Spannable spannable, Object span, int spanStart, int spanEnd, int selectionStart, int selectionEnd) {
        if (spanStart > selectionStart) {
            //handled by handleSpanStartAfterSelection
            return false;
        }

        //Handles the first case listed above
        if (spanEnd < selectionEnd) {
            spannable.setSpan(span, spanStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return true;
        }

        //Handles the second case listed above
        if (selectionStart == spanStart && spanEnd > selectionEnd) {
            spannable.setSpan(span, selectionEnd, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return true;
        }

        //Handles the third case listed above
        if (spanEnd > selectionEnd) {
            spannable.setSpan(span, spanStart, selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            Object duplicate = duplicateSpan(span);
            if (duplicate != null) {
                spannable.setSpan(duplicate, selectionEnd, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            return true;
        }

        //Handles the final case listed above
        spannable.setSpan(span, spanStart, selectionStart, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return true;
    }

    /**
     * If the specified Span starts after the <code>selectionStart</code>, then we need to update the span start
     * to the selection.  Additionally, if the Span ends before the <code>selectionEnd</code>, we need to
     * update the span end as well.
     * <p>
     * The cases that need to be handled below are:
     * <ol>
     *      <li>
     *          The selection start is before the <code>spanStart</code> and the <code>selectionEnd</code>
     *          is after the span end. (e.g. |***___***| will result in |_________|)
     *      </li>
     *      <li>
     *          The selection start is before the <code>spanStart</code> and the <code>selectionEnd</code>
     *          is before or equal to the span end. (e.g. (|***___| will result in |______| or |***___|___
     *          will result in |______|___)
     *      </li>
     * </ol>
     */
    protected boolean handleSpanStartAfterSelection(Spannable spannable, Object span, int spanStart, int spanEnd, int selectionStart, int selectionEnd) {
        if (spanStart <= selectionStart) {
            //handled by handleSpanStartBeforeSelection
            return false;
        }

        //Handles the first case listed above
        if (spanEnd < selectionEnd) {
            spannable.setSpan(span, selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return true;
        }

        //Handles the final case listed above
        spannable.setSpan(span, selectionStart, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return true;
    }

    protected List<StyleSpan> getOverlappingStyleSpans(Spannable spannable, int selectionStart, int selectionEnd, int style) {
        List<StyleSpan> spans = new LinkedList<>(Arrays.asList(spannable.getSpans(selectionStart, selectionEnd, StyleSpan.class)));

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

    /**
     * Used to duplicate spans when splitting an existing span in to two.
     * This would occur when the selection is only a partial of the styled
     * text and the styling is removed.
     *
     * @param span The span to duplicate
     * @return The duplicate span or null
     */
    @Nullable
    protected Object duplicateSpan(Object span) {

        if (span instanceof StyleSpan) {
            StyleSpan styleSpan = (StyleSpan)span;
            return new StyleSpan(styleSpan.getStyle());
        }

        return null;
    }
}
