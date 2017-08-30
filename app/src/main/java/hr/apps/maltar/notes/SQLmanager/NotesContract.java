package hr.apps.maltar.notes.SQLmanager;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Maltar on 30.8.2017..
 */

public class NotesContract {
    private NotesContract() {}

    public static final String CONTENT_AUTHORITY = "hr.apps.maltar.notes";
    public static final String BASE_CONTENT = "content://" + CONTENT_AUTHORITY;
    public static final String PATH_NOTES = "notes_get";
    public static final String PATH_NOTE_LOAD = "note_load";
    public static final String PATH_NOTE_UPDATE = "note_update";
    public static final String PATH_NOTE_ADD = "note_add";
    public static final String PATH_NOTES_DELETE = "notes_delete";
    public static final String PATH_NOTE_DELETE_ID = "note_delete_id";

    public static final Uri loadNotesUri = Uri.parse(BASE_CONTENT + "/" + PATH_NOTES);
    public static final Uri loadSingleNoteUri = Uri.parse(BASE_CONTENT + "/" + PATH_NOTE_LOAD);
    public static final Uri updateNoteUri = Uri.parse(BASE_CONTENT + "/" + PATH_NOTE_UPDATE);
    public static final Uri addNoteUri = Uri.parse(BASE_CONTENT + "/" + PATH_NOTE_ADD);
    public static final Uri deleteAllNotesUri = Uri.parse(BASE_CONTENT + "/" + PATH_NOTES_DELETE);

    public static class NotesEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static  final String COLUMN_DATE = "date";
        public static final String COLUMN_CONTENT = "content";
        public static final String _ID = BaseColumns._ID;
    }
}
