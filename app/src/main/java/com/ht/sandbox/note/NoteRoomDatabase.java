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
     *
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

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new ResetDbAsync(INSTANCE).execute();
                }
            };

    /**
     * Populate the database in the background.
     */
    private static class ResetDbAsync extends AsyncTask<Void, Void, Void> {

        private final NoteDao mDao;

        ResetDbAsync(NoteRoomDatabase db) {
            mDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate the database
            // when it is first created
            mDao.deleteAll();
            return null;
        }
    }
}
