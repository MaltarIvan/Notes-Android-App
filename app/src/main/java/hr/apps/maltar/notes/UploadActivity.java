package hr.apps.maltar.notes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import hr.apps.maltar.notes.entities.Note;

public class UploadActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_INTERNET = 111;

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
    }

    private void handleNoInternetConnection() {
        uploadStatTextView.setText("Your device is offline. Check your internet connection and try again.");
        progressBar.setVisibility(View.INVISIBLE);
        restartButton.setVisibility(View.VISIBLE);
    }
}
