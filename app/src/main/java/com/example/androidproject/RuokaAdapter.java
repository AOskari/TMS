package com.example.androidproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Luodaan erillinen Adapteri, jonka avulla saadaan listattua elintarvikkeiden tiedot
 * ja muutama tarvittava nappi.
 */

public class RuokaAdapter extends BaseAdapter {

    Context context;
    List<Elintarvike> lista;

    public RuokaAdapter(Context context, List<Elintarvike> lista) {
        this.context = context;
        this.lista = lista;
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

        /*

        Laitetaan ruokatiedot tänne vastaavalla tavalla:
        TextView name = convertView.findViewById();
        TextView name = convertView.findViewById();
         */

        nimi.setText(lista.get(position).haeNimi());
        tiedot.setText(lista.get(position).toString());

        return convertView;
    }
}
