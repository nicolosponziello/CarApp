package com.nicolosponziello.carparking.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.fragments.LoginFragment;
import com.nicolosponziello.carparking.fragments.RecoverPasswordFragment;
import com.nicolosponziello.carparking.fragments.RegistrationFragment;
import com.nicolosponziello.carparking.intro.IntroActivity;
import com.nicolosponziello.carparking.util.Const;

public class LoginRegistrationActivity extends AppCompatActivity {

    private Button changeLoginReg, recoverBtn;
    private FrameLayout fragmentHost;
    private ProgressBar loadingAnimation;
    private LinearLayout animationOverlay;

    private boolean registration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_registration_activity);

        changeLoginReg = findViewById(R.id.changeLogReg);
        fragmentHost = findViewById(R.id.fragmentHost);
        recoverBtn = findViewById(R.id.recoverBtn);
        loadingAnimation = findViewById(R.id.spinAnimation);
        animationOverlay = findViewById(R.id.animationOverlay);

        //setup animazione caricamento
        WanderingCubes cubes = new WanderingCubes();
        loadingAnimation.setIndeterminateDrawable(cubes);
        animationOverlay.setVisibility(View.GONE);


        registration = true;
        FragmentManager manager = getSupportFragmentManager();
        Mode mode = (Mode) getIntent().getSerializableExtra(Const.LOGIN_REG_REC_MODE);
        if(mode != null) {
            switch (mode) {
                case LOGIN:
                    manager.beginTransaction().add(R.id.fragmentHost, new LoginFragment()).commit();
                    changeLoginReg.setText(R.string.login_to_reg);
                    break;
                case RECOVER:
                    manager.beginTransaction().add(R.id.fragmentHost, new RecoverPasswordFragment()).commit();
                    break;
                case REGISTER:
                    manager.beginTransaction().add(R.id.fragmentHost, new RegistrationFragment()).commit();
                    changeLoginReg.setText(R.string.reg_to_login);
                    break;
                default:
                    manager.beginTransaction().add(R.id.fragmentHost, new RegistrationFragment()).commit();
            }
        }else{
            manager.beginTransaction().add(R.id.fragmentHost, new RegistrationFragment()).commit();
        }

        changeLoginReg.setOnClickListener(v -> {
            if(registration){
                //show login
                registration = false;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHost, new LoginFragment()).commit();
                changeLoginReg.setText(R.string.login_to_reg);
            }else{
                //show registration
                registration = true;
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHost, new RegistrationFragment()).commit();
                changeLoginReg.setText(R.string.reg_to_login);
            }
        });

        recoverBtn.setOnClickListener(v -> {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentHost, new RecoverPasswordFragment()).commit();
        });

    }

    public static enum Mode {
        LOGIN,
        REGISTER,
        RECOVER
    }

    public void showLoadingAnimation(){
        this.animationOverlay.setVisibility(View.VISIBLE);
    }
    public void stopLoadingAnimation(){
        this.animationOverlay.setVisibility(View.GONE);
    }


}
