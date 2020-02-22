package com.nicolosponziello.carparking.fragments;

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
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.LoginRegistrationActivity;

public class RecoverPasswordFragment extends Fragment {

    private EditText emailInput;
    private Button recoverBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recover_fragment, container, false);

        emailInput = view.findViewById(R.id.recoverEmailInput);
        recoverBtn = view.findViewById(R.id.recoverActionBtn);

        recoverBtn.setClickable(false);

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(emailInput.getText().length() > 0){
                    recoverBtn.setClickable(true);
                }else{
                    recoverBtn.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        emailInput.addTextChangedListener(watcher);

        recoverBtn.setOnClickListener(v -> {
            ((LoginRegistrationActivity)getActivity()).showLoadingAnimation();
            FirebaseAuth.getInstance().sendPasswordResetEmail(emailInput.getText().toString()).addOnCompleteListener(task ->{
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Link inviato per email!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), "Errore, riprovare", Toast.LENGTH_LONG).show();
                }
                ((LoginRegistrationActivity)getActivity()).stopLoadingAnimation();
            });
        });
        return view;
    }
}
