package com.ht.sandbox.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ht.sandbox.util.NoteListAdapter;
import com.ht.sandbox.R;
import com.ht.sandbox.note.Note;
import com.ht.sandbox.note.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY_TITLE = "note_title";
    public static final String EXTRA_REPLY_CONTENT = "note_description";
    public static final String EXTRA_REPLY_FAVOURITE = "note_favourite";
    private static final String TAG = "MainActivity";

    private NoteViewModel mNoteViewModel;
    private NoteListAdapter mNoteListAdapter;
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        }

        // Setup the Floating Action Button.
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });


        // Setup the RecyclerView.
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mNoteListAdapter = new NoteListAdapter(this);

        recyclerView.setAdapter(mNoteListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a ViewModel from the ViewModelProvider.
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                mNoteListAdapter.setNotes(notes);
            }
        });

        // Setup the SimpleItemTouchHelper to manage items in recyclerView.
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            /**
             * Handle swipe gesture.
             * @param viewHolder Holder swiped.
             * @param direction Direction of swipe.
             */
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                final Note note = mNoteListAdapter.mNotes.get(position);

                // Just delete if its not favourized.
                if(note.isFavourite()){
                    mNoteListAdapter.notifyItemChanged(position);
                    Snackbar.make(recyclerView, "Cannot delete this Note. Its your favourite!", Snackbar.LENGTH_LONG).show();
                } else {
                    mNoteViewModel.delete(note);
                    mNoteListAdapter.removeNote(position);

                    Snackbar.make(recyclerView, "Note deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mNoteViewModel.insert(note);
                                    mNoteListAdapter.insertNote(note, position);
                                }
                            }).show();
                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Setup DrawerLayout.
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(false);
                        switch (menuItem.getItemId()){
                            case R.id.nav_setting:
                                Toast.makeText(MainActivity.this, "Setting", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.nav_favourite:
                                Toast.makeText(MainActivity.this, "Favourite", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.nav_stuff:
                                Toast.makeText(MainActivity.this, "Stuff", Toast.LENGTH_LONG).show();
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(
                    data.getStringExtra(EXTRA_REPLY_TITLE),
                    data.getStringExtra(EXTRA_REPLY_CONTENT),
                    data.getBooleanExtra(EXTRA_REPLY_FAVOURITE, false));
            Log.d(TAG, note.toString());
            mNoteViewModel.insert(note);
        } else {
            Toast.makeText(this, "Wurde nicht gespeichert...", Toast.LENGTH_LONG).show();
        }
    }
}
