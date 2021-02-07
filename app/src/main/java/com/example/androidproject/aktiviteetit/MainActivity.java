package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.androidproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Asetetaan alapalkille Listener, joka vaihtaa aktiviteettia nappien perusteella.
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);
    }

    // Luodaan metodi, jonka avulla muutetaan näytön tilaa riippuen mitä nappia on painettu.
    private BottomNavigationView.OnNavigationItemSelectedListener alaPalkkiMethod=new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch(item.getItemId()) {

                        case R.id.koti:
                            //TODO: luo koodi, joka menee takaisin koti-ikkunaan
                            break;
                        case R.id.suunnittele:
                            startActivity(new Intent(MainActivity.this, AteriatActivity.class));
                            break;
                        case R.id.profiili:
                            //TODO: luo koodi, joka menee profiili-ikkunaan
                            break;
                    }
                    return false;
                }
            };
}