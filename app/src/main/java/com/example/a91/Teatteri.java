package com.example.a91;

import java.util.ArrayList;

public class Teatteri {
    private String paikka;
    private String id;
    private ArrayList<String> elokuvat = new ArrayList<>();

    public ArrayList<String> getElokuvat() {
        return elokuvat;
    }

    public void setElokuvat(ArrayList<String> elokuvat) {
        this.elokuvat = elokuvat;
    }

    public String getPaikka() {
        return paikka;
    }

    public void setPaikka(String paikka) {
        this.paikka = paikka;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Teatteri(String id, String paikka){
        this.id = id;
        this.paikka = paikka;
    }
}
