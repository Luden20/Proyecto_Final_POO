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

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 2;
    private SQLiteDatabase db;

    // SQL para crear la tabla
    private static final String TABLE_CREATE =
            "CREATE TABLE mytable (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER);";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
        try {
            CrearBase();
            insertarVacunaGato2();
        }
        catch (SQLException e)
        {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error", e.getMessage());
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS mytable");
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
    public ArrayAdapter<String> getAllArrayAdapter(String sql, Context context) {
        String[] opciones = getAllArray(sql);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public ArrayAdapter<String> getArrayAdapter(String sql, Context context) {
        String[] opciones = getArray(sql);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }
    public String get(String sql)
    {
        Cursor cursor = generarCursor(sql);
        String aux="null";
        if(cursor.moveToFirst())
        {
            aux=cursor.getString(0);
        }
        return aux;
    }

    public boolean UsuarioExiste(String usuario,String Password)
    {
        String Existe=get("SELECT CLI_CEDULA_RUC FROM CLIENTE WHERE CLI_CEDULA_RUC='"+usuario+"' AND CLI__CONTRASENA='"+Password+"';");
        if (Existe!="null")
        {
            return true;
        }
        return false;
    }
    public void CrearBase()
    {
        // Creación de la tabla CLIENTE
        String SQL_CLIENTE = "create table if not exists CLIENTE (" +
                "CLI_CEDULA_RUC VARCHAR(13) not null," +
                "CLI_NOMBRE VARCHAR(60)," +
                "CLI_APELLIDO VARCHAR(60)," +
                "CLI__TELEFONO VARCHAR(13)," +
                "CLI__CORREO VARCHAR(60)," +
                "CLI__DIRECCION VARCHAR(70)," +
                "CLI__CONTRASENA VARCHAR(8)," +
                "primary key (CLI_CEDULA_RUC)" +
                ");";
        db.execSQL(SQL_CLIENTE);

        String SQL_CLIENTE_PK = "create unique index if not exists CLIENTE_PK on CLIENTE (CLI_CEDULA_RUC ASC);";
        db.execSQL(SQL_CLIENTE_PK);

        // Creación de la tabla ESPECIE
        String SQL_ESPECIE = "create table if not exists ESPECIE (" +
                "SP_CODIGO VARCHAR(5) not null," +
                "SP_DESCRIPCION VARCHAR(60)," +
                "primary key (SP_CODIGO)" +
                ");";
        db.execSQL(SQL_ESPECIE);

        String SQL_ESPECIE_PK = "create unique index if not exists ESPECIE_PK on ESPECIE (SP_CODIGO ASC);";
        db.execSQL(SQL_ESPECIE_PK);

        // Creación de la tabla RAZA
        String SQL_RAZA = "create table if not exists RAZA (" +
                "RZ_CODIGO VARCHAR(8) not null," +
                "SP_CODIGO VARCHAR(5) not null," +
                "RZ_DESCRIPCION VARCHAR(60)," +
                "primary key (RZ_CODIGO)," +
                "foreign key (SP_CODIGO) references ESPECIE (SP_CODIGO)" +
                ");";
        db.execSQL(SQL_RAZA);

        String SQL_RAZA_PK = "create unique index if not exists RAZA_PK on RAZA (RZ_CODIGO ASC);";
        db.execSQL(SQL_RAZA_PK);

        String SQL_ESPECIE_TIENE_RAZA_FK = "create index if not exists ESPECIE_TIENE_RAZA_FK on RAZA (SP_CODIGO ASC);";
        db.execSQL(SQL_ESPECIE_TIENE_RAZA_FK);

        // Creación de la tabla MASCOTA
        String SQL_MASCOTA = "create table if not exists MASCOTA (" +
                "MSC_CODIGO VARCHAR(15) not null," +
                "CLI_CEDULA_RUC VARCHAR(13) not null," +
                "RZ_CODIGO VARCHAR(8) not null," +
                "MSC_NOMBRE VARCHAR(60)," +
                "MSC_SEXO VARCHAR(1)," +
                "MSC_COLOR VARCHAR(10)," +
                "MSC_FECHA DATE," +
                "MSC_DATOS VARCHAR(100)," +
                "MSC_ESTADO VARCHAR(15)," +
                "primary key (MSC_CODIGO)," +
                "foreign key (CLI_CEDULA_RUC) references CLIENTE (CLI_CEDULA_RUC)," +
                "foreign key (RZ_CODIGO) references RAZA (RZ_CODIGO)" +
                ");";
        db.execSQL(SQL_MASCOTA);

        String SQL_MASCOTA_PK = "create unique index if not exists MASCOTA_PK on MASCOTA (MSC_CODIGO ASC);";
        db.execSQL(SQL_MASCOTA_PK);

        String SQL_CLIENTE_TIENE_MASCOTA_FK = "create index if not exists CLIENTE_TIENE_MASCOTA_FK on MASCOTA (CLI_CEDULA_RUC ASC);";
        db.execSQL(SQL_CLIENTE_TIENE_MASCOTA_FK);

        String SQL_RAZA_TIENE_MASCOTA_FK = "create index if not exists RAZA_TIENE_MASCOTA_FK on MASCOTA (RZ_CODIGO ASC);";
        db.execSQL(SQL_RAZA_TIENE_MASCOTA_FK);

        // Creación de la tabla CARNET
        String SQL_CARNET = "create table if not exists CARNET (" +
                "CNT_CODIGO VARCHAR(8) not null," +
                "MSC_CODIGO VARCHAR(15) not null," +
                "primary key (CNT_CODIGO)," +
                "foreign key (MSC_CODIGO) references MASCOTA (MSC_CODIGO)" +
                ");";
        db.execSQL(SQL_CARNET);

        String SQL_CARNET_PK = "create unique index if not exists CARNET_PK on CARNET (CNT_CODIGO ASC);";
        db.execSQL(SQL_CARNET_PK);

        String SQL_MASCOTA_TIENE_CARNET_FK = "create index if not exists MASCOTA_TIENE_CARNET_FK on CARNET (MSC_CODIGO ASC);";
        db.execSQL(SQL_MASCOTA_TIENE_CARNET_FK);

        // Creación de la tabla DESPARACITACION
        String SQL_DESPARACITACION = "create table if not exists DESPARACITACION (" +
                "DST_CODIGO VARCHAR(8) not null," +
                "DST_DESCRIPCION VARCHAR(60)," +
                "DST_LOTE VARCHAR(10)," +
                "DST_FABRICANTE VARCHAR(60)," +
                "primary key (DST_CODIGO)" +
                ");";
        db.execSQL(SQL_DESPARACITACION);

        String SQL_DESPARACITACION_PK = "create unique index if not exists DESPARACITACION_PK on DESPARACITACION (DST_CODIGO ASC);";
        db.execSQL(SQL_DESPARACITACION_PK);

        // Creación de la tabla DETALLE_DESPARACITACION
        String SQL_DETALLE_DESPARACITACION = "create table if not exists DETALLE_DESPARACITACION (" +
                "CNT_CODIGO VARCHAR(8) not null," +
                "DST_CODIGO VARCHAR(8) not null," +
                "DCD_FECHA_VAC DATE," +
                "DCD_REFECHA_VAS DATE," +
                "DCD_ESTADO VARCHAR(15)," +
                "foreign key (CNT_CODIGO) references CARNET (CNT_CODIGO)," +
                "foreign key (DST_CODIGO) references DESPARACITACION (DST_CODIGO)" +
                ");";
        db.execSQL(SQL_DETALLE_DESPARACITACION);

        String SQL_CARNET_TIENE_DET_DES_FK = "create index if not exists CARNET_TIENE_DET_DES_FK on DETALLE_DESPARACITACION (CNT_CODIGO ASC);";
        db.execSQL(SQL_CARNET_TIENE_DET_DES_FK);

        String SQL_DESP_TIENE_DET_DES_FK = "create index if not exists DESP_TIENE_DET_DES_FK on DETALLE_DESPARACITACION (DST_CODIGO ASC);";
        db.execSQL(SQL_DESP_TIENE_DET_DES_FK);

        // Creación de la tabla VACUNA_CONTROL
        String SQL_VACUNA_CONTROL = "create table if not exists VACUNA_CONTROL (" +
                "VNCT_CODIGO VARCHAR(8) not null," +
                "VNCT_DESCRIPCION VARCHAR(60)," +
                "VNCT_LOTE VARCHAR(10)," +
                "VNCT_FABRICANTE VARCHAR(60)," +
                "primary key (VNCT_CODIGO)" +
                ");";
        db.execSQL(SQL_VACUNA_CONTROL);

        String SQL_VACUNA_CONTROL_PK = "create unique index if not exists VACUNA_CONTROL_PK on VACUNA_CONTROL (VNCT_CODIGO ASC);";
        db.execSQL(SQL_VACUNA_CONTROL_PK);

        // Creación de la tabla DETALLE_VAC_CONTROL
        String SQL_DETALLE_VAC_CONTROL = "create table if not exists DETALLE_VAC_CONTROL (" +
                "CNT_CODIGO VARCHAR(8) not null," +
                "VNCT_CODIGO VARCHAR(8) not null," +
                "DCC_FECHA_VAC DATE," +
                "DCC_REFECHA_VAC DATE," +
                "DCC_ESTADO VARCHAR(15)," +
                "foreign key (CNT_CODIGO) references CARNET (CNT_CODIGO)," +
                "foreign key (VNCT_CODIGO) references VACUNA_CONTROL (VNCT_CODIGO)" +
                ");";
        db.execSQL(SQL_DETALLE_VAC_CONTROL);

        String SQL_CARNET_TIENE_DET_CON_FK = "create index if not exists CARNET_TIENE_DET_CON_FK on DETALLE_VAC_CONTROL (CNT_CODIGO ASC);";
        db.execSQL(SQL_CARNET_TIENE_DET_CON_FK);

        String SQL_CON_TIENE_DET_CON_FK = "create index if not exists CON_TIENE_DET_CON_FK on DETALLE_VAC_CONTROL (VNCT_CODIGO ASC);";
        db.execSQL(SQL_CON_TIENE_DET_CON_FK);

        // Creación de la tabla VACUNA_RABIA
        String SQL_VACUNA_RABIA = "create table if not exists VACUNA_RABIA (" +
                "VCRB_CODIGO VARCHAR(8) not null," +
                "VCRB_DESCRIPCION VARCHAR(60)," +
                "VCRB_LOTE VARCHAR(10)," +
                "VCRB_FABRICANTE VARCHAR(60)," +
                "primary key (VCRB_CODIGO)" +
                ");";
        db.execSQL(SQL_VACUNA_RABIA);

        String SQL_VACUNA_RABIA_PK = "create unique index if not exists VACUNA_RABIA_PK on VACUNA_RABIA (VCRB_CODIGO ASC);";
        db.execSQL(SQL_VACUNA_RABIA_PK);

        // Creación de la tabla DETALLE_VAC_RABIA
        String SQL_DETALLE_VAC_RABIA = "create table if not exists DETALLE_VAC_RABIA (" +
                "CNT_CODIGO VARCHAR(8) not null," +
                "VCRB_CODIGO VARCHAR(8) not null," +
                "DCR_FECHA_VAC DATE," +
                "DCR_REFECHA_VAC DATE," +
                "DCC_ESTADO VARCHAR(15)," +
                "foreign key (CNT_CODIGO) references CARNET (CNT_CODIGO)," +
                "foreign key (VCRB_CODIGO) references VACUNA_RABIA (VCRB_CODIGO)" +
                ");";
        db.execSQL(SQL_DETALLE_VAC_RABIA);

        String SQL_CARNET_TIENE_DET_RAB_FK = "create index if not exists CARNET_TIENE_DET_RAB_FK on DETALLE_VAC_RABIA (CNT_CODIGO ASC);";
        db.execSQL(SQL_CARNET_TIENE_DET_RAB_FK);

        String SQL_VAC_RAB_TIENE_DET_RAB_FK = "create index if not exists VAC_RAB_TIENE_DET_RAB_FK on DETALLE_VAC_RABIA (VCRB_CODIGO ASC);";
        db.execSQL(SQL_VAC_RAB_TIENE_DET_RAB_FK);
    }

    public void IngresoPrueba()
    {
        db.execSQL("INSERT INTO CLIENTE (CLI_CEDULA_RUC, CLI_NOMBRE, CLI_APELLIDO, CLI__TELEFONO, CLI__CORREO, CLI__DIRECCION, CLI__CONTRASENA) VALUES ('1234567890123', 'Juan', 'Perez', '0991234567', 'juan.perez@example.com', 'Av. Siempre Viva 123', 'password');");
        db.execSQL("INSERT INTO ESPECIE (SP_CODIGO, SP_DESCRIPCION) VALUES ('001', 'Perro');");
        db.execSQL("INSERT INTO RAZA (RZ_CODIGO, SP_CODIGO, RZ_DESCRIPCION) VALUES ('0001', '001', 'Labrador');");
        db.execSQL("INSERT INTO MASCOTA (MSC_CODIGO, CLI_CEDULA_RUC, RZ_CODIGO, MSC_NOMBRE, MSC_SEXO, MSC_COLOR, MSC_FECHA, MSC_DATOS, MSC_ESTADO) VALUES ('M001', '1234567890123', '0001', 'Max', 'M', 'Negro', '2023-01-01', 'Datos adicionales sobre la mascota', 'Activo');");
    }

    public void IngresoPrueba2() {

        // Insertar una nueva especie (Gato)
        db.execSQL("INSERT INTO ESPECIE (SP_CODIGO, SP_DESCRIPCION) VALUES ('002', 'Gato');");

        // Insertar una nueva raza para el gato
        db.execSQL("INSERT INTO RAZA (RZ_CODIGO, SP_CODIGO, RZ_DESCRIPCION) VALUES ('0002', '002', 'Siamés');");

        // Insertar una nueva mascota (gato) asociada al cliente
        db.execSQL("INSERT INTO MASCOTA (MSC_CODIGO, CLI_CEDULA_RUC, RZ_CODIGO, MSC_NOMBRE, MSC_SEXO, MSC_COLOR, MSC_FECHA, MSC_DATOS, MSC_ESTADO) " +
                "VALUES ('M002', '1234567890123', '0002', 'Minino', 'M', 'Blanco', '2023-02-15', 'Datos adicionales sobre la mascota gato', 'Activo');");
    }
    public void insertarVacunaGato() {
        // Datos de ejemplo para la nueva vacuna
        String vnctCodigo = "V0001"; // Debe ser único y no estar repetido en la tabla
        String vnctDescripcion = "Vacuna para Gato";
        String vnctLote = "L001";
        String vnctFabricante = "Fabricante X";
        String dccFechaVac = "2023-06-12"; // Fecha de vacunación
        String dccRefechaVac = "2023-07-12"; // Fecha de próxima vacunación
        String dccEstado = "Activo"; // Estado de la vacunación

        // Sentencia SQL para insertar en VACUNA_CONTROL
        String sqlInsertVacuna = "INSERT INTO VACUNA_CONTROL (VNCT_CODIGO, VNCT_DESCRIPCION, VNCT_LOTE, VNCT_FABRICANTE) " +
                "VALUES ('" + vnctCodigo + "', '" + vnctDescripcion + "', '" + vnctLote + "', '" + vnctFabricante + "');";
        db.execSQL("INSERT INTO CARNET (CNT_CODIGO, MSC_CODIGO) VALUES ('CNT001', 'M002');");
        // Sentencia SQL para insertar en DETALLE_VAC_CONTROL
        String sqlInsertDetalle = "INSERT INTO DETALLE_VAC_CONTROL (CNT_CODIGO, VNCT_CODIGO, DCC_FECHA_VAC, DCC_REFECHA_VAC, DCC_ESTADO) " +
                "VALUES ('CNT001', '" + vnctCodigo + "', '" + dccFechaVac + "', '" + dccRefechaVac + "', '" + dccEstado + "');";

        // Ejecutar las sentencias SQL usando tu objeto de base de datos db
        db.execSQL(sqlInsertVacuna);
        db.execSQL(sqlInsertDetalle);
    }
    public void insertarVacunaGato2() {
        // Datos de ejemplo para la nueva vacuna
        String vnctCodigo = "V0032"; // Debe ser único y no estar repetido en la tabla
        String vnctDescripcion = "Otra Vacuna para Gato";
        String vnctLote = "L002";
        String vnctFabricante = "Fabricante Y";
        String dccFechaVac = "2023-08-15"; // Fecha de vacunación
        String dccRefechaVac = "2023-09-15"; // Fecha de próxima vacunación
        String dccEstado = "Activo"; // Estado de la vacunación

        // Sentencia SQL para insertar en VACUNA_CONTROL
        String sqlInsertVacuna = "INSERT INTO VACUNA_CONTROL (VNCT_CODIGO, VNCT_DESCRIPCION, VNCT_LOTE, VNCT_FABRICANTE) " +
                "VALUES ('" + vnctCodigo + "', '" + vnctDescripcion + "', '" + vnctLote + "', '" + vnctFabricante + "');";

        // Sentencia SQL para insertar en DETALLE_VAC_CONTROL
        String sqlInsertDetalle = "INSERT INTO DETALLE_VAC_CONTROL (CNT_CODIGO, VNCT_CODIGO, DCC_FECHA_VAC, DCC_REFECHA_VAC, DCC_ESTADO) " +
                "VALUES ('CNT001', '" + vnctCodigo + "', '" + dccFechaVac + "', '" + dccRefechaVac + "', '" + dccEstado + "');";

        // Ejecutar las sentencias SQL usando tu objeto de base de datos db
        db.execSQL(sqlInsertVacuna);
        db.execSQL(sqlInsertDetalle);
    }


}
