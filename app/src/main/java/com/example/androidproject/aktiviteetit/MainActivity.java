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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidproject.Ateria;
import com.example.androidproject.AteriaLista;
import com.example.androidproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

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

    Drawable drawable3;

    /**
     * Ylä palkkiin namin luominen.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Yläpalkin napin toiminnallisuus
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            Intent activity2Intent = new Intent(getApplicationContext(), TietoaMeista.class);
            startActivity(activity2Intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kalenteri = Calendar.getInstance();



        /**
         * Ympyrä progressbar
         * Lähde: https://stackoverflow.com/questions/12776587/android-circular-determinate-progressbar
         */
        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.circle2);
        Drawable drawable2 = res.getDrawable(R.drawable.circle2);

        /**
         * Vaihtaa ympyrän punaiseksi tavoitteen epäonnistuessa.
         */
        drawable3 = res.getDrawable(R.drawable.circle3);
        mProgress = findViewById(R.id.circularProgressbar);

        //mProgress = tavoite 1, mProgress2 = tavoite 2.
        mProgress2 = findViewById(R.id.circularProgressbar2);

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
         * Jos singletonia ei löydy, tallennetaan se pysyväismuistiin.
         */
        pref = getApplicationContext().getSharedPreferences("mainPref", 0);
        editor = pref.edit();
        aterialistaJson = pref.getString("aterialista", "");

        if (aterialistaJson.equals("")) {
            aterialistaJson = gson.toJson(haeLista());
            editor.putString("aterialista", aterialistaJson);
            editor.apply(); //vaihoin commitista applyhyn
            Log.d("aterialista", aterialistaJson);
        }

        aterialista = gson.fromJson(aterialistaJson, AteriaLista.class);

        /** Asetetaan alapalkille kuuntelija, joka vaihtaa aktiviteettia nappien perusteella.
         *
         */
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

        int kalorit = (int) Math.round(aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi).get(0));
        double syodytProtskut = aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi).get(1);
        double syodytHiilarit = aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi).get(2);
        //double syodytRasvat = aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi).get(3);

        /**
         * Haetaan pysyväismuistista käyttäjän tiedot ja tavoitteet.
         */
        SharedPreferences sharedPreferences = getSharedPreferences("Tiedot", Context.MODE_PRIVATE);
        String nimi = sharedPreferences.getString("Käyttäjä", "");
        String tiedot1 = sharedPreferences.getString("Tavoite1", "");
        String tiedot2 = sharedPreferences.getString("Tavoite2", "");


        /**
         * Asetetaan tiedot, mikäli tietoja on tallennettu asetuksissa.
         */

        if (!tiedot1.equals("")) {
            String[] tiedot1_lista = tiedot1.split(" ");
            String tiedot1_kalorit = tiedot1_lista[1];
            String tavoite1_nimi = tiedot1_lista[0];
            float saatuTieto1 = Float.parseFloat(tiedot1_kalorit);
            int kaloritYht = (int) Math.round(saatuTieto1);
            TextView prossat = findViewById(R.id.prossat);
            /**
             * Otsikot progressbarien yläpuolelle selkeydeksi.
             */
            TextView tavoite1Nimi = findViewById(R.id.tavoite1nimi);
            tavoite1Nimi.setText(tavoite1_nimi);




            /**
             *     Muutetaan intit doubleiksi laskua varten.
             */
            if (saatuTieto1 != 0.0f) {
                double k = kalorit;
                double kY = kaloritYht;
                double prosentit = k / kY * 100;
                int prosentitI = (int) Math.round(prosentit);
                /**
                 * Muutetaan vastaus Stringiksi ja pyöristetään se.
                 */
                String prosentitS = Integer.toString(prosentitI);

                /**
                 * Jäljellä olevat kalorit.
                 */
                int jaljella = kaloritYht - kalorit;

                /**
                 * Tekstin tasaus keskelle.
                 */
                kaloriTavoite.setGravity(Gravity.CENTER);
                kaloriTavoite.setText("Tavoite 1: " + tiedot1_kalorit + " kcal" + "\n Jäljellä: " + jaljella + " kcal");

                /**
                 * Ilmoittaa jos ylittää päivän tavoitteen.
                 */
                if (prosentitI > 100) {
                    //Kertoo että ylitit päivän kaloritavoitteen.
                    TextView ylitys = findViewById(R.id.ylitys);
                    ylitys.setText("Päivän tavoite 1 ylitetty.");

                    /**
                     * Vaihtaa ympyrän punaiseksi tavoitteen epäonnistuessa.
                     */
                    mProgress.setProgressDrawable(drawable3);
                }


                /**
                 * Näytetään prosentit.
                 */
                prossat.setText(prosentitS + " %");

                /**
                 * Prosenttien asettaminen palkkiin.
                 */
                mProgress.setProgress(prosentitI);   // Main Progress

            } else {
                kaloriTavoite.setText("");
                prossat.setText("");
            }

        }

        //Toisen itseasetetun tavoitteen näyttäminen.
        if (!tiedot2.equals("")) {
            /**
             * Olio joka pyöristää numerot kahteen desimaaliin ja muuntaa numeron Stringiksi.
             */
            DecimalFormat df = new DecimalFormat("#.##");

            String[] tiedot2_lista = tiedot2.split(" ");
            String tiedot2_2 = tiedot2_lista[1];
            String tiedot2_1 = tiedot2_lista[0];
            String tavoite2_nimi = tiedot2_lista[0];
            /**
             * Otsikot progressbarien yläpuolelle selkeydeksi.
             */
            TextView tavoite2Nimi = findViewById(R.id.tavoite2nimi);
            tavoite2Nimi.setText(tavoite2_nimi);

            String proteiinit = (tiedot2_2.substring(0, tiedot2_2.length() - 2));

            //Ei ole pelkät proteiinit, vaan paremminkin tavoite2.
            double proteiinitDouble = Double.parseDouble(proteiinit);
            //Pikaliimateippiviritelmä.
            double tavoite2Double = proteiinitDouble;

            /**
             * Prosenttien ja tekstien asettaminen valitun tavoitteen mukaan. (tavoite 2)
             */
            if (tiedot2_1.equals("Proteiini") || tiedot2_1.equals("proteiini")) {

                /**
                 * Käyttäjän painon hakeminen.
                 */
                SharedPreferences tiedot = getSharedPreferences("Tiedot", Activity.MODE_PRIVATE);
                float paino = tiedot.getFloat("Paino", 0.0f);

                int proteiiniProsentit = (int) Math.round(syodytProtskut / (proteiinitDouble * paino) * 100);

                mProgress2.setProgress(proteiiniProsentit);   // Main Progress
                TextView proteiiniTeksti = findViewById(R.id.protskuTeksti);
                proteiiniTeksti.setText(proteiiniProsentit + " %");

                float saatuTieto2 = Float.parseFloat(tiedot2_2);
                TextView lisatiedot = findViewById(R.id.lisatiedot);

                double proteiiniPainonKanssa = proteiinitDouble * paino;
                //Esim. tavoite oli 5g per kg. Nyt näemme kokonaistavoitteen.
                //esim. paino 80kg, 5g * 80kg.

                double jaljella = proteiiniPainonKanssa - syodytProtskut;
                if (jaljella <= 0) {
                    jaljella = 0;
                }

                if (saatuTieto2 != 0.0f) {
                    /**
                     *   Lisätiedot, proteiini, hiilarit, rasva
                     */
                    lisatiedot.setGravity(Gravity.CENTER);
                    lisatiedot.setText("Tavoite 2: " + proteiinitDouble + "g/kg/vrk \n Jäljellä (yht.): " + df.format(jaljella) + " g \n Tämän hetkinen g/kg: " + df.format(syodytProtskut / paino));
                } else {
                    lisatiedot.setText("");
                }

                if (proteiiniProsentit > 100) {
                    //Kertoo että ylitit päivän kaloritavoitteen.
                    TextView ylitys2 = findViewById(R.id.ylitys2);
                    ylitys2.setText("Päivän tavoite 2 ylitetty.");
                    mProgress2.setProgressDrawable(drawable3);

                }
            } else if (tiedot2_1.equals("Hiilihydraatti")) {
                //tee jotain
                //  double syodytHiilarit = aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi).get(2);

                int hiilariProsentit = (int) Math.round(syodytHiilarit / tavoite2Double * 100);

                mProgress2.setProgress(hiilariProsentit);   // Main Progress
                TextView hiilariteksti = findViewById(R.id.protskuTeksti);
                hiilariteksti.setText(hiilariProsentit + " %");


                float saatuTieto2 = Float.parseFloat(tiedot2_2);
                TextView lisatiedot = findViewById(R.id.lisatiedot);

                double jaljella = proteiinitDouble - syodytHiilarit;
                if (jaljella <= 0) {
                    jaljella = 0;
                }

                if (saatuTieto2 != 0.0f) {
                    /**
                     *   Lisätiedot, proteiini, hiilarit, rasva
                     */
                    lisatiedot.setGravity(Gravity.CENTER);
                    lisatiedot.setText("Tavoite 2: " + tavoite2Double + "g/vrk \n Jäljellä: " + df.format(jaljella) + " g/vrk");

                }

                /**
                 * Ilmoittaa jos ylittää päivän tavoitteen.
                 */
                if (hiilariProsentit > 100) {
                    //Kertoo että ylitit päivän kaloritavoitteen.
                    TextView ylitys2 = findViewById(R.id.ylitys2);
                    ylitys2.setText("Päivän tavoite 2 ylitetty.");
                }
            }
            /**
             * Asetetaan nimi, mikäli nimi on tallennettu asetuksissa.
             * Varmistetaan että nimessä on aina iso alkukirjain laatikossa näytettäessä,
             * vaikka käyttäjä kirjoittaisi nimensä pienellä.
             */
            TextView nimitextview = findViewById(R.id.nimiteksti);

            if (!nimi.equals("")) {

                String isoalkukirjain = nimi.substring(0, 1).toUpperCase() + nimi.substring(1);
                nimitextview.setText("Hei, " + isoalkukirjain + "!");
            } else {
                nimitextview.setText("Hei!");
            }
        }
        ArrayList<String> listOfRandomQuotes;

        listOfRandomQuotes = new ArrayList<String>();
        listOfRandomQuotes.add("The last three or four reps is what makes the muscle grow. This area of pain divides a champion from someone who is not a champion. -  Arnold Schwarzenegger");
        listOfRandomQuotes.add("You must be the change you wish to see in the world.");
        listOfRandomQuotes.add("I have decided to stick with love. Hate is too great a burden to bear.");
        listOfRandomQuotes.add("By failing to prepare, you are preparing to fail.");
        listOfRandomQuotes.add("Success usually comes to those who are too busy to be looking for it.");
        listOfRandomQuotes.add("If you think lifting is dangerous, try being weak. Being weak is dangerous. - Bret Contreras");
        listOfRandomQuotes.add("The clock is ticking. Are you becoming the person you want to be? - Greg Plitt");
        listOfRandomQuotes.add("Whether you think you can, or you think you can’t, you’re right. - Henry Ford");
        listOfRandomQuotes.add("You must expect great things of yourself before you can do them. - Michael Jordan");
        listOfRandomQuotes.add("‘All our dreams can come true if we have the courage to pursue them. - Walt Disney");

        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(listOfRandomQuotes.size());
        String string = listOfRandomQuotes.get(index);

        /**
         * Inspiroiva vaihtuva lainaus, joka motivoi jatkamaan omien tavoitteiden seuraamista.
         * https://www.lifefitness.com.au/20-fitness-motivation-quotes/
         * https://stackoverflow.com/questions/5034370/retrieving-a-random-item-from-arraylist
         */
        TextView quoteTeksti = findViewById(R.id.quoteTeksti);
        quoteTeksti.setGravity(Gravity.CENTER);
        quoteTeksti.setText(string);

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
