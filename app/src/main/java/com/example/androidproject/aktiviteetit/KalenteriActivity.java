package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.example.androidproject.R;
import com.google.gson.Gson;

public class KalenteriActivity extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kalenteri);

        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Piilotetaan yläpalkki.
        getSupportActionBar().hide();

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // Asetetaan popup-ikkunan koko 60% näytön koosta.
        getWindow().setLayout((int) (width * .6), (int) (height * 0.4));

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