package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.androidproject.Trendi.getInstance;

public class Profiili extends AppCompatActivity {
    private TextView pronimi, propaino, bmi, asetetut, tavoite1, tavoite2, pvm;
    public SharedPreferences tiedot;
    public SharedPreferences trendit;
    public SharedPreferences.Editor tallListat;
    private float paino, pituus;
    private String nimi, tiedot1, tiedot2, paiva;
    private float BMI;
    private  String trendiJson;
    private Trendi trendi;
    public List<Paino> paTrendi;
    public static ArrayList<String> x_aks;
    public static ArrayList<String> y_aks;
    DecimalFormat dec;
    Gson gson = new Gson();
    GraphView historia;
    LineGraphSeries<DataPoint> sarja1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profiili);

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

        Log.d("Listan koko ", String.valueOf(paTrendi.size()));
        for (int i=0; i<paTrendi.size(); i++){
            Log.d("Lista "+i, String.valueOf(paTrendi.get(i)));
        }
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

    public DataPoint[] data(){
        DataPoint[] painoArvot = new DataPoint[paTrendi.size()];
            for (int i = 0; i < paTrendi.size(); i++) {
                //DataPoint v = new DataPoint(paTrendi.get(i).paivamaaraString(), paTrendi.get(i));
                DataPoint v = new DataPoint(i, Double.parseDouble(y_aks.get(i)));
                painoArvot[i] = v;
            }
            return painoArvot;
    }

    public void teeKuvaaja(){
        //x_aks=new ArrayList<>();
        y_aks=new ArrayList<>();
        String[] x_nimi = new String[paTrendi.size()];

        for (int i=0; i<paTrendi.size(); i++){
            //String paiva = paTrendi.get(i).paivamaaraString();
            //x_nimi[i] = paiva;
            //x_aks.add(String.valueOf(i));
            y_aks.add(String.valueOf(paTrendi.get(i)));
            Log.d("Näytä arraylist: ",  y_aks.get(i));
            //Log.d("Näytä array ", x_nimi[i]);
        }

        for (int i = 0; i < paTrendi.size(); i++){
            String paiva = paTrendi.get(i).paivamaaraString();
            x_nimi[i] = paiva;
            Log.d("Näytä array ", x_nimi[i]);
        }
        if (paTrendi.size() >= 2) {
            StaticLabelsFormatter x_label = new StaticLabelsFormatter(historia);
            x_label.setHorizontalLabels(x_nimi);
            historia.getGridLabelRenderer().setLabelFormatter(x_label);
            historia.getGridLabelRenderer().setHorizontalLabelsAngle(150);
        }

        Log.d("Listan koko ", String.valueOf(paTrendi.size()));

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

        historia.addSeries(sarja1);

        /*historia.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return String.valueOf(x_nimi[(int) value]);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });*/
    }

    public void listaHae(){
       // trendit = getSharedPreferences("Trendit", MODE_PRIVATE);
       // String json = trendit.getString("Paino", null);
       // Type type = new TypeToken<ArrayList<Paino>>() {}.getType();

        paTrendi = trendi.getPaino();

   /*     if (paTrendi == null){
            paTrendi = new ArrayList<>();
        } */
    }

    private void haeTiedot(){
        paino = tiedot.getFloat("Paino", 0.0f);
        nimi = tiedot.getString("Käyttäjä", "");
        tiedot1 = tiedot.getString("Tavoite1", "");
        tiedot2 = tiedot.getString("Tavoite2", "");
        paiva = "(" + tiedot.getString("Päiväys", "") + ")";

        pronimi.setText(nimi);
        propaino.setText(String.valueOf(paino) + " kg");

        Log.d("Pituus", String.valueOf(pituus));
        Log.d("Paino", String.valueOf(paino));

        bmi.setText(dec.format(laskeBmi()));
        Log.d("Nayta bmi", bmi.getText().toString());
        pvm.setText(paiva);
        asetetut.setText("Asettamasi tavoitteet:");
        String[] tav1 = tiedot1.split(" ");
        String tyyppi1 = tav1[0];
        String[] tav2 = tiedot2.split(" ");
        String tyyppi2 = tav2[0];
        if (tyyppi1.equals("Valinta")){
            tavoite1.setText("");
        } else {
            tavoite1.setText("Tavoite 1: " + tiedot1);
        }
        if (tyyppi2.equals("Valinta")){
            tavoite2.setText("");
        } else {
            tavoite2.setText("Tavoite 2: " + tiedot2);
        }
        /*for (int i = 0; i < paTrendi.size(); i++) {
            String nayta = paTrendi.get(i).toString();
            Log.d("Lista:"+i, "löytyi " + nayta);
        }*/



    }
    public void siirry(View v) {
        if (v == findViewById(R.id.siirryAsetuksiin)) {
            Log.d("Siirry", "Mene asetuksiin");
            startActivity(new Intent(Profiili.this, Asetukset.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public float laskeBmi(){
        paino = tiedot.getFloat("Paino", 0.0f);
        pituus = tiedot.getFloat("Pituus", 0.0f) / 100;
        //DecimalFormat dec = new DecimalFormat("0.00");
        float lasku = paino / (pituus*pituus);
            if (paino == 0.0f || pituus == 0.0f){
                BMI = 0.0f;
            } else {
                BMI = lasku; // Double.parseDouble(String.format("%.2f", paino / (pituus * pituus)));
            }
            return BMI;
    }

    public void annaLisatiedot(View v){
        float tieto = laskeBmi();
        Toast toast = Toast.makeText(getApplicationContext(), "teksti", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 150, -320);

        //toast.getView().setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#34495E")));
        if (tieto > 0.0 && tieto < 18.5){
            toast.setText("Alipaino");
            toast.show();
        } else if (tieto >= 18.5 && tieto < 25.0){
            toast.setText("Normaali paino");
            toast.show();
        } else if (tieto > 25.0 && tieto <= 30.0){
            toast.setText("Lievä ylipaino");
            toast.show();
        } else if (tieto > 30.0 && tieto <= 35.0){
            toast.setText("Merkittävä lihavuus");
            toast.show();
        } else if (tieto > 35.0 && tieto <= 40.0){
            toast.setText("Vaikea lihavuus");
            toast.show();
        } else if (tieto > 40.0){
            toast.setText("Sairaalloinen lihavuus");
            toast.show();
        } else if (tieto == 0.0){
            toast.setText("Pituutta tai painoa ei ole asetettu");
            toast.show();
        }
    }

    public void setReuna(){

        float tieto = laskeBmi();

        if (tieto >= 18.5 && tieto < 25.0){
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