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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.Ateria;
import com.example.androidproject.AteriaLista;
import com.example.androidproject.R;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static com.example.androidproject.AteriaLista.haeLista;
import static com.example.androidproject.Trendi.getInstance;

/**
 * Pääaktiviteetti, kaikki koodi koskee sovelluksen koti/päänäkymää.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Luodaan muuttujia, joita tullaan tarvitsemaan.
     */
    private BottomNavigationView bottomNavigationView;

    private Calendar kalenteri;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private SharedPreferences trendit;
    private SharedPreferences.Editor tallListat;
    private Gson gson = new Gson();
    private AteriaLista aterialista;
    private String aterialistaJson;
    private String trendiJson;

    private int paiva;
    private int kuukausi;
    private int vuosi;

    ProgressBar mProgress;
    ProgressBar mProgress2;

    Drawable drawable3;

    /**
     * Ylä palkkiin napin luominen.
     *
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
     *
     * @param item Yläpalkin info-nappi
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
         * Etusivulle päivämäärän asettaminen ja Streak counter joka näyttää monta päivää putkeen on käyttänyt sovellusta.
         * Päivämäärä: https://stackoverflow.com/questions/12934661/android-get-current-date-and-show-it-in-textview
         * Streakcounter: https://stackoverflow.com/questions/45254071/android-checking-if-app-has-been-opened-multiple-days-in-a-row/4525415         *
         */
        SharedPreferences sharedPreferences = getSharedPreferences("prefavain", Context.MODE_PRIVATE);
        Calendar c = Calendar.getInstance();

        int thisDay = c.get(Calendar.DAY_OF_YEAR); // GET THE CURRENT DAY OF THE YEAR

        int lastDay = sharedPreferences.getInt("pvmstreak", 0); //If we don't have a saved value, use 0.

        int counterOfConsecutiveDays = sharedPreferences.getInt("counteri", 0); //If we don't have a saved value, use 0.

        SharedPreferences.Editor edit = sharedPreferences.edit();

        if (lastDay == thisDay - 1) {
            // CONSECUTIVE DAYS
            counterOfConsecutiveDays = counterOfConsecutiveDays + 1;

            edit.putInt("pvmstreak", thisDay);

            edit.putInt("counteri", counterOfConsecutiveDays).commit();
        } else {

            edit.putInt("pvmstreak", thisDay);

            edit.putInt("counteri", 1).commit();
        }

        int striikki = counterOfConsecutiveDays;

        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = sdf.format(date);

        TextView pvmTeksti = findViewById(R.id.pvmTeksti);
        pvmTeksti.setText(dateString + "\n\n🏆" + striikki);

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

        /**
         * Asetetaan ympyröiden tiedot.
         */
        mProgress2 = findViewById(R.id.circularProgressbar2); //mProgress = tavoite 1, mProgress2 = tavoite 2.

        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(100); // Secondary Progress
        mProgress.setMax(100); // Maximum Progress
        mProgress.setProgressDrawable(drawable);

        mProgress2.setProgress(0);   // Main Progress
        mProgress2.setSecondaryProgress(100); // Secondary Progress
        mProgress2.setMax(100); // Maximum Progress
        mProgress2.setProgressDrawable(drawable2);

        trendit = getSharedPreferences("Trendit", Activity.MODE_PRIVATE);
        tallListat = trendit.edit();

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
            editor.apply(); //vaihdoin commitista applyhyn
            Log.d("aterialista", aterialistaJson);
        }

        /**
         * Haetaan Trendi-singleton, jos sitä ei löydy, tallennetaan se SharedPreferencesiin.
         */
        trendiJson = trendit.getString("Trendi", "");
        if (trendiJson.equals("")) {
            trendiJson = gson.toJson(getInstance());
            tallListat.putString("Trendi", trendiJson);
            tallListat.commit();
        }

        aterialista = gson.fromJson(aterialistaJson, AteriaLista.class);

        /** Asetetaan alapalkille kuuntelija, joka vaihtaa aktiviteettia nappien perusteella.
         *
         */
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(alaPalkkiMethod);
        bottomNavigationView.getMenu().findItem(R.id.koti).setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * Haetaan tietoja etusivun laskuja tavoitteita varten.
         */
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

        /**
         * Tavoitteen 1 asettaminen. Tavoite 1 toimii tavoitteen 2 koodien pohjalta, koska tavoite 1 tuki alkuun vain kaloreita, nyt kaikkia.
         */

        if (!tiedot1.equals("")) {

            /**
             * Olio joka pyöristää numerot kahteen desimaaliin ja muuntaa numeron Stringiksi.
             */
            DecimalFormat df = new DecimalFormat("#.##");

            String[] tiedot1_lista = tiedot1.split(" ");
            String tiedot1_2 = tiedot1_lista[1];
            String tiedot1_1 = tiedot1_lista[0];
            String tavoite1_nimi = tiedot1_lista[0];
            /**
             * Otsikot progressbarien yläpuolelle selkeydeksi.
             */
            TextView tavoite2Nimi = findViewById(R.id.tavoite1nimi);
            tavoite2Nimi.setText(tavoite1_nimi);

            String proteiinit = (tiedot1_2.substring(0, tiedot1_2.length() - 2));

            //Ei ole pelkät proteiinit, vaan paremminkin tavoite2.
            double proteiinitDouble = Double.parseDouble(proteiinit);
            //Pikaliimateippiviritelmä.
            double tavoite2Double = proteiinitDouble;

            /**
             * Prosenttien ja tekstien asettaminen valitun tavoitteen mukaan. (tavoite 2)
             */
            if (tiedot1_1.equals("Proteiini") || tiedot1_1.equals("proteiini")) {

                /**
                 * Käyttäjän painon hakeminen.
                 */
                SharedPreferences tiedot = getSharedPreferences("Tiedot", Activity.MODE_PRIVATE);
                float paino = tiedot.getFloat("Paino", 0.0f);

                int proteiiniProsentit = (int) Math.round(syodytProtskut / (proteiinitDouble * paino) * 100);

                mProgress.setProgress(proteiiniProsentit);   // Main Progress
                TextView proteiiniTeksti = findViewById(R.id.prossat);
                proteiiniTeksti.setText(proteiiniProsentit + " %");

                float saatuTieto2 = Float.parseFloat(tiedot1_2);
                TextView lisatiedot = findViewById(R.id.testitext);

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
                    lisatiedot.setText("Tavoite 1: " + proteiinitDouble + "g/kg/vrk \n Jäljellä (yht.): " + df.format(jaljella) + " g \n Tämän hetkinen g/kg: " + df.format(syodytProtskut / paino));
                } else {
                    lisatiedot.setText("");
                }

                if (proteiiniProsentit > 100) {
                    //Kertoo että ylitit päivän kaloritavoitteen.
                    TextView ylitys2 = findViewById(R.id.ylitys);
                    ylitys2.setText("Päivän tavoite 1 ylitetty.");
                    mProgress.setProgressDrawable(drawable3);

                }
            } else if (tiedot1_1.equals("Hiilihydraatti")) {
                //tee jotain
                //  double syodytHiilarit = aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi).get(2);

                int hiilariProsentit = (int) Math.round(syodytHiilarit / tavoite2Double * 100);

                mProgress.setProgress(hiilariProsentit);   // Main Progress
                TextView hiilariteksti = findViewById(R.id.protskuTeksti);
                hiilariteksti.setText(hiilariProsentit + " %");


                float saatuTieto2 = Float.parseFloat(tiedot1_2);
                TextView lisatiedot = findViewById(R.id.testitext);

                double jaljella = proteiinitDouble - syodytHiilarit;
                if (jaljella <= 0) {
                    jaljella = 0;
                }

                if (saatuTieto2 != 0.0f) {
                    /**
                     *   Lisätiedot, proteiini, hiilarit, rasva
                     */
                    lisatiedot.setGravity(Gravity.CENTER);
                    lisatiedot.setText("Tavoite 1: " + tavoite2Double + "g/vrk \n Jäljellä: " + df.format(jaljella) + " g/vrk");

                }

                /**
                 * Ilmoittaa jos ylittää päivän tavoitteen.
                 */
                if (hiilariProsentit > 100) {
                    //Kertoo että ylitit päivän kaloritavoitteen.
                    TextView ylitys2 = findViewById(R.id.ylitys);
                    ylitys2.setText("Päivän tavoite 1 ylitetty.");
                }
            } else if (tiedot1_1.equals("Kalorit")) {
                //    String[] tiedot1_lista = tiedot1.split(" ");
                String tiedot1_kalorit = tiedot1_lista[1];
                //      String tavoite1_nimi = tiedot1_lista[0];
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

        }

        /**
         * Toisen itseasetetun tavoitteen näyttäminen.
         */
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
            } else if (tiedot2_1.equals("Kalorit")) {
                String[] tiedot1_lista = tiedot2.split(" ");
                String tiedot1_kalorit = tiedot2_lista[1];
                String tavoite1_nimi = tiedot2_lista[0];
                float saatuTieto1 = Float.parseFloat(tiedot1_kalorit);
                int kaloritYht = (int) Math.round(saatuTieto1);
                TextView prossat = findViewById(R.id.protskuTeksti);
                TextView lisatiedot = findViewById(R.id.lisatiedot);
                /**
                 * Otsikot progressbarien yläpuolelle selkeydeksi.
                 */
                TextView tavoite1Nimi = findViewById(R.id.tavoite2nimi);
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
                    lisatiedot.setGravity(Gravity.CENTER);
                    lisatiedot.setText("Tavoite 2: " + tiedot1_kalorit + " kcal" + "\n Jäljellä: " + jaljella + " kcal");

                    /**
                     * Ilmoittaa jos ylittää päivän tavoitteen.
                     */
                    if (prosentitI > 100) {
                        //Kertoo että ylitit päivän kaloritavoitteen.
                        TextView ylitys = findViewById(R.id.ylitys2);
                        ylitys.setText("Päivän tavoite 2 ylitetty.");

                        /**
                         * Vaihtaa ympyrän punaiseksi tavoitteen epäonnistuessa.
                         */
                        mProgress2.setProgressDrawable(drawable3);
                    }


                    /**
                     * Näytetään prosentit.
                     */
                    prossat.setText(prosentitS + " %");

                    /**
                     * Prosenttien asettaminen palkkiin.
                     */
                    mProgress2.setProgress(prosentitI);   // Main Progress

                } else {
                    lisatiedot.setText("");
                    prossat.setText("");
                }

            }
        }
        /**
         * Asetetaan nimi, mikäli nimi on tallennettu asetuksissa.
         * Varmistetaan että nimessä on aina iso alkukirjain laatikossa näytettäessä,
         * vaikka käyttäjä kirjoittaisi nimensä pienellä.
         */
        TextView nimitextview = findViewById(R.id.nimiteksti);

        if (!nimi.equals("")) {

            /**
             * Lisätään nimeen iso alkukirjain, jos käyttäjä itse ei ole pistänyt.
             */

            String isoalkukirjain = nimi.substring(0, 1).toUpperCase() + nimi.substring(1);
            nimitextview.setText("Hei, " + isoalkukirjain + "!");
        } else {
            nimitextview.setText("Hei!");
        }

        /**
         * Lista etusivun alalaidan sitaatteja varten.
         */
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


        /**
         * Näyttää käyttäjälle kehotteen täyttää omat tiedot, mikäli niitä ei ole ja napin mistä ne pääsee täyttämään asetuksiin.
         * https://stackoverflow.com/questions/6962030/how-to-check-if-sharedpreferences-file-exists-or-not
         */
        File tiedosto = new File(getApplicationContext().getApplicationInfo().dataDir + "/shared_prefs/Tiedot.xml");

        if ((tiedot1.equals("") && tiedot2.equals("")) || (tiedot1.equals(null)
                && tiedot2.equals(null)) || (!tiedosto.exists())) {
            TextView uusiKayttaja = findViewById(R.id.tvUusiKayttaja);
            uusiKayttaja.setText("Aloittaaksesi sovelluksen käytön käy syöttämässä tietosi asetuksissa.");

            /**
             * Muiden laatikoiden, tekstien jne piilotus
             *https://stackoverflow.com/questions/10403020/how-to-hide-elements-in-graphical-layout/17657597
             */
            mProgress2.setVisibility(View.GONE);
            mProgress.setVisibility(View.GONE);
            TextView prossat = findViewById(R.id.prossat);
            TextView protskuTeksti = findViewById(R.id.protskuTeksti);
            TextView hei = findViewById(R.id.nimiteksti);
            hei.setText("Tervetuloa!");
            prossat.setVisibility(View.GONE);
            protskuTeksti.setVisibility(View.GONE);
            quoteTeksti.setVisibility(View.GONE);


        } else {
            /**
             *     Sama logiikka mutta toistepäin. Halutaan näkyvän vain uusille/ilman tietoja oleville käyttäjille.
             */
            Button uK = findViewById(R.id.uusiKayttajaBtn);
            uK.setVisibility(View.GONE);
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


    /**
     * Etusivun asetuksiin napin onClick, nappi vie asetuksiin täyttämään tiedot.
     * Näkyy vain sovelluksen käyttäjälle, joka ei ole vielä täyttänyt tietojaan.
     *
     * @param view
     */
    public void onClickUusiKayttajaBtn(View view) {
        startActivity(new Intent(MainActivity.this, Asetukset.class));
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    /**
     * Jos käyttäjä klikkaa päivämäärää ja streakkia, laukaisee toastin.
     * @param view
     */

    public void onClickStreakki(View view) {

        Toast.makeText(this, "Näyttää kuinka monta päivää putkeen olet käyttänyt sovellusta.", Toast.LENGTH_SHORT).show();
    }
}
