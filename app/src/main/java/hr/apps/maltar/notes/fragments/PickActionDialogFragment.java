package hr.apps.maltar.notes.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hr.apps.maltar.notes.AddNoteActivity;
import hr.apps.maltar.notes.R;

/**
 * Created by Maltar on 18.9.2017..
 */

public class PickActionDialogFragment extends DialogFragment {
    private Button editNoteButton;
    private Button uploadNoteButton;

    public PickActionDialogFragment() {}

    public static PickActionDialogFragment newInstance(Uri uri) {
        PickActionDialogFragment fragment = new PickActionDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable("uri", uri);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_action, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Uri uri = getArguments().getParcelable("uri");
        editNoteButton = (Button) view.findViewById(R.id.edit_note_action);
        uploadNoteButton = (Button) view.findViewById(R.id.upload_note_action);

        editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                intent.putExtra(getString(R.string.service_intent_uri_key), uri);
                startActivity(intent);
            }
        });
    }
}
