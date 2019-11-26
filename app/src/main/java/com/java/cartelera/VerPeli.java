package com.java.cartelera;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class VerPeli extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_peli);

        DataBase db = new DataBase(this);

        Intent verPeli = getIntent();
        Peli peli = null;
        for(Peli peli1:db.getFilas()){
            if(peli1.toString().equals(verPeli.getStringExtra("Peli")))
                peli = peli1;
        }

        Button btVolver = findViewById(R.id.btInsertar);
        btVolver.setOnClickListener(this);

        ((TextView)findViewById(R.id.etNombre)).setText(peli.getNombre());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ((TextView)findViewById(R.id.etFecha_salida)).setText(df.format(peli.getFecha_salida()));
        ((TextView)findViewById(R.id.etDuracion)).setText(String.valueOf(peli.getDuracion()));
        ((TextView)findViewById(R.id.etSinopsis)).setText(peli.getSinopsis());
        ((TextView)findViewById(R.id.etReparto)).setText(peli.getReparto());
    }

    public void onClick(View v){
        onBackPressed();
    }
}
