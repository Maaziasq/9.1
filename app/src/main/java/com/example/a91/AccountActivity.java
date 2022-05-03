package com.example.a91;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.protobuf.StringValue;

public class AccountActivity extends AppCompatActivity {

    EditText setcalendartext, username;
    CalendarView calendarView;
    Button setcalendar, saveBtn;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PersonalInfo pI;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        username = findViewById(R.id.insertname);
        textView = findViewById(R.id.navheader);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        pI = new PersonalInfo();
        saveBtn = findViewById(R.id.saveData);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(AccountActivity.this, "Please add username", Toast.LENGTH_LONG).show();
                }
                else{
                    addDataToFirebase(name);
                    addTextToNavHeader();
                }
            }
        });

        //image button that takes the user back to MainActivity
        ImageButton backbutton = (ImageButton) findViewById(R.id.backButton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, MovieSearch.class);
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
    // Adding data to firebase realtime database
    private void addDataToFirebase(String username){
        pI.setUsername(username);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(firebaseAuth.getInstance().getCurrentUser().getUid()).setValue(pI);
                Toast.makeText(AccountActivity.this,"Added "+username+"!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountActivity.this, "Adding failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Change username to navigation header
    private void addTextToNavHeader(){

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference textRef = rootRef.child(firebaseAuth.getInstance().getCurrentUser().getUid()).child("username");
        textRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    DataSnapshot snapshot = task.getResult();
                    String text = snapshot.getValue(String.class);
                    System.out.println(text);
                    textView.setText(text);
                } else {
                    Log.d("TAG", task.getException().getMessage());
                }
            }
        });
    }
}
