package com.java.cartelera;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.java.cartelera.MainActivity.db;

public class Favoritos extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    private ArrayList<Peli> pelisFav;
    private ArrayAdapter<Peli> adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

        pelisFav = new ArrayList<>();
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pelisFav);
        ListView lvPelisFav = findViewById(R.id.lvFav);
        lvPelisFav.setAdapter(adaptador);
        lvPelisFav.setOnItemClickListener(this);
        registerForContextMenu(lvPelisFav);

        Button btRegresar = findViewById(R.id.btRegresar);
        btRegresar.setOnClickListener(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent verPeli = new Intent(this, VerPeli.class);
        Peli peliFav = db.getFav().get(i);
        ArrayList<Peli> pelis = db.getPelis();
        int pos = 0;
        for(Peli peli:pelis){
            if(peli.getID() == peliFav.getID())
                pos = pelis.indexOf(peli);
        }
        verPeli.putExtra("Peli", String.valueOf(pos));
        startActivity(verPeli);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favoritos, menu);
        MenuItem item = menu.findItem(R.id.itemFavoritos);
        item.setTitle("Quitar de favoritos");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int itemSeleccionado = info.position;

        switch (item.getItemId()) {
            case R.id.itemFavoritos:
                Peli peli = pelisFav.remove(itemSeleccionado);
                peli.setFav();
                adaptador.notifyDataSetChanged();
                db.updateFav(db.getPelis().get(itemSeleccionado).getID(), peli.isFav());
                return true;
            case R.id.itemEliminar:
                dialogoConfirmar(pelisFav.get(itemSeleccionado));
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
                        pelisFav.remove(pelisFav.indexOf(peli));
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

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btRegresar:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pelisFav.clear();
        pelisFav.addAll(db.getFav());
        adaptador.notifyDataSetChanged();
    }
}
