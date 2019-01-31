package com.ht.sandbox.note;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class NoteRoomDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();

    private static volatile NoteRoomDatabase INSTANCE;
    private static final String DATABASE_NAME = "note_database";

    /**
     * Returns instance of the database.
     * Instantiate database if null.
     * @param context Context of current activity.
     * @return Database instance.
     */
    static NoteRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteRoomDatabase.class,
                            DATABASE_NAME)

                            .build();
                }

            }
        }
        return INSTANCE;
    }

    /**
     * Creates callback for populating data into the note database.
     * Add ".addCallback(sRoomDatabaseCallback)" before building database.
     */
    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    /**
     * Deletes all entries in the database and inserts dummy data.
     */
    private static class PopulateDbAsync  extends AsyncTask<Void, Void, Void> {

        private final NoteDao mDao;

        PopulateDbAsync (NoteRoomDatabase db) {
            mDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAll();
            Note note = new Note("Hallo", "welt");
            mDao.insert(note);
            note = new Note("Was", "geht");
            mDao.insert(note);
            return null;
        }
    }
}
