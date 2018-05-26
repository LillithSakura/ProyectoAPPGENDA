package mx.edu.itlp.proyectoappgenda;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class PrivNoteActivity extends AppCompatActivity {

    private EditText nTitle;
    private  EditText nContent;
    private String nNoteFileName;
    private Note nLoadedNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_priv_note);

        nTitle = findViewById(R.id.note_et_title);
        nContent = findViewById(R.id.note_et_content);

        nNoteFileName = getIntent().getStringExtra("NOTE_FILE");
        if (nNoteFileName != null && !nNoteFileName.isEmpty()){
            nLoadedNote=Utilities.getNoteByName(getApplicationContext(),nNoteFileName);
            if (nLoadedNote != null){
                nTitle.setText(nLoadedNote.getnTitle());
                nContent.setText(nLoadedNote.getnContent());
            }

        }

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
                    .setMessage("Está a punto de eliminar el archivo " + nNoteFileName.toString() + ", ¿desea continuar?").setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Utilities.deleteNote(getApplicationContext(),nLoadedNote.getnDateTime()+Utilities.FILE_PRIVATE);
                            Toast.makeText(getApplicationContext(),"La nota " +nTitle.getText()+" se eliminó", Toast.LENGTH_SHORT).show();
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
                note = new Note(System.currentTimeMillis(), nTitle.getText().toString(), nContent.getText().toString());
            } else {
                note = new Note(nLoadedNote.getnDateTime(), nTitle.getText().toString(), nContent.getText().toString());
            }


            if (Utilities.savePrivate(this, note)) {
                Toast.makeText(this, "Se ha guardado la nota", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Ocurrió un error al intentar guardar", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
