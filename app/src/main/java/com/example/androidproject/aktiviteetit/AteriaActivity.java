package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidproject.Ateria;
import com.example.androidproject.Elintarvike;
import com.example.androidproject.R;
import com.example.androidproject.RuokaAdapter;
import com.google.gson.Gson;

import java.util.List;

public class AteriaActivity extends AppCompatActivity {

    Ateria ateria;
    String ateriaJson;
    Gson gson = new Gson();

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ateria);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();

        lv = findViewById(R.id.ateria);
    }

    public void paivitaLista(View v) {
        ateriaJson = pref.getString("ateria", "");
        ateria = gson.fromJson(ateriaJson, Ateria.class);

        if (ateriaJson != "") {
            Log.d("ateria", ateria.toString());
             initList();
        }
    }

    private void initList() {
        TextView tv = findViewById(R.id.ravintotiedot);
        tv.setText(ateria.toString());


       lv.setAdapter(new ArrayAdapter<Elintarvike>(
               this,
               android.R.layout.simple_list_item_1,
               ateria.haeTarvikkeet()
       ));
    }


    public void hae(View v) {
        startActivity(new Intent(AteriaActivity.this, HaeActivity.class));
    }
}