package hr.apps.maltar.notes.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;

import hr.apps.maltar.notes.R;
import hr.apps.maltar.notes.SQLmanager.NotesContract;
import hr.apps.maltar.notes.SQLmanager.NotesDBHelper;
import hr.apps.maltar.notes.entities.Note;

/**
 * Created by Maltar on 30.8.2017..
 */

public class NotesDataReceiver extends IntentService {
    private static final String LOG_TAG = "INTENT_SERVICE";

    private static final int NOTES_LOAD = 100;
    private static final int NOTE_LOAD_ID = 101;
    private static final int NOTE_UPDATE_ID = 102;
    private static final int NOTE_ADD = 103;
    private static final int NOTE_DELETE_ID = 104;
    private static final int NOTES_DELETE = 105;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES, NOTES_LOAD);
        uriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTE_LOAD + "/#", NOTE_LOAD_ID);
        uriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTE_UPDATE + "/#", NOTE_UPDATE_ID);
        uriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTE_ADD, NOTE_ADD);
        uriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES_DELETE, NOTES_DELETE);
        uriMatcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTE_DELETE_ID + "/#", NOTE_DELETE_ID);
    }

    private Note recievedNote;

    private NotesDBHelper notesDBHelper;
    private SQLiteDatabase database;
    private String[] projection = {
            NotesContract.NotesEntry._ID,
            NotesContract.NotesEntry.COLUMN_DATE,
            NotesContract.NotesEntry.COLUMN_CONTENT
    };

    public NotesDataReceiver() {
        super("Notes Data Receiver Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Uri uri = intent.getParcelableExtra(getString(R.string.service_intent_uri_key));
        recievedNote = intent.getParcelableExtra(getString(R.string.service_intent_note_key));
    }

    @Override
    public void onDestroy() {
        notesDBHelper.close();
        super.onDestroy();
    }

    private void handleURI(Uri uri) {
        notesDBHelper = new NotesDBHelper(getApplicationContext());
        int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES_LOAD:
                loadAllNotes();
                break;
            case NOTE_LOAD_ID:
                loadNote(uri);
                break;
            case NOTE_ADD:
                addNote();
                break;
            case NOTE_UPDATE_ID:
                updateNote(uri);
                break;
            case NOTES_DELETE:
                deleteAllNotes();
                break;
            case NOTE_DELETE_ID:
                deleteNote(uri);
                break;
        }
    }

    private void loadAllNotes() {
        database = notesDBHelper.getReadableDatabase();
        Cursor cursor = database.query(
                NotesContract.NotesEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                NotesContract.NotesEntry.COLUMN_DATE + " ASC"
        );
        ArrayList<Note> notes = new ArrayList<>();
        while (cursor.moveToNext()) {
            int noteContentColumnIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_CONTENT);
            int noteDateColumnIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DATE);
            int idColumnIndex = cursor.getColumnIndex(NotesContract.NotesEntry._ID);

            String noteContent = cursor.getString(noteContentColumnIndex);
            long noteDate = cursor.getLong(noteDateColumnIndex);

            Note note = new Note(noteDate, noteContent);
            notes.add(note);
        }
        Intent intent = new Intent("all_notes_intent");
        intent.putParcelableArrayListExtra("notes", notes);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void loadNote(Uri uri) {
        database = notesDBHelper.getReadableDatabase();
        int id = Integer.parseInt(uri.getLastPathSegment());
        String selectQuery = "SELECT * FROM " + NotesContract.NotesEntry.TABLE_NAME + " WHERE " + NotesContract.NotesEntry._ID + " = " + id;
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        cursor.moveToFirst();

        int noteContentColumnIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_CONTENT);
        int noteDateColumnIndex = cursor.getColumnIndex(NotesContract.NotesEntry.COLUMN_DATE);
        int idColumnIndex = cursor.getColumnIndex(NotesContract.NotesEntry._ID);

        String noteContent = cursor.getString(noteContentColumnIndex);
        long noteDate = cursor.getLong(noteDateColumnIndex);

        Note note = new Note(noteDate, noteContent);

        Intent intent = new Intent("single_note_intent");
        intent.putExtra("note", note);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void addNote() {
        // TODO: 30.8.2017.
    }

    private void updateNote(Uri uri) {
        // TODO: 30.8.2017.
    }

    private void deleteAllNotes() {
        // TODO: 30.8.2017.
    }

    private void deleteNote(Uri uri) {
        // TODO: 30.8.2017.
    }
}
