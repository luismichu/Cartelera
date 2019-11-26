package com.java.cartelera;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InsertarPeli extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_peli);

        Button btInsertar = findViewById(R.id.btInsertar);
        btInsertar.setOnClickListener(this);
        Button btCancelar = findViewById(R.id.btCancelar);
        btCancelar.setOnClickListener(this);
    }

    public void onClick(View v){
        switch(v.getId()) {
            case R.id.btInsertar:
                EditText etNombre = findViewById(R.id.etNombre);
                EditText etFecha_salida = findViewById(R.id.etFecha_salida);
                EditText etDuracion = findViewById(R.id.etDuracion);
                EditText etSinopsis = findViewById(R.id.etSinopsis);
                EditText etReparto = findViewById(R.id.etReparto);

                Peli peli = new Peli();
                peli.setNombre(etNombre.getText().toString());
                try {
                    peli.setFecha_salida(new SimpleDateFormat("dd/MM/yyyy").parse(etFecha_salida.getText().toString()));
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
                peli.setDuracion(Integer.valueOf(etDuracion.getText().toString()));
                peli.setSinopsis(etSinopsis.getText().toString());
                peli.setReparto(etReparto.getText().toString());

                MainActivity.db.insertarFila(peli);
                onBackPressed();
                break;

            case R.id.btCancelar:
                onBackPressed();
                break;
        }
    }
}
