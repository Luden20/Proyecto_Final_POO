package com.example.veterinaria;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new DB(this);
    }
    public void Pasar(View v)
    {
        EditText ETPassword = findViewById(R.id.Password);
        String pass = ETPassword.getText().toString();

        EditText ETUser= findViewById(R.id.User);
        String user = ETUser.getText().toString();
        if(db.VeterinarioExiste(user, pass))
        {
            Intent intent=new Intent(this, MenuAdmin.class);
            startActivity(intent);
        }
        else
        {
            if(db.UsuarioExiste(user, pass))
            {
                Intent intent =new Intent(this, VerCarnet.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        }


    }
    public void EntrarIngresoCliente(View v)
    {
        Intent i=new Intent(this, ingresoCliente.class);
        startActivity(i);
    }

}