package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject.Elintarvike;
import com.example.androidproject.R;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Luo popup-ikkunan keskelle ruutua, joka näyttää Elintarvike-olion tarkat ravintoarvot.
 * popup-tutoriaali: https://www.youtube.com/watch?v=fn5OlqQuOCk
 */
public class TiedotActivity extends AppCompatActivity {

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiedot);
        getSupportActionBar().hide();
        DecimalFormat df = new DecimalFormat("#.#");
        /**
         * Asetetaan popup-ikkunan koko 80% näytön leveydestä ja 40% korkeudesta.
         * Asetetaan taustaväri läpinäkyväksi, joka tuo esille popup-ikkunan pyöristetyt reunat.
         */
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * 0.5));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        /**
         * Haetaan ja asetetaan valitun Elintarvike-olion tiedot popup-ikkunaan.
         * Lopuksi asetetaan poistumisnapin onClick-toiminnallisuus.
         */
        Bundle b = getIntent().getExtras();
        Elintarvike e = gson.fromJson(b.getString("TARVIKE"), Elintarvike.class);

        List<Double> arvot = e.haeRavintoarvot();
        ((TextView)findViewById(R.id.tiedot_nimi)).setText(e.haeNimi() + " / " + df.format(arvot.get(9)) + "g");
        ((TextView)findViewById(R.id.tiedot_kalorit)).setText(df.format(arvot.get(1)) + " kcal");
        ((TextView)findViewById(R.id.tiedot_proteiinimaara)).setText(df.format(arvot.get(3)) + "g");
        ((TextView)findViewById(R.id.tiedot_hhmaara)).setText(df.format(arvot.get(4)) + "g");
        ((TextView)findViewById(R.id.tiedot_rasvamaara)).setText(df.format(arvot.get(2)) + "g");
        ((TextView)findViewById(R.id.tiedot_tyydmaara)).setText(df.format(arvot.get(6)) + "g");
        ((TextView)findViewById(R.id.tiedot_sokerimaara)).setText(df.format(arvot.get(7)) + "g");
        ((TextView)findViewById(R.id.tiedot_kuitumaara)).setText(df.format(arvot.get(8)) + "g");
        ((TextView)findViewById(R.id.tiedot_suolamaara)).setText(df.format(arvot.get(0)) + "g");


        ImageButton poistu = findViewById(R.id.tiedot_poistu);
        poistu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}