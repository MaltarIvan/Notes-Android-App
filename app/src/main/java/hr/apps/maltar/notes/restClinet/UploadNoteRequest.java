package hr.apps.maltar.notes.restClinet;

/**
 * Created by Maltar on 19.9.2017..
 */

public class UploadNoteRequest {
    final Long date;
    final String content;

    public UploadNoteRequest(Long date, String content) {
        this.date = date;
        this.content = content;
    }
}
