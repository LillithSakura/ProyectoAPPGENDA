package mx.edu.itlp.proyectoappgenda;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nTitle;
    private  EditText nContent;
    private String nNoteFileName;
    private String nNoteFileExtension;
    private Note nLoadedNote;
    public Button FechaButton,HoraButton;
    public EditText FechaText,HoraText;
    private int dia, mes, anio, hora, minutos;
    public LinearLayout DivFecha, DivHora;

    private String TempFecha = "", TempHora = "";

    private String tipo;
    private String tipoINICIAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        FechaButton = findViewById(R.id.FechaButton);
        HoraButton = findViewById(R.id.HoraButton);
        FechaText = findViewById(R.id.FechaText);
        HoraText = findViewById(R.id.HoraText);
        DivFecha = findViewById(R.id.DivFecha);
        DivHora = findViewById(R.id.DivHora);

        FechaButton.setOnClickListener(this);
        HoraButton.setOnClickListener(this);

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
                FechaText.setText(nLoadedNote.getnFecha());
                HoraText.setText(nLoadedNote.getnHora());
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
                "[Tarea]",
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
                if (selectedItemText.equals("[Apunte]")){
                    TempFecha = FechaText.getText().toString();
                    TempHora = HoraText.getText().toString();
                    DivFecha.setVisibility(View.GONE);
                    DivHora.setVisibility(View.GONE);
                    FechaText.setText("");
                    HoraText.setText("");
                }
                else{
                    if ( TempFecha.equals("") && TempHora.equals(""))
                    {
                        DivFecha.setVisibility(View.VISIBLE);
                        DivHora.setVisibility(View.VISIBLE);
                    }
                    else{
                        FechaText.setText(TempFecha);
                        HoraText.setText(TempHora);
                        DivFecha.setVisibility(View.VISIBLE);
                        DivHora.setVisibility(View.VISIBLE);
                    }

                }

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
                note = new Note(System.currentTimeMillis(), tipo +" "+ borrarTipo(nTitle.getText().toString()), nContent.getText().toString(), FechaText.getText().toString(),HoraText.getText().toString());
            } else {
                note = new Note(nLoadedNote.getnDateTime(), tipo +" "+ borrarTipo(nTitle.getText().toString()), nContent.getText().toString(), FechaText.getText().toString(),HoraText.getText().toString());
                Utilities.deleteNote(getApplicationContext(),nNoteFileExtension+nLoadedNote.getnDateTime()+Utilities.FILE_EXTENSIONS);
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
                "[Tarea]",
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


    @Override
    public void onClick(View v) {
        try {


            if (v == FechaButton) {
                final Calendar calendario = Calendar.getInstance();
                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                anio = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        FechaText.setText(String.format("%02d/%02d/%04d",dayOfMonth ,(month + 1) , year));

                    }
                }, anio, mes, dia);
                datePickerDialog.show();
            }
            if (v == HoraButton) {

                final Calendar c = Calendar.getInstance();
                hora = c.get(Calendar.HOUR_OF_DAY);
                minutos = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        HoraText.setText(String.format("%02d:%02d",hourOfDay , minute));
                    }
                }, hora, minutos, false);
                timePickerDialog.show();

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
