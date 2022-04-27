package com.example.a91;

public class Movie {
    private String name;
    private String time;
    private String rating;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Movie(String name, String time, String rating) {
        this.name = name;
        this.time = time;
        this.rating = rating;
    }
    public Movie(String name, String time) {
        this.name = name;
        this.time = time;
        this.rating = rating;
    }

    public Movie(){

    }
}