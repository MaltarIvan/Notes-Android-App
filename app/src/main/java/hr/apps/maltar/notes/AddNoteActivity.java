package hr.apps.maltar.notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hr.apps.maltar.notes.SQLmanager.NotesContract;
import hr.apps.maltar.notes.entities.Note;
import hr.apps.maltar.notes.services.NotesDataReceiver;

public class AddNoteActivity extends AppCompatActivity {
    private Uri currentNoteUri;
    private Note currentNote;

    private Button saveButton;
    private EditText contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Intent intent = getIntent();
        currentNoteUri = intent.getData();
        currentNote = null;

        contentEditText = (EditText) findViewById(R.id.new_note_edit_text);
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note(System.currentTimeMillis() ,contentEditText.getText().toString());
                Uri uri = null;
                if (currentNote == null) {
                    uri = NotesContract.addNoteUri;
                }
                Intent intent = new Intent(getApplicationContext(), NotesDataReceiver.class);
                intent.putExtra(getString(R.string.service_intent_uri_key), uri);
                intent.putExtra(getString(R.string.service_intent_note_key), note);
                startService(intent);
                finish();
            }
        });

        if (currentNoteUri != null) {
            // TODO: 4.9.2017. dohvati 'note' iz baze
        }


    }
}
