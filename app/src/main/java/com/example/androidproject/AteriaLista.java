package com.example.androidproject;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *  Singleton jota käytetään aterioiden listaamiseen ja SharedPreferencesiin tallentamiseen..
 */
public class AteriaLista {
    private List<Ateria> lista;
    private static final AteriaLista haeLista = new AteriaLista();
    private int id;

    public static AteriaLista haeLista() {
        return haeLista;
    }

    private AteriaLista() {
        lista = new ArrayList<>();
        id = 0;
    }

    public void lisaaAteria(Ateria ateria) {
        lista.add(ateria);
    }

    /**
     * Hakee AteriaListalta ateriat jotka sisältävät valitun päivämäärän, jonka jälkeen
     * järjestää listan kellonajan mukaan.
     *
     * @param paiva valittu päivä
     * @param kuukausi valittu kuukausi
     * @param vuosi valittu vuosi
     * @return palauttaa listan.
     */
    public List<Ateria> haePaivamaaralla(int paiva, int kuukausi, int vuosi) {
        //TODO: Luo toiminnallisuus, joka palauttaa kaikki ateriat haetulta päivämäärältä.

        List<Ateria> palautettavat = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            List<Integer> paivamaara = lista.get(i).haePaivamaara();
            if (paivamaara.contains(paiva) && paivamaara.contains(kuukausi) && paivamaara.contains(vuosi)) {
                palautettavat.add(lista.get(i));
            }

            // Ensin järjestetään ateriat tunnin perusteella,
            Collections.sort(palautettavat, new Comparator<Ateria>() {
                @Override
                public int compare(Ateria o1, Ateria o2) {
                    if (o1.haeAika().get(0) > o2.haeAika().get(0))
                        return 1;
                    if (o1.haeAika().get(0) < o2.haeAika().get(0))
                        return -1;
                    return 0;
                }
            });

            // jonka jälkeen järjestetään minuutin perusteella.
            Collections.sort(palautettavat, new Comparator<Ateria>() {
                @Override
                public int compare(Ateria o1, Ateria o2) {
                    if (o1.haeAika().get(0) == o2.haeAika().get(0) && o1.haeAika().get(1) > o2.haeAika().get(1))
                        return 1;
                    if (o1.haeAika().get(0) == o2.haeAika().get(0) && o1.haeAika().get(1) < o2.haeAika().get(1))
                        return -1;
                    return 0;
                }
            });
        }

        return palautettavat;
    }

    /**
     * @return palauttaa tämän päivän ateriat.
     */
    public List<Ateria> haeTulevatAteriat() {
        Calendar kalenteri = Calendar.getInstance();
        return haePaivamaaralla(kalenteri.get(Calendar.DAY_OF_MONTH), kalenteri.get(Calendar.MONTH), kalenteri.get(Calendar.YEAR));
    }

    /**
     * Poistaa valitun aterian listalta.
     */
    public void poistaAteria(int id) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).haeId() == id) {
                Log.d("poistettu", lista.get(i).toString());
                lista.remove(i);
                break;
            }
        }
    }

    public int haeKalorit(int paiva, int kuukausi, int vuosi) {
        List<Ateria> palautettavat = haePaivamaaralla(paiva, kuukausi, vuosi);
        int kalorit = 0;

        for (int i = 0; i < palautettavat.size(); i++) {
            kalorit += palautettavat.get(i).haeRavinto().get(4);
        }
        return kalorit;
    }

    public List<Double> haeRavintoarvot(int paiva, int kuukausi, int vuosi) {
        List<Ateria> lista = haePaivamaaralla(paiva, kuukausi, vuosi);

        double proteiini = 0;
        double hh = 0;
        double rasva = 0;

        for (int i = 0; i < lista.size(); i++) {
            proteiini += lista.get(i).haeRavinto().get(1);
            hh += lista.get(i).haeRavinto().get(2);
            rasva += lista.get(i).haeRavinto().get(3);
        }

        List<Double> arvot = Arrays.asList(proteiini, hh, rasva);
        return arvot;
    }

    public List<Integer> haeProsentit(int paiva, int kuukausi, int vuosi) {
        DecimalFormat df = new DecimalFormat("#.#");
        List<Ateria> lista = haePaivamaaralla(paiva, kuukausi, vuosi);
        double proteiini = 0;
        double hh = 0;
        double rasva = 0;

        for (int i = 0; i < lista.size(); i++) {
            proteiini += lista.get(i).haeRavinto().get(1);
            hh += lista.get(i).haeRavinto().get(2);
            rasva += lista.get(i).haeRavinto().get(3);
        }

        double yhteensa = proteiini + rasva + hh;

        float proteiiniPros = (float) (proteiini / yhteensa) * 100;
        float hhPros = (float)(hh / yhteensa) * 100;
        float rasvaPros = (float) (rasva / yhteensa) * 100;

        List<Integer> prosentit = Arrays.asList(Math.round(proteiiniPros), Math.round(hhPros), Math.round(rasvaPros));
        return prosentit;
    }

    public int seuraavaId(){
        id++;
        return id;
    }

    public List<Ateria> tulostaAteriat() {
        return this.lista;
    }
}
