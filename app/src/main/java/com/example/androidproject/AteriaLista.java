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
 *  Singleton jota käytetään aterioiden listaamiseen ja SharedPreferencesiin tallentamiseen.
 */
public class AteriaLista {
    private List<Ateria> lista;
    private List<Ateria> syodytAteriat;

    private static final AteriaLista haeLista = new AteriaLista();
    private int id;

    public static AteriaLista haeLista() {
        return haeLista;
    }

    private AteriaLista() {
        lista = new ArrayList<>();
        syodytAteriat = new ArrayList<>();
        id = 0;
    }

    public void lisaaAteria(Ateria ateria) {
        lista.add(ateria);
    }

    /**
     * Hakee AteriaListalta ateriat jotka sisältävät valitun päivämäärän, jonka jälkeen
     * järjestää listan kellonajan mukaan.
     */
    public List<Ateria> haePaivamaaralla(int paiva, int kuukausi, int vuosi) {
        List<Ateria> palautettavat = new ArrayList<>();

        for (int i = 0; i < lista.size(); i++) {
            List<Integer> paivamaara = lista.get(i).haePaivamaara();
            if (paivamaara.contains(paiva) && paivamaara.contains(kuukausi) && paivamaara.contains(vuosi)) {
                palautettavat.add(lista.get(i));
            }
        }
        return jarjesta(palautettavat);
    }

    /**
     * Palauttaa syödyt ateriat kellonaikajärjestyksessä ja päivämäärän perusteella.
     */
    public List<Ateria> haeSyodytPaivamaaralla(int paiva, int kuukausi, int vuosi) {
        List<Ateria> palautettavat = new ArrayList<>();

        for (int i = 0; i < syodytAteriat.size(); i++) {
            List<Integer> paivamaara = syodytAteriat.get(i).haePaivamaara();
            if (paivamaara.contains(paiva) && paivamaara.contains(kuukausi) && paivamaara.contains(vuosi)) {
                palautettavat.add(syodytAteriat.get(i));
            }
        }
        return jarjesta(palautettavat);
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

    /**
     * Siirtää valitun aterian syodytAteriat-listaan.
     * @param ateria valittu ateria.
     */
    public void asetaSyodyksi(Ateria ateria) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).haeId() == ateria.haeId()) {
                syodytAteriat.add(ateria);
                lista.remove(i);
                break;
            }
        }
    }

    /**
     * Palauttaa syötyjen aterioiden ravintoarvot valitun päivämäärän perusteella.
     */
    public List<Double> haeSyodytRavintoarvot(int paiva, int kuukausi, int vuosi) {
        double kalorit = 0;
        double proteiini = 0;
        double hh = 0;
        double rasva = 0;

        for (int i = 0; i < syodytAteriat.size(); i++) {
            kalorit += syodytAteriat.get(i).haeRavinto().get(4);
            proteiini += syodytAteriat.get(i).haeRavinto().get(1);
            hh += syodytAteriat.get(i).haeRavinto().get(2);
            rasva += syodytAteriat.get(i).haeRavinto().get(3);
        }

        List<Double> arvot = Arrays.asList(kalorit, proteiini, hh, rasva);
        return arvot;
    }


    /**
     * Palauttaa päivän aterioiden yhteiskalorimäärän valitun päivämäärän perusteella.
     */
    public int haeKalorit(int paiva, int kuukausi, int vuosi) {
        List<Ateria> palautettavat = haePaivamaaralla(paiva, kuukausi, vuosi);
        int kalorit = 0;

        for (int i = 0; i < palautettavat.size(); i++) {
            kalorit += palautettavat.get(i).haeRavinto().get(4);
        }
        return kalorit;
    }

    public int haeKaloritIlman(int id, int paiva, int kuukausi, int vuosi) {
        List<Ateria> palautettavat = haePaivamaaralla(paiva, kuukausi, vuosi);
        int kalorit = 0;

        for (int i = 0; i < palautettavat.size(); i++) {
            if (palautettavat.get(i).haeId() != id) {
                kalorit += palautettavat.get(i).haeRavinto().get(4);
            }
        }
        return kalorit;
    }

    /**
     * Palauttaa syömättömien aterioiden yhteisravintoarvot valitun päivämäärän perusteella.
     */
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

    /**
     * Palauttaa proteiinin, hiilihydraattien ja rasvan määrän prosentteina valitun päivämäärän perusteella.
     */
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

    /**
     * Palauttaa seuraavan vapaan id:n.
     */
    public int seuraavaId(){
        id++;
        return id;
    }

    /**
     * Palauttaa syömättömien aterioiden listan.
     */
    public List<Ateria> tulostaAteriat() {
        return this.lista;
    }



    // =================================================================== //
    // ========================= Private-metodit ========================= //
    // =================================================================== //

    /**
     * Järjestää ja palauttaa annetun listan.
     * @param ateria vaadittu lista.
     * @return Palauttaa järjestetyn listan.
     */
    private List<Ateria> jarjesta(List<Ateria> ateria) {
        List<Ateria> palautettavat = ateria;

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
        return palautettavat;
    }
}
