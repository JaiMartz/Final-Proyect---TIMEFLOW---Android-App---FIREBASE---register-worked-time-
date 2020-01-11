package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Historico extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private DatabaseReference mFirebaseTimeWorked;
    private ListView lista;
    private Button btConsultar;
    private Spinner spinnerAnyo;
    private Spinner spinnerMes;
    private String clave, UID, email, nombre, apellido, telefono, dni, fecha;
    private String [] array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);


        //RECIBIMOS DATOS
        UID = getIntent().getStringExtra("UID");
        email = getIntent().getStringExtra("EMAIL");
        nombre = getIntent().getStringExtra("NOMBRE");
        apellido = getIntent().getStringExtra("APELLIDO");
        dni = getIntent().getStringExtra("DNI");
        telefono = getIntent().getStringExtra("TELEFONO");
        fecha = getIntent().getStringExtra("FECHA");
        clave = getIntent().getStringExtra("CLAVE");

        //Asociamos atributos con ID
        spinnerAnyo = findViewById(R.id.spinnerAnyo);
        spinnerMes = findViewById(R.id.spinnerMes);
        btConsultar = findViewById(R.id.btConsulta);

        //Definimos ArrayAdapters
        ArrayAdapter<CharSequence> adapterAnyo = ArrayAdapter.createFromResource(this, R.array.anyos, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterMes = ArrayAdapter.createFromResource(this, R.array.meses, android.R.layout.simple_spinner_item);

        //Definimos los combobox
        adapterAnyo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Llenamos damos funciones a los adapters
        spinnerAnyo.setAdapter(adapterAnyo);
        spinnerMes.setAdapter(adapterMes);

        //Establecemos los listener
        spinnerAnyo.setOnItemSelectedListener(this);
        spinnerMes.setOnItemSelectedListener(this);
        btConsultar.setOnClickListener(this);

        //SET ELEMENTS
        lista = findViewById(R.id.listView);
        btConsultar = findViewById(R.id.btConsulta);
        btConsultar.setOnClickListener(this);

        //ASOCIAMOS VALORES A VARIABLES TRAS RECUPERARLOS
        if(savedInstanceState != null){
            UID = savedInstanceState.getString("UID");
            email = savedInstanceState.getString("EMAIL");
            nombre = savedInstanceState.getString("NOMBRE");
            apellido = savedInstanceState.getString("APELLIDO");
            dni = savedInstanceState.getString("DNI");
            telefono = savedInstanceState.getString("TELEFONO");
            fecha = savedInstanceState.getString("FECHA");
            clave = savedInstanceState.getString("CLAVE");
            array = savedInstanceState.getStringArray("ARRAY");

            ArrayList<String> listado = new ArrayList<String>();

            for(int position = 0; position<array.length; position++){
                listado.add(array[position]);
            }
            ArrayAdapter<String> adaptador =  new ArrayAdapter<String>(Historico.this, android.R.layout.simple_list_item_1, listado);
            lista.setAdapter(adaptador);

        }


        dl = (DrawerLayout) findViewById(R.id.dl);

        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close); //EN STRINGS
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id==R.id.Home){
                    Intent home = new Intent(Historico.this, MainActivity.class);
                    cargaDatos(home,UID, email,nombre, apellido,dni, telefono, fecha, clave);
                    Toast.makeText(Historico.this, "Home", Toast.LENGTH_SHORT).show();
                    startActivity(home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));


                }else if(id == R.id.historico){

                    Intent historico = new Intent(Historico.this, Historico.class);
                    cargaDatos(historico,UID, email,nombre, apellido,dni, telefono, fecha, clave);
                    Toast.makeText(Historico.this, "Historico", Toast.LENGTH_SHORT).show();
                    startActivity(historico.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));


                }else if(id == R.id.perfil){

                    Intent perfil = new Intent(Historico.this, Perfil.class);
                    cargaDatos(perfil,UID, email,nombre, apellido,dni, telefono, fecha, clave);
                    Toast.makeText(Historico.this, "Perfil", Toast.LENGTH_SHORT).show();
                    startActivity(perfil.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));


                }else if(id == R.id.politicas){

                    Intent politicas = new Intent(Historico.this, Politicas.class);
                    cargaDatos(politicas,UID, email,nombre, apellido,dni, telefono, fecha, clave);
                    Toast.makeText(Historico.this, "Politicas", Toast.LENGTH_SHORT).show();
                    startActivity(politicas.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));


                }else if(id == R.id.desconectar){

                    Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                    homeIntent.addCategory( Intent.CATEGORY_HOME );
                    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(homeIntent);
                }

                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        final String anyo = spinnerAnyo.getSelectedItem().toString();
        final String mes = spinnerMes.getSelectedItem().toString();


       mFirebaseTimeWorked= FirebaseDatabase.getInstance().getReference(getString(R.string.nodo_timeWorked));

       mFirebaseTimeWorked.child(clave).child(anyo).child(mes).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               ArrayAdapter<String> adaptador;
               ArrayList<String> listado = new ArrayList<String>();

            for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){
                timeWorked timeW = datasnapshot.getValue(timeWorked.class);
                String clave = timeW.getClave();
                String horaE = timeW.getHoraEntrada();
                String horaS = timeW.getHoraSalida();
                String infoTrabajo = "Dia "+timeW.getDia() + "   -   entrada: "+horaE+"   salida: "+horaS;

                listado.add(infoTrabajo);
                adaptador = new ArrayAdapter<String>(Historico.this, android.R.layout.simple_list_item_1, listado);
                lista.setAdapter(adaptador);

            }


           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * Package the data for send it to another Activity.
     * Empaqueta los datos para enviarlos a otra Activity
     *
     * @param intencion parametro que recibe un tipo Intent.
     * @param uid       parametro de tipo String para ser enviado.
     * @param email     parametro de tipo String para ser enviado.
     * @param nombre    parametro de tipo String para ser enviado.
     * @param apellido  parametro de tipo String para ser enviado.
     * @param dni       parametro de tipo String para ser enviado.
     * @param telefono  parametro de tipo String para ser enviado.
     * @param fecha     parametro de tipo String para ser enviado.
     * @param clave     parametro de tipo String para ser enviado.
     */
    public void cargaDatos(Intent intencion, String uid, String email, String nombre, String apellido, String dni, String telefono, String fecha, String clave) {
        intencion.putExtra("UID", uid);
        intencion.putExtra("EMAIL", email);
        intencion.putExtra("NOMBRE", nombre);
        intencion.putExtra("APELLIDO", apellido);
        intencion.putExtra("DNI", dni);
        intencion.putExtra("TELEFONO", telefono);
        intencion.putExtra("FECHA", fecha);
        intencion.putExtra("CLAVE", clave);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("UID", UID);
        outState.putString("EMAIL", email);
        outState.putString("NOMBRE", nombre);
        outState.putString("APELLIDO", apellido);
        outState.putString("DNI", dni);
        outState.putString("TELEFONO", telefono);
        outState.putString("FECHA", fecha);
        outState.putString("CLAVE", clave);

        int size = lista.getCount();
        //ArrayList<String> listaRecuperada = new ArrayList<String>();
        String arrayLista []= new String[size];
        System.out.println("********************************* "+lista.getCount());
        for(int position = 0; position<lista.getCount(); position++){

            //listaRecuperada.add(lista.getItemAtPosition(position).toString());
            arrayLista [position] = lista.getItemAtPosition(position).toString();
            System.out.println(lista.getItemAtPosition(position).toString());
            System.out.println("**********************************************");
        }
        //CONTINUAR CON EL DESARROLO DEL PASO DE DATOS EN HISTORICO
        outState.putStringArray("ARRAY", arrayLista);

    }

}