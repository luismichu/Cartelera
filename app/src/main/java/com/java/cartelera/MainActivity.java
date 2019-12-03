package com.java.cartelera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public ArrayList<Peli> pelis;
    public PeliAdapter adaptador;
    public static DataBase db;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pelis = new ArrayList<>();
        adaptador = new PeliAdapter(this, pelis);
        ListView lvPelis = findViewById(R.id.lvPelis);
        lvPelis.setAdapter(adaptador);
        lvPelis.setOnItemClickListener(this);
        registerForContextMenu(lvPelis);

        db = new DataBase(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent verPeli = new Intent(MainActivity.this, VerPeli.class);
        verPeli.putExtra("Peli", String.valueOf(i));
        startActivity(verPeli);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favoritos, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int itemSeleccionado = info.position;

        switch (item.getItemId()) {
            case R.id.itemFavoritos:
                Peli peli = pelis.get(itemSeleccionado);
                peli.setFav();
                adaptador.notifyDataSetChanged();
                db.updateFav(db.getPelis().get(itemSeleccionado).getID(), peli.isFav());
                cambiarMenu(peli.isFav());
                return true;
            case R.id.itemEliminar:
                dialogoConfirmar(pelis.get(itemSeleccionado));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.itemFav:
                Intent favoritos = new Intent(MainActivity.this, Favoritos.class);
                startActivity(favoritos);
                return true;

            case R.id.itemAnadirPeli:
                Intent insertarPeli = new Intent(MainActivity.this, InsertarPeli.class);
                startActivity(insertarPeli);
                return true;

            case R.id.itemAcercaDe:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Aplicación creada por Luis Miguel González")
                        .setTitle("Acerca de")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void dialogoConfirmar(final Peli peli) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Eliminar pelicula")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = peli.getNombre();
                        db.eliminarFila(peli);
                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Pelicula "+nombre+" eliminada correctamente", Toast.LENGTH_SHORT).show();
                        pelis.remove(pelis.indexOf(peli));
                        adaptador.notifyDataSetChanged();
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

    private void cambiarMenu(boolean fav){
        MenuItem item = menu.findItem(R.id.itemFavoritos);
        if(fav)
            item.setTitle("Quitar de favoritos");
        else
            item.setTitle("Añadir a favoritos");
    }

    @Override
    protected void onResume() {
        super.onResume();
        pelis.clear();
        pelis.addAll(db.getPelis());
        adaptador.notifyDataSetChanged();
    }
}
