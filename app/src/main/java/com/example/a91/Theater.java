package com.example.a91;

import java.util.ArrayList;

public class Theater {
    private final String location;
    private String id;
    private ArrayList<Movie> movies;
    private ArrayList<String> stringMovies;

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void moviesToString(){
        stringMovies = new ArrayList<>();
        for (Movie m : movies){
            stringMovies.add(m.getName()+"\nIMDB rating: "+m.getRating()+"\nStart time: "+m.getTime());
        }
    }

    public ArrayList<String> getStringMovies() {
        return stringMovies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public String getLocation() {
        return location;
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
