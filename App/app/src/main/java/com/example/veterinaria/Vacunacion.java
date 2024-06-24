package com.example.veterinaria;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.Calendar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ArrayAdapter;
import java.util.Arrays;
import java.util.List;
import androidx.appcompat.app.AppCompatDelegate;



public class Vacunacion extends AppCompatActivity {
    private Spinner SPCedulaBuscar;
    private Spinner SPMascota;
    private Spinner SPVacuna;
    private Spinner TP_VACUNA;
    private DB db;
    public String IngresoVac;
    private String IDCliente;
    private String IDMascota;
    public String IDCarnet;
    private String Cod_vacuna;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacunacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db=new DB(this);
        IngresoVac="";
        SPCedulaBuscar=findViewById(R.id.SPCedulaBuscar);
        SPVacuna=findViewById(R.id.SPVacuna);
        TP_VACUNA=findViewById(R.id.TP_VACUNA);
        SPMascota=findViewById(R.id.SPMascota);
        SPCedulaBuscar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPCedulaBuscarActionListener(view);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        TP_VACUNA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TP_VACUNAActionListener(view);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        SPVacuna.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPVacunaActionListener(view);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        SPMascota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPMascotaActionListener(view );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        TP_VACUNA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TP_VACUNAActionListener(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SPVacuna.setAdapter(db.getArrayAdapter("SELECT VAC_CODIGO FROM VACUNA;",this));
        SPMascota.setAdapter(db.getArrayAdapter("SELECT MSC_NOMBRE FROM MASCOTA M INNER JOIN CLIENTE C ON M.CLI_CEDULA_RUC=C.CLI_CEDULA_RUC WHERE M.CLI_CEDULA_RUC='"+IDCliente+"';",this));
        SPCedulaBuscar.setAdapter(db.getArrayAdapter("SELECT CLI_CEDULA_RUC FROM CLIENTE;",this));
        List<String> vacunas = Arrays.asList("VACUNA CONTROL", "VACUNA RABIA", "DESPARACITANTE");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vacunas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        TP_VACUNA.setAdapter(adapter);
        SPCedulaBuscar.setSelection(0);
        SPCedulaBuscarActionListener();
    }

    public void SPCedulaBuscarActionListener(View v)
    {
        SPCedulaBuscar=findViewById(R.id.SPCedulaBuscar);
        IDCliente=SPCedulaBuscar.getSelectedItem().toString();
        SPMascota.setAdapter(db.getArrayAdapter("SELECT MSC_NOMBRE FROM MASCOTA M INNER JOIN CLIENTE C ON M.CLI_CEDULA_RUC=C.CLI_CEDULA_RUC WHERE M.CLI_CEDULA_RUC='"+IDCliente+"';",this));
        TextView NOMBRE_CLI=findViewById(R.id.NOMBRE_CLI);
        NOMBRE_CLI.setText(db.get("SELECT CLI_NOMBRE FROM CLIENTE WHERE CLI_CEDULA_RUC='"+IDCliente+"';"));
        TextView APELLIDO_CLI=findViewById(R.id.APELLIDO_CLI);
        APELLIDO_CLI.setText(db.get("SELECT CLI_APELLIDO FROM CLIENTE WHERE CLI_CEDULA_RUC='"+IDCliente+"';"));
    }
    public void SPCedulaBuscarActionListener()
    {
        SPCedulaBuscar=findViewById(R.id.SPCedulaBuscar);
        IDCliente=SPCedulaBuscar.getSelectedItem().toString();
        SPMascota.setAdapter(db.getArrayAdapter("SELECT MSC_NOMBRE FROM MASCOTA M INNER JOIN CLIENTE C ON M.CLI_CEDULA_RUC=C.CLI_CEDULA_RUC WHERE M.CLI_CEDULA_RUC='"+IDCliente+"';",this));
        TextView NOMBRE_CLI=findViewById(R.id.NOMBRE_CLI);
        NOMBRE_CLI.setText(db.get("SELECT CLI_NOMBRE FROM CLIENTE WHERE CLI_CEDULA_RUC='"+IDCliente+"';"));
        TextView APELLIDO_CLI=findViewById(R.id.APELLIDO_CLI);
        APELLIDO_CLI.setText(db.get("SELECT CLI_APELLIDO FROM CLIENTE WHERE CLI_CEDULA_RUC='"+IDCliente+"';"));
    }
    public void SPMascotaActionListener(View v)
    {
        SPMascota=findViewById(R.id.SPMascota);
        String Mascota=SPMascota.getSelectedItem().toString();
        IDMascota=db.get("SELECT MSC_CODIGO FROM MASCOTA M INNER JOIN CLIENTE C ON M.CLI_CEDULA_RUC=C.CLI_CEDULA_RUC WHERE M.CLI_CEDULA_RUC='"+IDCliente+"' AND MSC_NOMBRE='"+Mascota+"';");
        IDCarnet =db.get("SELECT CNT_CODIGO FROM CARNET WHERE MSC_CODIGO='"+IDMascota+"';");
    }
    public void TP_VACUNAActionListener(View v)
    {
        TP_VACUNA=findViewById(R.id.TP_VACUNA);
        IngresoVac=TP_VACUNA.getSelectedItem().toString();
        SPVacuna.setAdapter(db.getArrayAdapter("SELECT VAC_CODIGO FROM  VACUNA WHERE VAC_TIPO='"+IngresoVac+"'", this));

    }
    public void SPVacunaActionListener(View v)
    {
        SPVacuna=findViewById(R.id.SPVacuna);
        Cod_vacuna=SPVacuna.getSelectedItem().toString();
        TextView TT_LOTE=findViewById(R.id.TT_LOTE);
        TT_LOTE.setText("");
        TextView TT_FRAB=findViewById(R.id.TT_FRAB);
        TT_FRAB.setText("Fab:"+db.get("SELECT VAC_FABRICANTE FROM VACUNA WHERE VAC_CODIGO='"+Cod_vacuna+"';"));
        TextView TT_DESC=findViewById(R.id.TT_DESC);
        TT_DESC.setText("Desc:"+db.get("SELECT VAC_DESCRIPCION FROM VACUNA WHERE VAC_CODIGO='"+Cod_vacuna+"';"));
    }
    public void INGRESO(View v)
    {
        Calendar calendar = Calendar.getInstance();
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        String currentDate = dia + "/" + (mes + 1) + "/" + anio;
        EditText TT_FECHA_REN=findViewById(R.id.TT_FECHA_REVACUNACION);
        String TT_FECHA_RE=TT_FECHA_REN.getText().toString();
        db.Instruccion("INSERT INTO DETALLE_VAC VALUES('"+IDCarnet+"','"+Cod_vacuna+"','"+currentDate+"','1','"+TT_FECHA_RE+"','Administrada')","Vacuna aplicada","Error al aplicar vacuna");
    }
    public void ObtenerFecha(View v)
    {
        final Calendar calendar = Calendar.getInstance();
        int anio = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        EditText TT_FECHA_REVACUNACION=findViewById(R.id.TT_FECHA_REVACUNACION);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) ->
                {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    TT_FECHA_REVACUNACION.setText(selectedDate);
                },
                anio, mes, dia);
        datePickerDialog.show();
    }
}