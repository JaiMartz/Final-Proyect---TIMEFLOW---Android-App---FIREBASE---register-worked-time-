package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class profileForm extends AppCompatActivity implements View.OnClickListener {

    private Button btSave;
    private EditText eName;
    private EditText eSurname;
    private EditText eDni;
    private EditText eTel;
    private EditText eDate;
    private TextView textError;
    private DatabaseReference mFirebase;
    private String UID, email, nombre, apellido, dni, telefono, fecha, clave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_form);

        //ESCONDEMOS ACTION BAR
        getSupportActionBar().hide();

        //OBTENEMOS REFERENCIAS DE FIREBASE
        mFirebase= FirebaseDatabase.getInstance().getReference(getString(R.string.nodo_employee)); //OBTENEMOS EL NODO DE STRINGS.XML EN VALUES
        //GENERAMOS CLAVE
        clave = mFirebase.push().getKey();
        //OBTENEMOS EMAIL & UID FROM REGISTER
        email = getIntent().getStringExtra("email");
        UID = getIntent().getStringExtra("UID");


        //ASOCIAMOS ID
        eName=findViewById(R.id.etName);
        eSurname=findViewById(R.id.etSurname);
        eDni=findViewById(R.id.etDNI);
        eTel=findViewById(R.id.etTel);
        eDate=findViewById(R.id.etBirthDate);
        textError = findViewById(R.id.textErrorInformation);

        btSave = findViewById(R.id.btSave);
        btSave.setOnClickListener(this);
    }

    /**
     * Crea un registro del usuario el la base de datos
     * @param clave
     */
    public void saveData(String clave){
        try {

            Employee emp = new Employee();
            emp.setUID(UID);
            emp.setEmail(email);
            emp.setNombre(eName.getText().toString().trim());
            emp.setApellido(eSurname.getText().toString().trim());
            emp.setDni(eDni.getText().toString().trim());
            emp.setTelefono(eTel.getText().toString().trim());
            emp.setFecha(eDate.getText().toString().trim());
            emp.setClave(clave);

            mFirebase.child(clave).setValue(emp);

            nombre = eName.getText().toString().trim();
            apellido = eSurname.getText().toString().trim();
            dni = eDni.getText().toString().trim();
            telefono = eTel.getText().toString().trim();
            fecha = eDate.getText().toString().trim();


            changeActivity(UID, email, nombre, apellido, dni, telefono, fecha, clave);
        }catch (Exception e){
            System.out.println(e.getCause());
        }

    }

    /**
     * Change the Activity and send the data parcel to it.
     * Permite cambiar de Activity y envia un paquete de datos a la misma
     *
     * @param UID parametro String que identifica la ID de Usuario
     * @param email parametro String que identifica el email
     * @param nombre parametro String que identifica el nombre
     * @param apellido parametro String que identifica el apellid
     * @param dni parametro String que identifica el DNI
     * @param telefono parametro String que identifica el telefono
     * @param fecha parametro String que identifica la fecha
     * @param clave parametro String que identifica la clave unica de empleado
     */
    public  void changeActivity(String UID, String email, String nombre, String apellido, String dni, String telefono, String fecha, String clave){

        Intent intencion = new Intent(getApplication(), MainActivity.class);
        intencion.putExtra("UID", UID);
        intencion.putExtra("EMAIL", email);
        intencion.putExtra("NOMBRE", nombre);
        intencion.putExtra("APELLIDO", apellido);
        intencion.putExtra("DNI", dni);
        intencion.putExtra("TELEFONO", telefono);
        intencion.putExtra("FECHA", fecha);
        intencion.putExtra("CLAVE", clave);
        startActivity(intencion);
        finish();
    }

    @Override
    public void onClick(View view) {

        if(checkMatches(dni, nombre, apellido, fecha) == true){
            saveData(clave);
        }else{
            String error ="Revise el formulario e introduzca los datos correctamente.";
            textError.setText(error);
        }
    }

    /**Compare the requiered field to check if all the information is correct by comparing it with a regular expression.
     * Comprueba que los campos requeridos cumplas con una expresion regular
     * @param DNI parametro String que identifica el DNI
     * @param Nom parametro String que identifica el Nombre
     * @param Ape parametro String que identifica el Apellido
     * @param Date parametro String que identifica la Fecha
     * @return parametro que devuelve un valor de tipo boolean
     */
    public boolean checkMatches(String DNI, String Nom, String Ape, String Date){

        boolean resultado;

        //Comprobacion DNI
        Pattern patDNI = Pattern.compile("[0-9]{8}[A-Z]{1}");
        Matcher matDNI = patDNI.matcher(eDni.getText());

        //Comprobacion Telefono
        Pattern patTelf = Pattern.compile("\\b\\d+\\b");
        Matcher matTelf = patTelf.matcher(eTel.getText());

        //Comprobacion Nombre y Apellido
        Pattern patName = Pattern.compile("^\\D*$");
        Matcher matName = patName.matcher(eName.getText());
        Matcher matApe = patName.matcher(eSurname.getText());

        //Comprobacion fecha
        Pattern patDate = Pattern.compile("^([0-2][0-9]||3[0-1])/(0[0-9]||1[0-2])/([0-9][0-9])?[0-9][0-9]$");
        Matcher matDate = patDate.matcher(eDate.getText());

        if (matDNI.matches() && matName.matches() && matApe.matches() && matDate.matches()) {
            resultado = true;
        } else {
            textError.setText("Revise el formulario e introduzca los datos correctamente.");
            resultado = false;
        }

        return resultado;
    }
}
