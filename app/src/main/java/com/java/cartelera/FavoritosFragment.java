package com.java.cartelera;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class FavoritosFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static ArrayList<PeliFB> pelisFav;
    public static ArrayAdapter<PeliFB> adaptador;
    public static DataBase db;

    public FavoritosFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favoritos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        pelisFav = new ArrayList<>();
        adaptador = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, pelisFav);
        ListView lvPelisFav = getActivity().findViewById(R.id.lvFav);
        lvPelisFav.setAdapter(adaptador);
        lvPelisFav.setOnItemClickListener(this);
        registerForContextMenu(lvPelisFav);

        db = new DataBase(getContext());
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent verPeli = new Intent(getContext(), VerPeli.class);
        PeliFB peliFav = db.getFav().get(i);
        ArrayList<PeliFB> pelis = db.getPelis();
        int pos = 0;
        for(PeliFB peli:pelis){
            if(peli.getID() == peliFav.getID())
                pos = pelis.indexOf(peli);
        }
        verPeli.putExtra("Peli", String.valueOf(pos));
        startActivity(verPeli);
    }


    public static void itemFavoritos(int itemSeleccionado){
        PeliFB peli = pelisFav.remove(itemSeleccionado);
        peli.setFav();
        adaptador.notifyDataSetChanged();
        db.updateFav(db.getPelis().get(itemSeleccionado).getID(), peli.isFav());
    }

    public static void actualizar(){
        if (pelisFav != null) {
            pelisFav.clear();
            pelisFav.addAll(db.getFav());
            adaptador.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pelisFav.clear();
        pelisFav.addAll(db.getFav());
        adaptador.notifyDataSetChanged();
    }
}
