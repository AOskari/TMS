package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
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
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Suunnittele painettu");
                            break;
                        case R.id.profiili:
                            startActivity(new Intent(MainActivity.this, Asetukset.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.i("Menu", "Profiili painettu");
                            break;
                    }
                    return false;
                }
            };
}