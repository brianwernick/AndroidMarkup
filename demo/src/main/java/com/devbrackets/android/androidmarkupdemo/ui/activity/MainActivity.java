package com.devbrackets.android.androidmarkupdemo.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.devbrackets.android.androidmarkup.parser.MarkdownParser;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_show_markup) {
            showMarkup();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupViews() {
        markupEditText = (MarkupEditText)findViewById(R.id.markup_edit_text);
        markupControls = (MarkupControls)findViewById(R.id.markup_controls);

        markupEditText.setMarkupParser(new MarkdownParser()); //TODO temp
        markupControls.setMarkupEditText(markupEditText);
    }

    private void showMarkup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(markupEditText.getMarkup());
        builder.setPositiveButton("Done", null);
        builder.show();
    }
}
