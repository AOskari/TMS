package com.example.androidproject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Paino {

    private float arvo;
    private int paiva;
    private int kuukausi;
    private int vuosi;

    public Paino(float arvo) {
        this.arvo = arvo;

        Calendar kalenteri = Calendar.getInstance();
        paiva = kalenteri.get(Calendar.DAY_OF_MONTH);
        kuukausi = kalenteri.get(Calendar.MONTH);
        vuosi = kalenteri.get(Calendar.YEAR);
    }

    public List<Integer> haePainonPaivamaara() {
        List<Integer> palautettavat = Arrays.asList(paiva, kuukausi, vuosi);
        return palautettavat;
    }

    public String toString(){
        return String.valueOf(this.arvo);
    }
}
