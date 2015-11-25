package com.devbrackets.android.androidmarkup.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.devbrackets.android.androidmarkup.R;
import com.devbrackets.android.androidmarkup.parser.HtmlParser;
import com.devbrackets.android.androidmarkup.parser.MarkdownParser;
import com.devbrackets.android.androidmarkup.parser.MarkupParser;

public class MarkupTextView extends AppCompatTextView {
    protected MarkupParser markupParser;

    public MarkupTextView(Context context) {
        super(context);
        init(context, null);
    }

    public MarkupTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MarkupTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, @Nullable AttributeSet attrs) {
        if (!readAttributes(context, attrs)) {
            markupParser = new HtmlParser();
        }
    }

    public void setMarkupParser(MarkupParser markupParser) {
        this.markupParser = markupParser;
    }

    public void setMarkup(String markup) {
        setText(markupParser.toSpanned(markup));
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

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MarkupTextView);
        if (typedArray == null) {
            return false;
        }

        //Updates the Parser
        ParserAttr parserAttr = ParserAttr.get(typedArray.getInteger(R.styleable.MarkupTextView_parser, 0));
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
}
