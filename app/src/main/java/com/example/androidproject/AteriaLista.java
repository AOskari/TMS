package com.example.androidproject;

import java.util.ArrayList;
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

    public static AteriaLista haeLista() {
        return haeLista;
    }

    private AteriaLista() {
        lista = new ArrayList<>();
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
    public void poistaAteria(int i) {
        lista.remove(i);
    }
}
