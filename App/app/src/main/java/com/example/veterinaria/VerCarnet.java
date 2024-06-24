package com.example.veterinaria;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatDelegate;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.List;

public class VerCarnet extends AppCompatActivity {
    private DB db;
    private String Cedula;
    private String IDMascota;
    private String IDCarnet;
    private Spinner TP_VACUNA;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_carnet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        TP_VACUNA=findViewById(R.id.TP_VACUNA);
        TP_VACUNA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TP_VACUNAActionListener(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        Intent intent=getIntent();
        Cedula=(String)intent.getStringExtra("user");
        db=new DB(this);
        Spinner spinner = (Spinner)findViewById(R.id.SPMascota);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerActionListener(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Método opcional, se llama cuando no se selecciona ningún elemento
            }
        });
        try {
            TextView TVNombre=findViewById(R.id.TVNombre);
            TVNombre.setText("Nombre:"+db.get("SELECT CLI_NOMBRE FROM CLIENTE WHERE CLI_CEDULA_RUC='"+Cedula+"';"));
            TextView TVApellido=findViewById(R.id.TVApellido);
            TVApellido.setText("Apellido:"+db.get("SELECT CLI_APELLIDO FROM CLIENTE WHERE CLI_CEDULA_RUC='"+Cedula+"';"));
            TextView TVTelefono=findViewById(R.id.TVTelefono);
            TVTelefono.setText("Telefono:"+db.get("SELECT CLI_TELEFONO FROM CLIENTE WHERE CLI_CEDULA_RUC='"+Cedula+"';"));
            TextView TVCorreo=findViewById(R.id.TVCorreo);
            TVCorreo.setText("Correo:"+db.get("SELECT CLI_CORREO FROM CLIENTE WHERE CLI_CEDULA_RUC='"+Cedula+"';"));
            TextView TVDireccion=findViewById(R.id.TVDireccion);
            TVDireccion.setText("Direcion:"+db.get("SELECT CLI_DIRECCION FROM CLIENTE WHERE CLI_CEDULA_RUC='"+Cedula+"';"));
            spinner.setAdapter(db.getArrayAdapter("SELECT MSC_NOMBRE FROM MASCOTA M INNER JOIN CLIENTE C ON M.CLI_CEDULA_RUC=C.CLI_CEDULA_RUC WHERE M.CLI_CEDULA_RUC='"+Cedula+"';",this));
            List<String> vacunas = Arrays.asList("VACUNA CONTROL", "VACUNA RABIA", "DESPARACITANTE");
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vacunas);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            TP_VACUNA.setAdapter(adapter);
        }
        catch(SQLException e)
        {
            TextView TVNombre=findViewById(R.id.TVNombre);
            TVNombre.setText(e.getMessage());
        }


    }
    public void TP_VACUNAActionListener(View v)
    {
        TP_VACUNA=findViewById(R.id.TP_VACUNA);
        String tipo=TP_VACUNA.getSelectedItem().toString();
        ListView Vacunas = findViewById(R.id.Vacunas);
        Vacunas.setAdapter(db.getAllArrayAdapter("SELECT V.VAC_DESCRIPCION,DV.DVC_LOTE,V.VAC_FABRICANTE,DV.DVC_FECHA_VAC,DV.DVC_REFECHA_VAC,DV.DVC_ESTADO FROM VACUNA V INNER JOIN DETALLE_VAC DV ON V.VAC_CODIGO=DV.VAC_CODIGO WHERE CNT_CODIGO='"+IDCarnet+"' AND V.VAC_TIPO='"+tipo+"';",this));
    }
    public void spinnerActionListener(View v)
    {
        Spinner sp=findViewById(R.id.SPMascota);
        String NombreMascota=sp.getSelectedItem().toString();
        IDMascota=db.get("SELECT MSC_CODIGO FROM MASCOTA M INNER JOIN CLIENTE C ON M.CLI_CEDULA_RUC=C.CLI_CEDULA_RUC WHERE M.CLI_CEDULA_RUC='"+Cedula+"' AND MSC_NOMBRE='"+NombreMascota+"';");
        IDCarnet =db.get("SELECT CNT_CODIGO FROM CARNET WHERE MSC_CODIGO='"+IDMascota+"';");
        TextView TVCodigo=findViewById(R.id.tTVCodigo);
        TVCodigo.setText("Codigo:"+IDMascota);
        TextView TVNombreMascota=findViewById(R.id.TVNombreMascota);
        TVNombreMascota.setText("Nombre Mascota:"+NombreMascota);
        TextView TVSexoMascota=findViewById(R.id.TVSexoMascota);
        TVSexoMascota.setText("Sexo:"+db.get("SELECT MSC_SEXO FROM MASCOTA WHERE MSC_CODIGO='"+IDMascota+"';"));
        TextView TVColorMascota=findViewById(R.id.TVColorMascota);
        TVColorMascota.setText("Color:"+db.get("SELECT MSC_COLOR FROM MASCOTA WHERE MSC_CODIGO='"+IDMascota+"';"));
        TextView TVFNMascota=findViewById(R.id.TVFNMascota);
        TVFNMascota.setText("Fecha de Nacimiento:"+db.get("SELECT MSC_FECHA FROM MASCOTA WHERE MSC_CODIGO='"+IDMascota+"';"));
        TextView TVEstadoMascota=findViewById(R.id.TVEstadoMascota);
        TVEstadoMascota.setText("Estado: "+db.get("SELECT MSC_ESTADO FROM MASCOTA WHERE MSC_CODIGO='"+IDMascota+"';"));
        TextView TVDPMascota=findViewById(R.id.TVDPMascota);
        TVDPMascota.setText("Detalles : "+db.get("SELECT MSC_DATOS FROM MASCOTA WHERE MSC_CODIGO='"+IDMascota+"';"));
        TextView TVERMascota=findViewById(R.id.TVERMascota);
        String IDRaza=db.get("SELECT RZ_CODIGO FROM MASCOTA WHERE MSC_CODIGO='"+IDMascota+"';");
        TVERMascota.setText("Raza:"+db.get("SELECT R.RZ_DESCRIPCION FROM MASCOTA M INNER JOIN RAZA R ON R.RZ_CODIGO=M.RZ_CODIGO INNER JOIN ESPECIE E ON E.SP_CODIGO=R.SP_CODIGO WHERE M.MSC_CODIGO='"+IDMascota+"';")+" Especie:"+db.get("SELECT E.SP_DESCRIPCION FROM MASCOTA M INNER JOIN RAZA R ON R.RZ_CODIGO=M.RZ_CODIGO INNER JOIN ESPECIE E ON E.SP_CODIGO=R.SP_CODIGO WHERE M.MSC_CODIGO='"+IDMascota+"';"));
        ListView Vacunas = findViewById(R.id.Vacunas);
        Vacunas.setAdapter(db.getAllArrayAdapter("SELECT V.VAC_DESCRIPCION,DV.DVC_LOTE,V.VAC_FABRICANTE,DV.DVC_FECHA_VAC,DV.DVC_REFECHA_VAC,DV.DVC_ESTADO FROM VACUNA V INNER JOIN DETALLE_VAC DV ON V.VAC_CODIGO=DV.VAC_CODIGO WHERE CNT_CODIGO='"+IDCarnet+"';",this));
        TP_VACUNA.setSelection(0);
        TP_VACUNAActionListener(v);
    }
}