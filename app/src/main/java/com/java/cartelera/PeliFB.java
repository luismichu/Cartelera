package com.java.cartelera;


import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class PeliFB {
    private int ID;
    private String nombre;
    private String sinopsis;
    private String fecha_salida;
    private String reparto;
    private int duracion;
    private boolean fav;

    //Constructor para recibir desde DB
    public PeliFB(int ID, String nombre, String sinopsis, String fecha_salida, String reparto, int duracion, boolean fav) {
        this.ID = ID;
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.fecha_salida = fecha_salida;
        this.reparto = reparto;
        this.duracion = duracion;
        this.fav = fav;
    }

    //Constructor para crear Peli a mano + setImagen()
    public PeliFB(String nombre, String sinopsis, String fecha_salida, String reparto, int duracion, boolean fav) {
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.fecha_salida = fecha_salida;
        this.reparto = reparto;
        this.duracion = duracion;
        this.fav = fav;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", ID);
        result.put("nombre", nombre);
        result.put("sinopsis", sinopsis);
        result.put("fecha_salida", fecha_salida);
        result.put("reparto", reparto);
        result.put("duracion", duracion);
        result.put("fav", fav);

        return result;
    }

    public PeliFB(){}

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getReparto() {
        return reparto;
    }

    public void setReparto(String reparto) {
        this.reparto = reparto;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public boolean isFav() { return fav; }

    public boolean setFav() {
        fav = !fav;
        return fav;
    }

    public boolean setFav(boolean fav) {
        this.fav = fav;
        return fav;
    }

    public String toString(){ return nombre; }
}
