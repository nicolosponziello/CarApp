package com.nicolosponziello.carparking.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.fragments.RegistrationFragment;

public class LoginRegistrationActivity extends AppCompatActivity {

    private Button changeLoginReg;
    private FrameLayout fragmentHost;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_registration_activity);

        changeLoginReg = findViewById(R.id.changeLogReg);
        fragmentHost = findViewById(R.id.fragmentHost);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.fragmentHost, new RegistrationFragment()).commit();

        changeLoginReg.setOnClickListener(v -> {

        });

    }


}
