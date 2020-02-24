package com.java.cartelera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class InsertarPeli extends AppCompatActivity implements View.OnClickListener {
    private final int FOTO_TAREA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insertar_peli);

        Button btInsertar = findViewById(R.id.btInsertar);
        btInsertar.setOnClickListener(this);
        Button btCancelar = findViewById(R.id.btCancelar);
        btCancelar.setOnClickListener(this);
        ImageView it_caratula = findViewById(R.id.it_caratula);
        it_caratula.setOnClickListener(this);
    }

    public void onClick(View v){
        switch(v.getId()) {
            case R.id.btInsertar:
                EditText etNombre = findViewById(R.id.it_etNombre);
                EditText etFecha_salida = findViewById(R.id.etFecha_salida);
                EditText etDuracion = findViewById(R.id.it_etDuracion);
                EditText etSinopsis = findViewById(R.id.etSinopsis);
                EditText etReparto = findViewById(R.id.etReparto);
                ImageView caratula = findViewById(R.id.it_caratula);
                CheckBox fav = findViewById(R.id.fav);

                boolean correcto = false;
                PeliFB peli = new PeliFB();

                if(etNombre.getText().toString().equals("") ||
                        etFecha_salida.getText().toString().equals("") ||
                        etDuracion.getText().toString().equals("") ||
                        etSinopsis.getText().toString().equals("") ||
                        etReparto.getText().toString().equals("")) {
                    Toast.makeText(this, "Faltan campos por rellenar", Toast.LENGTH_SHORT).show();

                } else{
                    correcto = true;
                    peli.setNombre(etNombre.getText().toString());
                    peli.setFecha_salida(etFecha_salida.getText().toString());
                    peli.setDuracion(Integer.valueOf(etDuracion.getText().toString()));
                    peli.setSinopsis(etSinopsis.getText().toString());
                    peli.setReparto(etReparto.getText().toString());
                    peli.setFav(fav.isChecked());
                }
                if(correcto){
                    new DataBase(this).insertarFila(peli);
                    onBackPressed();
                }
                break;

            case R.id.btCancelar:
                onBackPressed();
                break;

            case R.id.it_caratula:
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, FOTO_TAREA);
                }
                break;
        }
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(SP.getBoolean("night_mode", false))
            theme.applyStyle(R.style.AppThemeDark, true);
        else
            theme.applyStyle(R.style.AppTheme, true);

        return theme;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == FOTO_TAREA) && (resultCode == RESULT_OK) && (data != null)) {
            Uri imagenSeleccionada = data.getData();
            String selectedPath = imagenSeleccionada.getPath();

            if (selectedPath != null) {
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(
                            imagenSeleccionada);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bmp = BitmapFactory.decodeStream(imageStream);

                ImageView caratula = findViewById(R.id.it_caratula);
                caratula.setImageBitmap(bmp);
            }
        }
    }
}
