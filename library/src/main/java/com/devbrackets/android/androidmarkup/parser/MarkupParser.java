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
            if (spannable.getSpanStart(span) == start && spannable.getSpanEnd(span) == end) {
                modifiedSpan = true;
                spannable.removeSpan(span);
            }

            //TODO: other cases (partial matches, etc.)
        }

        if (!modifiedSpan) {
            spannable.setSpan(new StyleSpan(style), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
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
