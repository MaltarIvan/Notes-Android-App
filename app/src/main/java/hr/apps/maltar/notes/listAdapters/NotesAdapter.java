package hr.apps.maltar.notes.listAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import hr.apps.maltar.notes.R;
import hr.apps.maltar.notes.entities.Note;

/**
 * Created by Maltar on 4.9.2017..
 */

public class NotesAdapter extends ArrayAdapter<Note> {
    public NotesAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.notes_list_item, parent, false);
        }

        Note currentNote = getItem(position);

        TextView date = (TextView) listItemView.findViewById(R.id.date_text_view);
        TextView content = (TextView) listItemView.findViewById(R.id.content_text_view);

        date.setText(formatDateFromLong(currentNote.getDateLong()));
        content.setText(currentNote.getContent());

        return listItemView;
    }

    private String formatDateFromLong(long lDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, dd. MMM yyyy.");
        Date dDate = new Date(lDate);
        String sDate = sdf.format(dDate);
        return sDate;
    }
}
