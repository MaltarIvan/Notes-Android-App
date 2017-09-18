package hr.apps.maltar.notes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import hr.apps.maltar.notes.SQLmanager.NotesContract;
import hr.apps.maltar.notes.entities.Note;
import hr.apps.maltar.notes.fragments.PickActionDialogFragment;
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
                showActionPickerDialog(selectedNote);
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
        intentFilter.addAction(IntentFilterParams.ACTION_DELETE_ALL_NOTES);
        intentFilter.addAction(IntentFilterParams.ACTION_DELETE_NOTE);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
    }

    private void deleteAllNotesConfirmationDialog() {
        if (notesAdapter.getCount() > 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete all notes?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    deleteAllNotes();
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
    }

    private void deleteAllNotes() {
        Intent intent = new Intent(getApplicationContext(), NotesDataReceiver.class);
        intent.putExtra(getString(R.string.service_intent_uri_key), NotesContract.deleteAllNotesUri);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes_item:
                deleteAllNotesConfirmationDialog();
                Log.d("ITEM", String.valueOf(item.getItemId()) + " " + String.valueOf(R.id.delete_all_notes_item));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            if (IntentFilterParams.ACTION_DELETE_ALL_NOTES.equals(intent.getAction())) {
                notesAdapter.clear();
            }
            if (IntentFilterParams.ACTION_DELETE_NOTE.equals(intent.getAction())) {
                notesAdapter.clear();
                loadNotes(); // TODO: 5.9.2017. ???????????????????????????????????? 
            }
        }
    }

    private void showActionPickerDialog(Note note) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        PickActionDialogFragment pickActionDialogFragment = PickActionDialogFragment.newInstance(note);
        pickActionDialogFragment.show(fragmentManager, "fragment_pick_action");
    }
}
