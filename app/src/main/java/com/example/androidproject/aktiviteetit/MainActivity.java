package com.example.androidproject.aktiviteetit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidproject.Ateria;
import com.example.androidproject.AteriaLista;
import com.example.androidproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.util.Calendar;

import static com.example.androidproject.AteriaLista.haeLista;


public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private Calendar kalenteri;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Gson gson = new Gson();
    private AteriaLista aterialista;
    private String aterialistaJson;

    private int paiva;
    private int kuukausi;
    private int vuosi;

    ProgressBar mProgress;
    ProgressBar mProgress2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kalenteri = Calendar.getInstance();

        //Ympyrä progressbar
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circle2);
        Drawable drawable2 = res.getDrawable(R.drawable.circle2);

        mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);

        //mProgress = tavoite 1, mProgress tavoite 2.
        mProgress2 = (ProgressBar) findViewById(R.id.circularProgressbar2);

        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

        mProgress2.setProgress(0);   // Main Progress
        mProgress2.setSecondaryProgress(100); // Secondary Progress
        mProgress2.setMax(100); // Maximum Progress
        mProgress2.setProgressDrawable(drawable2);
        /**
         * Haetaan AteriaLista-singleton pysyväismuistista.
         * Jos singletonia ei löydy, luodaan uusi instanssi ja tallennetaan se pysyväismuistiin.
         */
        pref = getApplicationContext().getSharedPreferences("mainPref", 0);
        editor = pref.edit();
        aterialistaJson = pref.getString("aterialista", "");

        if (aterialistaJson.equals("")) {
            aterialistaJson = gson.toJson(haeLista());
            editor.putString("aterialista", aterialistaJson);
            editor.commit();
            Log.d("aterialista", aterialistaJson);
        }

        aterialista = gson.fromJson(aterialistaJson, AteriaLista.class);

        // Asetetaan alapalkille kuuntelija, joka vaihtaa aktiviteettia nappien perusteella.
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);
        bottomNavigationView.getMenu().findItem(R.id.koti).setChecked(true);
    }

    @SuppressLint("SetTextI18n")
    protected void onResume() {
        super.onResume();

        paiva = kalenteri.get(Calendar.DAY_OF_MONTH);
        kuukausi = kalenteri.get(Calendar.MONTH);
        vuosi = kalenteri.get(Calendar.YEAR);
        TextView kaloriTavoite = findViewById(R.id.testitext);

        /**
         * Tässä ne syödyt kalorit, käytä tätä sitten kun teet jonkun vitun hienon laskun : DDD.
         */
        int kalorit = (int) Math.round(aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi).get(0));
        int syodytProtskut = (int) Math.round(aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi).get(1));
        //int prosentit = kalorit / tiedot1_kalorit;

        /**
         * Haetaan pysyväismuistista käyttäjän tiedot ja tavoitteet.
         */
        SharedPreferences sharedPreferences = getSharedPreferences("Tiedot", Context.MODE_PRIVATE);
        String nimi = sharedPreferences.getString("Käyttäjä", "");
        String tiedot1 = sharedPreferences.getString("Tavoite1", "");
        String tiedot2 = sharedPreferences.getString("Tavoite2", "");

        //Päivän tähän asti syödyt kalorit verrattuna omaan tavoitteeseen.
        // String jokuvitunhienolaskujonkateenmyöhemmin = "";

        /**
         * Asetetaan tiedot, mikäli tietoja on tallennettu asetuksissa.
         */
        if (!tiedot1.equals("")) {
            String[] tiedot1_lista = tiedot1.split(" ");
            String tiedot1_kalorit = tiedot1_lista[1];
            float saatuTieto1 = Float.parseFloat(tiedot1_kalorit);
            int kaloritYht = (int) Math.round(saatuTieto1);
            TextView prossat = findViewById(R.id.prossat);

            /**
             *     Muutetaan intit doubleiksi laskua varten.
             */
            if (saatuTieto1 != 0.0f) {
                double k = kalorit;
                double kY = kaloritYht;
                double prosentit = k / kY * 100;
                int prosentitI = (int) Math.round(prosentit);
                //  double prosentitD = Math.round(prosentit);

                //Muutetaan vastaus Stringiksi ja pyöristetään se.
                String prosentitS = Integer.toString(prosentitI);

                //Jäljellä olevat kalorit.
                int jaljella = kaloritYht - kalorit;

                //Tekstin tasaus keskelle.
                kaloriTavoite.setGravity(Gravity.CENTER);


                kaloriTavoite.setText("Tavoite päivässä :" + tiedot1_kalorit + " kcal" + "\nKaloreita jäljellä: " + jaljella + " kcal");

                if (prosentitI > 100) {
                    //Kertoo että ylitit päivän kaloritavoitteen.
                    kaloriTavoite.setText("Tavoite päivässä: " + tiedot1_kalorit + " kcal" + "\nKaloreita jäljellä: " + jaljella + " kcal \nYlitit päivän kaloritavoitteen.");
                }
                //Näytetään prosentit.
                //TextView prossat = findViewById(R.id.prossat);
                prossat.setText(prosentitS + " %");

                //Prosenttipalkki.
                ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.progressBar); // initiate the progress bar
                simpleProgressBar.setProgress(prosentitI);

                //Prosenttipalkki ympyrä
                mProgress.setProgress(prosentitI);   // Main Progress

            } else {
                kaloriTavoite.setText("");
                prossat.setText("");
            }

        }

        //Toisen itseasetetun tavoitteen näyttäminen.
        if (!tiedot2.equals("")) {
            String[] tiedot2_lista = tiedot2.split(" ");
            String tiedot2_2 = tiedot2_lista[1];
            String tiedot2_1 = tiedot2_lista[0];

           // int proteiiniTavoite = (int) Integer.parseInt(tiedot2_2);
            String proteiinit = (tiedot2_2.substring(0, tiedot2_2.length() - 2));
            int proteiinitInt = Integer.parseInt(proteiinit);

          int proteiiniProsentit = syodytProtskut / proteiinitInt * 100;

            mProgress2.setProgress(proteiiniProsentit);   // Main Progress
            TextView proteiiniTeksti = findViewById(R.id.protskuTeksti);
            proteiiniTeksti.setText(proteiiniProsentit + " %");

            float saatuTieto2 = Float.parseFloat(tiedot2_2);
            TextView lisatiedot = findViewById(R.id.lisatiedot);

            if (saatuTieto2 != 0.0f) {
                //Lisätiedot, proteiini, hiilarit, rasva
                //TextView lisatiedot = findViewById(R.id.lisatiedot);
                lisatiedot.setGravity(Gravity.CENTER);
                lisatiedot.setText("Oma tavoite, \n" + tiedot2_1 + ": " + tiedot2_2 + " g/vrk");
            } else {
                lisatiedot.setText("");
            }
        }
        /**
         * Asetetaan nimi, mikäli nimi on tallennettu asetuksissa.
         * Varmistetaan että nimessä on aina iso alkukirjain laatikossa näytettäessä,
         * vaikka käyttäjä kirjoittaisi nimensä pienellä.
         */
        if (!nimi.equals("")) {
            TextView nimitextview = findViewById(R.id.nimiteksti);
            String isoalkukirjain = nimi.substring(0, 1).toUpperCase() + nimi.substring(1);
            nimitextview.setText("Hei, " + isoalkukirjain + "!");
        }
    }

    /**
     * Alapalkin toiminnallisuus, aloittaa valitun aktiviteetin.
     * Tutoriaali alapalkin luomiseen: https://www.youtube.com/watch?v=fODp1hZxfng
     */
    private BottomNavigationView.OnNavigationItemSelectedListener alaPalkkiMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.koti:
                            startActivity(new Intent(MainActivity.this, MainActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Koti painettu");
                            finish();
                            break;
                        case R.id.suunnittele:
                            startActivity(new Intent(MainActivity.this, AteriatActivity.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Suunnittele painettu");
                            finish();
                            break;
                        case R.id.profiili:
                            startActivity(new Intent(MainActivity.this, Profiili.class));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            Log.d("Menu", "Profiili painettu");
                            finish();
                            break;
                    }
                    return false;
                }
            };


}