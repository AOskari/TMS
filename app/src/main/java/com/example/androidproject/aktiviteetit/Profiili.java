package com.example.androidproject.aktiviteetit;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Profiili extends AppCompatActivity {
    private TextView pronimi, propaino, bmi, asetetut, tavoite1, tavoite2;
    public SharedPreferences tiedot;
    public SharedPreferences trendit;
    public SharedPreferences.Editor tallListat;
    private float paino, pituus;
    //private double bmi;
    private String nimi, tiedot1, tiedot2, BMI;

    public ArrayList<Paino> paTrendi;
    public static ArrayList<String> x_aks; //=new ArrayList<String>();
    public static ArrayList<String> y_aks; //=new ArrayList<String>();
    Gson gson = new Gson();
    GraphView historia;
    PointsGraphSeries<DataPoint> sarja1;
    //LineChart historia;
    //LineData naytaData;
    //List<Entry> kuvaaja1 = new ArrayList<>();



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
        historia = findViewById(R.id.historia);

        x_aks=new ArrayList<String>();
        y_aks=new ArrayList<String>();
        tiedot = getSharedPreferences("Tiedot", Activity.MODE_PRIVATE);
        trendit = getSharedPreferences("Trendit", Activity.MODE_PRIVATE);
        tallListat = trendit.edit();
        listaHae();

        for (int i=0; i<paTrendi.size(); i++){
            x_aks.add(String.valueOf(i));
            y_aks.add(String.valueOf(paTrendi.get(i)));
            Log.d("Lista "+i, String.valueOf(paTrendi.get(i)));
        }
        sarja1 = new PointsGraphSeries<>(data());
        historia.addSeries(sarja1);

        haeTiedot();
        Log.d("Listan koko ", String.valueOf(paTrendi.size()));
        for (int i=0; i<paTrendi.size(); i++){
            Log.d("Lista "+i, String.valueOf(paTrendi.get(i)));
        }
    }

    public DataPoint[] data(){
        int arvoja = x_aks.size();
        DataPoint[] painoArvot = new DataPoint[arvoja];
        for (int i = 0; i < arvoja; i++){
            DataPoint v = new DataPoint(Double.parseDouble(x_aks.get(i)), Double.parseDouble(y_aks.get(i)));
            painoArvot[i] = v;
        }
        return painoArvot;
    }


    public void listaHae(){
        trendit = getSharedPreferences("Trendit", MODE_PRIVATE);
        String json = trendit.getString("Paino", null);
        Type type = new TypeToken<ArrayList<Paino>>() {}.getType();
        paTrendi = gson.fromJson(json, type);

        if (paTrendi == null){
            paTrendi = new ArrayList<>();
        }
    }

    private void haeTiedot(){

        paino = tiedot.getFloat("Paino", 0.0f);
        nimi = tiedot.getString("Käyttäjä", "");
        tiedot1 = tiedot.getString("Tavoite1", "");
        tiedot2 = tiedot.getString("Tavoite2", "");
        pronimi.setText(nimi);
        propaino.setText(String.valueOf(paino) + " kg");
        Log.d("Pituus", String.valueOf(pituus));
        Log.d("Paino", String.valueOf(paino));
        //String pyoBmi = String.format("%.2f", laskeBmi());
        bmi.setText(laskeBmi());
        setReuna();
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
        /*for (int i = 0; i < paTrendi.size(); i++) {
            String nayta = paTrendi.get(i).toString();
            Log.d("Lista:"+i, "löytyi " + nayta);
        }*/


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

    public String laskeBmi(){
            paino = tiedot.getFloat("Paino", 0.0f);
            pituus = tiedot.getFloat("Pituus", 0.0f) / 100;
            String pyoBmi = String.format("%.2f", paino / (pituus * pituus));
            if (paino == 0.0f || pituus == 0.0f){
                BMI = "Tietoa ei löydy";
            } else {
                BMI = pyoBmi;
            }
            return BMI;
    }

    public void annaLisatiedot(View v){
        float tieto = Float.parseFloat(bmi.getText().toString());
        Toast toast = Toast.makeText(getApplicationContext(), "teksti", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 150, -250);

        //toast.getView().setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#34495E")));
        if (tieto < 18.5){
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
        }
    }

    public void setReuna(){
        float tieto = Float.parseFloat(bmi.getText().toString());
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