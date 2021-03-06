package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.Ateria;
import com.example.androidproject.Elintarvike;
import com.example.androidproject.R;
import com.example.androidproject.HakuAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.lang.*;

/**
 * Luo näkymän, jonka avulla voidaa hakea Elintarvikkeita käyttäen Fineli:n avointa rajapintaa.
 * Elintarvikkeet listataan ListViewiin, ja niitä voidaan tarkastella tai lisätä nykyiseen ateriasuunnitelmaan.
 */
public class HakuActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private List<Elintarvike> foodInfo;
    private Gson gson = new Gson();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private EditText input;
    private ListView lv;
    private TextView ilmoitus;
    private Ateria ateria;
    private String ateriaJson;
    private ProgressBar latausKuvake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_haku);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("TMS - Haku");

        foodInfo = new ArrayList<>();
        latausKuvake = findViewById(R.id.latauskuvake);
        latausKuvake.setVisibility(View.GONE);
        input = findViewById(R.id.elintarvike_haku);
        lv = findViewById(R.id.list_view);
        ilmoitus = findViewById(R.id.haku_ilmoitus);
        ilmoitus.setVisibility(View.GONE);

        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();
        ateriaJson = pref.getString("ateria", "");
        ateria = gson.fromJson(ateriaJson, Ateria.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);
        bottomNavigationView.getMenu().findItem(R.id.suunnittele).setChecked(true);
    }

    /**
     * Asetetaan takaisin-painikkeelle fade-animaatio.
     */
    @Override
    public void onBackPressed() {}

    /**
     * Hakee Elintarvikkeita Fineli:n avoimesta rajapinnasta EditText-kentän merkkijonon perusteella.
     */
    public void getInfo(View v) {
        /**
         * Asetetaan latausanimaatio näkyville Requestin ajaksi, ja luodaan uusi Request Volley:n avulla.
         * ohjeet API-kutsun tekemiseen: https://developer.android.com/training/volley/simple
         */
        ilmoitus.setVisibility(View.INVISIBLE);
        latausKuvake.setVisibility(v.VISIBLE);
        String url = "https://fineli.fi/fineli/api/v1/foods?q=" + input.getText().toString();
        requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    /**
                     * Kutsun onnistuessa, tyhjennetään foodInfo-lista ja tallennetaan saadut tiedot siihen.
                     * Tämän jälkeen luodaan ruudulle kustomoitu ListView initList() metodin avulla.
                     */
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response info", "onResponse called");
                        foodInfo.clear();
                        Log.d("current info", foodInfo.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                /**
                                 * Fineli:n avoin rajapinta tarjoaa suuren määrän dataa yhdestä elintarvikkeesta,
                                 * tästä syystä seuraavat 3 riviä koodia valikoi halutun datan ja tallentaa datan Elintarvike-olioon.
                                 * Tämän jälkeen olio tallennetaan listaan.
                                 */
                                JSONObject obj = response.getJSONObject(i);
                                JSONObject names = obj.getJSONObject("name");
                                String name = names.getString("fi");

                                Elintarvike elintarvike = new Elintarvike(name, obj.getDouble("salt") / 1000, obj.getDouble("energyKcal"), obj.getDouble("fat"),
                                        obj.getDouble("protein"), obj.getDouble("carbohydrate"),
                                        obj.getDouble("organicAcids"), obj.getDouble("saturatedFat"), obj.getDouble("sugar"),
                                        obj.getDouble("fiber"), 100.0
                                );

                                foodInfo.add(elintarvike);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        if (response.length() <= 0) {
                            ilmoitus.setVisibility(View.VISIBLE);
                            ilmoitus.setBackgroundResource(R.drawable.error_alusta);
                            ilmoitus.setText("Tarkenna hakua.");
                        }
                        /**
                         * Lopuksi poistetaan latausanimaatio näkyvistä ja luodaan sisältö ListView:ille.
                         */
                        latausKuvake.setVisibility(View.GONE);
                        initList();
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "error: " + error.toString());
                    }
                });

        /**
         * Tehdään API-kutsu ainoastaan jos hakukentässä on tekstiä.
         */
        if (input.getText().toString().length() > 0) {
            requestQueue.add(jsonArrayRequest);
            Log.d("checking info", foodInfo.toString());
        } else {
            latausKuvake.setVisibility(View.GONE);
            ilmoitus.setVisibility(View.VISIBLE);
            ilmoitus.setBackgroundResource(R.drawable.error_alusta);
            ilmoitus.setText("Tarkenna hakua.");
        }
    }

    /**
     * Sulkee virtuaalisen näppäimistön.
     */
    public void suljeNappaimisto() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Näyttää ilmoituksen ja poistaa ilmoitukset näkyvistä 5 sekunnin kuluttua animoituna.
     */
    public void naytaIlmoitus(String nimi, boolean error) {
        ilmoitus.setVisibility(View.VISIBLE);

        if (error) {
            ilmoitus.setBackgroundResource(R.drawable.error_alusta);
            ilmoitus.setText("Syötä haluttu määrä.");
        } else {
            ilmoitus.setBackgroundResource(R.drawable.vihrea_alusta);
            ilmoitus.setText(nimi + " lisätty ateriaan.");
        }

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator());
        fadeIn.setDuration(300);

        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new DecelerateInterpolator());
        fadeOut.setDuration(5000);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                ilmoitus.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        AnimationSet animaatiot = new AnimationSet(false);
        animaatiot.addAnimation(fadeIn);
        animaatiot.addAnimation(fadeOut);
        ilmoitus.setAnimation(animaatiot);
    }


    // =================================================================== //
    // ========================= Private-metodit ========================= //
    // =================================================================== //

    /**
     * Luo ja päivittää ListView:in sisällön sekä sulkee näppäimistön.
     */
    private void initList() {
        HakuAdapter adapter = new HakuAdapter(this, foodInfo, pref, this);
        lv.setAdapter(adapter);
        suljeNappaimisto();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener alaPalkkiMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.koti:
                            startActivity(new Intent(HakuActivity.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Koti painettu");
                            finish();
                            break;
                        case R.id.suunnittele:
                            startActivity(new Intent(HakuActivity.this, AteriatActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Suunnittele painettu");
                            finish();
                            break;
                        case R.id.profiili:
                            startActivity(new Intent(HakuActivity.this, Profiili.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.i("Menu", "Profiili painettu");
                            finish();
                            break;
                    }
                    return false;
                }
            };
}