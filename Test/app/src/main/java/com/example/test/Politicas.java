package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Politicas extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private String UID, email, nombre, apellido, dni, telefono, fecha, clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politicas);

        //RECUPERAMOS DATOS DE FORMULARIO
        UID = getIntent().getStringExtra("UID");
        email = getIntent().getStringExtra("EMAIL");
        nombre = getIntent().getStringExtra("NOMBRE");
        apellido = getIntent().getStringExtra("APELLIDO");
        dni = getIntent().getStringExtra("DNI");
        telefono = getIntent().getStringExtra("TELEFONO");
        fecha = getIntent().getStringExtra("FECHA");
        clave = getIntent().getStringExtra("CLAVE");

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

                    Intent home = new Intent(Politicas.this, MainActivity.class);
                    cargaDatos(home,UID, email,nombre, apellido,dni, telefono, fecha, clave);
                    Toast.makeText(Politicas.this, "Home", Toast.LENGTH_SHORT).show();
                    startActivity(home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                }else if(id == R.id.historico){

                    Intent historico = new Intent(Politicas.this, Historico.class);
                    cargaDatos(historico,UID, email,nombre, apellido,dni, telefono, fecha, clave);
                    Toast.makeText(Politicas.this, "Historico", Toast.LENGTH_SHORT).show();
                    startActivity(historico.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                }else if(id == R.id.perfil){

                    Intent perfil = new Intent(Politicas.this, Perfil.class);
                    cargaDatos(perfil,UID, email,nombre, apellido,dni, telefono, fecha, clave);
                    Toast.makeText(Politicas.this, "Perfil", Toast.LENGTH_SHORT).show();
                    startActivity(perfil.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                }else if(id == R.id.politicas){

                    Intent politicas = new Intent(Politicas.this, Politicas.class);
                    cargaDatos(politicas,UID, email,nombre, apellido,dni, telefono, fecha, clave);
                    Toast.makeText(Politicas.this, "Politicas", Toast.LENGTH_SHORT).show();

                    /* FLAG_ACTIVITY_CLEAR_TOP para no lanzar un activity si ya se esta ejecutando encima de la pila

                     */
                    startActivity(politicas.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP));

                }
                else if(id == R.id.desconectar){
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
    }

}
