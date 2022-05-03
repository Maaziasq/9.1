package com.example.a91;

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

import com.google.firebase.auth.FirebaseAuth;

import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register_Fragment extends Fragment {
    Button registerRegister;
    ImageButton backButton;
    EditText registerEmail, registerPassword, registerPassword2,registerName;
    private FirebaseAuth rAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.register_fragment, container, false);
        registerEmail = view.findViewById(R.id.rEmail);
        registerPassword = view.findViewById(R.id.rPassword);
        registerPassword2 = view.findViewById(R.id.rePassword);
        registerRegister = view.findViewById(R.id.r_registerBtn);
        backButton = view.findViewById(R.id.bButton);

        rAuth = FirebaseAuth.getInstance();
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        registerRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String eMail = registerEmail.getText().toString();
                String pWd = registerPassword.getText().toString();
                String pWd2 = registerPassword2.getText().toString();

                if (eMail.isEmpty()) {
                    registerEmail.setError("Email is required!");
                    return;
                } else if (pWd.isEmpty()) {
                    registerPassword.setError("Insert password!");
                } else if (eMail.isEmpty() && pWd.isEmpty()) {
                    Toast.makeText(getActivity(), "Fields are empty", Toast.LENGTH_SHORT).show();
                } else if (!pWd.equals(pWd2)) {
                    registerPassword.setError("Password doesn't match");
                    registerPassword2.setError("Password doesn't match");
                } else if (pWd.equals(pWd2)) {
                    boolean test;
                    test = passwordCheck(pWd);
                    if (!test) {
                        registerPassword.setError("Password must be at least 12 characters long, contain upper and lowercase letters, numbers and special characters");
                    }
                }
                if (!(eMail.isEmpty() && pWd.isEmpty() && pWd.equals(pWd2) && passwordCheck(pWd))){
                    rAuth.createUserWithEmailAndPassword(eMail,pWd).addOnCompleteListener(getActivity(), task -> {
                        if (!task.isSuccessful()){
                            Toast.makeText(getActivity(),"Register failed", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getActivity(),"Registration was successful",Toast.LENGTH_SHORT).show();
                            System.out.println(pWd);
                            System.out.println("Onnistui!!");
                            getFragmentManager().popBackStack();
                        }
                    });
                }
            }});
        return view;
    }

    //Checking password
    private boolean passwordCheck(String password) {
        boolean check = false;
        boolean length = false;
        boolean special = false;
        boolean upperCase =false;
        boolean lowerCase =false;
        boolean number =false;


        if (password.length() >= 12){
            length = true;
        }
        if (password.contains(" ")){
            check = false;
        }
        char[] pwArray = password.toCharArray();
        for (char chr: pwArray){
            if (Character.isDigit(chr)){
                number = true;
            }
            if (Character.isLowerCase(chr)){
                lowerCase = true;
            }
            if (Character.isUpperCase(chr)){
                upperCase = true;
            }
        }
        Pattern pattern = Pattern.compile("[^a-z0-9]",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(password);
        if (matcher.find()){
            special = true;
        }
        if (length && lowerCase && upperCase && special && number){
            check = true;
        }
        System.out.println(check);
        return check;
    }
}
