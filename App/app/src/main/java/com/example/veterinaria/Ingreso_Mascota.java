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
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Ingreso_Mascota extends AppCompatActivity {
    private Spinner SPEspecie;
    private Spinner SPRaza;
    private Spinner SPFiltro;
    private Spinner SPCedulaBuscar;
    public EditText EDBuscado;
    private DB db;
    private String IDEspecie;
    private String IDRaza;
    private String Atributo;
    private String Ingreso;
    private String IDCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ingreso_mascota);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Declaracion de variables
        db=new DB(this);
        Ingreso="";
        SPEspecie = (Spinner)findViewById(R.id.SPEspecie);
        SPCedulaBuscar=findViewById(R.id.SPCedulaBuscar);
        SPRaza = (Spinner)findViewById(R.id.SPRaza);
        SPFiltro=findViewById(R.id.SPFiltro);
        EDBuscado=findViewById(R.id.EDBuscador);
        //Declaracion de listeners
        SPEspecie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPEspecieActionListener(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Método opcional, se llama cuando no se selecciona ningún elemento
            }
        });
        SPRaza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SPRazaActionListener(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Método opcional, se llama cuando no se selecciona ningún elemento
            }
        });
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
        //Instrucciones Iniciales
        SPEspecie.setAdapter(db.getArrayAdapter("SELECT SP_DESCRIPCION FROM ESPECIE;",this));
        SPRaza.setAdapter(db.getArrayAdapter("SELECT SP_DESCRIPCION FROM ESPECIE;",this));
        SPFiltro.setAdapter(db.getAtributeArrayAdapter("SELECT * FROM CLIENTE;",this));
        SPCedulaBuscar.setAdapter(db.getArrayAdapter("SELECT CLI_CEDULA_RUC FROM CLIENTE;",this));
        Ingreso=EDBuscado.getText().toString();
    }
    public void SPEspecieActionListener(View view)
    {
        SPEspecie=findViewById(R.id.SPEspecie);
        String EspecieST=SPEspecie.getSelectedItem().toString();
        IDEspecie=db.get("SELECT SP_CODIGO FROM ESPECIE WHERE SP_DESCRIPCION='"+EspecieST+"';");
        SPRaza.setAdapter(db.getArrayAdapter("SELECT RZ_DESCRIPCION FROM RAZA WHERE SP_CODIGO='"+IDEspecie+"';",this));
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
        TextView TVNombre=findViewById(R.id.TVNombre);
        TVNombre.setText("Nombre:"+db.get("SELECT CLI_NOMBRE FROM CLIENTE WHERE CLI_CEDULA_RUC='"+IDCliente+"';"));
        TextView TVApellido=findViewById(R.id.TVApellido);
        TVApellido.setText("Apellido:"+db.get("SELECT CLI_APELLIDO FROM CLIENTE WHERE CLI_CEDULA_RUC='"+IDCliente+"';"));
        TextView TVTelefono=findViewById(R.id.TVTelefono);
        TVTelefono.setText("Telefono:"+db.get("SELECT CLI_TELEFONO FROM CLIENTE WHERE CLI_CEDULA_RUC='"+IDCliente+"';"));
        TextView TVCorreo=findViewById(R.id.TVCorreo);
        TVCorreo.setText("Correo:"+db.get("SELECT CLI_CORREO FROM CLIENTE WHERE CLI_CEDULA_RUC='"+IDCliente+"';"));
        TextView TVDireccion=findViewById(R.id.TVDireccion);
        TVDireccion.setText("Direcion:"+db.get("SELECT CLI_DIRECCION FROM CLIENTE WHERE CLI_CEDULA_RUC='"+IDCliente+"';"));
    }
    public void SPFiltroActionListener(View view)
    {
        SPFiltro=findViewById(R.id.SPFiltro);
        Atributo=SPFiltro.getSelectedItem().toString();
        SPCedulaBuscar.setAdapter(db.getArrayAdapter("SELECT CLI_CEDULA_RUC FROM CLIENTE WHERE "+Atributo+" LIKE '"+Ingreso+"';",this));
    }
    public void SPRazaActionListener(View view)
    {
        SPRaza=findViewById(R.id.SPRaza);
        String RazaST=SPRaza.getSelectedItem().toString();
        IDRaza=db.get("SELECT rz_CODIGO FROM RAZA WHERE RZ_DESCRIPCION='"+RazaST+"';");
    }

    public void Ingreso(View v)
    {
        EditText ETNombre=findViewById(R.id.ETNombre);
        String Nombre=ETNombre.getText().toString();
        EditText ETColor=findViewById(R.id.ETColor);
        String Color=ETColor.getText().toString();
        EditText ETFecha=findViewById(R.id.ETFecha);
        String Fecha=ETFecha.getText().toString();
        EditText ETDatos=findViewById(R.id.ETDatos);
        String Datos= ETDatos.getText().toString();
        ToggleButton TBSexo=findViewById(R.id.TBSexo);
        String Sexo=TBSexo.getText().toString();
        Integer Cantidad=Integer.parseInt(db.get("SELECT COUNT(*) FROM MASCOTA"))+1;
        String IDMacota=String.format("MSC%03d", Cantidad);;
        db.Instruccion("INSERT INTO MASCOTA VALUES ('"+IDMacota+"', '"+IDCliente+"', '"+IDRaza+"', '"+Nombre+"', '"+Sexo+"', '"+Color+"', '"+Fecha+"', '"+Datos+"', 'Activa');");
        db.Instruccion("INSERT INTO CARNET VALUES('"+IDMacota+"','"+IDMacota+"')");
    }
}