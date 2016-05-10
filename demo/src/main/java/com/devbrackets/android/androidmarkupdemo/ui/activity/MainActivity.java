package com.devbrackets.android.androidmarkupdemo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.devbrackets.android.androidmarkup.parser.markdown.MarkdownParser;
import com.devbrackets.android.androidmarkup.widget.MarkupEditText;
import com.devbrackets.android.androidmarkupdemo.R;
import com.devbrackets.android.androidmarkupdemo.ui.widget.MarkupControls;


public class MainActivity extends AppCompatActivity {

    private MarkupEditText markupEditText;
    private String storedMarkup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setupViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_show_markup:
                showMarkup();
                return true;
            case R.id.menu_store_markup:
                storedMarkup = markupEditText.getMarkup();
                markupEditText.setText("");
                return true;
            case R.id.menu_load_markup:
                markupEditText.setMarkup(storedMarkup);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        markupEditText = (MarkupEditText)findViewById(R.id.markup_edit_text);
        MarkupControls markupControls = (MarkupControls) findViewById(R.id.markup_controls);

        markupEditText.setMarkupParser(new MarkdownParser());
        markupControls.setMarkupEditText(markupEditText);
    }

    private void showMarkup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(markupEditText.getMarkup());
        builder.setPositiveButton("Done", null);
        builder.show();
    }
}
