package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.androidproject.R;

public class Asetukset extends AppCompatActivity {
    public TextView ohje, yksikko1, yksikko2, paino, pituus, tav1, tav2;
    public Spinner tavoite1, tavoite2;
    public Button tallenna;
    public EditText nimi;
    String[] valinta1, valinta2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asetukset);

        ohje = findViewById(R.id.ohje);
        tavoite1 = findViewById(R.id.tavoite1);
        tavoite2 = findViewById(R.id.tavoite2);
        tallenna = findViewById(R.id.tallennus);
        nimi = findViewById(R.id.annaNimi);
        paino = findViewById(R.id.paino);
        pituus = findViewById(R.id.pituus);
        tav1 = findViewById(R.id.tav1);
        tav2 = findViewById(R.id.tav2);
        yksikko1 = findViewById(R.id.yksikko1);
        yksikko2 = findViewById(R.id.yksikko2);
        //naytaVal1();
        //naytaVal2();

    }
    private void naytaVal1() {
        ArrayAdapter<String> val1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valinta1);
        val1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tavoite1.setAdapter(val1);
    }
    private void naytaVal2() {
        ArrayAdapter<String> val2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valinta2);
        val2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tavoite2.setAdapter(val2);
    }

}