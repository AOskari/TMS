package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.Paino;
import com.example.androidproject.R;
import com.example.androidproject.Trendi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.androidproject.Trendi.getInstance;

/**
 * Profiili-aktiviteettia käytetään näyttämään osa tallennetuista tiedoista ja näyttämään trendinäkymä (GraphView)
 */
public class Profiili extends AppCompatActivity {

    private TextView pronimi, propaino, bmi, asetetut, tavoite1, tavoite2, pvm;
    private SharedPreferences tiedot;
    private SharedPreferences trendit;
    private SharedPreferences.Editor tallListat;
    private Trendi trendi;
    private List<Paino> paTrendi;
    private ArrayList<String> y_aks;
    private ArrayList<String> x_aks;
    private DecimalFormat dec;
    private Gson gson = new Gson();
    private GraphView historia;
    private LineGraphSeries<DataPoint> sarja1;

    private float paino, pituus, BMI;
    private String trendiJson, nimi, tiedot1, tiedot2, paiva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiili);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        pronimi = findViewById(R.id.proNimi);
        propaino = findViewById(R.id.proPaino);
        bmi = findViewById(R.id.bmi);
        asetetut = findViewById(R.id.tavoitteet);
        tavoite1 = findViewById(R.id.eka);
        tavoite2 = findViewById(R.id.toka);
        pvm = findViewById(R.id.pvm);
        historia = findViewById(R.id.historia);
        dec = new DecimalFormat("0.00");
        tiedot = getSharedPreferences("Tiedot", Activity.MODE_PRIVATE);
        trendit = getSharedPreferences("Trendit", Activity.MODE_PRIVATE);
        tallListat = trendit.edit();

        /**
         * Haetaan Trendi-singleton, jos sitä ei löydy, tallennetaan se SharedPreferencesiin.
         */
        trendiJson = trendit.getString("Trendi", "");
        if (trendiJson.equals("")) {
            trendiJson = gson.toJson(getInstance());
            tallListat.putString("Trendi", trendiJson);
            tallListat.commit();
        }
        trendi = gson.fromJson(trendiJson, Trendi.class);

        listaHae();
        haeTiedot();
        setReuna();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);
        bottomNavigationView.getMenu().findItem(R.id.profiili).setChecked(true);
    }
    protected void onResume(){
        super.onResume();
        haeTiedot();
        teeKuvaaja();
        historia.onDataChanged(true, true);
    }

    /**
     * Metodissa haetaan paino-olioiden lista Trendi-Singletonista
     */
    private void listaHae(){
       paTrendi = trendi.getPaino();
    }

    /**
     * Metodissa tehdään kuvaaja datapisteiden perusteella. Tehdään y-akselia varten normaali arraylist listaHae-metodissa haetusta listasta.
     * Listan läpikäynnissä y-akselille lisätään indeksissä oleva arvo ja x-akselille lisätään indeksissä oleva painon tallennuksen yhteydessä
     * tallennettu päivämäärä. GraphView:n käyttö: https://www.bragitoff.com/2017/04/add-data-graphview-arrays-solved-android-studio/ ja näkymän
     * optimointi/konfigurointi: https://www.javatips.net/api/GraphView-master/src/main/java/com/jjoe64/graphview/GridLabelRenderer.java. Viimeksi
     * mainitussa kerrottu kaikki GraphView:n käyttöön liittyvät metodit.
     */
    private void teeKuvaaja(){
        x_aks = new ArrayList<>();
        y_aks = new ArrayList<>();

        for (int i=0; i<paTrendi.size(); i++){
            x_aks.add(paTrendi.get(i).paivamaaraString());
            y_aks.add(String.valueOf(paTrendi.get(i)));
            Log.d("Näytä arraylist ",  y_aks.get(i));
        }
        /**
         * X-akselin numeroarvot korvataan päivämäärillä. StaticLabelsFormatter pystyy hyödyntämään vain taulukoita, joten muunnetaan
         * x_aks-arraylist x_nimi-arrayksi. Pitkien merkkijonojen vuoksi niitä kallistetaan näytössä, jotta mahtuvat
         * paremmin (setHorizontalLabelsAngle().
         */
        String[] x_nimi = new String[x_aks.size()];
        x_nimi = x_aks.toArray(x_nimi);
        /**
         * StaticLabelsFormatter pystyy näyttämään muokatut arvot vain jos niitä on taulukossa vähintään kaksi, joten ehdollistetaan
         * niiden näyttäminen alkuperäisen listan pituuteen.
         */
        if (paTrendi.size() > 1) {
            StaticLabelsFormatter x_label = new StaticLabelsFormatter(historia);
            x_label.setHorizontalLabels(x_nimi);
            historia.getGridLabelRenderer().setLabelFormatter(x_label);
            historia.getGridLabelRenderer().setHorizontalLabelsAngle(150);
        }
        Log.d("Listan koko ", String.valueOf(paTrendi.size()));
        /**
         * Luodaan uusi LineGraphSeries-olio, jolle annetaan parametriksi datapisteet hakeva metodi
         */
        sarja1 = new LineGraphSeries<>(data());
        sarja1.setTitle("Paino");
        sarja1.setDrawDataPoints(true);
        sarja1.setDataPointsRadius(10f);

        historia.getViewport().setMinX(0);
        historia.getViewport().setMaxX(paTrendi.size());
        historia.getViewport().setXAxisBoundsManual(true);
        historia.getViewport().setScalable(true);
        historia.getViewport().setScrollable(true);
        historia.getLegendRenderer().setVisible(true);
        historia.getGridLabelRenderer().setNumHorizontalLabels(paTrendi.size());
        historia.getGridLabelRenderer().setHorizontalLabelsAngle(100);
        historia.getGridLabelRenderer().setHumanRounding(false);
        historia.getGridLabelRenderer().setPadding(15);

        historia.addSeries(sarja1);
    }

    /**
     * Luodaan taulukko, johon tallennetaan kuvaajan tarvitsemat x ja y muuttujat. Muuttujia varten käydään läpi teeKuvaaja-metodissa tehty
     * y_aks-lista, x-muuttujaksi tulee indeksinumero ja y-muuttujaksi indeksissä oleva arvo
     * @return palauttaa datapisteen (x,y)-muodossa
     */
    private DataPoint[] data(){
        DataPoint[] painoArvot = new DataPoint[paTrendi.size()];
        for (int i = 0; i < paTrendi.size(); i++) {
            DataPoint v = new DataPoint(i, Double.parseDouble(y_aks.get(i)));
            painoArvot[i] = v;
        }
        return painoArvot;
    }

    /**
     * Haetaan SharedPreferencesistä halutut tiedot ja asetetaan ne profiili-aktiviteetin näkymän kenttiin
     */
    private void haeTiedot(){
        paino = tiedot.getFloat("Paino", 0.0f);
        nimi = tiedot.getString("Käyttäjä", "");
        tiedot1 = tiedot.getString("Tavoite1", "");
        tiedot2 = tiedot.getString("Tavoite2", "");
        paiva = "(" + tiedot.getString("Päiväys", "") + ")";

        pronimi.setText(nimi);
        propaino.setText(String.valueOf(paino) + " kg");
        bmi.setText(dec.format(laskeBmi()));
        Log.d("Nayta bmi", bmi.getText().toString());
        pvm.setText(paiva);
        asetetut.setText("Asettamasi tavoitteet:");
        /**
         * Tarkistetaan mitä on tallennettu tavoitteiksi ja asetetaan sen mukaan oikea teksti. Jos tavoitteita ei ole, kentät jäävät tyhjiksi
         */
        if (tiedot1 == ""){
            tavoite1.setText("");
        } else {
            tavoite1.setText("Tavoite 1: " + tiedot1);
        }
        if (tiedot2 == ""){
            tavoite2.setText("");
        } else {
            tavoite2.setText("Tavoite 2: " + tiedot2);
        }
    }

    /**
     * Tarkistetaan mitä nappia näkymässä on painettu, ratas vie asetukset-aktiviteettiin ja rastit poistavat tavoitteet
     * näkymästä ja tallentavat tavoitteet oletusarvoisiksi SharedPreferencesiin
     * @param v parametrilla tarkistetaan painettu nappi
     */
    public void katsoNapit(View v) {
        SharedPreferences.Editor muokkaa = tiedot.edit();
        if (v == findViewById(R.id.siirryAsetuksiin)) {
            Log.d("Siirry", "Mene asetuksiin");
            startActivity(new Intent(Profiili.this, Asetukset.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else if (v== findViewById(R.id.poista1)) {
            muokkaa.putString("Tavoite1", "");
            muokkaa.commit();
            tavoite1.setText("");
        } else if (v== findViewById(R.id.poista2)) {
            muokkaa.putString("Tavoite2", "");
            muokkaa.commit();
            tavoite2.setText("");
        }
    }

    /**
     * Metodi, jolla lasketaan bmi tallennetusta pituudesta ja painosta
     * @return palautetaan desimaaliarvo, joka myöhemmin arvoa käytettäessä pyöristeään tarvittaessa DecimalFormat-luokan avulla
     * kahden desimaalin tarkkuuteen
     */
    private float laskeBmi(){
        paino = tiedot.getFloat("Paino", 0.0f);
        pituus = tiedot.getFloat("Pituus", 0.0f) / 100;
        float lasku = paino / (pituus*pituus);
            if (paino == 0.0f || pituus == 0.0f){
                BMI = 0.0f;
            } else {
                BMI = lasku;
            }
            return BMI;
    }

    /**
     * Näkymässä infonappi, josta painamalla saa lisätietoa, mitä kyseinen arvo (bmi) tarkoittaa. Tieto annetaan toastina.
     * @param v
     */
    public void annaLisatiedot(View v){
        float tieto = laskeBmi();

        Toast tuomio = Toast.makeText(getApplicationContext(), "teksti", Toast.LENGTH_SHORT);
        View nayta = tuomio.getView();
        nayta.setBackgroundResource(R.drawable.toast);

        //tuomio.setView(nayta);
        tuomio.setGravity(Gravity.TOP|Gravity.END, 150, 620);
        tuomio.setView(nayta);
        /**
         * Tarkistetaan millä alueella kentän arvo on ja asetetaan sen mukaan oikea toast-teksti
         */
        if (tieto > 0.0 && tieto < 18.5){
            tuomio.setText("Alipaino");
            tuomio.show();
        } else if (tieto >= 18.5 && tieto <= 25.0){
            tuomio.setText("Normaali paino");
            tuomio.show();
        } else if (tieto > 25.0 && tieto <= 30.0){
            tuomio.setText("Lievä ylipaino");
            tuomio.show();
        } else if (tieto > 30.0 && tieto <= 35.0){
            tuomio.setText("Merkittävä lihavuus");
            tuomio.show();
        } else if (tieto > 35.0 && tieto <= 40.0){
            tuomio.setText("Vaikea lihavuus");
            tuomio.show();
        } else if (tieto > 40.0){
            tuomio.setText("Sairaalloinen lihavuus");
            tuomio.show();
        } else if (tieto == 0.0){
            tuomio.setText("Pituutta tai painoa ei ole asetettu");
            tuomio.show();
        }
    }

    /**
     * Asetetaan kentän arvon mukaan vaihtuva kehysväri (https://www.viralandroid.com/2015/10/android-button-and-textview-border-color.html)
     */
    private void setReuna(){
        float tieto = laskeBmi();
        /**
         * Tarkistetaan millä alueella arvo on ja asetetaan sen mukaan kehysväri
          */
        if (tieto >= 18.5 && tieto <= 25.0){
            bmi.setBackgroundResource(R.drawable.border1);
        } else if (tieto > 25.0 && tieto <= 30.0){
            bmi.setBackgroundResource(R.drawable.border2);
        } else if (tieto > 30.0 && tieto <= 35.0){
            bmi.setBackgroundResource(R.drawable.border3);
        } else if (tieto > 35.0 && tieto <= 40.0){
            bmi.setBackgroundResource(R.drawable.border4);
        } else if (tieto > 40.0){
            bmi.setBackgroundResource(R.drawable.border5);
        }
    }
    /**
     * Alapalkin toiminnallisuus, aloittaa valitun aktiviteetin.
     * Tutoriaali alapalkin luomiseen: https://www.youtube.com/watch?v=fODp1hZxfng
     */
    private BottomNavigationView.OnNavigationItemSelectedListener alaPalkkiMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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