package com.devbrackets.android.androidmarkup.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.devbrackets.android.androidmarkup.R;
import com.devbrackets.android.androidmarkup.parser.HtmlParser;
import com.devbrackets.android.androidmarkup.parser.MarkdownParser;
import com.devbrackets.android.androidmarkup.parser.MarkupParser;

/**
 * A WYSIWYG EditText for Markup languages such as HTML or
 * Markdown.  This leaves the UI up to the implementing application.
 */
public class MarkupEditText extends AppCompatEditText {
    protected MarkupParser markupParser;

    public MarkupEditText(Context context) {
        super(context);
        init(context, null);
    }

    public MarkupEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MarkupEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void setMarkupParser(MarkupParser markupParser) {
        this.markupParser = markupParser;
    }

    public void setMarkup(String markup) {
        setText(markupParser.toSpanned(markup));
    }

    public String getMarkup() {
        return markupParser.fromSpanned(getText());
    }

    public void toggleBold() {
        markupParser.updateSpan(getText(), MarkupParser.SpanType.BOLD, getSelectionStart(), getSelectionEnd());
    }

    public void toggleItalics() {
        markupParser.updateSpan(getText(), MarkupParser.SpanType.ITALIC, getSelectionStart(), getSelectionEnd());
    }

    public void toggleOrderedList() {
        markupParser.updateSpan(getText(), MarkupParser.SpanType.ORDERED_LIST, getSelectionStart(), getSelectionEnd());
    }

    public void toggleUnOrderedList() {
        markupParser.updateSpan(getText(), MarkupParser.SpanType.UNORDERED_LIST, getSelectionStart(), getSelectionEnd());
    }

    protected void init(Context context, @Nullable AttributeSet attrs) {
        if (!readAttributes(context, attrs)) {
            markupParser = new HtmlParser();
        }
    }

    /**
     * Reads the attributes associated with this view, setting any values found
     *
     * @param context The context to retrieve the styled attributes with
     * @param attrs The {@link AttributeSet} to retrieve the values from
     * @return True if the attributes were read
     */
    protected boolean readAttributes(Context context, AttributeSet attrs) {
        if (isInEditMode()) {
            return false;
        }

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarkupEditText);
        if (typedArray == null) {
            return false;
        }

        //Updates the Parser
        ParserAttr parserAttr = ParserAttr.get(typedArray.getInteger(R.styleable.MarkupEditText_parser, 0));
        setParser(parserAttr);

        typedArray.recycle();
        return true;
    }

    protected void setParser(ParserAttr parserAttr) {
        switch (parserAttr) {
            default:
            case HTML:
                markupParser = new HtmlParser();
                break;

            case MARKDOWN:
                markupParser = new MarkdownParser();
                break;
        }
    }

    protected enum ParserAttr {
        HTML,
        MARKDOWN;

        static ParserAttr get(int ordinal) {
            return values()[ordinal];
        }
    }
}
