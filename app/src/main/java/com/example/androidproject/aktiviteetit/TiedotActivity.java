package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.TextView;

import com.example.androidproject.Elintarvike;
import com.example.androidproject.R;
import com.google.gson.Gson;

public class TiedotActivity extends AppCompatActivity {

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiedot);

        // Piilotetaan yläpalkki.
        getSupportActionBar().hide();

        // Luodaan popup-ikkuna, joka ei peitä koko ruutua.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        // Asetetaan popup-ikkunan koko 60% näytön koosta.
        getWindow().setLayout((int) (width * .8), (int) (height * 0.4));

        Bundle b = getIntent().getExtras();
        Elintarvike e = gson.fromJson(b.getString("TARVIKE"), Elintarvike.class);

        ((TextView)findViewById(R.id.tiedot_info)).setText(e.tarkatArvot());
        ((TextView)findViewById(R.id.tiedot_nimi)).setText(e.haeNimi());
    }


}