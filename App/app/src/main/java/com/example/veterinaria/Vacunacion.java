package com.example.veterinaria;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Vacunacion extends AppCompatActivity {
    private Spinner SPFiltro;
    private Spinner SPCedulaBuscar;
    private Spinner SPMascota;
    private Spinner SPVacuna;
    public EditText EDBuscado;
    private EditText EDVacunaBuscada;
    private DB db;
    private String Atributo;
    private String Ingreso;
    public String IngresoVac;
    private String IDCliente;
    private String IDMascota;
    public String IDCarnet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacunacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db=new DB(this);
        Ingreso="";
        IngresoVac="";
        EDVacunaBuscada=findViewById(R.id.EDVacunaBuscada);
        SPCedulaBuscar=findViewById(R.id.SPCedulaBuscar);
        SPVacuna=findViewById(R.id.SPVacuna);
        SPFiltro=findViewById(R.id.SPFiltro);
        SPMascota=findViewById(R.id.SPMascota);
        EDBuscado=findViewById(R.id.EDBuscador);
        SPFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPFiltroActionListener(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        SPCedulaBuscar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPCedulaBuscarActionListener(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        EDBuscado.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EDBuscadoAfterTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

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
        EDVacunaBuscada.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                EDVacunaBuscadaOnTextChanged(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SPVacuna.setAdapter(db.getArrayAdapter("SELECT VAC_CODIGO FROM VACUNA;",this));
        SPMascota.setAdapter(db.getArrayAdapter("SELECT MSC_NOMBRE FROM MASCOTA M INNER JOIN CLIENTE C ON M.CLI_CEDULA_RUC=C.CLI_CEDULA_RUC WHERE M.CLI_CEDULA_RUC='"+IDCliente+"';",this));
        SPFiltro.setAdapter(db.getAtributeArrayAdapter("SELECT * FROM CLIENTE;",this));
        SPCedulaBuscar.setAdapter(db.getArrayAdapter("SELECT CLI_CEDULA_RUC FROM CLIENTE;",this));
        Ingreso=EDBuscado.getText().toString();
    }
    public void EDBuscadoAfterTextChanged(CharSequence s)
    {
        EDBuscado=findViewById(R.id.EDBuscador);
        SPCedulaBuscar=findViewById(R.id.SPCedulaBuscar);
        Ingreso=EDBuscado.getText().toString();
        SPCedulaBuscar.setAdapter(db.getArrayAdapter("SELECT CLI_CEDULA_RUC FROM CLIENTE WHERE "+Atributo+" LIKE '%"+Ingreso+"%';",this));
        Log.d("SQL","SELECT CLI_CEDULA_RUC FROM CLIENTE WHERE "+Atributo+" LIKE '%"+Ingreso+"'%;");
    }
    public void SPCedulaBuscarActionListener(View v)
    {
        SPCedulaBuscar=findViewById(R.id.SPCedulaBuscar);
        IDCliente=SPCedulaBuscar.getSelectedItem().toString();
        SPMascota.setAdapter(db.getArrayAdapter("SELECT MSC_NOMBRE FROM MASCOTA M INNER JOIN CLIENTE C ON M.CLI_CEDULA_RUC=C.CLI_CEDULA_RUC WHERE M.CLI_CEDULA_RUC='"+IDCliente+"';",this));

    }
    public void SPFiltroActionListener(View view)
    {
        SPFiltro=findViewById(R.id.SPFiltro);
        Atributo=SPFiltro.getSelectedItem().toString();
        SPCedulaBuscar.setAdapter(db.getArrayAdapter("SELECT CLI_CEDULA_RUC FROM CLIENTE WHERE "+Atributo+" LIKE '"+Ingreso+"';",this));
    }
    public void SPMascotaActionListener(View v)
    {
        SPMascota=findViewById(R.id.SPMascota);
        String Mascota=SPMascota.getSelectedItem().toString();
        IDMascota=db.get("SELECT MSC_CODIGO FROM MASCOTA M INNER JOIN CLIENTE C ON M.CLI_CEDULA_RUC=C.CLI_CEDULA_RUC WHERE M.CLI_CEDULA_RUC='"+IDCliente+"' AND MSC_NOMBRE='"+Mascota+"';");
        IDCarnet =db.get("SELECT CNT_CODIGO FROM CARNET WHERE MSC_CODIGO='"+IDMascota+"';");
    }
    public void EDVacunaBuscadaOnTextChanged(CharSequence s)
    {
        EDVacunaBuscada=findViewById(R.id.EDVacunaBuscada);
        IngresoVac=EDVacunaBuscada.getText().toString();
        SPVacuna.setAdapter(db.getArrayAdapter("SELECT VAC_CODIGO FROM VACUNA WHERE VAC_CODIGO LIKE '"+IngresoVac+"';",this));

    }
    public void SPVacunaActionListener(View v)
    {

    }
}