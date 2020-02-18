package com.java.cartelera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class VerPeli extends AppCompatActivity implements View.OnClickListener {
    private static PeliFB peli;
    private static DataBase db;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_peli);

        db = PrincipalFragment.db;

        Intent verPeli = getIntent();

        peli = db.getPelis().get(Integer.valueOf(verPeli.getStringExtra("Peli")));

        Button btVolver = findViewById(R.id.btInsertar);
        btVolver.setOnClickListener(this);

        ((TextView)findViewById(R.id.it_etNombre)).setText(peli.getNombre());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        ((TextView)findViewById(R.id.etFecha_salida)).setText(peli.getFecha_salida());
        ((TextView)findViewById(R.id.it_etDuracion)).setText(String.valueOf(peli.getDuracion()));
        ((TextView)findViewById(R.id.etSinopsis)).setText(peli.getSinopsis());
        ((TextView)findViewById(R.id.etReparto)).setText(peli.getReparto());
        ((ImageView)findViewById(R.id.it_caratula)).setImageBitmap(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.default_img));
        ((CheckBox)findViewById(R.id.chkFav)).setChecked(peli.isFav());
    }

    public void onClick(View v){
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_favoritos, menu);
        cambiarMenu(peli.isFav());
        return true;
    }

    private void cambiarMenu(boolean fav){
        MenuItem item = menu.findItem(R.id.itemFavoritos);
        if(fav)
            item.setTitle("Quitar de favoritos");
        else
            item.setTitle("Añadir a favoritos");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.itemFavoritos:
                peli.setFav();
                ((CheckBox)findViewById(R.id.chkFav)).setChecked(peli.isFav());
                db.updateFav(peli.getID(), peli.isFav());
                cambiarMenu(peli.isFav());
                return true;

            case R.id.itemEliminar:
                dialogoConfirmar(peli);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void dialogoConfirmar(final PeliFB peli) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Eliminar pelicula")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = peli.getNombre();
                        db.eliminarFila(peli);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Pelicula "+nombre+" eliminada correctamente", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
