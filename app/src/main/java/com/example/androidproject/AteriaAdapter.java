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

public class AteriaAdapter extends BaseAdapter {

    Context context;
    Ateria ateria;
    String ateriaJson;

    SharedPreferences pref;
    SharedPreferences.Editor edit;

    Gson gson = new Gson();

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String tarvikkeenNimi = ateria.haeTarvikkeet().get(position).haeNimi();
        convertView = LayoutInflater.from(context).inflate(R.layout.tarviketiedot_layout, parent, false);

        TextView nimi = convertView.findViewById(R.id.tarvike_nimi);
        nimi.setText(tarvikkeenNimi);

        ImageButton tiedot = convertView.findViewById(R.id.tarvike_tiedot);
        ImageButton poista = convertView.findViewById(R.id.tiedot_poista);

        poista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Tuodaan esille varmistusikkuna esille, ja kysytään käyttäjältä poistetaanko valittu elintarvike.
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Poista " + tarvikkeenNimi + "?");

                // Jos käyttäjä valitsee poista,
                builder.setPositiveButton("Poista",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /* Poistetaan valittu Elintarvike ja päivitetään SharedPreferences
                                    sekä ravintoainetiedot päivitetyllä Ateria-oliolla. */
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
