package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.androidproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asetetaan alapalkille Listener, joka vaihtaa aktiviteettia nappien perusteella.
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);

    }

    protected void onResume() {

        super.onResume();

        //Päivän tähän asti syödyt kalorit verrattuna omaan tavoitteeseen.
        SharedPreferences sharedPreferences = getSharedPreferences("Tiedot", Context.MODE_PRIVATE);
        String tiedot1 = sharedPreferences.getString("Tavoite1", "");

        String jokuvitunhienolaskujonkateenmyöhemmin = "";

        String tiedot1_lista[] = tiedot1.split(",");
        String tiedot1_kalorit = tiedot1_lista[1];

        //Käyttäjän nimi.
        String nimi = sharedPreferences.getString("Käyttäjä", "");

        //Varmistaminen että nimessä on aina iso alkukirjain laatikossa näytettäessä.
        String isoalkukirjain = nimi.substring(0, 1).toUpperCase() + nimi.substring(1);

        TextView nimitextview = findViewById(R.id.nimiteksti);


        nimitextview.setText("Hei, " + isoalkukirjain + "!");



        TextView kaloriTavoite = findViewById(R.id.testitext);
        kaloriTavoite.setText("Tavoite päivässä :" + tiedot1_kalorit + " kcal" + "\n Kaloreita jäljellä: " + jokuvitunhienolaskujonkateenmyöhemmin);
    }

    // Luodaan metodi, jonka avulla muutetaan näytön tilaa riippuen mitä nappia on painettu.
    private BottomNavigationView.OnNavigationItemSelectedListener alaPalkkiMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.koti:
                            //TODO: luo koodi, joka menee takaisin koti-ikkunaan
                            break;
                        case R.id.suunnittele:
                            startActivity(new Intent(MainActivity.this, AteriatActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Suunnittele painettu");
                            break;
                        case R.id.profiili:
                            startActivity(new Intent(MainActivity.this, Asetukset.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.i("Menu", "Profiili painettu");
                            break;
                    }
                    return false;
                }
            };


}