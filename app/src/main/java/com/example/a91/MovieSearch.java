package com.example.a91;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
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
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MovieSearch extends AppCompatActivity implements CalendarAdapter.OnItemListener{

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

    //variables for sidemenu
    private androidx.drawerlayout.widget.DrawerLayout drawerLayout;

    //variables for calendar
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;

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

        //Reading all theaters from the Finnkino XML
        theaterRepo.readTheaters();

        //Converting all theaters all Theaters to string
        stringTheaters = theaterRepo.getStringTheaters();

        //Setting the theaters to theater selection spinner
        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringTheaters);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa2);


        // Fetching the correct movies based on theater choice
        spinner.setSelection(0,false);
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
                    if(theaterRepo.getTheaters().get(position) != null){
                        theaterRepo.getTheaters().get(position).moviesToString();
                    }
                    else{
                        System.out.println("Movies list was empty");
                    }
                    stringMovies = theaterRepo.getTheaters().get(position).getStringMovies();
                    ArrayAdapter aa2 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, stringMovies);
                    listView.setAdapter(aa2);
                } else {
                    theaterRepo.getTheaters().get(position).moviesToString();
                    stringMovies = theaterRepo.getTheaters().get(position).getStringMovies();
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
                Toast.makeText(context, "Movie added as watched!",Toast.LENGTH_LONG).show();
                fileWriter.applyContext(context);
                movies = theaterRepo.getTheaters().get(choice).getMovies();
                fileWriter.addMovie(movies.get(position).getName(),movies.get(position).getRating(),movies.get(position).getDirector(),movies.get(position).getDttm());
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
                if(id == R.id.nav_settings){
                    Intent intent = new Intent(MovieSearch.this, SettingsActivity.class);
                    startActivity(intent);
                }
                //takes to AccountActivity when My Account is pressed
                else if(id == R.id.nav_account){
                    Intent intent = new Intent(MovieSearch.this, AccountActivity.class);
                    startActivity(intent);
                }
                //logs out and takes to log in activity when Log Out is pressed
                else if(id == R.id.nav_log){
                    Intent intent = new Intent(MovieSearch.this, HomeActivity.class);
                    startActivity(intent);
                    Toast.makeText(MovieSearch.this, "Logged out", Toast.LENGTH_LONG).show();
                    firebaseAuth.signOut();
                }
                else if(id == R.id.nav_history){
                    Intent intent = new Intent(MovieSearch.this, HistoryActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        //this part is for the calendar

        initWidgets();
        this.selectedDate = LocalDate.now();
        setMonthView();

    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView()
    {
        System.out.println("tähän asti" + selectedDate.toString());
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> daysInMonth = daysInMonthArray(selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private ArrayList<String> daysInMonthArray(LocalDate date)
    {
        ArrayList<String> daysInMonthArray = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        int daysInMonth = yearMonth.lengthOfMonth();

        LocalDate firstOfMonth = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

        for(int i = 1; i <= 42; i++)
        {
            if(i <= dayOfWeek || i > daysInMonth + dayOfWeek)
            {
                daysInMonthArray.add("");
            }
            else
            {
                daysInMonthArray.add(String.valueOf(i - dayOfWeek));
            }
        }
        return  daysInMonthArray;
    }

    private String monthYearFromDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        System.out.println(date.toString());

        return date.format(formatter);
    }

    public void previousMonthAction(View view)
    {
        selectedDate = selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction(View view)
    {
        selectedDate = selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemClick(int position, String dayText)
    {
        if(!dayText.equals(""))
        {
            String message = "Selected Date " + dayText + " " + monthYearFromDate(selectedDate);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    //calendar part ends here

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