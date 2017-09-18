package hr.apps.maltar.notes.restClinet;

import hr.apps.maltar.notes.entities.NoteServer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Maltar on 19.9.2017..
 */

public interface RClient {

    @Headers("Content-Type: application/json")
    @POST("/api/notes")
    Call<NoteServer> uploadNote(@Body UploadNoteRequest uploadNoteRequest);
}
