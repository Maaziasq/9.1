package com.example.a91;

public class Movie {
    private String name;
    private String time;
    private String rating;
    private String director;
    private String dttm;

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

    public Movie(String name, String time, String rating, String director, String dttm) {
        this.name = name;
        this.time = time;
        this.rating = rating;
        this.director = director;
        this.dttm = dttm;
    }

    public Movie(String name, String time, String rating, String director) {
        this.name = name;
        this.time = time;
        this.rating = rating;
        this.director = director;
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

    public String getDirector() {
        return director;
    }

    public String getDttm() {
        return dttm;
    }

    public Movie(){

    }
}