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
import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.R;

public class LoginFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
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


        firebaseAuth = FirebaseAuth.getInstance();

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
            String email = mailInput.getText().toString();
            String pass = passInput.getText().toString();
            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    //l'utente è stato registrato con successo
                    Toast.makeText(getActivity(), "Login eseguito con successo!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    Toast.makeText(getActivity(), "Errore durante il login. Riprovare", Toast.LENGTH_SHORT).show();
                }
            });
        });

        return view;
    }

}
