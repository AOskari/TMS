package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.lang.*;

public class SearchActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private List<Elintarvike> foodInfo;

    EditText input;
    Button search;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        foodInfo = new ArrayList<>();

        input = findViewById(R.id.text_input);
        search = findViewById(R.id.searchbtn);
        lv = findViewById(R.id.list_view);

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
                  //      info.clear();
                        Log.d("current info", foodInfo.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                JSONObject type = obj.getJSONObject("type");
                                String code = type.getString("code");
                                JSONObject names = obj.getJSONObject("name");
                                String name = names.getString("fi");

                                Log.d("looping", obj.toString() + " " + i);
                                Elintarvike elintarvike = new Elintarvike(name, obj.getDouble("salt"), obj.getDouble("energyKcal"), obj.getDouble("fat"),
                                        obj.getDouble("protein"), obj.getDouble("carbohydrate"),
                                        obj.getDouble("organicAcids"), obj.getDouble("saturatedFat"), obj.getDouble("sugar"),
                                        obj.getDouble("fiber")
                                );

                                foodInfo.add(elintarvike);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        initList();
                        Log.d("Iterated", "Info-list data:");
                        Log.d("Iterated", foodInfo.toString());
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "error called: " + error.toString());

                    }
                });
        
        requestQueue.add(jsonArrayRequest);
        Log.d("checking info", foodInfo.toString());

    }


    private void initList() {
        // Päivitetään lista haetuilla ruoka-aineilla.
        lv.setAdapter(new ArrayAdapter<Elintarvike>(
                this,
                android.R.layout.simple_list_item_1,
                foodInfo
        ));

        // Lopuksi piilotetaan virtuaalinen näppäimistö
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }


}