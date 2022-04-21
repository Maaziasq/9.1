package com.example.a91;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    Context context = null;
    TeatteriHakemisto hakemisto = TeatteriHakemisto.getInstance();
    Spinner spinner;
    ArrayList<String> stringTeatterit;
    ArrayList<String> elokuvat;
    int valinta;
    ListView listView;
    EditText textViewDate;
    EditText textViewAfter;
    EditText textViewBefore;

    //variables for sidemenu
    private androidx.drawerlayout.widget.DrawerLayout DrawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        spinner = findViewById(R.id.spinner);
        listView = findViewById(R.id.listview);
        textViewAfter = findViewById(R.id.editTextAfter);
        textViewBefore = findViewById(R.id.editTextBefore);
        textViewDate = findViewById(R.id.editTextDate);

        hakemisto.lueTeatterit();
        stringTeatterit = hakemisto.getStringTeatterit();


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                valinta = position;
                hakemisto.lueElokuvat(textViewDate.getText().toString(), valinta);
                SimpleDateFormat formatter3 = new SimpleDateFormat("HH:mm");


                    if (textViewAfter.getText().toString().equals("") && textViewBefore.getText().toString().equals("")) {
                        elokuvat = hakemisto.getTeatterit().get(valinta).getElokuvat();
                        ArrayAdapter aa2 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, elokuvat);
                        listView.setAdapter(aa2);
                    } else {
                        elokuvat = hakemisto.getTeatterit().get(valinta).getElokuvat();
                        ArrayList<String> haetut = new ArrayList<>();
                        Date after = null;
                        Date before = null;
                        try {
                            after = formatter3.parse(textViewAfter.getText().toString());
                            before = formatter3.parse(textViewBefore.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        for (int i = 0; i < elokuvat.size(); i++) {
                            String elokuva = elokuvat.get(i);
                            String saika = elokuva.substring(elokuva.length() - 5);
                            System.out.println(saika);
                            try {
                                Date aika = formatter3.parse(saika);
                                if (aika.after(after) && aika.before(before)) {
                                    haetut.add(elokuva);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }
                        ArrayAdapter aa3 = new ArrayAdapter(context, android.R.layout.simple_list_item_1, haetut);
                        listView.setAdapter(aa3);
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

        //this part is for the navigation menu
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringTeatterit);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);

        findViewById(R.id.nav_settings);

        NavigationView navigationView = findViewById(R.id.navigationView);
        DrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, DrawerLayout, R.string.open, R.string.close);

        DrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //this makes the menu items start activities when clicked
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                //get item id for comparing with menu item ids
                int id = item.getItemId();

                //takes to SettingsActivity when Settings is pressed
                if(id == R.id.nav_settings){
                    Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);
                }
                //takes to AccountActivity when My Account is pressed
                else if(id == R.id.nav_account){
                    Intent intent = new Intent(MainActivity.this, AccountActivity.class);
                    startActivity(intent);
                }
                //logs out and takes to log in activity when Log Out is pressed
                else if(id == R.id.nav_log){
                    //here something to log out and start the login activity
                }
                return true;
            }
        });


    }

    //this allows the hamburger menu to open
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}