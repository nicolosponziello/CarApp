package com.nicolosponziello.carparking.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.fragments.LoginFragment;
import com.nicolosponziello.carparking.fragments.RegistrationFragment;

public class LoginRegistrationActivity extends AppCompatActivity {

    private Button changeLoginReg;
    private FrameLayout fragmentHost;

    private boolean registration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_registration_activity);

        changeLoginReg = findViewById(R.id.changeLogReg);
        fragmentHost = findViewById(R.id.fragmentHost);

        registration = true;
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragmentHost, new RegistrationFragment()).commit();

        changeLoginReg.setOnClickListener(v -> {
            if(registration){
                //show login
                registration = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHost, new LoginFragment()).commit();
            }else{
                //show registration
                registration = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHost, new RegistrationFragment()).commit();
            }
        });

    }


}
