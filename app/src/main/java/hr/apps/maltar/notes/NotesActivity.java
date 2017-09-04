package hr.apps.maltar.notes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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
    private Button addNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        notesListView = (ListView) findViewById(R.id.notes_list_view);
        addNoteButton = (Button) findViewById(R.id.floating_add_button);

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
                startActivity(intent);
            }
        });

        registerBroadcastManagerReceiner();

        loadNotes();
    }

    private void loadNotes() {
        Intent intent = new Intent(this, NotesDataReceiver.class);
        intent.putExtra("uri", NotesContract.loadNotesUri);
        startService(intent);
    }

    private void registerBroadcastManagerReceiner() {
        broadcastReceiver = new DataBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(IntentFilterParams.ACTION_LOAD_ALL_NOTES);
        intentFilter.addAction(IntentFilterParams.ACTION_ADD_NEW_NOTE);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private class DataBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (IntentFilterParams.ACTION_ADD_NEW_NOTE.equals(intent.getAction())) {
                // TODO: 4.9.2017. dodana nova bilješka 'note'
            }
            if (IntentFilterParams.ACTION_LOAD_ALL_NOTES.equals(intent.getAction())) {
                ArrayList<Note> notes = intent.getParcelableArrayListExtra("notes");
                notesAdapter = new NotesAdapter(getApplicationContext(), notes);
                notesListView.setAdapter(notesAdapter);
            }
        }
    }
}
