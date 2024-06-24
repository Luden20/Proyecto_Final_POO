package com.example.veterinaria;

import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ingresoCliente extends AppCompatActivity {
    private DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingreso_cliente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db=new DB(this);
    }
    public void Ingresar(View v)
    {
        EditText EDCedula=findViewById(R.id.EDCedula);
        String Cedula=EDCedula.getText().toString();
        if(validarCedula(Cedula))
        {
            EditText EDNombre=findViewById(R.id.EDNombre);
            String Nombre=EDNombre.getText().toString();
            EditText EDApellido=findViewById(R.id.EDApellido);
            String Apellido=EDApellido.getText().toString();
            EditText EDTelefono=findViewById(R.id.EDTelefono);
            String Telefono=EDTelefono.getText().toString();
            EditText EDDireccion=findViewById(R.id.EDDireccion);
            String Direccion=EDDireccion.getText().toString();
            EditText EDCorreo=findViewById(R.id.EDCorreo);
            String Correo=EDCorreo.getText().toString();
            EditText EDPassword=findViewById(R.id.EDPassword);
            String Password =EDPassword.getText().toString();

            db.Instruccion("INSERT INTO CLIENTE (CLI_CEDULA_RUC, CLI_NOMBRE, CLI_APELLIDO, CLI_TELEFONO, CLI_CORREO, CLI_DIRECCION, CLI_CONTRASENA) VALUES ('"+Cedula+"', '"+Nombre+"', '"+Apellido+"', '"+Telefono+"', '"+Correo+"', '"+Direccion+"', '"+Password+"');","Cliente creado","Error, revise los datos");
        }
        else
        {
            Toast.makeText(this,"Cedula incorrecta",Toast.LENGTH_SHORT).show();

        }


    }
    public static boolean validarCedula(String cedula) {
        if (cedula == null || cedula.length() != 10) {
            return false;
        }
        int suma = 0;
        int ultimoDigito=0;
        try {
            int digitoVerificador = Integer.parseInt(cedula.substring(9, 10));
            for (int i = 0; i < cedula.length() - 1; i++) {
                int digito = Integer.parseInt(cedula.substring(i, i + 1));
                if (i % 2 == 0) {
                    digito *= 2;
                    if (digito > 9) {
                        digito -= 9;
                    }
                }
                suma += digito;
            }
            ultimoDigito = suma % 10 == 0 ? 0 : 10 - suma % 10;
            return ultimoDigito==digitoVerificador;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}