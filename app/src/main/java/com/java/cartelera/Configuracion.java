package com.java.cartelera;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class Configuracion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
            SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                                      String key) {
                    Log.i("aaaaaaaaaaaaaaa", "pulsado");
                    if(key.equals("night_mode")){
                        Log.i("aaaaaaaaaaaaaaa", "pulsado");
                        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        setTheme(SP.getBoolean("night_mode",false)? R.style.AppThemeDark : R.style.AppTheme);
                        Intent intent = new Intent(Configuracion.this, Configuracion.class);
                        startActivity(intent);
                        finish();

                    }
                }
            };

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SP.registerOnSharedPreferenceChangeListener(spChanged);
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if(SP.getBoolean("night_mode",false))
            theme.applyStyle(R.style.AppThemeDark, true);
        else
            theme.applyStyle(R.style.AppTheme, true);

        return theme;
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
    }
}