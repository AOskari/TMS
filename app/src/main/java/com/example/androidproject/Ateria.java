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

    public Ateria(String name) {
        raakaAineet = new ArrayList<>();
        this.name = name;
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

    public List<Elintarvike> haeTarvikkeet() {
        return this.raakaAineet;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("#.#");

        return name + "\r\n" + "\r\n" + "Energia: " + df.format(this.kcal) +  " kcal \r\n" + "Proteiini: " +
                df.format(this.protein) + "g \r\n" + "Hiilihydraatit: " + df.format(this.carb) +  "g \r\n" + "Joista sokeria: " + df.format(this.sugar)
                + "g \r\n" + "Rasva: " + df.format(this.fat) + "g \r\n" + "Tyydyttynyt rasva: " + df.format(this.saturatedFat) +  "g \r\n"  + "\r\n" + "Suola: " +
                df.format(this.salt) + "g \r\n" + "Kuitua: " + df.format(this.fiber) + "g \r\n" + "Orgaaniset hapot: " + df.format(this.organicAcid) + "g";
    }
}
