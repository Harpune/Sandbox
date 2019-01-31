package com.ht.sandbox.note;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class NoteRepository {
    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAll();
    }

    LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    LiveData<Note> getById(long id) {
        return mNoteDao.getByID(id);
    }

    void insert(Note note) {
        new insertAsyncTask(mNoteDao).execute(note);
    }

    void delete(Note note) {
        new deleteAsyncTask(mNoteDao).execute(note);
    }

    void update(Note note) {
        new updateAsyncTask(mNoteDao).execute(note);
    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mAsyncTaskDao;

        insertAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncTaskDao.insert(notes[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mAsyncDao;

        deleteAsyncTask(NoteDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncDao.delete(notes[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mAsyncDao;

        updateAsyncTask(NoteDao dao) {
            mAsyncDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncDao.update(notes[0]);
            return null;
        }
    }
}
