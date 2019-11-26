package com.java.cartelera;

import java.util.Date;

public class Peli {
    private int ID;
    private String nombre;
    private String sinopsis;
    private Date fecha_salida;
    private String reparto;
    private int duracion;

    public Peli(int ID, String nombre, String sinopsis, Date fecha_salida, String reparto, int duracion) {
        this.ID = ID;
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.fecha_salida = fecha_salida;
        this.reparto = reparto;
        this.duracion = duracion;
    }

    public Peli(String nombre, String sinopsis, Date fecha_salida, String reparto, int duracion) {
        this.nombre = nombre;
        this.sinopsis = sinopsis;
        this.fecha_salida = fecha_salida;
        this.reparto = reparto;
        this.duracion = duracion;
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

    public String toString(){ return nombre; }
}
