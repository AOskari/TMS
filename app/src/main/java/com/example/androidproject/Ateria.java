package com.example.androidproject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ateria {

    private List<Elintarvike> raakaAineet;

    private String name;
    private double salt;
    private double kcal;
    private double fat;
    private double protein;
    private double carb;
    private double organicAcid;
    private double saturatedFat;
    private double sugar;
    private double fiber;
    private double maara;

    private int paiva;
    private int kuukausi;
    private int vuosi;

    private int tunnit;
    private int minuutit;

    public Ateria(String name) {
        raakaAineet = new ArrayList<>();
        this.name = name;
    }

    public void asetaPaivamaara(int paiva, int kuukausi, int vuosi) {
        this.paiva = paiva;
        this.kuukausi = kuukausi;
        this.vuosi = vuosi;
    }

    public void asetaAika(int tunnit, int minuutit) {
        this.tunnit = tunnit;
        this.minuutit = minuutit;
    }

    public void lisaaAine(Elintarvike tarvike) {

        //TODO: Mahdollisuus määrätä montako grammaa elintarviketta lisätään.

        raakaAineet.add(tarvike);

        salt += tarvike.haeRavintoarvot().get(0);
        kcal += tarvike.haeRavintoarvot().get(1);
        fat += tarvike.haeRavintoarvot().get(2);
        protein += tarvike.haeRavintoarvot().get(3);
        carb += tarvike.haeRavintoarvot().get(4);
        organicAcid += tarvike.haeRavintoarvot().get(5);
        saturatedFat += tarvike.haeRavintoarvot().get(6);
        sugar += tarvike.haeRavintoarvot().get(7);
        fiber += tarvike.haeRavintoarvot().get(8);
        maara += tarvike.haeRavintoarvot().get(9);
    }

    public List<Elintarvike> haeTarvikkeet() {
        return this.raakaAineet;
    }

    /**
     * Poistaa valitun Elintarvike-olion listalta ja sen sisältämät ravintoarvot.
     */
    public void poista(int i) {

        /* Vähennetään poistettavan elintarvikkeen ravintoarvot
         kokonaisuudesta, jonka jälkeen poistetaan Elintarvike. */

        this.salt = Math.max(this.salt - (raakaAineet.get(i).haeRavintoarvot().get(0)), 0);
        this.kcal = Math.max(this.kcal - (raakaAineet.get(i).haeRavintoarvot().get(1)), 0);
        this.fat = Math.max(this.fat - (raakaAineet.get(i).haeRavintoarvot().get(2)), 0);
        this.protein = Math.max(this.protein - (raakaAineet.get(i).haeRavintoarvot().get(3)), 0);
        this.carb = Math.max(this.carb - (raakaAineet.get(i).haeRavintoarvot().get(4)), 0);
        this.organicAcid = Math.max(this.organicAcid - (raakaAineet.get(i).haeRavintoarvot().get(5)), 0);
        this.saturatedFat = Math.max(this.saturatedFat - (raakaAineet.get(i).haeRavintoarvot().get(6)), 0);
        this.sugar = Math.max(this.sugar - (raakaAineet.get(i).haeRavintoarvot().get(7)), 0);
        this.fiber = Math.max(this.fiber - (raakaAineet.get(i).haeRavintoarvot().get(8)), 0);
        raakaAineet.remove(i);
    }

    public String haeNimi() {
        return this.name;
    }

    public List<Double> haeRavinto() {
        List<Double> lista = Arrays.asList(this.protein + this.carb + this.fat, this.protein, this.carb, this.fat, this.kcal, this.sugar, this.saturatedFat, this.salt, this.fiber);
        return lista;
    }

    public List<Integer> haePaivamaara() {
        List<Integer> paivamaara = Arrays.asList(this.paiva, this.kuukausi, this.vuosi);
        return paivamaara;
    }

    public List<Integer> haeAika() {
        List<Integer> aika = Arrays.asList(this.tunnit, this.minuutit);
        return aika;
    }

    public void asetaNimi(String nimi) {
        this.name = nimi;
    }

    public String paivamaaraString() {
        return this.paiva + "/" + this.kuukausi + "/" + this.vuosi;
    }

    public String aikaString() {

        String tunti = "";
        String minuutti = "";

        if (String.valueOf(tunnit).length() == 1) {
            tunti = "0" + tunnit;
        } else {
            tunti = String.valueOf(tunnit);
        }
        if (String.valueOf(minuutit).length() == 1) {
            minuutti = "0" + minuutit;
        } else {
            minuutti = String.valueOf(minuutit);
        }
        return tunti + ":" + minuutti;
    }

    public String toString() {
        DecimalFormat df = new DecimalFormat("#.##");

        return aikaString() + "\r\n" + name + "\r\n" + "\r\n" + "Energia: " + df.format(this.kcal) +  " kcal \r\n" + "Proteiini: " +
                df.format(this.protein) + "g \r\n" + "Hiilihydraatit: " + df.format(this.carb) +  "g \r\n" + "Joista sokeria: " + df.format(this.sugar)
                + "g \r\n" + "Rasva: " + df.format(this.fat) + "g \r\n" + "Tyydyttynyt rasva: " + df.format(this.saturatedFat) +  "g \r\n"  + "\r\n" + "Suola: " +
                df.format(this.salt) + "mg \r\n" + "Kuitua: " + df.format(this.fiber) + "g \r\n";
    }
}
