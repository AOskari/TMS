package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.applandeo.materialcalendarview.EventDay;
import com.example.androidproject.Ateria;
import com.example.androidproject.AteriaLista;
import com.example.androidproject.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static com.example.androidproject.AteriaLista.haeLista;

public class AteriatActivity extends AppCompatActivity {

    private String ateriatJson;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();

    private ListView lv;
    private TextView paivamaara;

    private int paiva;
    private int kuukausi;
    private int vuosi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ateriat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();

        paivamaara = findViewById(R.id.paivamaara);
        lv = findViewById(R.id.aterialista);

        ateriatJson = pref.getString("aterialista", "");

        if (ateriatJson.equals("")) {
            ateriatJson = gson.toJson(haeLista());
            editor.putString("aterialista", ateriatJson);
            editor.commit();
            Log.d("aterialista", ateriatJson);
        }

        ImageButton nappi = findViewById(R.id.paivamaaraNappi);
        nappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AteriatActivity.this, KalenteriActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        ateriatJson = pref.getString("aterialista", "");

        paiva = pref.getInt("paiva", 0);
        kuukausi = pref.getInt("kuukausi", 0);
        vuosi = pref.getInt("vuosi", 0);

        paivamaara.setText(paiva + "/" + kuukausi + "/" + vuosi);

        naytaAteriat();
    }

    private void naytaAteriat() {
        AteriaLista lista = gson.fromJson(ateriatJson, AteriaLista.class);
        lv.setAdapter(new ArrayAdapter<Ateria>(
                this,
                android.R.layout.simple_list_item_1,
                lista.haePaivamaaralla(paiva, kuukausi, vuosi)
        ));
    }

    // Luodaan funktio, joka avaa SearchActivity aktiviteetin.
    public void luoAteria(View v) {
        startActivity(new Intent(AteriatActivity.this, AteriaActivity.class));
    }

}