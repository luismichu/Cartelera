package com.java.cartelera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    public ArrayList<Peli> pelis;
    public ArrayAdapter<Peli> adaptador;
    public static DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pelis = new ArrayList<>();
        adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pelis);
        ListView lvPelis = findViewById(R.id.lvPelis);
        lvPelis.setAdapter(adaptador);
        lvPelis.setOnItemClickListener(this);
        registerForContextMenu(lvPelis);

        db = new DataBase(this);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent verPeli = new Intent(MainActivity.this, VerPeli.class);
        verPeli.putExtra("Peli", pelis.get(i).toString());
        startActivity(verPeli);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
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
                //TODO
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
                //TODO
                return true;

            case R.id.itemAnadirPeli:
                Intent insertarPeli = new Intent(MainActivity.this, InsertarPeli.class);
                startActivity(insertarPeli);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pelis.clear();
        pelis.addAll(db.getFilas());
        adaptador.notifyDataSetChanged();
    }
}
