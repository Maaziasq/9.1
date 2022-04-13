package com.example.a91;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Login_Fragment extends Fragment {
    Button loginSignin;
    EditText loginEmail, loginPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        loginEmail = view.findViewById(R.id.lEmail);
        loginPassword = view.findViewById(R.id.lPassword);
        loginSignin = view.findViewById(R.id.l_loginBtn);
        return view;
    }
}
