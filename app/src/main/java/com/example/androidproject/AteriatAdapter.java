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
import com.example.androidproject.aktiviteetit.AteriaInfoActivity;
import com.example.androidproject.aktiviteetit.AteriatActivity;
import com.example.androidproject.aktiviteetit.TiedotActivity;
import com.google.gson.Gson;

/**
 * Luodaan kustomoitu adapteri, jonka avulla luodaan kustomoidut ListView rivit.
 */
public class AteriatAdapter extends BaseAdapter {

    private Context context;
    private AteriaLista aterialista;
    private String listaJson;
    private SharedPreferences pref;
    private SharedPreferences.Editor edit;
    private Gson gson = new Gson();

    private int paiva;
    private int kuukausi;
    private int vuosi;

    /**
     * Adapterin konstruktori.
     * @param context viittaus nykyiseen Context-olioon.
     * @param pref valittu SharedPreferences.
     * @param paiva valittu päivä.
     * @param kuukausi valittu kuukausi.
     * @param vuosi valittu vuosi.
     */
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
     * @param parent widget, johon convertView liitetään lopuksi. Tässä tapauksessa ListView.
     * @return palauttaa muokatun convertViewin.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.ateriatiedot_layout, parent, false);

        TextView aterianNimi = convertView.findViewById(R.id.aterianNimi);
        TextView aika = convertView.findViewById(R.id.kellonaika);

        Ateria ateria = aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position);

        ImageButton poista = convertView.findViewById(R.id.poista);
        ImageButton muokkaa = convertView.findViewById(R.id.muokkaa);
        ImageButton syoty = convertView.findViewById(R.id.syoty);
        ImageButton kopioi = convertView.findViewById(R.id.ateriat_kopioi);
        ImageButton tiedot = convertView.findViewById(R.id.ateriat_info);

        /**
         * Asetetaan onClick-kuuntelija, joka painatessa avaa popup-ikkunan joka pyytää
         * käyttäjältä varmistusta haluaako hän poistaa valitun aterian.
         */
        poista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Luodaan varmistusikkuna, joka kysyy käyttäjältä haluaako poistaa aterian.
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Poista " + ateria.haeNimi() + "?");

                builder.setPositiveButton("Poista",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                aterialista.poistaAteria(ateria.haeId());
                                tallennaLista();
                                ((AteriatActivity)context).naytaAteriat();
                                Log.d("aterialista", "" + aterialista.tulostaAteriat());
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
         *  muokkaa-nappia painatessa asetetaan pysyväismuistiin valittu ateria, jolloin
         *  AteriaActivityä avatessa näkymään ilmestyy valittu ateria.
         *  Luodaan varmistusikkuna, joka kysyy käyttäjältä haluaako hän muokata valittua ateriaa.
         */
        muokkaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Muokkaa " + ateria.haeNimi() + "?");

                builder.setPositiveButton("Muokkaa",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String ateriaJson = gson.toJson(ateria);
                                edit.putString("ateria", ateriaJson);
                                edit.putBoolean("muokkaus", true);
                                edit.commit();
                                context.startActivity(new Intent(context, AteriaActivity.class));
                                ((AteriatActivity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
         * Sama ominaisuus kuin muokkaa-napissa, mutta ilman muokkaustilaa. Mahdollistaa aterian kopioinnin
         * ja uuden aterian luomisen.
         * Luodaan varmistusikkuna, jossa kysytään käyttäjältä haluaako hän kopioida valitun aterian.
         */
        kopioi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Kopioi " + ateria.haeNimi() + "?");

                builder.setPositiveButton("Kopioi",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String ateriaJson = gson.toJson(ateria);
                                edit.putString("ateria", ateriaJson);
                                edit.commit();
                                context.startActivity(new Intent(context, AteriaActivity.class));
                                ((AteriatActivity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
         * syoty-nappia painatessa luodaan popup-ikkuna, joka kysyy käyttäjältä varmistusta
         * haluaako hän asettaa aterian syödyksi. Tällöin Ateria siirtyy AteriaLista-luokan
         * syotyAteriat-listaan.
         */
        syoty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setMessage("Aseta syödyksi " + ateria.haeNimi() + "?");
                builder.setPositiveButton("Syöty",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                aterialista.asetaSyodyksi(ateria);
                                tallennaLista();
                                ((AteriatActivity)context).naytaAteriat();
                                ((AteriatActivity)context).asetaRavintoarvot();
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

        tiedot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(v.getContext(), AteriaInfoActivity.class);
                String ateriaJson = gson.toJson(ateria);
                nextActivity.putExtra("TIEDOT_ATERIA", ateriaJson);
                v.getContext().startActivity(nextActivity);
            }
        });

        aterianNimi.setText(aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position).haeNimi());
        aika.setText(aterialista.haePaivamaaralla(paiva, kuukausi, vuosi).get(position).aikaString());

        return convertView;
    }



    // =================================================================== //
    // ========================= Private-metodit ========================= //
    // =================================================================== //

    /**
     * Päivittää AteriaListan pysyväismuistiin.
     */
    private void tallennaLista() {
        listaJson = gson.toJson(aterialista);
        aterialista = gson.fromJson(listaJson, AteriaLista.class);
        edit.putString("aterialista", listaJson);
        edit.commit();
    }
}
