package hr.apps.maltar.notes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import hr.apps.maltar.notes.entities.Note;
import hr.apps.maltar.notes.entities.NoteServer;
import hr.apps.maltar.notes.restClinet.RClient;
import hr.apps.maltar.notes.restClinet.UploadNoteRequest;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {
    private final String API_URL_BASE = "http://192.168.1.5:8080/";

    private ProgressBar progressBar;
    private TextView uploadStatTextView;
    private Button restartButton;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        note = getIntent().getParcelableExtra(getString(R.string.upload_activity_intent_note_key));
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        uploadStatTextView = (TextView) findViewById(R.id.upload_status_text_view);
        restartButton = (Button) findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                startProcess();
            }
        });

        startProcess();
    }

    private void startProcess() {
        if (isOnline()) {
            uploadNote();
        } else {
            handleNoInternetConnection();
        }
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void uploadNote() {
        uploadStatTextView.setText("Uploading note...");

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_URL_BASE)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        RClient client = retrofit.create(RClient.class);

        Call<NoteServer> call = client.uploadNote(new UploadNoteRequest(note.getDateLong(), note.getContent()));
        call.enqueue(new Callback<NoteServer>() {
            @Override
            public void onResponse(Call<NoteServer> call, Response<NoteServer> response) {
                Log.d("SUCESS", response.toString());
                uploadStatTextView.setText("Note uploaded!");
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<NoteServer> call, Throwable t) {
                Log.d("FAILURE", t.getMessage()); //// TODO: 19.9.2017. prazan response nije fail... 
                uploadStatTextView.setText("Note upload failed: " + t.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                restartButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void handleNoInternetConnection() {
        uploadStatTextView.setText("Your device is offline. Check your internet connection and try again.");
        progressBar.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.VISIBLE);
    }
}
