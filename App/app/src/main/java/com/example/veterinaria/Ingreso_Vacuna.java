package com.example.veterinaria;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    }
    public void IngresarVacuna(View v)
    {
        EditText ETCodigo=findViewById(R.id.ETCodigo);
        String Codigo=ETCodigo.getText().toString();
        EditText ETDescripcion=findViewById(R.id.ETDescripcion);
        String Descripcion=ETDescripcion.getText().toString();
        EditText ETFabricante=findViewById(R.id.ETFabricante);
        String Fabricante=ETFabricante.getText().toString();
        db.Instruccion("INSERT INTO VACUNA VALUES('"+Codigo+"','"+Descripcion+"','"+Fabricante+"')");
    }
}