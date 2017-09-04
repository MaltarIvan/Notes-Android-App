package hr.apps.maltar.notes.SQLmanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maltar on 30.8.2017..
 */

public class NotesDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " +
            NotesContract.NotesEntry.TABLE_NAME + " (" +
            NotesContract.NotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NotesContract.NotesEntry.COLUMN_DATE + " DATE," +
            NotesContract.NotesEntry.COLUMN_CONTENT + " TEXT NOT NULL)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + NotesContract.NotesEntry.TABLE_NAME;

    public NotesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
