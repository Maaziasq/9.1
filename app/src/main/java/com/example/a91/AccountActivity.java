package com.example.a91;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AccountActivity extends AppCompatActivity {

    EditText username;
    Button saveBtn;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    PersonalInfo pI;
    TextView textView;


    @RequiresApi(api = Build.VERSION_CODES.O)
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

        saveBtn.setOnClickListener(view -> {
            String name = username.getText().toString();
            if (TextUtils.isEmpty(name)){
                Toast.makeText(AccountActivity.this, "Please add username", Toast.LENGTH_LONG).show();
            }
            else{
                addDataToFirebase(name);
            }
        });

        //image button that takes the user back to MainActivity
        ImageButton backbutton = findViewById(R.id.backButton);
        backbutton.setOnClickListener(view -> {
            Intent intent = new Intent(AccountActivity.this, MovieSearch.class);
            startActivity(intent);
        });

    }
    // Adding data to firebase realtime database
    private void addDataToFirebase(String username){
        pI.setUsername(username);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(pI);
                Toast.makeText(AccountActivity.this,"Added "+username+"!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountActivity.this, "Adding failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
