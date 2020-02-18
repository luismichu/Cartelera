package com.java.cartelera;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PeliAdapter extends BaseAdapter {

    private Context contexto;
    private ArrayList<PeliFB> listaPelis;
    private LayoutInflater lyInf;

    public PeliAdapter(Context contexto, ArrayList<PeliFB> listaPelis) {
        this.contexto = contexto;
        this.listaPelis = listaPelis;
        lyInf = LayoutInflater.from(contexto);
    }

    static class ViewHolder {
        ImageView caratula;
        TextView nombre;
        TextView duracion;
        CheckBox fav;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            convertView = lyInf.inflate(R.layout.item_peli, null);
            viewHolder = new ViewHolder();
            viewHolder.caratula = convertView.findViewById(R.id.it_caratula);
            viewHolder.nombre = convertView.findViewById(R.id.it_etNombre);
            viewHolder.duracion = convertView.findViewById(R.id.it_etDuracion);
            viewHolder.fav = convertView.findViewById(R.id.it_chkFav);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PeliFB peli = listaPelis.get(position);
        viewHolder.caratula.setImageBitmap(BitmapFactory.decodeResource(contexto.getResources(),
                R.drawable.default_img));
        viewHolder.nombre.setText(peli.getNombre());
        viewHolder.duracion.setText("Duración: " + peli.getDuracion());
        viewHolder.fav.setChecked(peli.isFav());

        return convertView;
    }

    @Override
    public int getCount() {
        return listaPelis.size();
    }

    @Override
    public Object getItem(int position) {
        return listaPelis.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
