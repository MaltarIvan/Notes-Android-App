package hr.apps.maltar.notes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import hr.apps.maltar.notes.SQLmanager.NotesContract;
import hr.apps.maltar.notes.entities.Note;
import hr.apps.maltar.notes.listAdapters.NotesAdapter;
import hr.apps.maltar.notes.params.IntentFilterParams;
import hr.apps.maltar.notes.services.NotesDataReceiver;

public class NotesActivity extends AppCompatActivity {
    private BroadcastReceiver broadcastReceiver;

    private ListView notesListView;
    private NotesAdapter notesAdapter;
    private FloatingActionButton addNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        notesAdapter = new NotesAdapter(getApplicationContext(), new ArrayList<Note>());

        notesListView = (ListView) findViewById(R.id.notes_list_view);
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Note selectedNote = (Note) parent.getItemAtPosition(position);
                Uri uri = NotesContract.loadSingleNoteUri.withAppendedPath(NotesContract.loadSingleNoteUri, String.valueOf(selectedNote.getId()));
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                intent.putExtra(getString(R.string.service_intent_uri_key), uri);
                startActivity(intent);
            }
        });
        addNoteButton = (FloatingActionButton) findViewById(R.id.floating_add_button);

        notesListView.setAdapter(notesAdapter);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });

        registerBroadcastManagerReceiver();

        loadNotes();
    }

    private void loadNotes() {
        Intent intent = new Intent(this, NotesDataReceiver.class);
        intent.putExtra("uri", NotesContract.loadNotesUri);
        startService(intent);
    }

    private void registerBroadcastManagerReceiver() {
        broadcastReceiver = new DataBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(IntentFilterParams.ACTION_LOAD_ALL_NOTES);
        intentFilter.addAction(IntentFilterParams.ACTION_ADD_NEW_NOTE);
        intentFilter.addAction(IntentFilterParams.ACTION_NOTE_UPDATED);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private class DataBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (IntentFilterParams.ACTION_ADD_NEW_NOTE.equals(intent.getAction())) {
                notesAdapter.clear();
                loadNotes();
            }
            if (IntentFilterParams.ACTION_LOAD_ALL_NOTES.equals(intent.getAction())) {
                ArrayList<Note> notes = intent.getParcelableArrayListExtra("notes");
                notesAdapter.addAll(notes);
            }
            if (IntentFilterParams.ACTION_NOTE_UPDATED.equals(intent.getAction())) {
                notesAdapter.clear();
                loadNotes(); // TODO: 5.9.2017. možda neka bolja metoda ?
            }
        }
    }
}
