package com.example.androidproject;

import android.content.Context;
import android.text.Editable;
import android.util.Log;
import android.widget.ArrayAdapter;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FoodInfo {
    private static List<JSONObject> info;
    private static final FoodInfo information = new FoodInfo();
    private static RequestQueue requestQueue;

    private FoodInfo() {
        info = new ArrayList<>();
    }

}
