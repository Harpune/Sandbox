package com.ht.sandbox.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.ht.sandbox.R;

public class CreateNoteActivity extends AppCompatActivity {

    private EditText mNoteTitleView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mNoteTitleView = findViewById(R.id.edit_title);

    }
}
