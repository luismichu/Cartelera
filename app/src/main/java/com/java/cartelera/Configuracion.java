package com.java.cartelera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class Configuracion extends AppCompatActivity {
    static boolean theme;
    private SharedPreferences SP;
    private SharedPreferences.OnSharedPreferenceChangeListener spChanged;

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

        spChanged = new
                SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                        final SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        if(key.equals("night_mode") && theme != SP.getBoolean("night_mode",false)){
                            theme = SP.getBoolean("night_mode",false);
                            Intent intent = new Intent(getBaseContext(), MainActivity.class);
                            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NO_ANIMATION);
                            finish();
                            startActivity(intent);
                        }
                    }
                };
        SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SP.registerOnSharedPreferenceChangeListener(spChanged);
        theme = SP.getBoolean("night_mode",false);
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (SP.getBoolean("night_mode", false))
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