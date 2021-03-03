package com.example.androidproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Trendi {

    private static final Trendi trendit = new Trendi();
    private List<Paino> paino; // = new ArrayList<>();
    private List<Double> kal; // = new ArrayList<>();
    private List<Double> prot; // = new ArrayList<>();
    private List<Double> hh; // = new ArrayList<>();

    public static Trendi getInstance(){
        return trendit;
    }

    private Trendi() {
        paino = new ArrayList<>();
        kal = new ArrayList<>();
        prot = new ArrayList<>();
        hh = new ArrayList<>();
    }

    public List<Paino>getPaino(){
        return this.paino;
    }
    public List<Double> getKal() {
        return kal;
    }
    public List<Double>getProt(){
        return prot;
    }
    public List<Double> getHh() {
        return hh;
    }

    public void addPaino(Paino kg){
        this.paino.add(kg);
        jarjestaPainot();
    }


    /**
     * Järjestää Paino-listan päivämäärän mukaan.
     */
    private void jarjestaPainot() {

        /**
         * Järjestetään lista seuraavassa järjestyksessä:
         * Vuosi, kuukausi ja lopuksi päivän perusteella.
         * Näiden jälkeen järjestetään kellonajan perusteella.
         */
        Collections.sort(paino, new Comparator<Paino>() {
            @Override
            public int compare(Paino p1, Paino p2) {
                if (p1.haePainonPaivamaara().get(2) > p2.haePainonPaivamaara().get(2))
                    return 1;
                if (p1.haePainonPaivamaara().get(2) < p2.haePainonPaivamaara().get(2))
                    return -1;
                return 0;
            }
        });

        Collections.sort(paino, new Comparator<Paino>() {
            @Override
            public int compare(Paino p1, Paino p2) {
                if (p1.haePainonPaivamaara().get(2) == p2.haePainonPaivamaara().get(2) && p1.haePainonPaivamaara().get(1) > p2.haePainonPaivamaara().get(1))
                    return 1;
                if (p1.haePainonPaivamaara().get(2) == p2.haePainonPaivamaara().get(2) && p1.haePainonPaivamaara().get(1) < p2.haePainonPaivamaara().get(1))
                    return -1;
                return 0;
            }
        });

        Collections.sort(paino, new Comparator<Paino>() {
            @Override
            public int compare(Paino p1, Paino p2) {
                if (p1.haePainonPaivamaara().get(1) == p2.haePainonPaivamaara().get(1) && p1.haePainonPaivamaara().get(0) > p2.haePainonPaivamaara().get(0))
                    return 1;
                if (p1.haePainonPaivamaara().get(1) == p2.haePainonPaivamaara().get(1) && p1.haePainonPaivamaara().get(0) < p2.haePainonPaivamaara().get(0))
                    return -1;
                return 0;
            }
        });

        Collections.sort(paino, new Comparator<Paino>() {
            @Override
            public int compare(Paino p1, Paino p2) {
                if (p1.haePainonPaivamaara().get(0) == p2.haePainonPaivamaara().get(0) && p1.haeKellonaika().get(0) > p2.haeKellonaika().get(0))
                    return 1;
                if (p1.haePainonPaivamaara().get(0) == p2.haePainonPaivamaara().get(0) && p1.haeKellonaika().get(0) < p2.haeKellonaika().get(0))
                    return -1;
                return 0;
            }
        });

        Collections.sort(paino, new Comparator<Paino>() {
            @Override
            public int compare(Paino p1, Paino p2) {
                if (p1.haeKellonaika().get(0) == p2.haeKellonaika().get(0) && p1.haeKellonaika().get(1) > p2.haeKellonaika().get(1))
                    return 1;
                if (p1.haeKellonaika().get(0) == p2.haeKellonaika().get(0) && p1.haeKellonaika().get(1) < p2.haeKellonaika().get(1))
                    return -1;
                return 0;
            }
        });
    }
}
