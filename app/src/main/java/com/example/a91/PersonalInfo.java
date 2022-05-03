package com.example.a91;

public class PersonalInfo {
    public String username;
     //Firebase database requires empty constructor
    public PersonalInfo(){
    }

    //Getter- and setter-methods
    public void setUsername(String username){
        this.username = username;
    }
}
