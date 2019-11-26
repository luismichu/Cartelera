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

    public DataBase(Context contexto) {
        super(contexto, BASE_DATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_PELIS + "(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NOMBRE + " TEXT, " + SINOPSIS + " TEXT, " + FECHA_SALIDA + " TEXT, "
                + REPARTO + " TEXT, " + DURACION + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PELIS);
        onCreate(db);
    }

    public void insertarFila(Peli peli){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOMBRE, peli.getNombre());
        values.put(SINOPSIS, peli.getSinopsis());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        values.put(FECHA_SALIDA, df.format(peli.getFecha_salida()));
        values.put(REPARTO, peli.getReparto());
        values.put(DURACION, peli.getDuracion());

        db.insertOrThrow(TABLA_PELIS, null, values);
        db.close();
    }

    public void eliminarFila(Peli peli) {
        SQLiteDatabase db = getWritableDatabase();

        String[] argumentos = new String[]{String.valueOf(peli.getID())};
        db.delete(TABLA_PELIS, "ID = ?", argumentos);
        db.close();
    }

    public ArrayList<Peli> getFilas() {

        final String[] SELECT = {_ID, NOMBRE, FECHA_SALIDA, DURACION, SINOPSIS,
                REPARTO};
        final String ORDER_BY = "fecha_salida";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLA_PELIS, SELECT, null, null, null, null,
                ORDER_BY);

        ArrayList<Peli> listaPelis = new ArrayList<>();
        Peli peli = null;
        while (cursor.moveToNext()) {
            peli = new Peli();
            peli.setID(cursor.getInt(0));
            peli.setNombre(cursor.getString(1));
            try {
                peli.setFecha_salida(new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(2)));
            } catch(ParseException pe){
                pe.printStackTrace();
            }
            peli.setDuracion(Integer.valueOf(cursor.getString(3)));
            peli.setSinopsis(cursor.getString(4));
            peli.setReparto(cursor.getString(5));

            listaPelis.add(peli);
        }
        cursor.close();
        db.close();

        return listaPelis;
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
}
