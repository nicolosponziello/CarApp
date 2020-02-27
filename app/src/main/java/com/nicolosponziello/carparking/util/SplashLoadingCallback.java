package com.nicolosponziello.carparking.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.activity.SplashActivity;

public class SplashLoadingCallback implements Callback {
    private Context context;

    public SplashLoadingCallback(Context context){
        this.context = context;
    }
    @Override
    public void onSuccess() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        if(context instanceof SplashActivity) {
            ((SplashActivity)context).finish();
            ((SplashActivity)context).finishAffinity();
        }
    }

    @Override
    public void onError() {
        Log.d("CarParking", "SplashLoadingCallaback error");
    }
}
