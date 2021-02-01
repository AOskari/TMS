package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    EditText input;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = findViewById(R.id.textInput);
        search = findViewById(R.id.search);
       // foodList = findViewById(R.id.foodList);

    }

    public void search(View v) {
        Log.i("search", "search called");
        FoodInfo.getInfo(input.getText(), this);
    }
}