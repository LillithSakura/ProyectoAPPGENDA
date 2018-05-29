package mx.edu.itlp.proyectoappgenda;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteActivity extends AppCompatActivity {

    private EditText nTitle;
    private  EditText nContent;
    private String nNoteFileName;
    private String nNoteFileExtension;
    private Note nLoadedNote;

    private String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        nTitle = findViewById(R.id.note_et_title);
        nContent = findViewById(R.id.note_et_content);

        nNoteFileExtension = getIntent().getStringExtra("NOTE_EXTENSION");
        if (nNoteFileExtension == null || nNoteFileExtension.isEmpty()){
            nNoteFileExtension = "[Examen]";
        }
        nNoteFileName = getIntent().getStringExtra("NOTE_FILE");
        if (nNoteFileName != null && !nNoteFileName.isEmpty()){
            nLoadedNote=Utilities.getNoteByName(getApplicationContext(),nNoteFileExtension+nNoteFileName);
            if (nLoadedNote != null){
                nTitle.setText(borrarTipo(nLoadedNote.getnTitle()));
                nContent.setText(nLoadedNote.getnContent());
                setTitle("Editando Apunte");
            }

        }

        // Get reference of widgets from XML layout
        final Spinner spinner = findViewById(R.id.spinner);

        // Initializing a String Array
        String[] tipos = new String[]{
                "[Examen]",
                "[Exposición]",
                "[Reunión]",
                "[Otro]",
                "[Apunte]"
        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(tipos));

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, plantsList);

        spinner.setAdapter(adapter);
        for (int i=0; i < tipos.length; i++)
        {
            if (nNoteFileExtension.equals(tipos[i]))
            {
                spinner.setSelection(i);
            }

        }



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               final String selectedItemText = (String) parent.getItemAtPosition(position);

                // Notify the selected item text
                //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                tipo = selectedItemText;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note_new,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_note_save:
                saveNote();
                break;
            case R.id.action_note_delete:
                deleteNote();
                break;
        }
        return true;
    }

    private void deleteNote() {
        if (nLoadedNote == null){
            //nueva nota
            finish();
        }else{

            AlertDialog.Builder dialog = new AlertDialog.Builder(this).setTitle("Borrar Nota")
                    .setMessage("Está a punto de eliminar el archivo " + nTitle.getText() +Utilities.FILE_EXTENSIONS + ", ¿desea continuar?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utilities.deleteNote(getApplicationContext(),nNoteFileExtension+nLoadedNote.getnDateTime()+Utilities.FILE_EXTENSIONS);
                            Toast.makeText(getApplicationContext(),"El apunte " +nTitle.getText()+" se eliminó", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).setNegativeButton("No",null)
                    .setCancelable(false);
            dialog.show();
        }
    }

    private void saveNote(){
        Note note;
        if (nTitle.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"Por favor ingrese un título",Toast.LENGTH_SHORT).show();
        }
        else {


            if (nLoadedNote == null) {
                note = new Note(System.currentTimeMillis(), tipo +" "+ borrarTipo(nTitle.getText().toString()), nContent.getText().toString());
            } else {
                note = new Note(nLoadedNote.getnDateTime(), tipo +" "+ borrarTipo(nTitle.getText().toString()), nContent.getText().toString());
            }


            if (Utilities.saveNote(this, note, tipo)) {
                Toast.makeText(this, "Se ha guardado la nota", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ocurrió un error al intentar guardar", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private String borrarTipo( String titulo){

        String[] tipos = new String[]{
                "[Examen]",
                "[Exposición]",
                "[Reunión]",
                "[Otro]",
                "[Apunte]"
        };

        for (int i=0; i < tipos.length; i++)
        {
            titulo = titulo.replace(tipos[i],"");

        }

        if (titulo.equals("") || titulo.isEmpty())
        {
            titulo = "Apunte sin título";
        }

        return titulo.trim();

    }
}
