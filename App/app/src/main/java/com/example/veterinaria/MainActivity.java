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

    }
    public void Pasar(View v)
    {
        EditText ETPassword = findViewById(R.id.Password);
        String pass = ETPassword.getText().toString();

        EditText ETUser= findViewById(R.id.User);
        String user = ETUser.getText().toString();
        if(pass.equals("alf")&user.equals("alf"))
        {
            Intent intent =new Intent(this, VerCarnet.class);
            startActivity(intent);
        }
        else if(pass.equals("admin")&&user.equals("admin"))
        {
            Intent intent =new Intent(this, MenuAdmin.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }

    }

}