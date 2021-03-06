package com.example.androidproject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Luokassa luodaan paino-olioita, joilla ominaisuuksina arvo, päivämäärä ja kellonaika
 */
public class Paino {

    private float arvo;
    private int paiva;
    private int kuukausi;
    private int vuosi;
    private int tunnit;
    private int minuutit;

    /**
     * Luokan konstruktori. Luokassa haetaan Kalenteri-luokan avulla päivämäärä ja kellonaika
     * @param arvo
     */
    public Paino(float arvo) {
        this.arvo = arvo;

        Calendar kalenteri = Calendar.getInstance();
        paiva = kalenteri.get(Calendar.DAY_OF_MONTH);
        kuukausi = kalenteri.get(Calendar.MONTH)+1;
        vuosi = kalenteri.get(Calendar.YEAR);

        this.tunnit = kalenteri.get(Calendar.HOUR_OF_DAY);
        this.minuutit = kalenteri.get(Calendar.MINUTE);

    }

    /**
     * Metodi, jolla saadaan haettua lista painon tallennuksen yhteydessä tallennetuista päivämääristä
     * @return
     */
    public List<Integer> haePainonPaivamaara() {
        List<Integer> palautettavat = Arrays.asList(paiva, kuukausi, vuosi);
        return palautettavat;
    }
    /**
     * Metodi, jolla saadaan haettua lista painon tallennuksen yhteydessä tallennetuista kellonajoista
     * @return
     */
    public List<Integer> haeKellonaika() {
        List<Integer> palautettavat = Arrays.asList(tunnit, minuutit);
        return palautettavat;
    }
    /**
     * Metodi, jolla saadaan haettua painon tallennuksen yhteydessä tallennettu päivämäärä
     * @return
     */
    public String paivamaaraString() {
        return this.paiva + "/" + this.kuukausi + "/" + this.vuosi;
    }

    public String toString(){
        return String.valueOf(this.arvo);
    }
}
