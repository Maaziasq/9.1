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

public class Register_Fragment extends Fragment {
    Button registerRegister;
    EditText registerEmail, registerPassword, registerPassword2, registerName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        registerName = view.findViewById(R.id.rName);
        registerEmail = view.findViewById(R.id.rEmail);
        registerPassword = view.findViewById(R.id.rPassword);
        registerPassword2 = view.findViewById(R.id.rePassword);
        registerRegister = view.findViewById(R.id.r_registerBtn);
        return view;
    }

}
