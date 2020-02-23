package com.java.cartelera;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.java.cartelera.Constantes.*;

public class DataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private final String[] SELECT = new String[]{_ID, NOMBRE, SINOPSIS, FECHA_SALIDA,
                                                 REPARTO, DURACION, FAV};
    private final String ORDER_BY = "fecha_salida";

    public DataBase(Context contexto) {
        super(contexto, BASE_DATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_PELIS + "(" + _ID + " INTEGER, "
                + NOMBRE + " TEXT, " + SINOPSIS + " TEXT, " + FECHA_SALIDA + " TEXT, "
                + REPARTO + " TEXT, " + DURACION + " INTEGER, " + FAV + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PELIS);
        onCreate(db);
    }

    public void insertarFila(PeliFB peli){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(_ID, peli.getID());
        values.put(NOMBRE, peli.getNombre());
        values.put(SINOPSIS, peli.getSinopsis());
        //DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        //values.put(FECHA_SALIDA, df.format(peli.getFecha_salida()));
        values.put(FECHA_SALIDA, peli.getFecha_salida());
        values.put(REPARTO, peli.getReparto());
        values.put(DURACION, peli.getDuracion());
        values.put(FAV, peli.isFav());

        db.insertOrThrow(TABLA_PELIS, null, values);
        db.close();
    }

    public void eliminarFila(PeliFB peli) {
        SQLiteDatabase db = getWritableDatabase();

        String[] argumentos = new String[]{String.valueOf(peli.getID())};
        db.delete(TABLA_PELIS, "_ID = ?", argumentos);
        db.close();
    }

    public ArrayList<PeliFB> getFilas(Cursor cursor) {
        ArrayList<PeliFB> listaPelis = new ArrayList<>();
        PeliFB peli = null;
        while (cursor.moveToNext()) {
            peli = new PeliFB();
            peli.setID(cursor.getInt(0));
            peli.setNombre(cursor.getString(1));
            peli.setSinopsis(cursor.getString(2));
            peli.setFecha_salida(cursor.getString(3));
            peli.setReparto(cursor.getString(4));
            peli.setDuracion(Integer.valueOf(cursor.getString(5)));
            peli.setFav(cursor.getInt(6) >= 1);

            listaPelis.add(peli);
        }
        cursor.close();

        return listaPelis;
    }

    public ArrayList<PeliFB> getPelis(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_PELIS, SELECT, null, null, null, null,
                ORDER_BY);

        return getFilas(cursor);
    }

    public PeliFB getPeli(int id) throws Exception{
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_PELIS, SELECT, _ID + "=" +id, null, null, null,
                ORDER_BY);

        return getFilas(cursor).get(0);
    }

    public ArrayList<PeliFB> getFav(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_PELIS, SELECT, FAV + ">= 1", null, null, null,
                ORDER_BY);

        return getFilas(cursor);
    }

    public ArrayList<PeliFB> getNoFav(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_PELIS, SELECT, FAV + "= 0", null, null, null,
                ORDER_BY);

        return getFilas(cursor);
    }

    public void updateFav(int id, boolean fav){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAV, fav);
        String[] argumentos = new String[]{String.valueOf(id)};
        db.update(TABLA_PELIS, values,_ID+" = ?", argumentos);
    }
}

class Constantes {
    public static final String BASE_DATOS = "pelis.db";
    public static final String TABLA_PELIS = "pelis";
    public static final String NOMBRE = "nombre";
    public static final String SINOPSIS = "sinopsis";
    public static final String FECHA_SALIDA = "fecha_salida";
    public static final String REPARTO = "reparto";
    public static final String DURACION = "duracion";
    public final static String FAV = "fav";
    public final static String IMAGEN = "imagen";
}
