package com.devbrackets.android.androidmarkupdemo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.devbrackets.android.androidmarkup.widget.MarkupEditText;
import com.devbrackets.android.androidmarkupdemo.R;
import com.devbrackets.android.androidmarkupdemo.ui.widget.MarkupControls;


public class MainActivity extends AppCompatActivity {

    private MarkupEditText markupEditText;
    private MarkupControls markupControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setupViews();
    }

    private void setupViews() {
        markupEditText = (MarkupEditText)findViewById(R.id.markup_edit_text);
        markupControls = (MarkupControls)findViewById(R.id.markup_controls);

        markupControls.setMarkupEditText(markupEditText);
    }
}
