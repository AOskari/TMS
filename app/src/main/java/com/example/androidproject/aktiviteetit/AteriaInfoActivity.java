package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject.Ateria;
import com.example.androidproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AteriaInfoActivity extends AppCompatActivity {

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ateria_info);

        getSupportActionBar().hide();
        DecimalFormat df = new DecimalFormat("#.#");

        /**
         * Asetetaan popup-ikkunan koko 80% näytön leveydestä ja 60% korkeudesta.
         * Asetetaan taustaväri läpinäkyväksi, joka tuo esille popup-ikkunan pyöristetyt reunat.
         */
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * 0.6));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Bundle b = getIntent().getExtras();
        Ateria a = gson.fromJson(b.getString("TIEDOT_ATERIA"), Ateria.class);

        PieChart piiras = findViewById(R.id.ateria_tiedot_piiras);

        /**
         * Luodaan uudet tiedot ympyrädiagrammia varten.
         */
        ArrayList lista = new ArrayList();
        ArrayList ravintoarvo = new ArrayList();

        double prot = a.haeRavinto().get(1);
        double hh = a.haeRavinto().get(2);
        double rasva = a.haeRavinto().get(3);
        double kalorit = a.haeRavinto().get(4);
        double tyydyt = a.haeRavinto().get(6);
        double sokeri = a.haeRavinto().get(5);
        double kuitu = a.haeRavinto().get(8);
        double suola = a.haeRavinto().get(7);

        float m1 = (float) prot;
        float m2 = (float) hh;
        float m3 = (float) rasva;
        lista.add(new Entry(m1, 0));
        lista.add(new Entry(m2, 1));
        lista.add(new Entry(m3, 2));

        PieDataSet dataSet = new PieDataSet(lista, "");

        ravintoarvo.add("proteiini");
        ravintoarvo.add("hiilihydraatti");
        ravintoarvo.add("rasva");

        PieData data = new PieData(ravintoarvo, dataSet);

        /**
         * Asetetaan uudet ravintoarvot diagrammiin, poistetaan ylimääräiset merkinnät
         * ja asetetaan värit.
         */

        data.setDrawValues(false);
        piiras.setData(data);
        piiras.setDescription("");
        piiras.getLegend().setEnabled(false);
        piiras.setDrawSliceText(false);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        TextView protTxt = findViewById(R.id.ateriainfo_prot);
        TextView hhTxt = findViewById(R.id.ateriainfo_hh);
        TextView rasvaTxt = findViewById(R.id.ateriainfo_rasva);
        TextView kaloriTxt = findViewById(R.id.ateriainfo_kalorit);

        TextView tyydytTxt = findViewById(R.id.ateriainfo_tyydmaara);
        TextView sokeriTxt = findViewById(R.id.ateriainfo_sokerimaara);
        TextView kuituTxt = findViewById(R.id.ateriainfo_kuitumaara);
        TextView suolaTxt = findViewById(R.id.ateriainfo_suolamaara);

        protTxt.setText(df.format(prot) + "g");
        hhTxt.setText(df.format(hh) + "g");
        rasvaTxt.setText(df.format(rasva) + "g");
        kaloriTxt.setText(Math.round(kalorit) + " kcal");

        tyydytTxt.setText(df.format(tyydyt) + " g");
        sokeriTxt.setText(df.format(sokeri) + " g");
        kuituTxt.setText(df.format(kuitu) + " g");
        suolaTxt.setText(df.format(suola) + " g");

        ImageButton poistu = findViewById(R.id.ateriainfo_poistu);
        poistu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}