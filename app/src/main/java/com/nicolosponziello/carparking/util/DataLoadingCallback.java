package com.nicolosponziello.carparking.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.activity.LoginRegistrationActivity;

public class DataLoadingCallback implements Callback {
    private Context context;

    public DataLoadingCallback(Context context){
        this.context = context;
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if(context instanceof LoginRegistrationActivity){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ((LoginRegistrationActivity) context).finish();
            //((LoginRegistrationActivity) context).stopLoadingAnimation();
            ((LoginRegistrationActivity) context).finishAffinity();

        }
        context.startActivity(intent);


    }

    @Override
    public void onError() {
        Log.d("CarParking", "callback error");
    }
}
