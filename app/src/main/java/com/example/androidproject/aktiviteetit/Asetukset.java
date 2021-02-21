package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.androidproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class Asetukset extends AppCompatActivity {
    public TextView yksikko1, yksikko2, paino, pituus, tav1, tav2;
    public Spinner tavoite1, tavoite2;
    public Button tallenna;
    public EditText nimi;
    public SharedPreferences asetukset;
    public SharedPreferences.Editor tiedot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asetukset);

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

        asetukset = getSharedPreferences("Tiedot", Activity.MODE_PRIVATE);

        String kayttaja = asetukset.getString("Käyttäjä", "");
        int annaPaino = asetukset.getInt("Paino", 0);
        int annaPituus = asetukset.getInt("Paino", 0);
        String naytaTav1 = asetukset.getString("Tavoite1", "");
        String naytaTav2 = asetukset.getString("Tavoite2", "");


        List<String> valinta = new ArrayList<String>();
        valinta.add("Valinta");
        valinta.add("Kalorit");
        valinta.add("Hiilihydraatti");
        valinta.add("Proteiini");

        ArrayAdapter<String> val = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valinta);
        val.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tavoite1.setAdapter(val);
        tavoite2.setAdapter(val);

        tavoite1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nimi1 = tavoite1.getSelectedItem().toString();

                if (nimi1.equals("Valinta")) {
                    tav1.setText("");
                    yksikko1.setText("");
                } else if (nimi1.equals("Kalorit")) {
                    tav1.setText("");
                    yksikko1.setText("kcal/vrk");
                } else {
                    tav1.setText("");
                    yksikko1.setText("g/vrk");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        tavoite2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nimi2 = tavoite2.getSelectedItem().toString();

                if (nimi2.equals("Valinta")) {
                    tav2.setText("");
                    yksikko2.setText("");
                } else if (nimi2.equals("Kalorit")) {
                    tav2.setText("");
                    yksikko2.setText("kcal/vrk");
                } else {
                    tav2.setText("");
                    yksikko2.setText("g/vrk");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);
    }

    public void tallenna(View v){
        String kuka = nimi.getText().toString();
        int kg = Integer.parseInt(paino.getText().toString());
        int cm = Integer.parseInt(pituus.getText().toString());
        String naytaT1 = tavoite1.getSelectedItem().toString() + "," + tav1.getText()+ "," + yksikko1.getText();
        String naytaT2 = tavoite2.getSelectedItem().toString() + "," + tav2.getText() + "," + yksikko2.getText();

        tiedot = asetukset.edit();
        tiedot.putString("Käyttäjä", kuka);
        tiedot.putInt("Paino", kg);
        tiedot.putInt("Pituus", cm);
        tiedot.putString("Tavoite1", naytaT1);
        tiedot.putString("Tavoite2", naytaT2);
        tiedot.commit();
    }

    /**
     * Luo alapalkin toiminnallisuuden.
     */
    private BottomNavigationView.OnNavigationItemSelectedListener alaPalkkiMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.koti:
                            startActivity(new Intent(Asetukset.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Koti painettu");
                            break;
                        case R.id.suunnittele:
                            startActivity(new Intent(Asetukset.this, AteriatActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Suunnittele painettu");
                            break;
                        case R.id.profiili:
                            startActivity(new Intent(Asetukset.this, Asetukset.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.i("Menu", "Profiili painettu");
                            break;
                    }
                    return false;
                }
            };
}