package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.androidproject.R;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Profiili extends AppCompatActivity {
    private TextView pronimi, propaino, proBMI, asetetut, tavoite1, tavoite2;
    public SharedPreferences tiedot;
    private float paino, pituus;
    private double bmi;
    private String nimi, tiedot1, tiedot2;
    LineChart historia;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiili);

        pronimi = findViewById(R.id.proNimi);
        propaino = findViewById(R.id.proPaino);
        proBMI = findViewById(R.id.proBMI);
        asetetut = findViewById(R.id.tavoitteet);
        tavoite1 = findViewById(R.id.eka);
        tavoite2 = findViewById(R.id.toka);
        historia = findViewById(R.id.historia);

        haeTiedot();



    }

    private void haeTiedot(){
        tiedot = getSharedPreferences("Tiedot", Activity.MODE_PRIVATE);
        paino = tiedot.getFloat("Paino", 0.0f);
        nimi = tiedot.getString("Käyttäjä", "");
        tiedot1 = tiedot.getString("Tavoite1", "");
        tiedot2 = tiedot.getString("Tavoite2", "");
        pronimi.setText(nimi);
        propaino.setText(String.valueOf(paino) + " kg");
        Log.d("Pituus", String.valueOf(pituus));
        Log.d("Paino", String.valueOf(paino));
        String pyoBmi = String.format("%.2f", laskeBmi());
        proBMI.setText("BMI: " + pyoBmi);
        asetetut.setText("Asettamasi tavoitteet:");
        String[] tav1 = tiedot1.split(" ");
        String tyyppi1 = tav1[0];
        String[] tav2 = tiedot2.split(" ");
        String tyyppi2 = tav2[0];
        if (tyyppi1.equals("Valinta")){
            tavoite1.setText("");
        } else {
            tavoite1.setText("Tavoite 1: " + tiedot.getString("Tavoite1", ""));
        }
        if (tyyppi2.equals("Valinta")){
            tavoite2.setText("");
        } else {
            tavoite2.setText("Tavoite 2: " + tiedot.getString("Tavoite2", ""));
        }



        /*BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);
        bottomNavigationView.getMenu().findItem(R.id.profiili).setChecked(true);*/
    }
    public void siirry(View v) {
        if (v == findViewById(R.id.siirryAsetuksiin)) {
            Log.d("Siirry", "Mene asetuksiin");
            startActivity(new Intent(Profiili.this, Asetukset.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public double laskeBmi(){
            //paino = tiedot.getFloat("Paino", 0.0f);
            pituus = tiedot.getFloat("Pituus", 0.0f) / 100;
            bmi = paino / (pituus * pituus);

        return bmi;
    }
    /**
     * Alapalkin toiminnallisuus, aloittaa valitun aktiviteetin.
     * Tutoriaali alapalkin luomiseen: https://www.youtube.com/watch?v=fODp1hZxfng
     */
    private BottomNavigationView.OnNavigationItemSelectedListener alaPalkkiMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.koti:
                            startActivity(new Intent(Profiili.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Koti painettu");
                            finish();
                            break;
                        case R.id.suunnittele:
                            startActivity(new Intent(Profiili.this, AteriatActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Suunnittele painettu");
                            finish();
                            break;
                        case R.id.profiili:
                            startActivity(new Intent(Profiili.this, Profiili.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Profiili painettu");
                            finish();
                            break;
                    }
                    return false;
                }
            };
}