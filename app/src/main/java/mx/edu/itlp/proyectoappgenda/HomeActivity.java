package mx.edu.itlp.proyectoappgenda;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    RadioGroup radioGroup1;
    RadioButton deals;
    private String nNoteFileName;
    private Note nLoadedNote;
    private ListView nListViewNotes;
    private TextView Spinner;

    NotificationCompat.Builder notificacion;
    private static final int uniqueID = 45612;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        notificacion = new NotificationCompat.Builder(this);
        notificacion.setAutoCancel(true);



        nListViewNotes = findViewById(R.id.ListaRecordatorios);
        Spinner = findViewById(R.id.fakeSpinner);
        Spinner.setText("Recordatorios para Hoy");
        Spinner.setOnClickListener(this);

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

        Calendar c = Calendar.getInstance(); System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c.getTime());

        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String formattedHour = sdf.format(dt);

        ArrayList<Note> notes = Utilities.loadNote(this, "[Home]");
        if (notes == null || notes.size()==0){
            Toast.makeText(this,"No tienes recordatorios pendientes",Toast.LENGTH_SHORT).show();
            return;
        }else {

            NoteAdapter na = new NoteAdapter(this, R.layout.item_note,notes);

            /*
            ArrayList notifi = new ArrayList();
            int LongitudNotas = notes.size();
            for (int i = 0; i < LongitudNotas; i++) {
                if (notes.get(i).getnHora().contains(formattedHour)) {
                    //Mantener renglón
                    notifi.add(notes.get(i).getnTitle());
                }
            }
            */

            ArrayList notifi = new ArrayList();
            int LongitudNotas = notes.size();
            for (int i = 0; i < LongitudNotas; i++) {
                if (notes.get(i).getnFecha().contains(formattedDate)) {
                    //Mantener renglón
                    notifi.add(notes.get(i).getnTitle()+ " (" + notes.get(i).getnHora()+") ");
                }
            }
            CrearNotificacion(notifi);



            if (Spinner.getText().toString().equals("Recordatorios para Hoy"))
            {
                int searchListLength = notes.size();
                for (int i = 0; i < searchListLength; i++) {
                    if (notes.get(i).getnFecha().contains(formattedDate)) {
                        //Mantener renglón

                    }
                    else{
                        //Matar renglón
                        notes.remove(i);
                        searchListLength--;
                        i--;
                    }
                }
            }


            nListViewNotes.setAdapter(na);

            nListViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //run the NoteActivity in view/edit mode
                    String fileName = ((Note) nListViewNotes.getItemAtPosition(position)).getnDateTime()
                            + Utilities.FILE_EXTENSIONS;
                    Intent viewNoteIntent = new Intent(getApplicationContext(), NoteActivity.class);
                    viewNoteIntent.putExtra("NOTE_FILE", fileName);
                    nNoteFileName = fileName;
                    nLoadedNote = Utilities.getNoteByName(HomeActivity.this,buscarTipo(((Note) nListViewNotes.getItemAtPosition(position)).getnTitle())+nNoteFileName);
                    viewNoteIntent.putExtra("NOTE_EXTENSION", buscarTipo(nLoadedNote.getnTitle()));
                    startActivity(viewNoteIntent);
                }
            });
        }
    }

    public void CrearNotificacion( ArrayList titulos){
        //Construir la notificación
        String Mensaje = "";
        notificacion.setSmallIcon(R.drawable.appgenda1);
        notificacion.setTicker("Recordatorios diarios");
        notificacion.setWhen(System.currentTimeMillis());
        notificacion.setContentTitle("Recordatorios para Hoy");
        if (titulos != null || titulos.isEmpty())
        {
            for (int i = 0; i < titulos.size(); i++) {
                Mensaje = Mensaje + titulos.get(i).toString() + "\n";
            }
        }

        notificacion.setStyle(new NotificationCompat.BigTextStyle().bigText(Mensaje));

        Intent intent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificacion.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(uniqueID,notificacion.build());



        //
    }


    @Override
    public void onBackPressed() { }

    @Override
    public void onClick(View v) {
        if (v == Spinner)
        {
            final CharSequence[] items = {"Recordatorios Pendientes","Recordatorios para Hoy"};

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {


                    if( items[item].equals("Recordatorios Pendientes")){

                        Spinner.setText("Recordatorios Pendientes");
                        onResume();

                    }
                    if( items[item].equals("Recordatorios para Hoy")){

                        Spinner.setText("Recordatorios para Hoy");
                        onResume();
                    }

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
