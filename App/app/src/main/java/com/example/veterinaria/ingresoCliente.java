package com.example.veterinaria;

import android.os.Bundle;
import android.widget.EditText;
import android.view.View;
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

        db.Instruccion("INSERT INTO CLIENTE (CLI_CEDULA_RUC, CLI_NOMBRE, CLI_APELLIDO, CLI_TELEFONO, CLI_CORREO, CLI_DIRECCION, CLI_CONTRASENA) VALUES ('"+Cedula+"', '"+Nombre+"', '"+Apellido+"', '"+Telefono+"', '"+Correo+"', '"+Direccion+"', '"+Password+"');");

    }
}