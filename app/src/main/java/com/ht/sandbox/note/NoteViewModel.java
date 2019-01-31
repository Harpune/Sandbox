package com.ht.sandbox.note;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);
    }

    public LiveData<List<Note>> getAllNotes() {
        return mRepository.getAllNotes();
    }

    public LiveData<Note> getById(long id) {
        return mRepository.getById(id);
    }

    public void insert(Note note) {
        mRepository.insert(note);
    }

    public void delete(Note note) {
        mRepository.delete(note);
    }

    public void update(Note note) {
        mRepository.update(note);
    }
}
