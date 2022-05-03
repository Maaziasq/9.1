package com.example.a91;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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
import androidx.biometric.BiometricManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.biometric.BiometricPrompt;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class Login_Fragment extends Fragment {
    Button loginSignin, Biometric;
    ImageButton backButton;
    EditText loginEmail, loginPassword;
    FirebaseAuth firebaseAuth;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);
        loginEmail = view.findViewById(R.id.lEmail);
        loginPassword = view.findViewById(R.id.lPassword);
        loginSignin = view.findViewById(R.id.l_loginBtn);
        backButton = view.findViewById(R.id.bButton2);
        firebaseAuth = FirebaseAuth.getInstance();
        Biometric = view.findViewById(R.id.authentication);

        /*BiometricManager biometricManager = BiometricManager.from(getActivity());
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, );
                break;
        }*/

        // Handling different results of biometric authentication
        executor = ContextCompat.getMainExecutor(getActivity());
        biometricPrompt = new BiometricPrompt(getActivity(), executor, new BiometricPrompt.AuthenticationCallback(){
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getActivity(),"Authentication error: "+ errString, Toast.LENGTH_LONG).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getActivity(),"Authentication succeed!", Toast.LENGTH_LONG).show();
                Intent goHome = new Intent(getActivity(), MovieSearch.class);
                startActivity(goHome);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_LONG).show();
            }
        });
        //Set prompt infos to biometric authentication
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric authentication")
                .setSubtitle("Login using your fingerprint")
                .setNegativeButtonText("Use email and password")
                .build();

        //Biometric authentication
        Biometric.setOnClickListener(view1 -> {
                    biometricPrompt.authenticate(promptInfo);
                });
        //Back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        //Authentication when pressed
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
                    //signin
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //email or password doesn't match to firebase
                            if(!task.isSuccessful()){
                                Toast.makeText(getActivity(), "Wrong password or e-mail!", Toast.LENGTH_LONG).show();
                            }
                            //email and password match
                            else {
                                Toast.makeText(getActivity(), "Logged in!", Toast.LENGTH_LONG).show();
                                Intent goHome = new Intent(getActivity(), MovieSearch.class);
                                startActivity(goHome);
                            }
                        }
                    });

                };
            }
        });

        return view;
    }
}
