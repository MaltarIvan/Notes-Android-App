package hr.apps.maltar.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import hr.apps.maltar.notes.entities.Note;

public class UploadActivity extends AppCompatActivity {
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        note = getIntent().getParcelableExtra(getString(R.string.upload_activity_intent_note_key));


    }
}
