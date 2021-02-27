package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.example.androidproject.R;

import java.text.DecimalFormat;

public class AteriaInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ateria_info);

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
        getWindow().setLayout((int) (width * .8), (int) (height * 0.4));
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}