package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.applandeo.materialcalendarview.EventDay;
import com.example.androidproject.R;

import java.util.ArrayList;
import java.util.List;

public class AteriatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ateriat);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    // Luodaan funktio, joka avaa SearchActivity aktiviteetin.
    public void luoAteria(View v) {
        startActivity(new Intent(AteriatActivity.this, AteriaActivity.class));
        Log.d("Funktio", "luoAteria kutsuttu.");
    }

}