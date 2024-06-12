package com.example.veterinaria;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.veterinaria.DB;
import com.example.veterinaria.R;

public class VerCarnet extends AppCompatActivity {
    private DB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_carnet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        db = new DB(this);
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        spinner.setAdapter(db.getArrayAdapter("SELECT name FROM mytable;",this));
    }
    public void Ver(View v)
    {
        Spinner sp=findViewById(R.id.spinner);
        String SelectedItem=sp.getSelectedItem().toString();
    }
}