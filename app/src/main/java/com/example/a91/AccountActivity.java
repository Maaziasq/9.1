package com.example.a91;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;

public class AccountActivity extends AppCompatActivity {

    EditText setcalendartext;
    CalendarView calendarView;
    Button setcalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //image button that takes the user back to MainActivity
        ImageButton backbutton = (ImageButton) findViewById(R.id.backButton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

       /* setcalendar = findViewById(R.id.datebutton);
        setcalendartext =findViewById(R.id.setdatetext);

        setcalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setcalendartext.getText().isEmpty()){        //TÄÄ ON KESKEN NO PANIC

                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE)
                }

            }
        });*/

    }
}