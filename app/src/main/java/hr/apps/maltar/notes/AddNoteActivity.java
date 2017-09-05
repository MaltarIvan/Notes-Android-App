package hr.apps.maltar.notes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import hr.apps.maltar.notes.SQLmanager.NotesContract;
import hr.apps.maltar.notes.entities.Note;
import hr.apps.maltar.notes.params.IntentFilterParams;
import hr.apps.maltar.notes.services.NotesDataReceiver;

public class AddNoteActivity extends AppCompatActivity {
    private Uri currentNoteUri;
    private Note currentNote;

    private Button saveButton;
    private EditText contentEditText;

    private BroadcastReceiver broadcastReceiver;

    private String oldContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        contentEditText = (EditText) findViewById(R.id.new_note_edit_text);
        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!contentEditText.getText().toString().isEmpty()) {
                    Uri uri;
                    Note note;
                    if (currentNote == null) {
                        uri = NotesContract.addNoteUri;
                        note = new Note(System.currentTimeMillis() ,contentEditText.getText().toString());
                    } else {
                        if (oldContent.equals(contentEditText.getText().toString())) {
                            finish();
                            return;
                        }
                        uri = NotesContract.updateNoteUri.withAppendedPath(NotesContract.updateNoteUri, String.valueOf(currentNote.getId()));
                        note = new Note(System.currentTimeMillis() ,contentEditText.getText().toString(), currentNote.getId());
                    }
                    Intent intent = new Intent(getApplicationContext(), NotesDataReceiver.class);
                    intent.putExtra(getString(R.string.service_intent_uri_key), uri);
                    intent.putExtra(getString(R.string.service_intent_note_key), note);
                    startService(intent);
                    finish();
                }
            }
        });

        registerLocalBroadcastManagerReceiver();

        Intent thisIntent = getIntent();
        currentNoteUri = thisIntent.getParcelableExtra(getString(R.string.service_intent_uri_key));
        currentNote = null;
        if (currentNoteUri != null) {
            saveButton.setText("update");
            Intent intent = new Intent(getApplicationContext(), NotesDataReceiver.class);
            intent.putExtra(getString(R.string.service_intent_uri_key), currentNoteUri);
            startService(intent);
        } else {

        }
    }

    private void registerLocalBroadcastManagerReceiver() {
        broadcastReceiver = new DataBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IntentFilterParams.ACTION_LOADED_SINGLE_NOTE);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void deleteNoteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete note?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNote();
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteNote() {
        Uri uri = NotesContract.deleteNoteUri.withAppendedPath(NotesContract.deleteNoteUri, String.valueOf(currentNote.getId()));
        Intent intent = new Intent(getApplicationContext(), NotesDataReceiver.class);
        intent.putExtra(getString(R.string.service_intent_uri_key), uri);
        startService(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentNoteUri != null) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
            return true;
        } else {
            return super.onCreateOptionsMenu(menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_note_item:
                deleteNoteConfirmationDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class DataBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (IntentFilterParams.ACTION_LOADED_SINGLE_NOTE.equals(intent.getAction())) {
                currentNote = intent.getParcelableExtra("note");
                contentEditText.setText(currentNote.getContent());
                oldContent = currentNote.getContent();
            }
        }
    }
}
