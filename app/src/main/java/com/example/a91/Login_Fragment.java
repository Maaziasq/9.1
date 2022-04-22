package com.example.a91;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.NoSuchAlgorithmException;

public class Login_Fragment extends Fragment {
    Button loginSignin;
    ImageButton backButton;
    EditText loginEmail, loginPassword;
    FirebaseAuth firebaseAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        loginEmail = view.findViewById(R.id.lEmail);
        loginPassword = view.findViewById(R.id.lPassword);
        loginSignin = view.findViewById(R.id.l_loginBtn);
        backButton = view.findViewById(R.id.bButton2);
        Hash hash = new Hash();
        firebaseAuth = FirebaseAuth.getInstance();

        //Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        //Authentication when presed
        loginSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //First we check that fields are filled
                String email = loginEmail.getText().toString();
                String password = loginPassword.getText().toString();
                if (email.isEmpty()){
                    loginEmail.setError("Please, insert email");
                    loginEmail.requestFocus();
                }
                else if (password.isEmpty()) {
                    loginPassword.setError("Insert password!");
                    loginPassword.requestFocus();
                }
                else if (!password.isEmpty() && !email.isEmpty()) {

                    try {
                        String salt = Hash.getSalt();
                        String securepasswrd = Hash.securePassword(password);

                        firebaseAuth.signInWithEmailAndPassword(email, securepasswrd).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                System.out.println(securepasswrd);
                                if(!task.isSuccessful()){
                                    Toast.makeText(getActivity(), "Wrong password or e-mail!", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    Intent goHome = new Intent(getActivity(), MainActivity.class);
                                    startActivity(goHome);
                                }
                            }
                        });
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    };
            }
        });

        return view;
    }
}
