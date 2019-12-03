package com.java.cartelera;

import android.graphics.Bitmap;

import java.util.Date;

public class Peli {
    private int ID;
    private String nombre;
    private String sinopsis;
    private Date fecha_salida;
    private String reparto;
    private int duracion;
    private boolean fav;
    private Bitmap imagen;

    //Constructor para recibir desde DB
    public Peli(int ID, String nombre, String sinopsis, Date fecha_salida, String reparto, int duracion, boolean fav, Bitmap imagen) {
        this.ID = ID;
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.fecha_salida = fecha_salida;
        this.reparto = reparto;
        this.duracion = duracion;
        this.fav = fav;
        this.imagen = imagen;
    }

    //Constructor para crear Peli a mano + setImagen()
    public Peli(String nombre, String sinopsis, Date fecha_salida, String reparto, int duracion, boolean fav) {
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.fecha_salida = fecha_salida;
        this.reparto = reparto;
        this.duracion = duracion;
        this.fav = fav;
    }

    public Peli(){}

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

    public Date getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(Date fecha_salida) {
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

    public Bitmap getImagen() { return imagen; }

    public void setImagen(Bitmap imagen) { this.imagen = imagen; }

    public String toString(){ return nombre; }
}
