package mx.edu.itlp.proyectoappgenda;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class PrivateActivity extends AppCompatActivity {

    RadioGroup radioGroup1;
    RadioButton deals;
    private ListView nListViewNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private);

        nListViewNotes = findViewById(R.id.main_listview_notes);

        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent in;
                switch (checkedId)
                {
                    case R.id.home:
                        in=new Intent(getBaseContext(),HomeActivity.class);
                        startActivity(in);
                        //MOVER HACIA LA DERECHA
                        overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
                        break;

                    case R.id.notas:
                        Log.i("notas", "abre pantalla notas" + checkedId);
                        in = new Intent(getBaseContext(), ListaNotasActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_private, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main_create_new_private:
                //empezar NoteActiviry en NewNote mode
                Intent newNoteActivity = new Intent(this,PrivNoteActivity.class);
                startActivity(newNoteActivity);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        nListViewNotes.setAdapter(null);

        ArrayList<Note> notes = Utilities.loadListPrivate(this);
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(PrivateActivity.this);
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {

                            final String fileName = ((Note) nListViewNotes.getItemAtPosition(position)).getnDateTime()
                                    + Utilities.FILE_PRIVATE;

                            if( items[item].equals("Eliminar")){

                                AlertDialog.Builder mensaje = new AlertDialog.Builder(PrivateActivity.this).setTitle("Borrar Nota")
                                        .setMessage("Está a punto de eliminar el archivo " + fileName.toString() + ", ¿desea continuar?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                Utilities.deleteNote(getApplicationContext(),fileName);
                                                Toast.makeText(getApplicationContext(),"La nota se eliminó", Toast.LENGTH_SHORT).show();
                                                recreate();
                                            }
                                        }).setNegativeButton("No",null)
                                        .setCancelable(false);
                                AlertDialog alerta = mensaje.create();
                                alerta.show();


                            }
                            if( items[item].equals("Editar")){
                                Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                                viewNoteIntent.putExtra("NOTE_FILE", fileName);
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
                            + Utilities.FILE_PRIVATE;
                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    startActivity(viewNoteIntent);
                }
            });
        }
    }
}

