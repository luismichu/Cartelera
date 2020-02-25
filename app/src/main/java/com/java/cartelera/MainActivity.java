package com.java.cartelera;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    TabLayout tabLayout;
    ViewPager viewPager;
    public static DataBase db;
    private Menu menu;
    private int pos;
    private DatabaseReference ref;
    private ArrayList<PeliFB> listaPelis;
    private SharedPreferences SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.vPager);

        AdaptaPag ppa = new AdaptaPag(getSupportFragmentManager(), 2);
        viewPager.setAdapter(ppa);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pos = tab.getPosition();
                viewPager.setCurrentItem(tab.getPosition());

                if(pos == 0)
                    PrincipalFragment.actualizar();
                else
                    FavoritosFragment.actualizar();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        db = new DataBase(this);
        ref = FirebaseDatabase.getInstance().getReference();

        listaPelis = new ArrayList<>();
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> iter = dataSnapshot.getChildren();
                for(DataSnapshot d : iter){
                    PeliFB p = d.getValue(PeliFB.class);
                    listaPelis.add(p);
                    Log.i("peli", p.toString());
                    try{
                        db.getPeli(p.getID());
                    }catch(Exception e) {
                        db.insertarFila(p);
                    }
                }

                PrincipalFragment.actualizar();
                FavoritosFragment.actualizar();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("error", "loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.addListenerForSingleValueEvent(postListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.itemConf:
                Intent conf = new Intent(this, Configuracion.class);
                startActivity(conf);
                return true;

            case R.id.itemMapa:
                Intent mapa = new Intent(this, Mapa.class);
                startActivity(mapa);
                return true;

            case R.id.itemAnadirPeli:
                Intent insertarPeli = new Intent(MainActivity.this, InsertarPeli.class);
                startActivity(insertarPeli);
                return true;

            case R.id.itemAcercaDe:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Aplicación creada por Luis Miguel González")
                        .setTitle("Acerca de")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        PrincipalFragment.menu = menu;
        this.menu = menu;
        MenuInflater inflater = this.getMenuInflater();
        inflater.inflate(R.menu.menu_favoritos, menu);

        cambiarMenu(false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int itemSeleccionado = info.position;
        if (pos == 0) {
            switch (item.getItemId()) {
                case R.id.itemFavoritos:
                    PrincipalFragment.itemFavoritos(itemSeleccionado);
                    return true;
                case R.id.itemEliminar:
                    final PeliFB peli = PrincipalFragment.pelis.get(itemSeleccionado);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Eliminar pelicula")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String nombre = peli.getNombre();
                                    db.eliminarFila(peli);
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Pelicula "+nombre+" eliminada correctamente", Toast.LENGTH_SHORT).show();
                                    PrincipalFragment.pelis.remove(peli);
                                    PrincipalFragment.adaptador.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        else{
            switch (item.getItemId()) {
                case R.id.itemFavoritos:
                    cambiarMenu(false);
                    FavoritosFragment.itemFavoritos(itemSeleccionado);
                    return true;
                case R.id.itemEliminar:
                    final PeliFB peli = FavoritosFragment.pelisFav.get(itemSeleccionado);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Eliminar pelicula")
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String nombre = peli.getNombre();
                                    db.eliminarFila(peli);
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Pelicula "+nombre+" eliminada correctamente", Toast.LENGTH_SHORT).show();
                                    FavoritosFragment.pelisFav.remove(FavoritosFragment.pelisFav.indexOf(peli));
                                    FavoritosFragment.adaptador.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    builder.create().show();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
    }

    private void cambiarMenu(boolean fav){
        MenuItem item = menu.findItem(R.id.itemFavoritos);
        if(fav)
            item.setTitle("Quitar de favoritos");
        else
            item.setTitle("Añadir a favoritos");
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(SP.getBoolean("night_mode", false))
            theme.applyStyle(R.style.AppThemeDark, true);
        else
            theme.applyStyle(R.style.AppTheme, true);

        return theme;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(SP.getBoolean("notif", false)) {
            Intent intent = new Intent(this, Mapa.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationManager mNotificationManager;

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), "notify_001");

            NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
            bigText.setBigContentTitle("Pulsa para abrir el mapa con las ubicaciones");
            bigText.setSummaryText("Mapa");

            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setSmallIcon(R.mipmap.ic_launcher_round);
            mBuilder.setContentTitle("Mapa");
            mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
            mBuilder.setStyle(bigText);

            mNotificationManager =
                    (NotificationManager) this.getSystemService(MainActivity.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelId = "Your_channel_id";
                NotificationChannel channel = new NotificationChannel(
                        channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_HIGH);
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId(channelId);
            }

            mNotificationManager.notify(0, mBuilder.build());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}
