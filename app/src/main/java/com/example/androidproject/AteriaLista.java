package com.example.androidproject;

import java.util.ArrayList;
import java.util.List;

public class AteriaLista {
    private List<Ateria> lista = new ArrayList<>();
    private static final AteriaLista haeLista = new AteriaLista();

    public static AteriaLista haeLista() {
        return haeLista;
    }

    private AteriaLista() {
    }

    public List<Ateria> ateriat() {
        return lista;
    }


    public List<Ateria> haeAteriat() {
        return this.lista;
    }

}
