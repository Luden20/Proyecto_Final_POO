package com.example.veterinaria;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MenuAdmin extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }
    public void EntrarIngresoCliente(View v)
    {
        Intent i=new Intent(this, ingresoCliente.class);
        startActivity(i);
    }
    public void EntrarInresoMascota(View v)
    {
        startActivity(new Intent(this, Ingreso_Mascota.class));
    }
    public void EntrarIngresoVacuna(View v)
    {
        startActivity(new Intent(this,Ingreso_Vacuna.class));
    }
    public void EntrarVacunacion(View v)
    {
        startActivity(new Intent(this, Vacunacion.class));
    }
}