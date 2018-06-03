package mx.edu.itlp.proyectoappgenda;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListaNotasActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{

    RadioGroup radioGroup1;
    RadioButton deals;
    private EditText nTitle;
    private  EditText nContent;
    private String nNoteFileName;
    private Note nLoadedNote;
    private String tipo = "[Todos]";
    private SwipeRefreshLayout swipeContainer;
    private ListView lv2Refresh;
    private ArrayAdapter<String> lvAdapter;

    private ListView nListViewNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        swipeContainer =  findViewById(R.id.srlContainer);
        swipeContainer.setOnRefreshListener(this);
        nListViewNotes = findViewById(R.id.main_listview_notes);

        // Get reference of widgets from XML layout
        final Spinner spinner = findViewById(R.id.spinner2);

        // Initializing a String Array
        String[] tipos = new String[]{
                "[Todos]",
                "[Examen]",
                "[Exposición]",
                "[Reunión]",
                "[Tarea]",
                "[Apunte]"

        };

        final List<String> plantsList = new ArrayList<>(Arrays.asList(tipos));

        ArrayAdapter adapter = new ArrayAdapter<String>(ListaNotasActivity.this,
                android.R.layout.simple_spinner_dropdown_item, plantsList);

        spinner.setAdapter(adapter);
        spinner.setSelection(0);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String selectedItemText = (String) parent.getItemAtPosition(position);

                // Notify the selected item text
                //Toast.makeText(getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT).show();
                tipo = selectedItemText;
                onResume();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent in;
                Log.i("matching", "matching inside1 bro" + checkedId);
                switch (checkedId)
                {
                    case R.id.home:
                        in=new Intent(getBaseContext(),HomeActivity.class);
                        startActivity(in);
                        //MOVER HACIA LA DERECHA
                        overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
                        break;

                    case R.id.notas:
                        //Log.i("notas", "abre pantalla notas" + checkedId);
                        //in = new Intent(getBaseContext(), ListaNotasActivity.class);
                        //startActivity(in);
                        //overridePendingTransition(0, 0);
                        break;

                    case R.id.foro:
                        in = new Intent(getBaseContext(),LoginActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        break;

                    default:
                        break;
                }





            }
        });
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Update data in ListView
                onResume();
                Toast.makeText(getApplicationContext(), "Lista Actualizada" , Toast.LENGTH_SHORT).show();
                // Remove widget from screen.
                swipeContainer.setRefreshing(false);
            }
        }, 1000);
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


        return titulo.trim();

    }

    private String buscarTipo( String titulo){

        String palabra = "";
        String[] tipos = new String[]{
                "[Examen]",
                "[Exposición]",
                "[Reunión]",
                "[Tarea]",
                "[Apunte]"
        };

        for (int i=0; i < tipos.length; i++)
        {
            int resultado = titulo.indexOf(tipos[i]);

            if(resultado != -1) {
                palabra = tipos[i];
            }

        }

        return palabra;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main_create_new_note:
                //empezar NoteActiviry en NewNote mode
                Intent newNoteActivity = new Intent(this,NoteActivity.class);
                startActivity(newNoteActivity);
                break;
            case R.id.action_private_save:
                //empezar PrivateActivity en NewNote mode
                Intent newPrivateActivity = new Intent(this,FingerActivity.class); //PONER AQUÍ LA VALIDACIÓN DE ENTRADA
                startActivity(newPrivateActivity);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        nListViewNotes.setAdapter(null);
        nListViewNotes.setLongClickable(true);

        final ArrayList<Note> notes = Utilities.loadNote(this, tipo);


        if (notes == null || notes.size()==0){
            Toast.makeText(this,"No tienes notas creadas",Toast.LENGTH_SHORT).show();
            return;
        }else {
            NoteAdapter na = new NoteAdapter(this, R.layout.item_note,notes);
            nListViewNotes.setAdapter(na);

            nListViewNotes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                    //Toast.makeText(getApplicationContext(), "long clicked, "+"pos: " + position, Toast.LENGTH_SHORT).show();


                    final CharSequence[] items = {"Eliminar", "Editar","Cancelar"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(ListaNotasActivity.this);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                           final String fileName = buscarTipo(((Note) nListViewNotes.getItemAtPosition(position)).getnTitle()) + ((Note) nListViewNotes.getItemAtPosition(position)).getnDateTime()
                                    + Utilities.FILE_EXTENSIONS;
                            nNoteFileName = fileName;

                            nLoadedNote = Utilities.getNoteByName(ListaNotasActivity.this,nNoteFileName);

                            if( items[item].equals("Eliminar")){

                                AlertDialog.Builder mensaje = new AlertDialog.Builder(ListaNotasActivity.this).setTitle("Borrar Nota")
                                        .setMessage("Está a punto de eliminar el archivo " + borrarTipo(((Note) nListViewNotes.getItemAtPosition(position)).getnTitle()) + Utilities.FILE_EXTENSIONS + ", ¿desea continuar?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {


                                                if (nNoteFileName != null && !nNoteFileName.isEmpty()){

                                                    if (nLoadedNote != null){
                                                        Utilities.deleteNote(getApplicationContext(),fileName);
                                                        Toast.makeText(getApplicationContext(),"El apunte "+ borrarTipo(nLoadedNote.getnTitle()) +" se eliminó", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                else{
                                                    Utilities.deleteNote(getApplicationContext(),fileName);
                                                    Toast.makeText(getApplicationContext(),"El Apunte se eliminó", Toast.LENGTH_SHORT).show();

                                                }

                                                onResume();
                                                //recreate();

                                            }
                                        }).setNegativeButton("No",null)
                                        .setCancelable(false);
                                AlertDialog alerta = mensaje.create();
                                alerta.show();


                            }
                            if( items[item].equals("Editar")){
                                String fileName2 = ((Note) nListViewNotes.getItemAtPosition(position)).getnDateTime()
                                        + Utilities.FILE_EXTENSIONS;
                                Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                                viewNoteIntent.putExtra("NOTE_FILE", fileName2);
                                nNoteFileName = fileName2;
                                nLoadedNote = Utilities.getNoteByName(ListaNotasActivity.this,buscarTipo(((Note) nListViewNotes.getItemAtPosition(position)).getnTitle())+nNoteFileName);
                                viewNoteIntent.putExtra("NOTE_EXTENSION", buscarTipo(nLoadedNote.getnTitle()));
                                startActivity(viewNoteIntent);
                            }
                            if( items[item].equals("Cancelar")){

                            }

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                    return true;

                }
            });
            nListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //run the NoteActivity in view/edit mode
                    String fileName = ((Note) nListViewNotes.getItemAtPosition(position)).getnDateTime()
                            + Utilities.FILE_EXTENSIONS;
                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    nNoteFileName = fileName;
                    nLoadedNote = Utilities.getNoteByName(ListaNotasActivity.this,buscarTipo(((Note) nListViewNotes.getItemAtPosition(position)).getnTitle())+nNoteFileName);
                    viewNoteIntent.putExtra("NOTE_EXTENSION", buscarTipo(nLoadedNote.getnTitle()));
                    startActivity(viewNoteIntent);
                }
            });

        }
    }

    @Override
    public void onBackPressed() { }
}
