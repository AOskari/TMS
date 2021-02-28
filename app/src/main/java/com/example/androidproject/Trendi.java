package com.example.androidproject;

import java.util.ArrayList;
import java.util.List;

public class Trendi {

    private static final Trendi trendit = new Trendi();
    private List<Paino> paino; // = new ArrayList<>();
    private List<Double> kal; // = new ArrayList<>();
    private List<Double> prot; // = new ArrayList<>();
    private List<Double> hh; // = new ArrayList<>();

    public static Trendi getInstance(){
        return trendit;
    }

    private Trendi() {
        paino = new ArrayList<>();
        kal = new ArrayList<>();
        prot = new ArrayList<>();
        hh = new ArrayList<>();
    }

    public List<Paino>getPaino(){
        return this.paino;
    }
    public List<Double> getKal() {
        return kal;
    }
    public List<Double>getProt(){
        return prot;
    }
    public List<Double> getHh() {
        return hh;
    }
    public void addPaino(Paino kg){
        this.paino.add(kg);
    }
}
