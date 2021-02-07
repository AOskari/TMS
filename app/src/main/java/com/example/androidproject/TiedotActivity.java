package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class TiedotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiedot);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle b = getIntent().getExtras();
        Elintarvike e = b.getParcelable("TARVIKE");

        ((TextView)findViewById(R.id.tiedot_info)).setText(e.tarkatArvot());
        ((TextView)findViewById(R.id.tiedot_nimi)).setText(e.haeNimi());
    }


}