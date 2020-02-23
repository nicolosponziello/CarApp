package com.nicolosponziello.carparking.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.nicolosponziello.carparking.R;

public class ChangePasswordDialog extends Dialog {

    private Context context;
    private EditText pass1, pass2;
    private Button confirm, cancel;

    public ChangePasswordDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_dialog);

        pass1 = findViewById(R.id.pass1);
        pass2 = findViewById(R.id.pass2);
        confirm = findViewById(R.id.confirmDialog);
        cancel = findViewById(R.id.cancelDialog);

        confirm.setEnabled(false);

        cancel.setOnClickListener(v -> {
            dismiss();
        });

        pass2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //controlla che le due password siano uguali
                if(pass1.getText().toString().equals(pass2.getText().toString())){
                    confirm.setEnabled(true);
                }else{
                    confirm.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirm.setOnClickListener(v -> {
            FirebaseAuth.getInstance().getCurrentUser().updatePassword(pass2.getText().toString()).addOnCompleteListener( task -> {
                if(task.isSuccessful()){
                    dismiss();
                    Toast.makeText(this.context, "Password aggiornata con successo!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this.context, "Impossibile aggiornare la password, riprovare", Toast.LENGTH_LONG).show();
                }
            });
        });

    }
}
