package com.example.a91;

import android.annotation.SuppressLint;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class FileWriter {

    private Context context;
    //Singleton

    @SuppressLint("StaticFieldLeak")
    private static final FileWriter singleton = new FileWriter();

    private FileWriter() {

    }

    public static FileWriter getInstance() {
        return singleton;
    }

    public void applyContext(Context context) {
        this.context = context;
    }


    //Make a new file if one does not exist
    private void makeFile() {
        try {
            String fname = "Movie history of " + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() + ".csv";
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fname, Context.MODE_APPEND));
            String uHeader = "Data of: " + FirebaseAuth.getInstance().getCurrentUser().getEmail() + "\n";
            String dHeader = "Name;Rating;Director;Dttm\n";
            ows.write(uHeader);
            ows.write(dHeader);
            ows.close();
            System.out.println("Uusi tiedosto luotiin");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //Write to an existing file
    private void writeFile(String name, String rating, String director, String dttm) {
        try {
            String fname = "Movie history of " + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() + ".csv";
            OutputStreamWriter ows = new OutputStreamWriter(context.openFileOutput(fname, Context.MODE_APPEND));
            String newMovie = name + ";" + rating + ";" + director + ";" + dttm + ";";
            ows.append(newMovie).append("\n");
            ows.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addMovie(String name, String rating, String director, String dttm) {
        String fname = "Movie history of " + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() + ".csv";
        String fpath = context.getFilesDir().getAbsolutePath();
        System.out.println(fpath + "/" + fname);

        File file = new File(fpath + "/" + fname);

        if (file.length() == 0) {
            makeFile();
            writeFile(name, rating, director, dttm);
            System.out.println("File created and movie added");
        } else {
            writeFile(name, rating, director, dttm);
            System.out.println("Movie added to existing file");
        }
    }

    public ArrayList<String> readFile() throws FileNotFoundException {
        String fname = "Movie history of " + Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail() + ".csv";
        String fpath = context.getFilesDir().getAbsolutePath();

        ArrayList<String> records = new ArrayList<>();
        Scanner scanner = new Scanner(new File(fpath + "/" + fname));
        while (scanner.hasNextLine()) {
            records.add(scanner.nextLine());
        }
        return records;
    }

}
