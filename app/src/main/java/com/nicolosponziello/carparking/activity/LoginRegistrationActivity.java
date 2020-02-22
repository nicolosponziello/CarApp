package com.nicolosponziello.carparking.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.fragments.LoginFragment;
import com.nicolosponziello.carparking.fragments.RecoverPasswordFragment;
import com.nicolosponziello.carparking.fragments.RegistrationFragment;
import com.nicolosponziello.carparking.util.Const;

public class LoginRegistrationActivity extends AppCompatActivity {

    private Button changeLoginReg, recoverBtn;
    private FrameLayout fragmentHost;

    private boolean registration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_registration_activity);

        changeLoginReg = findViewById(R.id.changeLogReg);
        fragmentHost = findViewById(R.id.fragmentHost);
        recoverBtn = findViewById(R.id.recoverBtn);

        registration = true;
        FragmentManager manager = getSupportFragmentManager();
        Mode mode = (Mode) getIntent().getSerializableExtra(Const.LOGIN_REG_REC_MODE);
        if(mode != null) {
            switch (mode) {
                case LOGIN:
                    manager.beginTransaction().add(R.id.fragmentHost, new LoginFragment()).commit();
                    break;
                case RECOVER:
                    manager.beginTransaction().add(R.id.fragmentHost, new RecoverPasswordFragment()).commit();
                    break;
                case REGISTER:
                    manager.beginTransaction().add(R.id.fragmentHost, new RegistrationFragment()).commit();
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


}
