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
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.androidproject.aktiviteetit.AteriaActivity;
import com.example.androidproject.aktiviteetit.AteriatActivity;
import com.google.gson.Gson;

/**
 * Luodaan kustomoitu adapteri, jonka avulla luodaan kustomoidut ListView rivit.
 */
public class AteriatAdapter extends BaseAdapter {

    Context context;
    AteriaLista aterialista;
    String listaJson;

    SharedPreferences pref;
    SharedPreferences.Editor edit;
    Gson gson = new Gson();

    private int paiva;
    private int kuukausi;
    private int vuosi;

    public AteriatAdapter(Context context, SharedPreferences pref, int paiva, int kuukausi, int vuosi) {
        this.context = context;
        this.pref = pref;
        edit = pref.edit();

        this.paiva = paiva;
        this.kuukausi = kuukausi;
        this.vuosi = vuosi;

        listaJson = pref.getString("aterialista", "");
        aterialista = gson.fromJson(listaJson, AteriaLista.class);
    }

    @Override
    public int getCount() {
        return aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).size();
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
     * @param parent widget, johon convertView liitetään lopuksi.
     * @return palauttaa muokatun convertViewin.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.ateriatiedot_layout, parent, false);

        TextView aterianNimi = convertView.findViewById(R.id.aterianNimi);
        TextView aika = convertView.findViewById(R.id.kellonaika);

        ImageButton poista = convertView.findViewById(R.id.poista);
        ImageButton muokkaa = convertView.findViewById(R.id.muokkaa);
        ImageButton syoty = convertView.findViewById(R.id.syoty);

        poista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Luodaan varmistusikkuna, joka kysyy käyttäjältä haluaako poistaa aterian.
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Poista " + aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position).haeNimi() + "?");

                builder.setPositiveButton("Poista",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Poistetaan valittu ateria, ja tallennetaan muutokset SharedPreferencesiin.
                                aterialista.poistaAteria(aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position).haeId());
                                tallennaLista();

                                Log.d("aterialista", "" + aterialista.tulostaAteriat());

                                // Lopuksi päivitetään AteriatActivityn ListView
                                ((AteriatActivity)context).naytaAteriat();
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
         *  Lisätään muokkaa-napille mahdollisuus muokata ateriaa.
         */
        muokkaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ateriaJson = gson.toJson(aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position));
                edit.putString("ateria", ateriaJson);
                edit.putBoolean("muokkaus", true);
                edit.commit();
                context.startActivity(new Intent(context, AteriaActivity.class));
            }
        });

        syoty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Aseta syödyksi " + aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position).haeNimi() + "?");
                builder.setPositiveButton("Syöty",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                aterialista.asetaSyodyksi(aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position));
                                tallennaLista();
                                ((AteriatActivity)context).naytaAteriat();
                                Log.d("Asetettu syödyksi", aterialista.haeSyodytRavintoarvot(paiva, kuukausi, vuosi) + "");
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

        aterianNimi.setText(aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position).haeNimi());
        aika.setText(aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position).aikaString());

        return convertView;
    }

    private void tallennaLista() {
        listaJson = gson.toJson(aterialista);
        aterialista = gson.fromJson(listaJson, AteriaLista.class);
        edit.putString("aterialista", listaJson);
        edit.commit();
    }
}
