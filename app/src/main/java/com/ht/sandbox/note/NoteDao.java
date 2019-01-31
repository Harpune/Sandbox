package com.ht.sandbox.note;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note")
    LiveData<List<Note>> getAll();

    @Query("SELECT * FROM note WHERE id = :noteId LIMIT 1")
    LiveData<Note> getByID(long noteId);

    @Query("DELETE FROM note")
    void deleteAll();

    @Delete
    void delete(Note note);

    @Insert
    void insert(Note note);

    @Update
    void update(Note note);


}
