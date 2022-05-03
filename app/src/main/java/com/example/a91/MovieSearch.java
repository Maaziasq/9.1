package com.example.a91;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MovieSearch extends AppCompatActivity {

    Context context = null;
    TheaterRepo theaterRepo = TheaterRepo.getInstance();
    Spinner spinner;
    ArrayList<String> stringTheaters;
    ArrayList<Movie> movies = new ArrayList<>();
    ArrayList<String> stringMovies = new ArrayList<>();
    ListView listView;
    int choice;
    EditText textViewDate;
    EditText textViewAfter;
    EditText textViewBefore;
    EditText textVewNimi;
    long mLastClickTime = 0;
    FirebaseAuth firebaseAuth;
    FileWriter fileWriter = FileWriter.getInstance();
    ImageButton searchbutton;
    ImageButton menubutton;
    TextView textView;



    //variables for sidemenu
    private androidx.drawerlayout.widget.DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_search);
        context = MovieSearch.this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        menubutton = findViewById(R.id.menubutton);
        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listview);
        textViewAfter = findViewById(R.id.editTextAfter);
        textViewBefore = findViewById(R.id.editTextBefore);
        textViewDate = findViewById(R.id.editTextDate);
        textVewNimi = findViewById(R.id.editTextNimi);
        searchbutton = findViewById(R.id.button);
        stringTheaters = theaterRepo.getStringTheaters();
        firebaseAuth = FirebaseAuth.getInstance();
        LocalTime time = LocalTime.now();
        fileWriter.applyContext(context);
        textView = findViewById(R.id.navheader);


        //Reading all theaters from the Finnkino XML
        theaterRepo.readTheaters();

        //Converting all theaters all Theaters to string
        stringTheaters = theaterRepo.getStringTheaters();

        //Setting the theaters to theater selection spinner
        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringTheaters);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa2);


        // Fetching the correct movies based on theater choice
        spinner.setSelection(0, false);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                choice = position;
                /*String date = textViewDate.getText().toString();
                if(date.equals("")){
                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    date = formatter.format(new Date());
                }*/
                theaterRepo.readMovies(textViewDate.getText().toString(), position);
                SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm");

                if (textViewAfter.getText().toString().equals("") && textViewBefore.getText().toString().equals("")) {
                    if (theaterRepo.getTheaters().get(position) != null) {
                        theaterRepo.getTheaters().get(position).moviesToString();
                    } else {
                        System.out.println("Movies list was empty");
                    }
                    stringMovies = theaterRepo.getTheaters().get(position).getStringMovies();
                    ArrayAdapter aa2 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, stringMovies);
                    listView.setAdapter(aa2);
                } else {
                    theaterRepo.getTheaters().get(position).moviesToString();
                    stringMovies = theaterRepo.getTheaters().get(position).getStringMovies();
                    ArrayList<String> searched = new ArrayList<>();
                    Date after;
                    Date before;
                    try {
                        after = formatter3.parse(textViewAfter.getText().toString());
                        before = formatter3.parse(textViewBefore.getText().toString());


                    for (int i = 0; i < stringMovies.size(); i++) {
                        String movie = stringMovies.get(i);
                        String stringTime = movie.substring(movie.length() - 5);
                        System.out.println("####### movie: " + movie);
                        System.out.println("####### substring: " + stringTime);

                        Date aika = formatter3.parse(stringTime);
                        assert aika != null;
                        if (aika.after(after) && aika.before(before)) {
                            searched.add(movie);
                        }
                    }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter aa3 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, searched);
                    listView.setAdapter(aa3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });


        //Help for double click handling from https://stackoverflow.com/questions/5608720/android-preventing-double-click-on-a-button
        //Add a movie to a user specific CSV on double click
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                long currTime = System.currentTimeMillis();
                if (currTime - mLastClickTime < ViewConfiguration.getDoubleTapTimeout()) {
                    onItemDoubleClick(adapterView, view, position, l);
                }
                mLastClickTime = currTime;
            }

            public void onItemDoubleClick(AdapterView<?> adapterView, View view, int position, long l) {
                System.out.println("Double press occurred!");
                Toast.makeText(context, "Movie added as watched!", Toast.LENGTH_LONG).show();
                fileWriter.applyContext(context);
                movies = theaterRepo.getTheaters().get(choice).getMovies();
                fileWriter.addMovie(movies.get(position).getName(), movies.get(position).getRating(), movies.get(position).getDirector(), movies.get(position).getDttm());

                //this is for adding data to as calendar events
                ArrayList<String> allMovies = new ArrayList<>();

                try {
                    allMovies = fileWriter.readFile();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < allMovies.size(); i++) {
                    String movie = allMovies.get(i);

                    String moviesplit[] = movie.split(";"); //this is one movie splitted
                    String movieName = moviesplit[0];
                    System.out.println(movieName);
                    LocalDate movieDate = LocalDate.parse(moviesplit[3].substring(0, 10));
                    System.out.println(movieDate);
                    System.out.println(CalendarUtils.selectedDate);
                    LocalTime movieTime = LocalTime.parse(moviesplit[3].substring(11, 16));
                    System.out.println(movieTime);


                    Event newEvent = new Event(movieName, movieDate, movieTime); //here put data parsed from csv
                    Event.eventsList.add(newEvent);

                }

            }
        });


        //this part is for the navigation menu
        findViewById(R.id.nav_settings);
        findViewById(R.id.nav_account);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        toggle.setDrawerIndicatorEnabled(true);


        NavigationView navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);

        navigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //this makes the menu items start activities when clicked
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //get item id for comparing with menu item ids
                int id = item.getItemId();

                //takes to SettingsActivity when Settings is pressed
                if (id == R.id.nav_settings) {
                    Intent intent = new Intent(MovieSearch.this, SettingsActivity.class);
                    startActivity(intent);
                }
                //takes to AccountActivity when My Account is pressed
                else if (id == R.id.nav_account) {
                    Intent intent = new Intent(MovieSearch.this, AccountActivity.class);
                    startActivity(intent);
                }
                //takes to CalendarActivity when Calendar is pressed
                else if (id == R.id.nav_calendar) {
                    Intent intent = new Intent(MovieSearch.this, CalendarActivity.class);
                    startActivity(intent);
                }
                //logs out and takes to log in activity when Log Out is pressed
                else if (id == R.id.nav_log) {
                    Intent intent = new Intent(MovieSearch.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(MovieSearch.this, "Logged out", Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                } else if (id == R.id.nav_history) {
                    Intent intent = new Intent(MovieSearch.this, HistoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

    }

    //makes hamburger icon open the sidemenu
    public void onClick(View v){
        drawerLayout.openDrawer(Gravity.LEFT);
    }



    //Enables searching from all theaters based on movie name
    public void searchByName(View v){
        theaterRepo.readMovies(textViewDate.getText().toString(), choice);
        SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm");
        String nimi = textVewNimi.getText().toString();


        if (!nimi.equals("")) {
            ArrayList<String> kaikki = theaterRepo.readAll(nimi);

            if (textViewAfter.getText().toString().equals("") && textViewBefore.getText().toString().equals("")) {
                ArrayAdapter aa4 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, kaikki);
                listView.setAdapter(aa4);
            }else{
                ArrayList<String> searched = new ArrayList<>();
                Date after = null;
                Date before = null;
                try {
                    after = formatter3.parse(textViewAfter.getText().toString());
                    before = formatter3.parse(textViewBefore.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < kaikki.size(); i++) {
                    String movie = kaikki.get(i);
                    String stringTime = movie.substring(movie.length() - 5);
                    System.out.println(stringTime);
                    try {
                        Date aika = formatter3.parse(stringTime);
                        if (aika.after(after) && aika.before(before)) {
                            searched.add(movie);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                ArrayAdapter aa3 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, searched);
                listView.setAdapter(aa3);
            }
        } else if (textViewAfter.getText().toString().equals("") && textViewBefore.getText().toString().equals("")) {
            theaterRepo.getTheaters().get(choice).moviesToString();
            stringMovies = theaterRepo.getTheaters().get(choice).getStringMovies();
            ArrayAdapter aa2 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, stringMovies);
            listView.setAdapter(aa2);
        } else {
            theaterRepo.getTheaters().get(choice).moviesToString();
            stringMovies = theaterRepo.getTheaters().get(choice).getStringMovies();
            ArrayList<String> searched = new ArrayList<>();
            Date after = null;
            Date before = null;
            try {
                after = formatter3.parse(textViewAfter.getText().toString());
                before = formatter3.parse(textViewBefore.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < stringMovies.size(); i++) {
                String movie = stringMovies.get(i);
                String stringTime = movie.substring(movie.length() - 5);
                System.out.println(stringTime);
                try {
                    Date aika = formatter3.parse(stringTime);
                    assert aika != null;
                    if (aika.after(after) && aika.before(before)) {
                        searched.add(movie);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
            ArrayAdapter aa3 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, searched);
            listView.setAdapter(aa3);
        }

    }



}