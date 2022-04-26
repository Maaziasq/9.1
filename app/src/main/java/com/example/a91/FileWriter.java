package com.example.a91;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileWriter {
    private Context context;

    //Singleton
    private static final FileWriter singleton = new FileWriter();

    private FileWriter(){

    }

    public static FileWriter getInstance(){return singleton;}

    public void applyContext(Context context){this.context = context;}

    private void makeFile(){
        try{
            String fname = "Movie history of "+ FirebaseAuth.getInstance().getCurrentUser().getEmail() + ".csv";
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fname, context.MODE_APPEND));
            String uHeader = "Data of: " + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "\n";
            String dHeader = "Name;Time;Rating;\n";
            ows.write(uHeader);
            ows.write(dHeader);
            ows.close();
            System.out.println("Uusi tiedosto luotiin");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(String name){
        try{
            String fname = "Movie history of "+ FirebaseAuth.getInstance().getCurrentUser().getEmail() + ".csv";
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fname, context.MODE_APPEND));
            String newMovie = name + ";";
            ows.append(newMovie+"\n");
            ows.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMovie(String name){
        String fname = "Movie history of "+ FirebaseAuth.getInstance().getCurrentUser().getEmail() + ".csv";
        String fpath = context.getFilesDir().getAbsolutePath();
        System.out.println(fpath + "/" + fname);

        File file = new File(fpath + "/" + fname);

        if(file.length() == 0){
            makeFile();
            writeFile(name);
            System.out.println("File created and movie added");
        }
        else {
            writeFile(name);
            System.out.println("Movie added to existing file");
        }
    }
}
