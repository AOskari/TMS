package com.example.androidproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.androidproject.fragmentit.Koti;
import com.example.androidproject.fragmentit.Profiili;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavMethod);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, new Koti()).commit();

    }

    // Luodaan metodi, jonka avulla muutetaan näytön tilaa riippuen mitä nappia on painettu.
    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavMethod=new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    Fragment fragment = null;

                    switch(item.getItemId()) {

                        case R.id.koti:
                            fragment = new Koti();
                            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
                            break;
                        case R.id.suunnittele:
                            startActivity(new Intent(MainActivity.this, SearchActivity.class));
                            break;
                        case R.id.profiili:
                            fragment = new Profiili();
                            getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, fragment).commit();
                            break;
                    }
                    return false;
                }
            };
}