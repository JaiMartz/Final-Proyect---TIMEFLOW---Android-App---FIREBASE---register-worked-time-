package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button btLogin;
    private Button btAccount;
    private EditText etEmail;
    private EditText etPassword;
    private String email;
    private String password;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mFirebaseE;
    private boolean cargado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //INSTANCIA DE AUTHENTICATION FIREBASE
        mAuth = FirebaseAuth.getInstance();


        //ASOCIAMOS DI
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btAccount = findViewById(R.id.btAccount);

        btLogin.setOnClickListener(this);
        btAccount.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);


        //ASOCIAMOS VALORES A VARIABLES TRAS RECUPERARLOS
        if(savedInstanceState != null){
            email = savedInstanceState.getString("email");
            password = savedInstanceState.getString("password");
            cargado = true;
        }else{
            cargado = false;
        }

    }

    /**
     * Realize a query into the database to determinate if the credentials are corrects.
     * Realizamos consulta en la BD para determinar si las credenciales son correctas.
     */
    public void loginUser(boolean cargado) {
                if(cargado == false){
                    email = etEmail.getText().toString().trim();
                    password = etPassword.getText().toString().trim();
                }

        if (TextUtils.isEmpty(email)) {

            Toast.makeText(Login.this, "Ingrese un email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(Login.this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Login.this, "¡Bienvenido!", Toast.LENGTH_SHORT).show();

                    readDatabase(email);

                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) { //Si se presenta una colision de usuario
                        Toast.makeText(Login.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     * Realiza una consulta en la base de datos
     * @param email parametro String que almacena el email.
     */
    public void readDatabase(final String email){

        mFirebaseE = FirebaseDatabase.getInstance().getReference(getString(R.string.nodo_employee));
        Query q = mFirebaseE.orderByChild(getString(R.string.campo_email)).equalTo(email);
        q.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String prueba;
                    for(DataSnapshot datasnapshot: dataSnapshot.getChildren()) {
                        prueba = datasnapshot.getRef().getKey();


                        Intent envio = new Intent(getApplication(), MainActivity.class);
                        envio.putExtra("CLAVE", prueba);
                        envio.putExtra("EMAIL", email);
                        startActivity(envio);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {}
            });
    }

    /**
     * Cambia la Activity a Register y finaliza la actual.
     */
    public void changeToRegister(){
        Intent intencion = new Intent(getApplication(), Register.class);
        startActivity(intencion);
        finish();
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btLogin:
                loginUser(cargado);
            break;
            case R.id.btAccount:
                changeToRegister();
                break;
        }
    }

    /**
     * Guardamos la información en paquetes de datos para enviarlos.
     * @param outState parametro que recibe e identifica al paquete de datos.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", etEmail.getText().toString());
        outState.putString("password", etPassword.getText().toString());
    }

}
