package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.example.androidproject.R;

import java.util.ArrayList;
import java.util.List;
// implements OnItemSelectedListener
public class Asetukset extends AppCompatActivity {
    public TextView yksikko1, yksikko2, paino, pituus, tav1, tav2;
    public Spinner tavoite1, tavoite2;
    public Button tallenna;
    public EditText nimi;
    //String[] valinta1, valinta2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asetukset);


        tavoite1 = findViewById(R.id.tavoite1);
        tavoite2 = findViewById(R.id.tavoite2);
        tallenna = findViewById(R.id.tallennus);
        nimi = findViewById(R.id.annaNimi);
        paino = findViewById(R.id.paino);
        pituus = findViewById(R.id.pituus);
        tav1 = findViewById(R.id.tav1);
        tav2 = findViewById(R.id.tav2);
        yksikko1 = findViewById(R.id.yksikko1);
        yksikko2 = findViewById(R.id.yksikko2);

        //tavoite1.setOnItemSelectedListener(this);
        //tavoite2.setOnItemSelectedListener(this);

        List<String> valinta = new ArrayList<String>();
        valinta.add("Valinta");
        valinta.add("Kalorit");
        valinta.add("Hiilihydraatti");
        valinta.add("Proteiini");

        ArrayAdapter<String> val = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valinta);
        val.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tavoite1.setAdapter(val);
        tavoite2.setAdapter(val);
        //naytaYksikko();

        tavoite1.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nimi1 = tavoite1.getSelectedItem().toString();

                if (nimi1.equals("Valinta")) {
                    tav1.setText("");
                    yksikko1.setText("");
                } else if (nimi1.equals("Kalorit")) {
                    tav1.setText("");
                    yksikko1.setText("kcal/vrk");
                } else {
                    tav1.setText("");
                    yksikko1.setText("g/vrk");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        tavoite2.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nimi2 = tavoite2.getSelectedItem().toString();

                if (nimi2.equals("Valinta")) {
                    tav2.setText("");
                    yksikko2.setText("");
                } else if (nimi2.equals("Kalorit")) {
                    tav2.setText("");
                    yksikko2.setText("kcal/vrk");
                } else {
                    tav2.setText("");
                    yksikko2.setText("g/vrk");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}