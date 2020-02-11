package com.java.cartelera;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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

public class PrincipalActivity extends Fragment implements AdapterView.OnItemClickListener {
    public ArrayList<Peli> pelis;
    public PeliAdapter adaptador;
    public static DataBase db;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_principal, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pelis = new ArrayList<>();
        adaptador = new PeliAdapter(super.getContext(), pelis);
        ListView lvPelis = getActivity().findViewById(R.id.lvPelis);
        lvPelis.setAdapter(adaptador);
        lvPelis.setOnItemClickListener(this);
        registerForContextMenu(lvPelis);

        db = new DataBase(super.getContext());

        Log.i("mensaje", "Hasta aqui hemos llegado");
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent verPeli = new Intent(super.getContext(), VerPeli.class);
        verPeli.putExtra("Peli", String.valueOf(i));
        startActivity(verPeli);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.menu = menu;
        MenuInflater inflater = getActivity().getMenuInflater();
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

    public void dialogoConfirmar(final Peli peli) {
        AlertDialog.Builder builder = new AlertDialog.Builder(super.getContext());
        builder.setMessage("Eliminar pelicula")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombre = peli.getNombre();
                        db.eliminarFila(peli);
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Pelicula "+nombre+" eliminada correctamente", Toast.LENGTH_SHORT).show();
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
/*
    @Override
    protected void onResume() {
        super.onResume();
        pelis.clear();
        pelis.addAll(db.getPelis());
        adaptador.notifyDataSetChanged();
    }*/
}

class PrincipalPagerAdapter extends FragmentPagerAdapter {
    public PrincipalPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new PrincipalActivity();
        return fragment;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
