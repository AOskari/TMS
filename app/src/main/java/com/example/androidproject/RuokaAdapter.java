package com.example.androidproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.androidproject.aktiviteetit.HaeActivity;
import com.google.gson.Gson;
import java.util.List;


/**
 * Luodaan erillinen Adapteri, jonka avulla saadaan listattua elintarvikkeiden tiedot
 * ja kaksi nappia tietojen selaamiseen ja elintarvikkeen lisäämiseen ateriaan.
 */

public class RuokaAdapter extends BaseAdapter {

    Context context;
    List<Elintarvike> lista;

    Gson gson = new Gson();
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Ateria ateria;
    String ateriaJson;

    HaeActivity activity;

    public RuokaAdapter(Context context, List<Elintarvike> lista, SharedPreferences pref, HaeActivity activity) {
        this.context = context;
        this.lista = lista;
        this.pref = pref;
        editor = pref.edit();

        this.activity = activity;
        ateriaJson = pref.getString("ateria", "");


        // Jos SharedPreferences ei sisällä ateria-nimikkeellä tallennettua merkkijonoa,
        if (ateriaJson == "") {

            // Luodaan uusi Ateria-olio, johon tullaan tallentamaan kaikki valitut Elintarvike-oliot.
            ateria = new Ateria("Luonnos ateria");
            ateriaJson = gson.toJson(ateria);
            editor.putString("ateria", ateriaJson);
            editor.commit();
            Log.d("ateria", ateria.toString());
        } else {
            // Jos on tallennettu, muunnetaan Json-merkkijono Ateria-olioksi.
            ateria = gson.fromJson(ateriaJson, Ateria.class);
            Log.d("ateria", ateria.toString());
        }
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.rowdesign, parent, false);

        TextView nimi = convertView.findViewById(R.id.nimi);
        TextView tiedot = convertView.findViewById(R.id.info);
        Button lisaa = convertView.findViewById(R.id.lisaa);
        Button plus = convertView.findViewById(R.id.plus);
        Button miinus = convertView.findViewById(R.id.miinus);
        Button tiedotNappi = convertView.findViewById(R.id.tiedot);
        EditText editText = convertView.findViewById(R.id.maaraInput);

        // EditTextin inputTypeksi on asetettu numberPassword; poistetaan asteriskit seuraavalla komennolla
        editText.setTransformationMethod(null);

        nimi.setText(lista.get(position).haeNimi());
        tiedot.setText(lista.get(position).toString());

        lisaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Reagoidaan klikkaukseen vain, jos tekstikenttä sisältää merkkejä.
                if (editText.getText().toString().length() > 0) {
                    Double maara = Double.parseDouble(editText.getText().toString());
                    List<Double> ravinto = lista.get(position).haeRavintoarvot();

                    // Luodaan uusi Elintarvike-olio, joka sisältää valitun grammamäärän.
                    Elintarvike uusiTarvike = new Elintarvike(lista.get(position).haeNimi(), ravinto.get(0), ravinto.get(1),
                            ravinto.get(2), ravinto.get(3), ravinto.get(4), ravinto.get(5), ravinto.get(6),
                            ravinto.get(7), ravinto.get(8), maara);

                    ateria.lisaaAine(uusiTarvike);

                    ateriaJson = gson.toJson(ateria);
                    editor.putString("ateria", ateriaJson);
                    editor.commit();
                    Log.d("Päivitetty", "lisätty " + lista.get(position).haeNimi() + " ateriaan.");

                    // Lopuksi tyhjennetään tekstikenttä ja piilotetaan näppäimistö.
                    editText.setText("0");
                    activity.suljeNappaimisto();
                }
            }
        });

        // Asetetaaan onClick-metodit plus ja miinus napeille.
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().length() == 0) {
                    editText.setText("1");
                } else {
                    int maara = Integer.parseInt(editText.getText().toString());
                    maara++;
                    editText.setText(String.valueOf(maara));
                }
            }
        });

        miinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maara = Integer.parseInt(editText.getText().toString());
                  if (maara > 0) {
                      maara--;
                      editText.setText(String.valueOf(maara));
                  }
            }
        });

   // Ei toimi vielä
        /*
        tiedotNappi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(activity, TiedotActivity.class);
                nextActivity.putExtra("TARVIKE", (Parcelable) lista.get(position));
                activity.startActivity(nextActivity);
            }
        });
        */
        return convertView;
    }
}
