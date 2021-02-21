package com.example.androidproject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Haetut elintarvikkeet Fineli:n avoimesta rajapinnasta tallennetaan
 * Elintarvike-luokan instansseina.
 */
public class Elintarvike {

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

    /**
     * Luokan konstruktori, joka ottaa parametreiksi nimen, kaikki elintarvikkeen ravintoarvot ja halutun määärän.
     */
    public Elintarvike(String name, double salt, double kcal, double fat, double protein, double carb,
                       double organicAcid, double saturatedFat, double sugar, double fiber, double maara) {

        this.maara = maara;
        double maaraKerroin = this.maara / 100.0;

        this.name = name;
        this.salt = salt * maaraKerroin;
        this.kcal = kcal * maaraKerroin;
        this.fat = fat * maaraKerroin;
        this.protein = protein * maaraKerroin;
        this.carb = carb * maaraKerroin;
        this.organicAcid = organicAcid * maaraKerroin;
        this.saturatedFat = saturatedFat * maaraKerroin;
        this.sugar = sugar * maaraKerroin;
        this.fiber = fiber * maaraKerroin;
    }

    /**
     * @return Palauttaa listan kaikista ravintoarvoista.
     */
    public List<Double> haeRavintoarvot() {
        List<Double> lista = Arrays.asList(this.salt, this.kcal, this.fat, this.protein, this.carb, this.organicAcid, this.saturatedFat, this.sugar, this.fiber, this.maara);
        return lista;
    }

    /**
     * @return Palauttaa nimen.
     */
    public String haeNimi() {
        return this.name;
    }

    /**
     * @return Palauttaa Elintarvikkeen tiedot.
     */
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.#");
        return "Ravintosisältö / " + df.format(this.maara) + "g \r\n" + "Energia: " + df.format(this.kcal) +  " kcal \r\n" + "Proteiini: " +
                df.format(this.protein) + "g" + "\r\n" + "Hiilihydraatit: " + df.format(this.carb)  + "g" + "\r\n" + "Rasva: " + df.format(this.fat)  + "g";
    }

}
