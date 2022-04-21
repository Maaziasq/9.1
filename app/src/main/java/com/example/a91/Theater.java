package com.example.a91;

import java.util.ArrayList;

public class Theater {
    private String location;
    private String id;
    private ArrayList<String> movies = new ArrayList<>();

    public ArrayList<String> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<String> movies) {
        this.movies = movies;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Theater(String id, String location){
        this.id = id;
        this.location = location;
    }
}
