package com.example.androidproject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Paino {

    private float arvo;
    private int paiva;
    private int kuukausi;
    private int vuosi;
    private int tunnit;
    private int minuutit;

    public Paino(float arvo) {
        this.arvo = arvo;

        Calendar kalenteri = Calendar.getInstance();
        paiva = kalenteri.get(Calendar.DAY_OF_MONTH);
        kuukausi = kalenteri.get(Calendar.MONTH)+1;
        vuosi = kalenteri.get(Calendar.YEAR);

        this.tunnit = kalenteri.get(Calendar.HOUR_OF_DAY);
        this.minuutit = kalenteri.get(Calendar.MINUTE);

    }

    public List<Integer> haePainonPaivamaara() {
        List<Integer> palautettavat = Arrays.asList(paiva, kuukausi, vuosi);
        return palautettavat;
    }

    public List<Integer> haeKellonaika() {
        List<Integer> palautettavat = Arrays.asList(tunnit, minuutit);
        return palautettavat;
    }

    public String paivamaaraString() {
        return this.paiva + "/" + this.kuukausi + "/" + this.vuosi;
    }

    public String toString(){
        return String.valueOf(this.arvo);
    }
}
