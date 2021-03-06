package com.example.androidproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject.aktiviteetit.HakuActivity;
import com.example.androidproject.aktiviteetit.TiedotActivity;
import com.google.gson.Gson;
import java.util.List;


/**
 * Adapteri, jonka avulla saadaan listattua elintarvikkeiden tiedot
 * ja kaksi nappia tietojen selaamiseen sekä elintarvikkeen lisäämiseen ateriaan.
 */
public class HakuAdapter extends BaseAdapter {

    private Context context;
    private List<Elintarvike> lista;
    private Gson gson = new Gson();
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private Ateria ateria;
    private String ateriaJson;
    private HakuActivity activity;

    /**
     * Adapterin konstruktori.
     * @param context viittaus nykyiseen Context-olioon.
     * @param lista Lista API:sta haetuista Elintarvike-olioista.
     * @param pref valittu SharedPreferences, jossa sijaitsee muokkauksessa oleva ateria.
     * @param activity alkuperäinen aktiviteetti, tässä tapauksessa viittaus HakuActivityyn.
     */
    public HakuAdapter(Context context, List<Elintarvike> lista, SharedPreferences pref, HakuActivity activity) {
        this.context = context;
        this.lista = lista;
        this.pref = pref;
        editor = pref.edit();
        this.activity = activity;
        ateriaJson = pref.getString("ateria", "");

        ateria = gson.fromJson(ateriaJson, Ateria.class);
        ateriaJson = gson.toJson(ateria);
    }

    @Override
    public int getCount() {
        return lista.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * @param position osoittaa paikkaa listalla.
     * @param convertView alkuperäinen View jota tullaan muokkaamaan.
     * @param parent widget, johon convertView liitetään lopuksi. Tässä tapauksessa ListView.
     * @return palauttaa muokatun convertViewin.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.hakurivi_layout, parent, false);

        TextView nimi = convertView.findViewById(R.id.nimi);
        ImageButton lisaa = convertView.findViewById(R.id.lisaa);
        Button plus = convertView.findViewById(R.id.plus);
        Button miinus = convertView.findViewById(R.id.miinus);
        ImageButton tiedotNappi = convertView.findViewById(R.id.tiedot);
        EditText editText = convertView.findViewById(R.id.maaraInput);
        TextView ilmoitus = convertView.findViewById(R.id.haku_ilmoitus);

        /**
         * EditTextin inputTypeksi on asetettu numberPassword, tämän takia teksti näkyy tähtinä. Poistetaan tähdet.
         * Tämän jälkeen tyhjennetään tekstikenttä sitä klikattaessa.
         * Kun käyttäjä ei enää muokkaa tekstiä, lisätään "g" numeron perään.
         */
        editText.setTransformationMethod(null);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editText.setText("");
                return false;
            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String teksti = editText.getText().toString();
                if (!hasFocus && !teksti.contains("g")) {
                    if (teksti.equals("")) {
                        editText.setText("0g");
                    } else {
                        editText.setText(editText.getText() + "g");
                    }
                }
            }
        });

        nimi.setText(lista.get(position).haeNimi());

        /**
         * lisaa-napin toiminnan edellytyksenä on, että käyttäjä on asettanut halutun grammamäärän.
         * Kun haluttu summa on annettu, elintarvike lisätään olemassa olevaan ateria-olioon, jonka jälkeen
         * ateria tallennetaan uudestaan pysyväismuistiin.
         */
        lisaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!editText.getText().toString().equals("0g")) {
                    Double maara = Double.parseDouble(editText.getText().toString().replaceAll("g", ""));
                    List<Double> ravinto = lista.get(position).haeRavintoarvot();

                    Elintarvike uusiTarvike = new Elintarvike(lista.get(position).haeNimi(), ravinto.get(0), ravinto.get(1),
                            ravinto.get(2), ravinto.get(3), ravinto.get(4), ravinto.get(5), ravinto.get(6),
                            ravinto.get(7), ravinto.get(8), maara);

                    ateria.lisaaAine(uusiTarvike);
                    activity.naytaIlmoitus(lista.get(position).haeNimi(), false);

                    ateriaJson = gson.toJson(ateria);
                    editor.putString("ateria", ateriaJson);
                    editor.commit();
                    Log.d("Päivitetty", "lisätty " + lista.get(position).haeNimi() + " ateriaan.");

                    editText.setText("0g");
                    activity.suljeNappaimisto();
                } else if (editText.getText().toString().equals("0g") ||editText.getText().toString().equals("g")) {
                    activity.naytaIlmoitus(lista.get(position).haeNimi(), true);
                }
            }
        });

        /**
         * plus-nappi lisää valitun summan määrää yhdellä grammalla.
         */
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teksti = editText.getText().toString();
                if (teksti.equals("0g") || teksti.equals("g") ||teksti.equals("")) {
                    editText.setText("1g");
                } else {
                    int maara = Integer.parseInt(editText.getText().toString().replaceAll("g", ""));
                    maara++;
                    editText.setText(maara + "g");
                }
        }
        });

        /**
         * miinus-nappi vähentää valitun summan määrää yhdellä grammalla. Ei mene alle 0g.
         */
        miinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String teksti = editText.getText().toString();
                if (!teksti.equals("g") && !teksti.equals("")) {
                    int maara = Integer.parseInt(teksti.replaceAll("g", ""));
                    if (maara > 0) {
                        maara--;
                        editText.setText(maara + "g");
                    }
                }
            }
        });

        /**
         * Tuo esille popup-ikkunan, joka näyttää elintarvikkeen ravintotiedot.
         */
        tiedotNappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(v.getContext(), TiedotActivity.class);
                String ateriaJson = gson.toJson(lista.get(position));
                nextActivity.putExtra("TARVIKE", ateriaJson);
                v.getContext().startActivity(nextActivity);
            }
        });

        return convertView;
    }
}
