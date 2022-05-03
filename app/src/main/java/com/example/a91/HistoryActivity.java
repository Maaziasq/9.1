package com.example.a91;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ImageButton imageButton;
    EditText editText;
    ListView listView;
    Context context;
    FileWriter fileWriter = FileWriter.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ImageButton backbutton = (ImageButton) findViewById(R.id.backButton5);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this, MovieSearch.class);
                startActivity(intent);


            }
        });
        context = HistoryActivity.this;
        imageButton = findViewById(R.id.button5);
        editText = findViewById(R.id.editTextNimi5);
        listView = findViewById(R.id.listview5);
        fileWriter.applyContext(context);



    }


    //Read movie history from user's CSV to list view
    ArrayList<String> lista = new ArrayList<>();
    public void readToList(View v){
        try {
            if(fileWriter.readFile() != null){
                lista = fileWriter.readFile();
            }
           else{
                Toast.makeText(context, "No movie history to show", Toast.LENGTH_LONG).show();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_list_item_1, lista);
        listView.setAdapter(aa);
    }
}