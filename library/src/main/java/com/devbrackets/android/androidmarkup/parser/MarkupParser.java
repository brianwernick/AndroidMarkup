package com.devbrackets.android.androidmarkup.parser;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StyleSpan;

import com.devbrackets.android.androidmarkup.text.style.ListSpan;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * An abstract base class that supports the Markup editing in the
 * {@link com.devbrackets.android.androidmarkup.widget.MarkupEditText}
 * <p>
 * For the simplification of examples the pipe character "|" will represent selection points,
 * the underscore character "_" will represent the current span, and a the asterisk character "*"
 * will represent any characters between the span endpoints and the selection points.
 */
public abstract class MarkupParser {
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

    public boolean updateSpan(Spannable spannable, int spanType, int startIndex, int endIndex) {
        switch (spanType) {
            case SpanType.BOLD:
                style(spannable, startIndex, endIndex, Typeface.BOLD);
                return true;

            case SpanType.ITALIC:
                style(spannable, startIndex, endIndex, Typeface.ITALIC);
                return true;

            case SpanType.ORDERED_LIST:
                list(spannable, startIndex, endIndex, true);
                return true;

            case SpanType.UNORDERED_LIST:
                list(spannable, startIndex, endIndex, false);
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

            modifiedSpan |= handleSpanStartBeforeSelection(spannable, span, spanStart, spanEnd, selectionStart, selectionEnd);
            modifiedSpan |= handleSpanStartAfterSelection(spannable, span, spanStart, spanEnd, selectionStart, selectionEnd);
        }

        if (!modifiedSpan) {
            spannable.setSpan(new StyleSpan(style), selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        optimizeSpans(spannable, getOverlappingStyleSpans(spannable, selectionStart - 1, selectionEnd + 1, style));
    }

    protected void list(Spannable spannable, int selectionStart, int selectionEnd, boolean ordered) {
        List<ListSpan> overlappingSpans = getOverlappingListSpans(spannable, selectionStart, selectionEnd);

        //Updates the selectionStart to the new line
        if (selectionStart != 0) {
            int previousNewline = findPreviousChar(spannable, selectionStart, '\n');
            selectionStart = previousNewline == -1 ? 0 : previousNewline;
        }

        //Updates the selectionEnd to the new line
        if (selectionEnd != spannable.length() - 1) {
            int nextNewline = findNextChar(spannable, selectionEnd, '\n');
            selectionEnd = nextNewline == -1 ? spannable.length() - 1 : nextNewline;
        }

        boolean modifiedSpan = false;
        for (ListSpan span : overlappingSpans) {
            int spanStart = spannable.getSpanStart(span);
            int spanEnd = spannable.getSpanEnd(span);

            if (spanStart == selectionStart && spanEnd == selectionEnd) {
                modifiedSpan = true;
                spannable.removeSpan(span);
                continue;
            }

            modifiedSpan |= handleSpanStartBeforeSelection(spannable, span, spanStart, spanEnd, selectionStart, selectionEnd);
            modifiedSpan |= handleSpanStartAfterSelection(spannable, span, spanStart, spanEnd, selectionStart, selectionEnd);
        }

        if (!modifiedSpan) {
            spannable.setSpan(new ListSpan(ordered ? ListSpan.Type.NUMERICAL : ListSpan.Type.BULLET), selectionStart, selectionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        optimizeSpans(spannable, getOverlappingListSpans(spannable, selectionStart - 1, selectionEnd + 1));
    }

    /**
     * If the specified Span starts before or equal to the selection, then we need to update the span end
     * only if the span ending is less than the <code>selectionEnd</code>.  If the span ending is
     * greater than or equal to the <code>selectionEnd</code> then the selected area will have the style
     * removed.
     * <p>
     * The cases that need to be handled below are:
     * <ol>
     * <li>
     * The selection start is contained within or equal to the span start and the selection end goes beyond the
     * span end.  (e.g. __|___***| will result in __|______| or |___***| will result in |______|)
     * </li>
     * <li>
     * The selection start is equal to the span start and the span end is contained within the
     * span.  (e.g. |______|__ will result in |******|__)
     * </li>
     * <li>
     * Both the selection start and end are contained within the span.
     * (e.g. __|______|__ will result in __|******|__)
     * </li>
     * <li>
     * The selection start is contained within the span and the selection end is equal to the
     * span end.  (e.g. __|______| will result in __|******|)
     * </li>
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
     * <li>
     * The selection start is before the <code>spanStart</code> and the <code>selectionEnd</code>
     * is after the span end. (e.g. |***___***| will result in |_________|)
     * </li>
     * <li>
     * The selection start is before the <code>spanStart</code> and the <code>selectionEnd</code>
     * is before or equal to the span end. (e.g. (|***___| will result in |______| or |***___|___
     * will result in |______|___)
     * </li>
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
        //Makes sure the start and end are contained in the spannable
        selectionStart = selectionStart < 0 ? 0 : selectionStart;
        selectionEnd = selectionEnd >= spannable.length() ? spannable.length() - 1 : selectionEnd;

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

    protected List<ListSpan> getOverlappingListSpans(Spannable spannable, int selectionStart, int selectionEnd) {
        //Makes sure the start and end are contained in the spannable
        selectionStart = selectionStart < 0 ? 0 : selectionStart;
        selectionEnd = selectionEnd >= spannable.length() ? spannable.length() - 1 : selectionEnd;

        return new LinkedList<>(Arrays.asList(spannable.getSpans(selectionStart, selectionEnd, ListSpan.class)));
    }

    /**
     * Optimizes the spans by joining any overlapping or abutting spans of
     * the same type.  This assumes that the specified <code>spans</code>
     * are of the same type.
     * <p>
     * NOTE: this method is O(n^2) for <code>spans</code>
     *
     * @param spannable The spannable that the <code>spans</code> are associated with
     * @param spans     The spans to optimize
     */
    protected void optimizeSpans(Spannable spannable, List<?> spans) {
        Set<Object> removeSpans = new HashSet<>();

        for (Object span : spans) {
            if (removeSpans.contains(span)) {
                continue;
            }

            for (Object compareSpan : spans) {
                if (span != compareSpan && !removeSpans.contains(compareSpan) && compareAndMerge(spannable, span, compareSpan)) {
                    removeSpans.add(compareSpan);
                }
            }
        }

        // Actually remove any spans that were merged (the compareSpan items)
        for (Object span : removeSpans) {
            spannable.removeSpan(span);
        }
    }

    /**
     *
     * @param spannable The spannable that the spans to check for overlaps are associated with
     * @param lhs The first span object to determine if it overlaps with <code>rhs</code>.
     *            If the spans are merged, this will be the span left associated with the
     *            <code>spannable</code>
     * @param rhs The second span object to determine if it overlaps with <code>lhs</code>.
     *            If the spans are merged, this will be the span removed from the
     *            <code>spannable</code>
     * @return True if the spans have been merged
     */
    protected boolean compareAndMerge(Spannable spannable, Object lhs, Object rhs) {
        int lhsStart = spannable.getSpanStart(lhs);
        int lhsEnd = spannable.getSpanEnd(lhs);
        int rhsStart = spannable.getSpanStart(rhs);
        int rhsEnd = spannable.getSpanEnd(rhs);

        if (lhsStart < rhsStart && rhsStart <= lhsEnd) {
            int end = lhsEnd > rhsEnd ? lhsEnd : rhsEnd;
            spannable.setSpan(lhs, lhsStart, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return true;
        } else if (lhsStart >= rhsStart && lhsStart <= rhsEnd) {
            int end = lhsEnd > rhsEnd ? lhsEnd : rhsEnd;
            spannable.setSpan(lhs, rhsStart, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return true;
        }

        return false;
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
            StyleSpan styleSpan = (StyleSpan) span;
            return new StyleSpan(styleSpan.getStyle());
        }

        return null;
    }

    protected int findPreviousChar(Spannable spannable, int start, char character) {
        if (start < 0) {
            return -1;
        }

        if (start >= spannable.length()) {
            start = spannable.length() - 1;
        }

        for (int i = start; i >= 0; i--) {
            if (spannable.charAt(i) == character) {
                return i;
            }
        }

        return -1;
    }

    protected int findNextChar(Spannable spannable, int start, char character) {
        if (start < 0) {
            start = 0;
        }

        if (start >= spannable.length()) {
            return -1;
        }

        for (int i = start; i < spannable.length(); i++) {
            if (spannable.charAt(i) == character) {
                return i;
            }
        }

        return -1;
    }
}
