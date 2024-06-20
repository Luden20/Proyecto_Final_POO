package com.example.veterinaria;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class Ingreso_Vacuna extends AppCompatActivity {
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingreso_vacuna);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db=new DB(this);
        Spinner TP_VACUNA=findViewById(R.id.TP_VACUNA);
        List<String> vacunas = Arrays.asList("VACUNA CONTROL", "VACUNA RABIA", "DESPARACITANTE");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vacunas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TP_VACUNA.setAdapter(adapter);
    }
    public void IngresarVacuna(View v)
    {
        EditText ETCodigo=findViewById(R.id.ETCodigo);
        String Codigo=ETCodigo.getText().toString();
        EditText ETDescripcion=findViewById(R.id.ETDescripcion);
        String Descripcion=ETDescripcion.getText().toString();
        EditText ETFabricante=findViewById(R.id.ETFabricante);
        String Fabricante=ETFabricante.getText().toString();
        Spinner TP_VACUNA=findViewById(R.id.TP_VACUNA);
        String tipo=TP_VACUNA.getSelectedItem().toString();
        db.Instruccion("INSERT INTO VACUNA VALUES('"+Codigo+"','"+tipo+"','"+Descripcion+"','"+Fabricante+"')","Ingreso vac correcto","Datos incorrectos");
    }
}