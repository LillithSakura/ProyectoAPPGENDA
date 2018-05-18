package mx.edu.itlp.proyectoappgenda;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MR_TODOGAMES on 15/05/2018.
 */

public class NoteAdapter extends ArrayAdapter<Note>{

    public NoteAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Note> notes) {
        super(context, resource, notes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note,null);
        }

        Note note = getItem(position);

        if (note != null){
            TextView title = convertView.findViewById(R.id.list_note_title);
            TextView date = convertView.findViewById(R.id.list_note_date);
            TextView content = convertView.findViewById(R.id.list_note_content);
            title.setText(note.getnTitle());
            date.setText(note.getDateTimeFormated(getContext()));
            if (note.getnContent().length() > 30){ //MUESTRA MENOS DE 10 CARACTERES
                content.setText(note.getnContent().substring(0,30) + "...");
            } else{
                content.setText(note.getnContent());
            }

        }
        return convertView;
    }
}
