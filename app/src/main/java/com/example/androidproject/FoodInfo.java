package com.example.androidproject;

import android.content.Context;
import android.text.Editable;
import android.util.Log;

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

public class FoodInfo {
    private static List<JSONObject> info;
    private static final FoodInfo information = new FoodInfo();
    private static RequestQueue requestQueue;

    //ListView view;

    private FoodInfo() {
        info = new ArrayList<>();
    //    view = view.findViewById(R.id.list_view);
    }

    public static void getInfo(Editable itemName, Context name) {
        String url = "https://fineli.fi/fineli/api/v1/foods?q=" + itemName.toString();
        Log.i("getInfo","getInfo called");
        Log.i("itemname",itemName.toString());
        Log.i("url", url);
        
     
        
        requestQueue = Volley.newRequestQueue(name);
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
        // test test
            @Override
            public void onResponse(JSONArray response) {
                Log.d("response info", "onResponse called");
                Log.d("response info", response.toString());

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        JSONObject type = obj.getJSONObject("type");
                        String code = type.getString("code");

                        Log.d("looping", obj.toString() + " " + i);
                        Log.d("looping", code + " " + i);
                        if (code.equals("FOOD")) {
                            info.add(obj);

                            //TODO: Add food items to the ListView-widget.
                            //TODO: Create a Food-class.
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Iterated", "Info-list data:");
                Log.d("Iterated", info.toString());
                // TODO: Save the data somewhere
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error", "error called: " + error.toString());
                // TODO: Handle error
            }
        });
        requestQueue.add(jsonObjectRequest);
        Log.i("checking info", info.toString());
    }
}
