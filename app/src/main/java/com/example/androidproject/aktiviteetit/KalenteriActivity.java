package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.androidproject.R;
import com.google.gson.Gson;

/**
 * Luo popup-ikkunan, joka näyttää kalenterin.
 * Päivämäärän voi asettaa painamalla kalenterissa päivämäärää.
 */
public class KalenteriActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalenteri);
        getSupportActionBar().hide();

        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();

        /**
         * Asetetaan popup-ikkunan koko 80% näytön leveydestä ja 40% korkeudesta.
         * Asetetaan taustaväri läpinäkyväksi, joka tuo esille popup-ikkunan pyöristetyt reunat.
         */
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getWindow().setLayout((int) (width * .6), (int) (height * 0.4));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /**
         * Asetetaan kalenteriin kuuntelija, joka muuttaa valinnan perusteella päivämäärää
         * ja lopuksi poistaa kalenterin näkyvistä.
         */
        CalendarView kalenteri = findViewById(R.id.kalenteri);
        kalenteri.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                editor.putInt("vuosi", year);
                editor.putInt("kuukausi", month);
                editor.putInt("paiva", dayOfMonth);
                editor.commit();
                finish();
            }
        });

    }
}