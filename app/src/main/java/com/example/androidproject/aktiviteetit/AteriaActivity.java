package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.androidproject.Ateria;
import com.example.androidproject.AteriaAdapter;

import com.example.androidproject.AteriaLista;
import com.example.androidproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class AteriaActivity extends AppCompatActivity {

    Ateria ateria;
    String ateriaJson;
    Gson gson = new Gson();

    private TextView proteiini;
    private TextView hiilihydraatti;
    private TextView rasva;
    private TextView kalorit;

    private TextView sokeri;
    private TextView tyydyttynytRasva;
    private TextView suola;
    private TextView kuitu;
    private TextView kellonaika;

    private int paiva;
    private int kuukausi;
    private int vuosi;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    PieChart piiras;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ateria);

        piiras = findViewById(R.id.piiras);
        kalorit = findViewById(R.id.kalorit);

        proteiini = findViewById(R.id.proteiini);
        hiilihydraatti = findViewById(R.id.hiilihydraatti);
        rasva = findViewById(R.id.rasva);

        sokeri = findViewById(R.id.sokeri_maara);
        tyydyttynytRasva = findViewById(R.id.rasva_maara);
        suola = findViewById(R.id.suola_maara);
        kuitu = findViewById(R.id.kuitu_maara);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();

        kellonaika = findViewById(R.id.aika);
        lv = findViewById(R.id.ateria);

        ateriaJson = pref.getString("ateria", "");

        // Jos SharedPreferences ei sisällä ateria-nimikkeellä tallennettua merkkijonoa,
        if (ateriaJson == "") {

            // Luodaan uusi Ateria-olio, johon tullaan tallentamaan kaikki valitut Elintarvike-oliot.
            ateria = new Ateria("Luonnos ateria");
            ateriaJson = gson.toJson(ateria);
            editor.putString("ateria", ateriaJson);
            editor.commit();
        } else {
            ateria = gson.fromJson(ateriaJson, Ateria.class);
        }

        ImageButton kalenterNappi = findViewById(R.id.kalenteri_nappi);

        // Lisätään mahdollisuus lisätä päiväys käyttäen kalenteria.
            kalenterNappi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AteriaActivity.this, KalenteriActivity.class);
                    startActivity(intent);
                }
            });
    }

    @Override
    public void onResume() {
        super.onResume();
        paivitaLista();

        paiva = pref.getInt("paiva", 0);
        kuukausi = pref.getInt("kuukausi", 0);
        vuosi = pref.getInt("vuosi", 0);

        if (paiva != 0) {
            ((TextView)findViewById(R.id.paivays)).setText(paiva + "/" + kuukausi + "/" + vuosi);
        }

        ateria.asetaAika(pref.getInt("tunnit", 0), pref.getInt("minuutit", 0));
        kellonaika.setText(ateria.aikaString());

    }

    public void paivitaLista() {
        ateriaJson = pref.getString("ateria", "");
        ateria = gson.fromJson(ateriaJson, Ateria.class);

        if (ateriaJson != "") {
            DecimalFormat df = new DecimalFormat("#.#");
            DecimalFormat df2 = new DecimalFormat("#.##");

            Log.d("ateria", ateria.toString());
             initList();

             ArrayList lista = new ArrayList();
             ArrayList ravintoarvo = new ArrayList();

            //TODO: piiraan toiminnallisuus

            double prot = ateria.haeRavinto().get(1);
            double hh = ateria.haeRavinto().get(2);
            double ras = ateria.haeRavinto().get(3);

            double protPros = (prot / ateria.haeRavinto().get(0)) * 100;
            double hhPros = (hh / ateria.haeRavinto().get(0)) * 100;
            double rasvaPros = (ras / ateria.haeRavinto().get(0)) * 100;

            if (prot == 0 && hh == 0 && ras == 0) {
                proteiini.setText("");
                hiilihydraatti.setText("");
                rasva.setText("");

                sokeri.setText("");
                tyydyttynytRasva.setText("");
                suola.setText("");
                kuitu.setText("");
            } else {
                proteiini.setText("Proteiini  " + df.format(protPros) + "%  " + df.format(prot) + "g");
                hiilihydraatti.setText("Hiilihydraatit  " + df.format(hhPros) + "%  " + df.format(hh) + "g");
                rasva.setText("Rasva  " + df.format(rasvaPros) + "%  " + df.format(ras) + "g");

                sokeri.setText(df.format(ateria.haeRavinto().get(5)) + "g");
                tyydyttynytRasva.setText(df.format(ateria.haeRavinto().get(6)) + "g");
                suola.setText(df2.format(ateria.haeRavinto().get(7)) + "g");
                kuitu.setText(df.format(ateria.haeRavinto().get(8)) + "g");
            }

            float m1 = (float) prot;
            float m2 = (float) hh;
            float m3 = (float) ras;

            lista.add(new Entry(m1, 0));
            lista.add(new Entry(m2, 1));
            lista.add(new Entry(m3, 2));

            PieDataSet dataSet = new PieDataSet(lista, "");

            ravintoarvo.add("proteiini");
            ravintoarvo.add("hiilihydraatti");
            ravintoarvo.add("rasva");

            PieData data = new PieData(ravintoarvo, dataSet);

            // Asetetaan uudet ravintoarvot ja poistetaan ylimääräiset merkinnät pois.
            data.setDrawValues(false);
            piiras.setData(data);
            piiras.setDescription("");
            piiras.getLegend().setEnabled(false);
            piiras.setDrawSliceText(false);

            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            piiras.animateXY(2000, 2000);

            kalorit.setText(Math.round(ateria.haeRavinto().get(4)) + " kcal");
        }
    }

    private void initList() {
        AteriaAdapter adapter = new AteriaAdapter(this, pref);
        lv.setAdapter(adapter);
    }

    // Luodaan metodi aterian tallentamiseen

    public void tallennaAteria(View v) {

        // Tuodaan esille varmistusikkuna esille, ja kysytään käyttäjältä tallennetaanko ateria.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Tallenna suunniteltu ateria?");

        // Jos käyttäjä valitsee poista,
        builder.setPositiveButton("Tallenna",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Tallennetaan ateria AteriaListaan, päivitetään tiedot SharedPreferencesiin ja tyhjennetään AteriaActivityn ikkuna.
                        ateria.asetaPaivamaara(paiva, kuukausi, vuosi);
                        //TODO: Kellon ajan asettaminen

                        String aterialistaJson = pref.getString("aterialista", "");

                        AteriaLista lista = gson.fromJson(aterialistaJson, AteriaLista.class);
                        lista.lisaaAteria(ateria);
                        aterialistaJson = gson.toJson(lista);
                        editor.putString("aterialista", aterialistaJson);
                        editor.commit();

                        ateria = new Ateria("luonnos ateria");
                        ateriaJson = gson.toJson(ateria);
                        editor.putString("ateria", ateriaJson);
                        editor.commit();

                        paivitaLista();
                        Log.d("aterialista", aterialistaJson);

                    }
                });

        builder.setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    // Luodaan metodi ajan asettamista varten.
    public void asetaAika(View v) {

        Calendar kalenteri = Calendar.getInstance();
        int tunti = kalenteri.get(Calendar.HOUR_OF_DAY);
        int minuutti = kalenteri.get(Calendar.MINUTE);

        // Luodaan TimePickerDialog-olio ajan valitsemista varten
        TimePickerDialog aika = new TimePickerDialog(AteriaActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editor.putInt("tunnit", hourOfDay);
                editor.putInt("minuutit", minute);
                editor.commit();

                ateria.asetaAika(hourOfDay, minute);
                Log.d("ateria aika", ateria.aikaString());
                kellonaika.setText(ateria.aikaString());

            }
        }, tunti, minuutti, true);

        // Lopuksi näytetään TimePickerDialog olio näytöllä.
        aika.show();
    }

    public void hae(View v) {
        startActivity(new Intent(AteriaActivity.this, HakuActivity.class));
    }
}