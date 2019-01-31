package com.ht.sandbox.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ht.sandbox.R;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText mNoteTitleView;
    public static final String EXTRA_REPLY_TITLE = "note_title";
    public static final String EXTRA_REPLY_CONTENT = "note_description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mNoteTitleView = findViewById(R.id.edit_title);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if(TextUtils.isEmpty(mNoteTitleView.getText())){
                    setResult(RESULT_CANCELED, intent);
                } else{
                    String title = mNoteTitleView.getText().toString();
                    intent.putExtra(EXTRA_REPLY_TITLE, title);
                    setResult(RESULT_OK, intent);
                }

                finish();
            }
        });
    }
}
