package mx.edu.itlp.proyectoappgenda;

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

public class HomeActivity extends AppCompatActivity {

    RadioGroup radioGroup1;
    RadioButton deals;
    private ListView nListViewNotes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        nListViewNotes = findViewById(R.id.ListaRecordatorios);

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
                        //Log.i("home", "pantalla home" +  checkedId);
                        //in=new Intent(getBaseContext(),HomeActivity.class);
                        //startActivity(in);
                        //overridePendingTransition(0, 0);
                        break;

                    case R.id.notas:
                        in = new Intent(getBaseContext(), ListaNotasActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        //MOVER HACIA LA IZQUIERDA

                        break;
                        /*
                    case R.id.rates:
                        Log.i("matching", "matching inside1 rate" + checkedId);

                        in = new Intent(getBaseContext(),RatesActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                        */
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main_create_new_note:
                //empezar NoteActiviry en NewNote mode
                //Intent newNoteActivity = new Intent(this,NoteActivity.class);
                //startActivity(newNoteActivity);
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        nListViewNotes.setAdapter(null);

        ArrayList<Note> notes = Utilities.loadNote(this);
        if (notes == null || notes.size()==0){
            Toast.makeText(this,"No tienes notas creadas",Toast.LENGTH_SHORT).show();
            return;
        }else {
            NoteAdapter na = new NoteAdapter(this, R.layout.item_note,notes);
            nListViewNotes.setAdapter(na);

            nListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //run the NoteActivity in view/edit mode
                    String fileName = ((Note) nListViewNotes.getItemAtPosition(position)).getnDateTime()
                            + Utilities.FILE_EXTENSIONS;
                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    startActivity(viewNoteIntent);
                }
            });
        }
    }
}
