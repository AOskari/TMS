package com.example.androidproject;

import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    private DecimalFormat df = new DecimalFormat("#.##");

    public Elintarvike(String name, double salt, double kcal, double fat, double protein, double carb,
                       double organicAcid, double saturatedFat, double sugar, double fiber) {

        this.name = name;
        this.salt = salt;
        this.kcal = kcal;
        this.fat = fat;
        this.protein = protein;
        this.carb = carb;
        this.organicAcid = organicAcid;
        this.saturatedFat = saturatedFat;
        this.sugar = sugar;
        this.fiber = fiber;
    }

    public String specificInfo() {
        //TODO: Palauta tarkat ravintotiedot.
        return null;
    }

    public List<Double> haeRavintoarvot() {
        List<Double> lista = Arrays.asList(this.salt, this.kcal, this.fat, this.protein, this.carb, this.organicAcid, this.saturatedFat, this.sugar, this.fiber);
        return lista;
    }


    public String toString() {
        return name + "\r\n" + "Ravintosisältö / 100g" + "\r\n" + "Kilokalorit: " + df.format(this.kcal) +  "\r\n" + "Proteiini: " +
                df.format(this.protein) + "g" + "\r\n" + "Hiilihydraatit: " + df.format(this.carb)  + "g" + "\r\n" + "Rasva: " + df.format(this.fat)  + "g";
    }

}
