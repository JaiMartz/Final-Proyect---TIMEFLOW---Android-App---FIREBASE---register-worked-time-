package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private String UID, email, nombre, apellido, dni, telefono, fecha, clave;
    private TextView textInformation, textInformationSalida, textInformationDia, textInformationMes, textInformationAnyo;
    private TextView textNombre, textClave;
    private Button btInicio;
    private Button btSalida;
    private DatabaseReference mFirebaseEmployee;
    private DatabaseReference mFirebaseTimeWorked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ASOCIAMOS ELEMENTOS

        btInicio = findViewById(R.id.btInicio);
        btSalida = findViewById(R.id.btSalida);
        btInicio.setOnClickListener(this);
        btSalida.setOnClickListener(this);
        textInformation = findViewById(R.id.txtPerfilInfo);
        textInformationSalida = findViewById(R.id.textInformacionSalida);
        textInformationDia = findViewById(R.id.textInfoDia);
        textInformationMes = findViewById(R.id.textInfoMes);
        textInformationAnyo = findViewById(R.id.textInfoAnyo);

        textNombre = findViewById(R.id.txtNombre);
        textClave = findViewById(R.id.txtClave);

        //FIN ASOCIAR ELEMENTOS

        //REFERENCIA BASE DE DATOS
        mFirebaseEmployee = FirebaseDatabase.getInstance().getReference(getString(R.string.nodo_employee)); //STRING DE STRINGS.XML
        mFirebaseTimeWorked = FirebaseDatabase.getInstance().getReference(getString(R.string.nodo_timeWorked));

        //RECUPERAMOS PAQUETE DE DATOS DE FORMULARIO
        UID = getIntent().getStringExtra("UID");
        email = getIntent().getStringExtra("EMAIL");
        nombre = getIntent().getStringExtra("NOMBRE");
        apellido = getIntent().getStringExtra("APELLIDO");
        dni = getIntent().getStringExtra("DNI");
        telefono = getIntent().getStringExtra("TELEFONO");
        fecha = getIntent().getStringExtra("FECHA");
        clave = getIntent().getStringExtra("CLAVE");


        limpiarCampos();

        //ASOCIAMOS VALORES A VARIABLES TRAS RECUPERARLOS
        if(savedInstanceState != null){
            email = savedInstanceState.getString("EMAIL");
            UID = savedInstanceState.getString("UID");
            nombre = savedInstanceState.getString("NOMBRE");
            apellido = savedInstanceState.getString("APELLIDO");
            dni = savedInstanceState.getString("DNI");
            telefono = savedInstanceState.getString("TELEFONO");
            fecha = savedInstanceState.getString("FECHA");
            clave = savedInstanceState.getString("CLAVE");

            textInformationAnyo.setText(savedInstanceState.getString("ANYO"));
            textInformationMes.setText(savedInstanceState.getString("MES"));
            textInformationDia.setText(savedInstanceState.getString("DIA"));
            textInformation.setText(savedInstanceState.getString("ENTRADA"));
            textInformationSalida.setText(savedInstanceState.getString("SALIDA"));
            System.out.println("************************************");
            System.out.println("EMAIL: "+nombre+" ---- "+apellido+" ---- "+clave);
            System.out.println("************************************");
        }



        mFirebaseEmployee.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot datasnapshot: dataSnapshot.getChildren()){

                    Employee nodo = datasnapshot.getValue(Employee.class);

                    if(nodo.getClave().equals(clave)){
                        nombre = nodo.getNombre();
                        apellido = nodo.getApellido();
                        UID = nodo.getUID();
                        dni = nodo.getDni();
                        telefono = nodo.getTelefono();
                        fecha = nodo.getFecha();
                        textNombre.setText(nombre + " " + apellido);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //FIN CONSULTA
        textClave.setText(clave);
        textNombre.setText(nombre + " " + apellido);

        //SET DATA



        dl = (DrawerLayout) findViewById(R.id.dl);

        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close); //EN STRINGS
        abdt.setDrawerIndicatorEnabled(true);

        dl.addDrawerListener(abdt);
        abdt.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //NAVIGATION DRAWER --> NAVEGABILIDAD
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.Home) {
                    Intent home = new Intent(MainActivity.this, MainActivity.class);
                    cargaDatos(home, UID, email, nombre, apellido, dni, telefono, fecha, clave);
                    Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    startActivity(home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                } else if (id == R.id.historico) {
                    Intent historico = new Intent(MainActivity.this, Historico.class);
                    cargaDatos(historico, UID, email, nombre, apellido, dni, telefono, fecha, clave);
                    Toast.makeText(MainActivity.this, "Historico", Toast.LENGTH_SHORT).show();
                    startActivity(historico.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                } else if (id == R.id.perfil) {
                    Intent perfil = new Intent(MainActivity.this, Perfil.class);
                    cargaDatos(perfil, UID, email, nombre, apellido, dni, telefono, fecha, clave);
                    Toast.makeText(MainActivity.this, "Perfil", Toast.LENGTH_SHORT).show();
                    startActivity(perfil.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                } else if (id == R.id.politicas) {
                    Intent politicas = new Intent(MainActivity.this, Politicas.class);
                    cargaDatos(politicas, UID, email, nombre, apellido, dni, telefono, fecha, clave);
                    Toast.makeText(MainActivity.this, "Politicas", Toast.LENGTH_SHORT).show();
                    startActivity(politicas.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                } else if (id == R.id.desconectar) {

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
        switch (v.getId()) {
            case R.id.btInicio:
                insercionDatosInicio();
                break;
            case R.id.btSalida:
                insercionDatosSalida();
                break;

        }
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


    /**
     * Allow to save the exit hour in the database.
     * Permite guardar la hora de salida en la base de datos.
     */
    private void insercionDatosSalida() {

        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatH = new SimpleDateFormat("HH:mm");
            SimpleDateFormat simpleDateFormatD = new SimpleDateFormat("dd");
            SimpleDateFormat simpleDateFormatM = new SimpleDateFormat("MMM");
            SimpleDateFormat simpleDateFormatY = new SimpleDateFormat("yyyy");
            SimpleDateFormat simpleDateFormatE = new SimpleDateFormat("EEEE");


            final String hora = simpleDateFormatH.format(cal.getTime());
            final String dia = simpleDateFormatD.format(cal.getTime());
            String mes = simpleDateFormatM.format(cal.getTime());
            final String anyo = simpleDateFormatY.format(cal.getTime());
            final String diaSemana = simpleDateFormatE.format((cal.getTime()));


            String mesSinPunto = mes.replaceAll("[.]", "");
            final String mesMod = mesSinPunto.substring(0,1).toUpperCase() + mesSinPunto.substring(1);



            mFirebaseTimeWorked.child(clave).child(anyo).child(mesMod).child(dia).child("horaSalida").setValue(hora);
            textInformationSalida.setText(hora);


        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "No se pudo guardar la informacion", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Save the entry hour in the database.
     * Guarda la hora de entrada en la base de datos.
     */
    private void insercionDatosInicio() {
        //TODO: write content
        //TODO: write content
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormatH = new SimpleDateFormat("HH:mm");
            SimpleDateFormat simpleDateFormatD = new SimpleDateFormat("dd");
            SimpleDateFormat simpleDateFormatM = new SimpleDateFormat("MMM");
            SimpleDateFormat simpleDateFormatY = new SimpleDateFormat("yyyy");
            SimpleDateFormat simpleDateFormatE = new SimpleDateFormat("EEEE");

            String hora = simpleDateFormatH.format(cal.getTime());
            String dia = simpleDateFormatD.format(cal.getTime());
            String mes = simpleDateFormatM.format(cal.getTime());
            String anyo = simpleDateFormatY.format(cal.getTime());
            String diaSemana = simpleDateFormatE.format((cal.getTime()));


            String mesSinPunto = mes.replaceAll("[.]", "");
            String mesMod = mesSinPunto.substring(0,1).toUpperCase() + mesSinPunto.substring(1);


            String informacionDia = dia + " " + diaSemana;

            textInformation.setText(hora);
            textInformationDia.setText(informacionDia);
            textInformationMes.setText(mesMod);
            textInformationAnyo.setText(anyo);

            timeWorked timeW = new timeWorked();
            timeW.setClave(clave);
            timeW.setDia(dia);
            timeW.setMes(mesMod);
            timeW.setAnyo(anyo);
            timeW.setHoraEntrada(hora);
            timeW.setHoraSalida("");


            mFirebaseTimeWorked.child(clave).child(anyo).child(mesMod).child(dia).setValue(timeW);
        }catch(Exception e){
            Toast.makeText(MainActivity.this, "No se pudo guardar la informacion", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clean the textfields.
     * Limpia los campos de texto
      */
    public void limpiarCampos(){
        textInformation.setText("");
        textInformationSalida.setText("");
        textInformationDia.setText("");
        textInformationMes.setText("");
        textInformationAnyo.setText("");

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("EMAIL", email);
        outState.putString("NOMBRE", nombre);
        outState.putString("APELLIDO", apellido);
        outState.putString("DNI", dni);
        outState.putString("TELEFONO", telefono);
        outState.putString("FECHA", fecha);
        outState.putString("UID", UID);
        outState.putString("CLAVE", clave);
        outState.putString("ANYO", textInformationAnyo.getText().toString());
        outState.putString("MES", textInformationMes.getText().toString());
        outState.putString("DIA",textInformationDia.getText().toString());
        outState.putString("ENTRADA", textInformation.getText().toString());
        outState.putString("SALIDA", textInformationSalida.getText().toString());
        //GUARDAMOS LOS DATOS QUE NECESITAMOS Y CREAMOS LA APP QUE NECESITEMOS
    }

}

