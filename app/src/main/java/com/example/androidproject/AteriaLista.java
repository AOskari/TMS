package com.example.androidproject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    /* Luodaan metodi, joka ensin hakee listalta kaikki Ateriat jotka sisältävät halutun päivämäärän,
    * jonka jälkeen järjestää ateriat kellonajan mukaan.  */
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
}
