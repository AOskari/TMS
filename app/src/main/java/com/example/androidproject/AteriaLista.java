package com.example.androidproject;

import java.util.ArrayList;
import java.util.List;

public class AteriaLista {
    private static final List<Ateria> lista = new ArrayList<>();

    private AteriaLista() {
    }


    public List<Ateria> haeAteriat() {
        return this.lista;
    }

}
