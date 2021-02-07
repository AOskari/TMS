package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.androidproject.Elintarvike;
import com.example.androidproject.R;

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