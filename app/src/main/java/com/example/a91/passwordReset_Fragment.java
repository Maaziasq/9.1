package com.example.a91;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class passwordReset_Fragment extends Fragment {
    Button reset;
    EditText email;
    ImageButton back;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.passwordreset_fragment, container, false);
        back = v.findViewById(R.id.backBtn);
        reset = v.findViewById(R.id.resetBtn);
        email = v.findViewById(R.id.resetEmail);

        //Back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Tähän asti toimii");
                getFragmentManager().popBackStack();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString();
                if (userEmail.isEmpty()){
                    Toast.makeText(getActivity(),"Please write valid email.", Toast.LENGTH_LONG).show();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity(),"Please check your inbox", Toast.LENGTH_LONG).show();
                                getFragmentManager().popBackStack();
                            }
                        }
                    });
                }
            }
        });
        return v;
    }
}
