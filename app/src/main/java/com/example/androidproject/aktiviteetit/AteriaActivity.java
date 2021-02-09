package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidproject.Ateria;
import com.example.androidproject.AteriaAdapter;
import com.example.androidproject.Elintarvike;
import com.example.androidproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AteriaActivity extends AppCompatActivity {

    Ateria ateria;
    String ateriaJson;
    Gson gson = new Gson();

    private TextView proteiini;
    private TextView hiilihydraatti;
    private TextView rasva;
    private TextView kalorit;

    private TextView sokeri;
    private TextView tyydyttynytRasva;
    private TextView suola;
    private TextView kuitu;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    PieChart piiras;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ateria);

        piiras = findViewById(R.id.piiras);
        kalorit = findViewById(R.id.kalorit);

        proteiini = findViewById(R.id.proteiini);
        hiilihydraatti = findViewById(R.id.hiilihydraatti);
        rasva = findViewById(R.id.rasva);

        sokeri = findViewById(R.id.sokeri_maara);
        tyydyttynytRasva = findViewById(R.id.rasva_maara);
        suola = findViewById(R.id.suola_maara);
        kuitu = findViewById(R.id.kuitu_maara);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();

        lv = findViewById(R.id.ateria);
        ViewCompat.setNestedScrollingEnabled(lv, true);
        ImageButton kalenterNappi = findViewById(R.id.kalenteri_nappi);

        // Lisätään mahdollisuus lisätä päiväys käyttäen kalenteria.
            kalenterNappi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AteriaActivity.this, KalenteriActivity.class);
                    startActivity(intent);
                }
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        paivitaLista();

        String paivays = pref.getString("paivays", "");

        if (paivays != "") {
            ((TextView)findViewById(R.id.paivays)).setText(paivays);
        }
    }

    public void paivitaTiedot() {
        ateriaJson = pref.getString("ateria", "");
        ateria = gson.fromJson(ateriaJson, Ateria.class);
    }

    public void paivitaLista() {
        ateriaJson = pref.getString("ateria", "");
        ateria = gson.fromJson(ateriaJson, Ateria.class);

        if (ateriaJson != "") {
            DecimalFormat df = new DecimalFormat("#.#");
            DecimalFormat df2 = new DecimalFormat("#.##");

            Log.d("ateria", ateria.toString());
             initList();

             ArrayList lista = new ArrayList();
            ArrayList ravintoarvo = new ArrayList();

            //TODO: piiraan toiminnallisuus

            double prot = ateria.haeRavinto().get(1);
            double hh = ateria.haeRavinto().get(2);
            double ras = ateria.haeRavinto().get(3);

            double protPros = (prot / ateria.haeRavinto().get(0)) * 100;
            double hhPros = (hh / ateria.haeRavinto().get(0)) * 100;
            double rasvaPros = (ras / ateria.haeRavinto().get(0)) * 100;

            proteiini.setText("Proteiini  " + df.format(protPros) + "%  " + df.format(prot) + "g");
            hiilihydraatti.setText("Hiilihydraatit  " + df.format(hhPros) + "%  " + df.format(hh) + "g");
            rasva.setText("Rasva  " + df.format(rasvaPros) + "%  " + df.format(ras) + "g");

            sokeri.setText(df.format(ateria.haeRavinto().get(5)) + "g");
            tyydyttynytRasva.setText(df.format(ateria.haeRavinto().get(6)) + "g");
            suola.setText(df2.format(ateria.haeRavinto().get(7)) + "g");
            kuitu.setText(df.format(ateria.haeRavinto().get(8)) + "g");

            float m1 = (float) prot;
            float m2 = (float) hh;
            float m3 = (float) ras;

            lista.add(new Entry(m1, 0));
            lista.add(new Entry(m2, 1));
            lista.add(new Entry(m3, 2));

            PieDataSet dataSet = new PieDataSet(lista, "");

            ravintoarvo.add("proteiini");
            ravintoarvo.add("hiilihydraatti");
            ravintoarvo.add("rasva");

            PieData data = new PieData(ravintoarvo, dataSet);

            // Asetetaan uudet ravintoarvot ja poistetaan ylimääräiset merkinnät pois.
            data.setDrawValues(false);
            data.setValueTextSize(10f);
            piiras.setData(data);
            piiras.setDescription("");
            piiras.getLegend().setEnabled(false);
            piiras.setDrawSliceText(false);

            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            piiras.animateXY(2000, 2000);

            kalorit.setText(Math.round(ateria.haeRavinto().get(4)) + " kcal");
        }
    }

    private void initList() {
        AteriaAdapter adapter = new AteriaAdapter(this, pref);
        lv.setAdapter(adapter);
    }


    public void hae(View v) {
        startActivity(new Intent(AteriaActivity.this, HaeActivity.class));
    }
}