package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.example.androidproject.Ateria;
import com.example.androidproject.AteriaLista;
import com.example.androidproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.Calendar;

import static com.example.androidproject.AteriaLista.haeLista;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private Calendar kalenteri;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();
    private AteriaLista aterialista;
    private String aterialistaJson;

    private int paiva;
    private int kuukausi;
    private int vuosi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kalenteri = Calendar.getInstance();

        /**
         * Haetaan AteriaLista-singleton pysyväismuistista.
         * Jos singletonia ei löydy, luodaan uusi instanssi ja tallennetaan se pysyväismuistiin.
         */
        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();
        aterialistaJson = pref.getString("aterialista", "");

        if (aterialistaJson.equals("")) {
            aterialistaJson = gson.toJson(haeLista());
            editor.putString("aterialista", aterialistaJson);
            editor.commit();
            Log.d("aterialista", aterialistaJson);
        }

        aterialista = gson.fromJson(aterialistaJson, AteriaLista.class);

        // Asetetaan alapalkille kuuntelija, joka vaihtaa aktiviteettia nappien perusteella.
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);
        bottomNavigationView.getMenu().findItem(R.id.koti).setChecked(true);
    }

    @SuppressLint("SetTextI18n")
    protected void onResume() {
        super.onResume();

        paiva = kalenteri.get(Calendar.DAY_OF_MONTH);
        kuukausi = kalenteri.get(Calendar.MONTH);
        vuosi = kalenteri.get(Calendar.YEAR);
        TextView kaloriTavoite = findViewById(R.id.testitext);

        /**
         * Tässä ne syödyt kalorit, käytä tätä sitten kun teet jonkun vitun hienon laskun : DDD.
         */
        int kalorit = (int) Math.round(aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi).get(0));

        /**
         * Haetaan pysyväismuistista käyttäjän tiedot ja tavoitteet.
         */
        SharedPreferences sharedPreferences = getSharedPreferences("Tiedot", Context.MODE_PRIVATE);
        String nimi = sharedPreferences.getString("Käyttäjä", "");
        String tiedot1 = sharedPreferences.getString("Tavoite1", "");

        //Päivän tähän asti syödyt kalorit verrattuna omaan tavoitteeseen.
        String jokuvitunhienolaskujonkateenmyöhemmin = "";

        /**
         * Asetetaan tiedot, mikäli tietoja on tallennettu asetuksissa.
         */
        if (!tiedot1.equals("")) {
            String[] tiedot1_lista = tiedot1.split(",");
            String tiedot1_kalorit = tiedot1_lista[1];
            kaloriTavoite.setText("Tavoite päivässä :" + tiedot1_kalorit + " kcal" + "\n Kaloreita jäljellä: " + jokuvitunhienolaskujonkateenmyöhemmin);
        }

        /**
         * Asetetaan nimi, mikäli nimi on tallennettu asetuksissa.
         * Varmistetaan että nimessä on aina iso alkukirjain laatikossa näytettäessä,
         * vaikka käyttäjä kirjoittaisi nimensä pienellä.
        */
        if (!nimi.equals("")) {
            TextView nimitextview = findViewById(R.id.nimiteksti);
            String isoalkukirjain = nimi.substring(0, 1).toUpperCase() + nimi.substring(1);
            nimitextview.setText("Hei, " + isoalkukirjain + "!");
        }
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
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Koti painettu");
                            finish();
                            break;
                        case R.id.suunnittele:
                            startActivity(new Intent(MainActivity.this, AteriatActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Suunnittele painettu");
                            finish();
                            break;
                        case R.id.profiili:
                            startActivity(new Intent(MainActivity.this, Asetukset.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Profiili painettu");
                            finish();
                            break;
                    }
                    return false;
                }
            };


}