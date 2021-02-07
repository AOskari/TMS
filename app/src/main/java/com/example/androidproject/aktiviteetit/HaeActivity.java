package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.androidproject.Ateria;
import com.example.androidproject.Elintarvike;
import com.example.androidproject.R;
import com.example.androidproject.RuokaAdapter;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.lang.*;

public class HaeActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private List<Elintarvike> foodInfo;

    EditText input;
    Button search;
    ListView lv;

    Gson gson = new Gson();
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Ateria ateria;
    String ateriaJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hae);

        // Asetetaan takaisin nappi yläpalkkiin
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        foodInfo = new ArrayList<>();

        input = findViewById(R.id.text_input);
        search = findViewById(R.id.searchbtn);
        lv = findViewById(R.id.list_view);

        // Haetaan yhteinen SharedPreferences-olio, jonka avulla talletetaan lisätyt Elintarvikkeet aterioihin.
        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();

        ateriaJson = pref.getString("ateria", "");
        ateria = gson.fromJson(ateriaJson, Ateria.class);

    }

    public void getInfo(View v) {

        String url = "https://fineli.fi/fineli/api/v1/foods?q=" + input.getText().toString();
        Log.i("getInfo","getInfo called");
        Log.i("itemname",input.getText().toString());
        Log.i("url", url);

        requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("response info", "onResponse called");
                        foodInfo.clear();
                        Log.d("current info", foodInfo.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                JSONObject names = obj.getJSONObject("name");
                                String name = names.getString("fi");

                                Log.d("looping", obj.toString() + " " + i);
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

                        initList();
                        Log.d("Iterated", foodInfo.toString());
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "error called: " + error.toString());

                    }
                });

        // Tehdään API-kutsu ainoastaan jos hakukentässä on tekstiä.
        if (input.getText().toString().length() > 0) {
            requestQueue.add(jsonArrayRequest);
            Log.d("checking info", foodInfo.toString());
        }
    }

    public void suljeNappaimisto() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void initList() {
        // Päivitetään lista asettamalla RuokaAdapter ListViewille.
        RuokaAdapter adapter = new RuokaAdapter(this, foodInfo, pref, this);
        lv.setAdapter(adapter);

        // Lopuksi piilotetaan virtuaalinen näppäimistö
        suljeNappaimisto();
    }


}