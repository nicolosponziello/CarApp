package com.nicolosponziello.carparking.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.LoginRegistrationActivity;
import com.nicolosponziello.carparking.database.FirebaseHandler;
public class LoginFragment extends Fragment {

    private Button loginBtn;
    private EditText mailInput, passInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        loginBtn = view.findViewById(R.id.loginBtn);
        mailInput = view.findViewById(R.id.loginEmailInput);
        passInput = view.findViewById(R.id.passwordLoginInput);

        loginBtn.setClickable(false);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mailInput.getText().length() > 0 && passInput.getText().length() > 0 ){
                    loginBtn.setClickable(true);
                }else{
                    loginBtn.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        mailInput.addTextChangedListener(textWatcher);
        passInput.addTextChangedListener(textWatcher);

        loginBtn.setOnClickListener( v -> {
            ((LoginRegistrationActivity)getActivity()).showLoadingAnimation();
            String email = mailInput.getText().toString();
            String pass = passInput.getText().toString();

            FirebaseHandler.getInstance(getActivity()).login(email, pass, ((LoginRegistrationActivity)getActivity()));

        });

        return view;
    }

}
