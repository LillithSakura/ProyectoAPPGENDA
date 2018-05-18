package mx.edu.itlp.proyectoappgenda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity implements OnClickListener //implementamos la interface OnClickListener
{
    //las clases podemos hacer que tenga mas interfaces, forzandome a escribir el código del objeto que tengan la interface

    RadioGroup radioGroup1;
    @Override //elimina las funciones del padre
    protected void onCreate(Bundle savedInstanceState) { //El objeto Bundle tiene información sobre el estado de la aplicación
        super.onCreate(savedInstanceState); //Super hace que se haga lo que hará el padre una vez que se borró con el override, si no se quiere que se haga lo que el padre hace, se omite esta linea
        setContentView(R.layout.activity_login);
        final Button boton = findViewById(R.id.button);
        final TextView saludo = findViewById(R.id.textView2);
        boton.setClickable(true);
        boton.setOnClickListener(this);

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
                        in=new Intent(getBaseContext(),HomeActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
                        break;

                    case R.id.notas:
                        in = new Intent(getBaseContext(), ListaNotasActivity.class);
                        startActivity(in);
                        overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
                        //MOVER HACIA LA IZQUIERDA

                        break;

                    case R.id.foro:
                        //in = new Intent(getBaseContext(),LoginActivity.class);
                        //startActivity(in);
                        //overridePendingTransition(R.anim.rigth_in, R.anim.rigth_out);
                        break;

                    default:
                        break;
                }
            }
        });

    }

    @Override
    public void onClick(final View view) {
        final EditText txt1 = findViewById(R.id.editText);
        final EditText txt2 = findViewById(R.id.editText3);


        final String nombre = txt1.getText().toString();
        final String contraseña = txt2.getText().toString();

        Thread tr = new Thread() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (nombre.equals("Leonardo") && contraseña.equals("123")) {
                            Toast.makeText(getApplicationContext(), "Bienvenido " + nombre, Toast.LENGTH_LONG).show();
                            if (view.getId() == R.id.button) {
                                Button btn = (Button) findViewById(R.id.button);
                                btn.setEnabled(false);

                            }
                            //Intent intent = new Intent(getApplicationContext(), HomeActivity.class); //Enviar invo de un activity a otra
                            //intent.putExtra("nombre", nombre);
                            //startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: Usuario o Contraseña inexistentes, Registrese", Toast.LENGTH_LONG).show();
                            txt1.getText().clear();
                            txt2.getText().clear();
                        }
                    }
                });

            }
        };
        tr.start();
    }
    @Override
    public void onBackPressed() {

    }
}



    /*
                        String nombre = getIntent().getExtras().getString("respuesta1");
                    String nombre2 = getIntent().getExtras().getString("respuesta2");
                    String nombre3 = getIntent().getExtras().getString("respuesta3");
                    String nombre4 = getIntent().getExtras().getString("respuesta4");


                    Intent intent = new Intent(getApplicationContext(), Final.class); //Enviar invo de un activity a otra
                    intent.putExtra("respuesta1",nombre);
                    intent.putExtra("respuesta2",nombre2);
                    intent.putExtra("respuesta3",nombre3);
                    intent.putExtra("respuesta4",nombre4);
                    intent.putExtra("respuesta5",selectedSuperStar);
                    startActivity(intent);
     */