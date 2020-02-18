package com.java.cartelera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class PrincipalFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static ArrayList<PeliFB> pelis;
    public static PeliAdapter adaptador;
    public static DataBase db;
    public static Menu menu;

    public PrincipalFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pelis = new ArrayList<>();
        adaptador = new PeliAdapter(getContext(), pelis);
        ListView lvPelis = getActivity().findViewById(R.id.lvPelis);
        lvPelis.setAdapter(adaptador);
        lvPelis.setOnItemClickListener(this);
        registerForContextMenu(lvPelis);

        db = new DataBase(getContext());
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent verPeli = new Intent(getContext(), VerPeli.class);
        verPeli.putExtra("Peli", String.valueOf(i));
        startActivity(verPeli);
    }

    public static void itemFavoritos(int itemSeleccionado){
        PeliFB peli = pelis.get(itemSeleccionado);
        peli.setFav();
        adaptador.notifyDataSetChanged();
        db.updateFav(db.getPelis().get(itemSeleccionado).getID(), peli.isFav());
        cambiarMenu(peli.isFav());
    }

    private static void cambiarMenu(boolean fav){
        MenuItem item = menu.findItem(R.id.itemFavoritos);
        if(fav)
            item.setTitle("Quitar de favoritos");
        else
            item.setTitle("Añadir a favoritos");
    }

    public static void actualizar(){
        if (pelis != null) {
            pelis.clear();
            pelis.addAll(db.getPelis());
            adaptador.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pelis.clear();
        pelis.addAll(db.getPelis());
        adaptador.notifyDataSetChanged();
    }
}
