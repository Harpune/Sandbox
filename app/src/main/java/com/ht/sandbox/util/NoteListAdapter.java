package com.ht.sandbox.util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ht.sandbox.R;
import com.ht.sandbox.activity.NoteActivity;
import com.ht.sandbox.note.Note;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {

    private final static String INTENT_NOTE_ID = "intent:note:id";
    private final LayoutInflater mInflater;
    private final Context mContext;
    public List<Note> mNotes;

    public NoteListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int i) {
        View view = mInflater.inflate(R.layout.note_list_row, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int i) {
        if (mNotes != null) {
            final Note current = mNotes.get(i);

            holder.title.setText(current.getTitle());
            holder.content.setText(current.getText());

            Date date = new Date(current.getCreatedAt());
            String dateString = new SimpleDateFormat("dd.M.yyyy", Locale.GERMAN).format(date);
            holder.createdAt.setText(dateString);

            if(!current.isFavourite()){
                holder.favourite.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mNotes != null) {
            return mNotes.size();
        }
        else return 0;
    }

    /**
     * Set the notes to adapter.
     * @param notes New notes.
     */
    public void setNotes(List<Note> notes){
        mNotes = notes;
        notifyDataSetChanged();
    }

    /**
     * Remove note from list.
     * @param position Position from the note.
     */
    public void removeNote(int position){
        mNotes.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * Insert Note to list.
     * @param note Note to insert.
     * @param position Position to add.
     */
    public void insertNote(Note note, int position){
        mNotes.add(position, note);
        notifyItemInserted(position);
    }

    /**
     * ViewHolder of the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, content, createdAt;
        ImageView favourite;
        NoteListAdapter mAdapter;

        ViewHolder(@NonNull View view, NoteListAdapter adapter) {
            super(view);
            title = view.findViewById(R.id.note_title);
            content = view.findViewById(R.id.note_text);
            createdAt = view.findViewById(R.id.note_created);
            favourite = view.findViewById(R.id.favourite_icon);
            mAdapter = adapter;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Note current = mNotes.get(getAdapterPosition());
            Intent intent = new Intent(mContext, NoteActivity.class);
            intent.putExtra(INTENT_NOTE_ID, current.getId());
            mContext.startActivity(intent);
        }
    }
}
