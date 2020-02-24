package com.nicolosponziello.carparking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.LoginRegistrationActivity;
import com.nicolosponziello.carparking.database.FirebaseHandler;

public class RegistrationFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private Button regBtn;
    private EditText mailInput, passInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, container, false);

        regBtn = view.findViewById(R.id.regBtn);
        mailInput = view.findViewById(R.id.regEmailInput);
        passInput = view.findViewById(R.id.regPassInput);

        regBtn.setClickable(false);


        firebaseAuth = FirebaseAuth.getInstance();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mailInput.getText().length() > 0 && passInput.getText().length() > 0 ){
                    regBtn.setClickable(true);
                }else{
                    regBtn.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        mailInput.addTextChangedListener(textWatcher);
        passInput.addTextChangedListener(textWatcher);

        regBtn.setOnClickListener( v -> {
            ((LoginRegistrationActivity)getActivity()).showLoadingAnimation();
            String email = mailInput.getText().toString();
            String pass = passInput.getText().toString();
            FirebaseHandler.getInstance(getActivity()).register(email, pass, ((LoginRegistrationActivity) getActivity()));
        });

        return view;
    }
}
