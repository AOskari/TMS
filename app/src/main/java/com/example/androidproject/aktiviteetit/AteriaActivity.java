package com.example.androidproject.aktiviteetit;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

/**
 * Aktiviteetti, jonka avulla luodaan, muokataan ja tarkastellaan aterioita.
 */
public class AteriaActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private PieChart piiras;
    private ListView lv;
    private EditText editText;
    private Ateria ateria;
    private String ateriaJson;
    private Gson gson = new Gson();
    private Calendar kalenteri;

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
    private boolean muokkaus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ateria);

        kalenteri = Calendar.getInstance();
        piiras = findViewById(R.id.piiras);
        kalorit = findViewById(R.id.kalorit);
        proteiini = findViewById(R.id.proteiini);
        hiilihydraatti = findViewById(R.id.hiilihydraatti);
        rasva = findViewById(R.id.rasva);
        sokeri = findViewById(R.id.sokeri_maara);
        tyydyttynytRasva = findViewById(R.id.rasva_maara);
        suola = findViewById(R.id.suola_maara);
        kuitu = findViewById(R.id.kuitu_maara);
        kellonaika = findViewById(R.id.aika);
        lv = findViewById(R.id.ateria);
        editText = findViewById(R.id.aterianimi);
        pref = getApplicationContext().getSharedPreferences("mainPref",0);
        editor = pref.edit();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ateriaJson = pref.getString("ateria", "");

        /**
         * Tarkastellaan sisältääkö pysyväismuisti Ateria-oliota, jos ei, luodaan uusi Ateria-olio.
         */
        if (ateriaJson.equals("")) {
            ateria = new Ateria("Luonnos ateria");
        } else {
            ateria = gson.fromJson(ateriaJson, Ateria.class);
            paivitaLista();
        }

        /**
         * Asetetaan alustavasti nykyinen päivämäärä ja tallennetaan ateria pysyväismuistiin.
         */
        ateria.asetaPaivamaara(kalenteri.get(Calendar.DAY_OF_MONTH), kalenteri.get(Calendar.MONTH), kalenteri.get(Calendar.YEAR));
        tallenna();

        /**
         * Lisätään mahdollisuus lisätä päiväys käyttäen KalenteriActivityä.
         */
            findViewById(R.id.kalenteri_nappi).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AteriaActivity.this, KalenteriActivity.class);
                    startActivity(intent);
                }
            });

        /**
         *  Tallennetaan aterian nimi joka kerta, kun syöttökenttää muokataan.
         */
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ateria.asetaNimi(editText.getText().toString());
                tallenna();
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        paivitaLista();
        editText.setText(ateria.haeNimi());

        /**
         * Tarkastetaan onko käyttäjä muokkaustilassa.
         */
        try{
            Bundle b = getIntent().getExtras();
            muokkaus = b.getBoolean("EXTRA");
        } catch(Exception e) {
            Log.d("Exception", e.toString());
            muokkaus = false;
        }

        Log.d("ateria aika", ateria.aikaString());
        Log.d("ateria paivamaara", ateria.paivamaaraString());

        /**
         * Päivitetään päivämäärä ja kellonaika.
         */
        paiva = pref.getInt("paiva", 0);
        kuukausi = pref.getInt("kuukausi", 0);
        vuosi = pref.getInt("vuosi", 0);
        ((TextView)findViewById(R.id.paivays)).setText(ateria.paivamaaraString());
        kellonaika.setText(ateria.aikaString());
        asetaKaloriPalkki();
    }

    /**
     * Päivittää AteriaActivityn näkymän tiedot.
     */
    public void paivitaLista() {
        ateriaJson = pref.getString("ateria", "");
        ateria = gson.fromJson(ateriaJson, Ateria.class);

        DecimalFormat df = new DecimalFormat("#.#");
        DecimalFormat df2 = new DecimalFormat("#.##");

        asetaKaloriPalkki();
        initList();

        double prot = ateria.haeRavinto().get(1);
        double hh = ateria.haeRavinto().get(2);
        double ras = ateria.haeRavinto().get(3);

        double protPros = (prot / ateria.haeRavinto().get(0)) * 100;
        double hhPros = (hh / ateria.haeRavinto().get(0)) * 100;
        double rasvaPros = (ras / ateria.haeRavinto().get(0)) * 100;

        if (prot == 0 && hh == 0 && ras == 0) {
           asetaTekstit("", "", "", "", "", "", "");
        } else {
            asetaTekstit("Proteiini  " + df.format(protPros) + "%  " + df.format(prot) + "g",
                    "Hiilihydraatit  " + df.format(hhPros) + "%  " + df.format(hh) + "g",
                    "Rasva  " + df.format(rasvaPros) + "%  " + df.format(ras) + "g",
                    df.format(ateria.haeRavinto().get(5)) + "g",
                    df.format(ateria.haeRavinto().get(6)) + "g", df2.format(ateria.haeRavinto().get(7)) + "g",
                    df.format(ateria.haeRavinto().get(8)) + "g");
        }

        /**
         * Luodaan uudet tiedot ympyrädiagrammia varten.
         */
        ArrayList lista = new ArrayList();
        ArrayList ravintoarvo = new ArrayList();
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

        /**
         * Asetetaan uudet ravintoarvot diagrammiin, poistetaan ylimääräiset merkinnät
         * ja asetetaan värit sekä animaatio.
         */
        data.setDrawValues(false);
        piiras.setData(data);
        piiras.setDescription("");
        piiras.getLegend().setEnabled(false);
        piiras.setDrawSliceText(false);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        piiras.animateXY(2000, 2000);
        kalorit.setText(Math.round(ateria.haeRavinto().get(4)) + " kcal");
        editText.setText(ateria.haeNimi());
        kellonaika.setText(ateria.aikaString());
    }

    /**
     * Luo uuden Ateria-olion AteriaActivityn näkymässä olevilla tiedoilla
     * ja tallentaa kyseisen olion AteriaLista singletoniin. Lopuksi tyhjentää aktiviteetin näkymän.
     */
    public void tallennaAteria(View v) {

        /**
         * Tuodaan esille varmistusikkuna, ja kysytään käyttäjältä tallennetaanko ateria.
         */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Tallenna" + ateria.haeNimi() +  "ateria?");

        builder.setPositiveButton("Tallenna",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String aterialistaJson = pref.getString("aterialista", "");
                        AteriaLista lista = gson.fromJson(aterialistaJson, AteriaLista.class);

                        Log.d("muokkaus boolean", "" + muokkaus);

                        Log.d("listan sisältö", lista.toString());

                        boolean muokkaus = pref.getBoolean("muokkaus", false);

                        if (muokkaus) {
                            lista.poistaAteria(ateria.haeId());
                            editor.putBoolean("muokkaus", false);
                        } else {
                            ateria.asetaId(lista.seuraavaId());
                        }

                        ateria.asetaPaivamaara(paiva, kuukausi, vuosi);
                        lista.lisaaAteria(ateria);
                        aterialistaJson = gson.toJson(lista);
                        editor.putString("aterialista", aterialistaJson);
                        editor.commit();

                        Log.d("aterialista", "" + lista.tulostaAteriat());

                        ateria = new Ateria("luonnos ateria");
                        tallenna();
                        paivitaLista();
                        finish();
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

    /**
     * Tuo esille olion jonka avulla voi asettaa halutun ajan.
     */
    public void asetaAika(View v) {
        int tunti = kalenteri.get(Calendar.HOUR_OF_DAY);
        int minuutti = kalenteri.get(Calendar.MINUTE);

        TimePickerDialog aika = new TimePickerDialog(AteriaActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                ateria.asetaAika(hourOfDay, minute);
                Log.d("ateria aika", ateria.aikaString());
                kellonaika.setText(ateria.aikaString());
                tallenna();
                paivitaLista();
            }
        }, tunti, minuutti, true);
        aika.show();
    }

    public void hae(View v) {
        startActivity(new Intent(AteriaActivity.this, HakuActivity.class));
    }

    /**
     * Luo ListViewin kustomoidun sisällön.
     */
    private void initList() {
        AteriaAdapter adapter = new AteriaAdapter(this, pref);
        lv.setAdapter(adapter);
    }

    /**
     * Asettaa tiedot kaloripalkkiin.
     */
    private void asetaKaloriPalkki() {
        TextView nykyinen = findViewById(R.id.nykyinen_kalorit);
        TextView ateriaKal = findViewById(R.id.ateria_kalorit);
        TextView yhteensa = findViewById(R.id.yhteensa_kalorit);

        String listaJson = pref.getString("aterialista", "");
        AteriaLista lista = gson.fromJson(listaJson, AteriaLista.class);

        nykyinen.setText(lista.haeKalorit(paiva, kuukausi, vuosi) + " kcal");
        ateriaKal.setText(Math.round(ateria.haeRavinto().get(4)) + " kcal");
        yhteensa.setText(lista.haeKalorit(paiva, kuukausi, vuosi) + Math.round(ateria.haeRavinto().get(4)) + " kcal");

    }

    /**
     * Asettaa tekstit näytöllä oleville TextVieweille.
     */
    private void asetaTekstit(String prot, String hh, String rasva, String sokeri, String tyydyt, String suola, String kuitu) {
        this.proteiini.setText(prot);
        this.hiilihydraatti.setText(hh);
        this.rasva.setText(rasva);
        this.sokeri.setText(sokeri);
        this.tyydyttynytRasva.setText(tyydyt);
        this.suola.setText(suola);
        this.kuitu.setText(kuitu);
    }

    /**
     * Tallentaa nykyisen aterian pysyväismuistiin.
     */
    private void tallenna() {
        ateriaJson = gson.toJson(ateria);
        editor.putString("ateria", ateriaJson);
        editor.commit();
    }
}