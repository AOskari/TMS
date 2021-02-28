package com.example.androidproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject.aktiviteetit.AteriaActivity;
import com.example.androidproject.aktiviteetit.TiedotActivity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Adapteri, jonka avulla luodaan kustomoidut ListView rivit AteriaActivityyn.
 */
public class AteriaAdapter extends BaseAdapter {

    Context context;
    Ateria ateria;
    String ateriaJson;
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    Gson gson = new Gson();

    /**
     * Adapterin konstruktori.
     * @param context viittaus nykyiseen Context-olioon.
     * @param pref valittu SharedPreferences.
     */
    public AteriaAdapter(Context context, SharedPreferences pref) {
        this.context = context;
        this.pref = pref;
        edit = pref.edit();
        ateriaJson = pref.getString("ateria", "");
        this.ateria = gson.fromJson(ateriaJson, Ateria.class);
    }

    @Override
    public int getCount() {
        return ateria.haeTarvikkeet().size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.tarviketiedot_layout, parent, false);

        ImageButton tiedot = convertView.findViewById(R.id.tarvike_tiedot);
        ImageButton poista = convertView.findViewById(R.id.tiedot_poista);
        TextView nimi = convertView.findViewById(R.id.tarvike_nimi);
        String tarvikkeenNimi = ateria.haeTarvikkeet().get(position).haeNimi();

        /**
         * Asetetetaan TextViewiin nimi ja poista sekä tiedot napille onClick-kuuntelija.
         * Lopuksi palautetaan muokattu convertView.
         */
        nimi.setText(tarvikkeenNimi);
        poista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * Tuodaan esille popup-ikkuna, joka kysyy haluaako käyttäjä poistaa Elintarvikkeen.
                 * Jos Poista on valittu, poistetaan Elintarvike listalta ja päivitetään lista.
                 */
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Poista " + tarvikkeenNimi + "?");

                builder.setPositiveButton("Poista",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ateria.poista(position);
                                ateriaJson = gson.toJson(ateria);
                                edit.putString("ateria", ateriaJson);
                                edit.commit();
                                AteriaAdapter.this.notifyDataSetChanged();
                                ((AteriaActivity)context).paivitaLista();
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
        });

        /**
         * Käynnistää TiedotActivityn, joka näyttää valitun Elintarvikkeen tiedot näytöllä.
         */
        tiedot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(v.getContext(), TiedotActivity.class);
                String elintarvikeJson = gson.toJson(ateria.haeTarvikkeet().get(position));
                nextActivity.putExtra("TARVIKE", elintarvikeJson);
                v.getContext().startActivity(nextActivity);
            }
        });

        return convertView;
    }


}
