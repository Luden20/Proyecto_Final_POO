package com.example.veterinaria;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

public class DB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    // SQL para crear la tabla
    private static final String TABLE_CREATE =
            "CREATE TABLE mytable (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER);";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
        db.execSQL(TABLE_CREATE);
        db.execSQL("DELETE FROM mytable");

        db.execSQL("INSERT INTO mytable (name, age) VALUES ('John', 30);");
        db.execSQL("INSERT INTO mytable (name, age) VALUES ('Alice', 25);");
        db.execSQL("INSERT INTO mytable (name, age) VALUES ('Bob', 35);");
        db.execSQL("INSERT INTO mytable (name, age) VALUES ('Emily', 28);");
        db.execSQL("INSERT INTO mytable (name, age) VALUES ('Michael', 40);");
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
}
