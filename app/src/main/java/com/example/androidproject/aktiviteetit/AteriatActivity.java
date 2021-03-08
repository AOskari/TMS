package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidproject.Ateria;
import com.example.androidproject.AteriaLista;
import com.example.androidproject.AteriatAdapter;
import com.example.androidproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Luo näkymän, joka näyttää kaikki valitun päivän ateriat sekä
 * summan niiden ravintoaineista ja kaloreista.
 */
public class AteriatActivity extends AppCompatActivity {

    private String ateriatJson;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();

    private ListView lv;
    private TextView paivamaara;
    private TextView kalorimaara;
    private TextView proteiiniMaara;
    private TextView hhMaara;
    private TextView rasvaMaara;

    private int paiva;
    private int kuukausi;
    private int vuosi;

    private ProgressBar proteiiniPalkki;
    private ProgressBar hhPalkki;
    private ProgressBar rasvaPalkki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ateriat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle("TMS - Ateriat");

        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();

        editor.putBoolean("muokkaus", false);

        Calendar kalenteri = Calendar.getInstance();

        editor.putInt("paiva", kalenteri.get(Calendar.DAY_OF_MONTH));
        editor.putInt("kuukausi", kalenteri.get(Calendar.MONTH));
        editor.putInt("vuosi", kalenteri.get(Calendar.YEAR));
        editor.commit();

        paiva = pref.getInt("paiva", 0);
        kuukausi = pref.getInt("kuukausi", 0);
        vuosi = pref.getInt("vuosi", 0);

        paivamaara = findViewById(R.id.paivamaara);
        lv = findViewById(R.id.aterialista);

        ateriatJson = pref.getString("aterialista", "");

        /**
         * Asetetaan alustavat tiedot näkymän ravintoaineiden ProgressBar:eille ja TextView:eille.
         * Lopuksi asetetaan onClick kuuntelija päivämääränapille, joka avaa Kalenteri.
         */
        proteiiniPalkki = findViewById(R.id.proteiini_palkki);
        hhPalkki = findViewById(R.id.hh_palkki);
        rasvaPalkki = findViewById(R.id.rasva_palkki);
        kalorimaara = findViewById(R.id.ateriat_kalorimaara);

        proteiiniMaara = findViewById(R.id.proteiini_palkkiteksti);
        hhMaara = findViewById(R.id.hh_palkkiteskti);
        rasvaMaara = findViewById(R.id.rasva_palkkiteksti);
        proteiiniPalkki.setMax(100);
        hhPalkki.setMax(100);
        rasvaPalkki.setMax(100);

       asetaRavintoarvot();
        ImageButton nappi = findViewById(R.id.paivamaaraNappi);
        nappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AteriatActivity.this, KalenteriActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);
        bottomNavigationView.getMenu().findItem(R.id.suunnittele).setChecked(true);
    }

    /**
     * Asetetaan takaisin-painikkeelle fade-animaatio.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onResume() {
        super.onResume();
        ateriatJson = pref.getString("aterialista", "");

        paiva = pref.getInt("paiva", 0);
        kuukausi = pref.getInt("kuukausi", 0);
        vuosi = pref.getInt("vuosi", 0);

        paivamaara.setText(paiva + "/" + (kuukausi + 1) + "/" + vuosi);

        naytaAteriat();
        try {
            asetaRavintoarvot();
        } catch (Exception e) {
            Log.d("exception", e.toString());
        }
    }

    /**
     * Asettaa ateria-ListViewin sisällön.
     */
    public void naytaAteriat() {
        AteriatAdapter adapter = new AteriatAdapter(this, pref, paiva, kuukausi, vuosi);
        lv.setAdapter(adapter);
    }

    /**
     * Luo uuden tyhjän aterian pysyväismuistiin ja aloittaa AteriaActivityn.
     */
    public void luoAteria(View v) {
        Ateria ateria = new Ateria("luonnos ateria");
        ateriatJson = gson.toJson(ateria);
        editor.putString("ateria", ateriatJson);
        editor.commit();
        startActivity(new Intent(AteriatActivity.this, AteriaActivity.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Asettaa päivän kalorimäärän ja ravintoaineiden ProgressBar:ien arvot.
     */
    public void asetaRavintoarvot() {
        ateriatJson = pref.getString("aterialista", "");
        AteriaLista lista = gson.fromJson(ateriatJson, AteriaLista.class);
        DecimalFormat df = new DecimalFormat("#.#");

        proteiiniPalkki.setProgress(lista.haeProsentit(paiva, kuukausi, vuosi).get(0));
        hhPalkki.setProgress(lista.haeProsentit(paiva, kuukausi, vuosi).get(1));
        rasvaPalkki.setProgress(lista.haeProsentit(paiva, kuukausi, vuosi).get(2));
        kalorimaara.setText(String.valueOf(lista.haeKalorit(paiva, kuukausi, vuosi)));

        List<Double> arvot = lista.haeRavintoarvot(paiva, kuukausi, vuosi);

        proteiiniMaara.setText("Proteiini " + " / " + df.format(arvot.get(0)) + "g");
        hhMaara.setText("Hiilihydraatit " + " / " + df.format(arvot.get(1)) + "g");
        rasvaMaara.setText("Rasva " + " / " + df.format(arvot.get(2)) + "g");
    }


    // =================================================================== //
    // ========================= Private-metodit ========================= //
    // =================================================================== //

    private BottomNavigationView.OnNavigationItemSelectedListener alaPalkkiMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.koti:
                            startActivity(new Intent(AteriatActivity.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Koti painettu");
                            finish();
                            return true;
                        case R.id.suunnittele:
                            startActivity(new Intent(AteriatActivity.this, AteriatActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Ateriat painettu");
                            finish();
                            return true;
                        case R.id.profiili:
                            startActivity(new Intent(AteriatActivity.this, Profiili.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.i("Menu", "Profiili painettu");
                            finish();
                            return true;
                    }
                    return true;
                }
            };
}