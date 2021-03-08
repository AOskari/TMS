package com.example.androidproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Singleton-luokka, jonka avulla paino-olioita voidaan lisätä listaan ja hakea olioita sisältävä lista
 */
public class Trendi {

    private static final Trendi trendit = new Trendi();
    private List<Paino> paino;

    /**
     * Metodi, jolla päästään käsiksi singletonin sisältämiin metodeihin
     * @return
     */
    public static Trendi getInstance(){
        return trendit;
    }

    /**
     * Konstruktori, jossa alustetaan hauissa tarvittavat listat
     */
    private Trendi() {
        paino = new ArrayList<>();
    }

    /**
     * Metodi, jolla saadaan haettua paino-olioihin tallennetut arvot
     * @return palauttaa listan paino-olioista
     */
    public List<Paino>getPaino(){
        return this.paino;
    }

    /**
     * Metodi, jolla saadaan lisättyä paino listaan
     * @param kg asetukset-aktiviteetissa annettu arvo
     */
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
