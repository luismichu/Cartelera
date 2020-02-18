package com.java.cartelera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    TabLayout tabLayout;
    ViewPager viewPager;
    public static DataBase db;
    private Menu menu;
    private int pos;
    private DatabaseReference ref;
    private ArrayList<PeliFB> listaPelis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.vPager);

        AdaptaPag ppa = new AdaptaPag(getSupportFragmentManager(), 2);
        viewPager.setAdapter(ppa);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pos = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());

                if(pos == 0)
                    PrincipalFragment.actualizar();
                else
                    FavoritosFragment.actualizar();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        db = new DataBase(this);
        ref = FirebaseDatabase.getInstance().getReference();

//        for(int i=0;i<10;i++)
//            ref.child(String.valueOf(i)).setValue(new PeliFB(i, "Peli"+i, "Sinopsis"+i, "Fecha"+i, "Reparto"+i, i*100, false));

//        for(PeliFB p : db.getPelis())
//            db.eliminarFila(p);

        listaPelis = new ArrayList<>();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                for(DataSnapshot d : iter){
                    PeliFB p = d.getValue(PeliFB.class);
                    listaPelis.add(p);
                    Log.i("peli", p.toString());
                    try{
                        db.getPeli(p.getID());
                    }catch(Exception e) {
                        db.insertarFila(p);
                    }
                }

                PrincipalFragment.actualizar();
                FavoritosFragment.actualizar();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.addListenerForSingleValueEvent(postListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
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

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        PrincipalFragment.menu = menu;
        this.menu = menu;
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_favoritos, menu);

        cambiarMenu(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int itemSeleccionado = info.position;
        if (pos == 0) {
            switch (item.getItemId()) {
                case R.id.itemFavoritos:
                    PrincipalFragment.itemFavoritos(itemSeleccionado);
                    return true;
                case R.id.itemEliminar:
                    final PeliFB peli = PrincipalFragment.pelis.get(itemSeleccionado);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Eliminar pelicula")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String nombre = peli.getNombre();
                                    db.eliminarFila(peli);
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Pelicula "+nombre+" eliminada correctamente", Toast.LENGTH_SHORT).show();
                                    PrincipalFragment.pelis.remove(peli);
                                    PrincipalFragment.adaptador.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        else{
            switch (item.getItemId()) {
                case R.id.itemFavoritos:
                    cambiarMenu(false);
                    FavoritosFragment.itemFavoritos(itemSeleccionado);
                    return true;
                case R.id.itemEliminar:
                    final PeliFB peli = FavoritosFragment.pelisFav.get(itemSeleccionado);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Eliminar pelicula")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String nombre = peli.getNombre();
                                    db.eliminarFila(peli);
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Pelicula "+nombre+" eliminada correctamente", Toast.LENGTH_SHORT).show();
                                    FavoritosFragment.pelisFav.remove(FavoritosFragment.pelisFav.indexOf(peli));
                                    FavoritosFragment.adaptador.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
    }

    private void cambiarMenu(boolean fav){
        MenuItem item = menu.findItem(R.id.itemFavoritos);
        if(fav)
            item.setTitle("Quitar de favoritos");
        else
            item.setTitle("Añadir a favoritos");
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        FavoritosFragment.pelisFav.clear();
        FavoritosFragment.pelisFav.addAll(db.getFav());
        FavoritosFragment.adaptador.notifyDataSetChanged();

        PrincipalFragment.pelis.clear();
        PrincipalFragment.pelis.addAll(db.getPelis());
        PrincipalFragment.adaptador.notifyDataSetChanged();
        */
    }
}
