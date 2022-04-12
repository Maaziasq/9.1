package com.example.a91;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

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

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, stringTeatterit);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);


    }


}