package com.example.veterinaria;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.io.Serializable;


public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "vet1.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    private Context c;

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
        c=context;
        try {
            crearBase();
        }
        catch (SQLException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error", e.getMessage());
            //Toast.makeText(context, get("SELECT COUNT(*)  FROM sqlite_master  WHERE type = 'table' AND name NOT LIKE 'sqlite_%';\n"), Toast.LENGTH_SHORT).show();
            //Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public Cursor generarCursor(String query) {
        return db.rawQuery(query,null);
    }
    public String[] getArray(String query)
    {
        Cursor cursor = generarCursor(query);
        String[] aux=new String[cursor.getCount()];
        int i=0;
        while(cursor.moveToNext()) {
            String a=cursor.getString(0);
            aux[i] = a;
            i++;
        }
        return aux;
    }
    public String[] getAllArray(String query) {
        Cursor cursor = generarCursor(query);
        String[] aux = new String[cursor.getCount()];
        int i = 0;

        while (cursor.moveToNext()) {
            String rowData = "";
            for (int j = 0; j < cursor.getColumnCount(); j++) {
                String value = cursor.getString(j);
                rowData += "\n"+value;
            }
            aux[i] = rowData;
            i++;
        }
        cursor.close();
        return aux;
    }
    public String[] getAtributeArray(String query) {
        Cursor cursor = generarCursor(query);
        String[] aux = new String[ cursor.getColumnCount()];

        for (int i = 0; i < cursor.getColumnCount(); i++) {
            aux[i]=cursor.getColumnName(i);
        }
        cursor.close();
        return aux;
    }
    public ArrayAdapter<String> getAllArrayAdapter(String sql, Context context) {
        String[] opciones = getAllArray(sql);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public ArrayAdapter<String> getArrayAdapter(String sql, Context context) {
        String[] opciones = getArray(sql);
        Log.d("SQL Result",opciones.toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public ArrayAdapter<String> getAtributeArrayAdapter(String sql, Context context) {
        String[] opciones = getAtributeArray(sql);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public String get(String sql)
    {
        try {
            Cursor cursor = generarCursor(sql);
            String aux = "null";
            if (cursor.moveToFirst()) {
                aux = cursor.getString(0);
            }
            return aux;
        }
        catch(SQLException e)
        {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e("SQLERROR",e.getMessage());
            return e.getMessage();
        }

    }

    public boolean UsuarioExiste(String usuario,String Password)
    {
        String Existe=get("SELECT CLI_CEDULA_RUC FROM CLIENTE WHERE CLI_CEDULA_RUC='"+usuario+"' AND CLI_CONTRASENA ='"+Password+"';");
        if (Existe!="null")
        {
            return true;
        }
        return false;
    }
    public void crearBase()
    {
        db.execSQL("create table CLIENTE (CLI_CEDULA_RUC VARCHAR(13) not null, CLI_NOMBRE VARCHAR(60), CLI_APELLIDO VARCHAR(60), CLI_TELEFONO VARCHAR(13), CLI_CORREO VARCHAR(60), CLI_DIRECCION VARCHAR(70), CLI_CONTRASENA VARCHAR(8), primary key (CLI_CEDULA_RUC));");
        db.execSQL("create table ESPECIE (SP_CODIGO VARCHAR(5) not null, SP_DESCRIPCION VARCHAR(60), primary key (SP_CODIGO));");
        db.execSQL("create table RAZA (RZ_CODIGO VARCHAR(8) not null, SP_CODIGO VARCHAR(5) not null, RZ_DESCRIPCION VARCHAR(60), primary key (RZ_CODIGO), foreign key (SP_CODIGO) references ESPECIE (SP_CODIGO));");
        db.execSQL("create table MASCOTA (MSC_CODIGO VARCHAR(15) not null, CLI_CEDULA_RUC VARCHAR(13) not null, RZ_CODIGO VARCHAR(8) not null, MSC_NOMBRE VARCHAR(60), MSC_SEXO VARCHAR(1), MSC_COLOR VARCHAR(10), MSC_FECHA DATE, MSC_DATOS VARCHAR(100), MSC_ESTADO VARCHAR(15), primary key (MSC_CODIGO), foreign key (CLI_CEDULA_RUC) references CLIENTE (CLI_CEDULA_RUC), foreign key (RZ_CODIGO) references RAZA (RZ_CODIGO));");
        db.execSQL("create table CARNET (CNT_CODIGO VARCHAR(8) not null, MSC_CODIGO VARCHAR(15) not null, primary key (CNT_CODIGO), foreign key (MSC_CODIGO) references MASCOTA (MSC_CODIGO));");
        db.execSQL("create unique index CARNET_PK on CARNET (CNT_CODIGO ASC);");
        db.execSQL("create index MASCOTA_TIENE_CARNET_FK on CARNET (MSC_CODIGO ASC);");
        db.execSQL("create unique index CLIENTE_PK on CLIENTE (CLI_CEDULA_RUC ASC);");
        db.execSQL("create table VACUNA (VAC_CODIGO VARCHAR(8) not null, VAC_DESCRIPCION VARCHAR(60), VAC_FABRICANTE VARCHAR(60), primary key (VAC_CODIGO));");
        db.execSQL("create table DETALLE_VAC (CNT_CODIGO VARCHAR(8) not null, VAC_CODIGO VARCHAR(8) not null, DVC_FECHA_VAC DATE, DVC_LOTE VARCHAR(10), DVC_REFECHA_VAC DATE, DVC_ESTADO VARCHAR(15), foreign key (CNT_CODIGO) references CARNET (CNT_CODIGO), foreign key (VAC_CODIGO) references VACUNA (VAC_CODIGO));");
        db.execSQL("create index CARNET_TIENE_DET_VAC_FK on DETALLE_VAC (CNT_CODIGO ASC);");
        db.execSQL("create index VAC_TIENE_DET_VAC_FK on DETALLE_VAC (VAC_CODIGO ASC);");
        db.execSQL("create unique index ESPECIE_PK on ESPECIE (SP_CODIGO ASC);");
        db.execSQL("create unique index MASCOTA_PK on MASCOTA (MSC_CODIGO ASC);");
        db.execSQL("create index CLIENTE_TIENE_MASCOTA_FK on MASCOTA (CLI_CEDULA_RUC ASC);");
        db.execSQL("create index RAZA_TIENE_MASCOTA_FK on MASCOTA (RZ_CODIGO ASC);");
        db.execSQL("create unique index RAZA_PK on RAZA (RZ_CODIGO ASC);");
        db.execSQL("create index ESPECIE_TIENE_RAZA_FK on RAZA (SP_CODIGO ASC);");
        db.execSQL("create unique index VACUNA_PK on VACUNA (VAC_CODIGO ASC);");
        db.execSQL("create unique index VACUNA_RABIA_PK on VACUNA_RABIA (VCRB_CODIGO ASC);");
        db.execSQL("create table VETERINARIO (VET_CEDULA_RUC VARCHAR(13) not null, VET_NOMBRE VARCHAR(60), VET_APELLIDO VARCHAR(60), VET_TELEFONO VARCHAR(13), VET_CORREO VARCHAR(60), VET_DIRECCION VARCHAR(70), VET_CONTRASENA VARCHAR(8), primary key (VET_CEDULA_RUC));");
        db.execSQL("create unique index VETERINARIO_PK on VETERINARIO (VET_CEDULA_RUC ASC);");
    }
    public boolean Instruccion(String SQL)
    {
        try
        {
            db.execSQL(SQL);
            return true;
        }
        catch (SQLException e)
        {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e("SQLERROR",e.getMessage());
            return false;
        }
    }
}
