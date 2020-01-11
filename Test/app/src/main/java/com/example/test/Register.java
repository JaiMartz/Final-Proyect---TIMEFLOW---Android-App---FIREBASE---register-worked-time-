package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class Register extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmailR;
    private EditText etPasswordR;
    private Button btRegister;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private String UID;
    private boolean cargado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();

        //ASIGNAMOS ID
        etEmailR = findViewById(R.id.etEmailR);
        etPasswordR =findViewById(R.id.etPasswordR);
        btRegister = findViewById(R.id.btSave);

        btRegister.setOnClickListener(this);
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
     * Allow to register a new user.
     * Permite registrar un nuevo usuario
     */
    private void registerUser(String email, String password, boolean cargado){
        if(cargado == false){
            email = etEmailR.getText().toString().trim();
            password = etPasswordR.getText().toString().trim();
        }

        if(TextUtils.isEmpty(email)){

            Toast.makeText(Register.this, "Ingrese un email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(Register.this,"Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Realizando registro");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Register.this, "Registro realizado",Toast.LENGTH_SHORT).show();
                    changeActivity();
                }     else  {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){ //Si se presenta una colision de usuario
                        Toast.makeText(Register.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                    }   else  {
                        Toast.makeText(Register.this, "Registro fallido", Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();
            }
        });

    }

    /**
     * Allow to change to another Activity and send a data parcel.
     * Permite cambiar de Activity y envia un paquete de datos
     */
    public void changeActivity(){
        Intent intencion = new Intent(getApplication(), profileForm.class);
        UID = mAuth.getUid();
        intencion.putExtra("email", etEmailR.getText().toString().trim());
        intencion.putExtra("UID", UID);
        startActivity(intencion);
        finish();
    }

    @Override
    public void onClick(View view) {
        registerUser(this.email, this.password, this.cargado);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("email", etEmailR.getText().toString());
        outState.putString("password", etPasswordR.getText().toString());
        //GUARDAMOS LOS DATOS QUE NECESITAMOS Y CREAMOS LA APP QUE NECESITEMOS
    }
}
