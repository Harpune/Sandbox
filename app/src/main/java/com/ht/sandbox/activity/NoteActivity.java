package com.ht.sandbox.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Delete;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.ht.sandbox.R;
import com.ht.sandbox.note.Note;
import com.ht.sandbox.note.NoteViewModel;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {

    private final static String TAG = "NoteActivity";
    private final static String INTENT_NOTE_ID = "intent:note:id";

    private NoteViewModel mNoteViewModel;
    private Note mNote = new Note();

    private ViewSwitcher switcher;
    private TextView textView;
    private EditText editText;

    private boolean isFABOpen = false;
    private FloatingActionButton fab, fab1, fab2, fab3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        Intent intent = getIntent();
        long id = intent.getLongExtra(INTENT_NOTE_ID, -1);

        // Get a ViewModel from the ViewModelProvider.
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getById(id).observe(this, new Observer<Note>() {
            @Override
            public void onChanged(@Nullable Note note) {
                mNote = note;
                updateView();
            }
        });

        switcher = findViewById(R.id.switcher);

        textView = switcher.findViewById(R.id.clickable_text_view);
        editText = switcher.findViewById(R.id.clickable_edit);
        // TODO make bullet enable in settings.
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() != KeyEvent.ACTION_UP) {
                    return false;
                }

                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

                    int curserPosition = editText.getSelectionStart();
                    String oldString = editText.getText().toString();

                    String first = oldString.substring(0, curserPosition); // before cursor
                    String second = oldString.substring(curserPosition); // after cursor
                    String newString = first + "\u25CF " + second; // add bullet
                    editText.setText(newString);
                    editText.setSelection(curserPosition + "\u25CF ".length());
                }
                return false;
            }
        });

        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);
        fab.setOnClickListener(this);
    }

    /**
     * Setup view with note information.
     */
    void updateView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(mNote.getTitle());

        TextView contentView = findViewById(R.id.clickable_text_view);
        contentView.setText(mNote.getContent());

        TextView dateView = findViewById(R.id.date);
        Date date = new Date(mNote.getCreatedAt());
        String dateString = new SimpleDateFormat("E dd.M.yyyy").format(date);
        dateView.setText(dateString);
    }

    /**
     * Set text to edit text and show.
     *
     * @param view Clicked view.
     */
    public void switchTextView(View view) {
        editText.setText(textView.getText());
        switcher.showNext();
    }

    /**
     * Get text of the edit text and save it in database.
     *
     * @param view Clicked view.
     */
    public void saveTextView(View view) {
        String updatedContent = editText.getText().toString();
        mNote.setContent(updatedContent);
        mNoteViewModel.update(mNote);
        textView.setText(updatedContent);
        switcher.showPrevious();
    }

    private void showFABMenu() {
        isFABOpen = true;

        final OvershootInterpolator interpolator = new OvershootInterpolator();
        ViewCompat.animate(fab).
                rotationBy(180f).
                withLayer().
                setDuration(300).
                setInterpolator(interpolator).
                start();

        fab1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.standard_155));
    }

    private void closeFABMenu() {
        isFABOpen = false;

        final OvershootInterpolator interpolator = new OvershootInterpolator();
        ViewCompat.animate(fab).
                rotationBy(-180f).
                withLayer().
                setDuration(300).
                setInterpolator(interpolator).
                start();

        fab1.animate().translationY(0);
        fab1.setOnClickListener(this);
        fab2.animate().translationY(0);
        fab2.setOnClickListener(this);
        fab3.animate().translationY(0);
        fab3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                if (!isFABOpen) {
                    showFABMenu();
                } else {
                    closeFABMenu();
                }
                break;
            case R.id.fab1:
                Toast.makeText(getApplicationContext(), "first", Toast.LENGTH_LONG).show();
                break;
            case R.id.fab2:
                Toast.makeText(getApplicationContext(), "first", Toast.LENGTH_LONG).show();
                break;
            case R.id.fab3:
                Toast.makeText(getApplicationContext(), "first", Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                switchTextView(item.getActionView());
                Toast.makeText(getApplicationContext(), "Edit", Toast.LENGTH_LONG).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
