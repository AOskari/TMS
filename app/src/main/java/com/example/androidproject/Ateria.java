package com.example.androidproject;

import java.text.DecimalFormat;
import java.util.ArrayList;
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

    private DecimalFormat df = new DecimalFormat("#.##");

    public Ateria() {
        raakaAineet = new ArrayList<>();
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
    }


    public String toString() {
        return name + "\r\n" + "\r\n" + "Kilokalorit: " + df.format(this.kcal) +  "\r\n" + "Proteiini: " +
                df.format(this.protein) + "\r\n" + "Hiilihydraatit: " + df.format(this.carb) + "Joista sokeria: " + df.format(this.sugar)
                + "\r\n" + "Rasva: " + df.format(this.fat) + "Tyydyttynyt rasva: " + df.format(this.saturatedFat) +  "\r\n"  + "\r\n" + "Suola: " +
                df.format(this.salt) + "\r\n" + "Kuitua: " + df.format(this.fiber) + "\r\n" + "Orgaaniset hapot: " + df.format(this.organicAcid);
    }
}
