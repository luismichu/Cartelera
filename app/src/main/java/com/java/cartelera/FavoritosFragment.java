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
    private ArrayList<Peli> pelisFav;
    private ArrayAdapter<Peli> adaptador;
    private DataBase db;

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
        MenuInflater inflater = getActivity().getMenuInflater();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Eliminar pelicula")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = peli.getNombre();
                        db.eliminarFila(peli);
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Pelicula "+nombre+" eliminada correctamente", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onResume() {
        super.onResume();
        pelisFav.clear();
        pelisFav.addAll(db.getFav());
        adaptador.notifyDataSetChanged();
    }
}
