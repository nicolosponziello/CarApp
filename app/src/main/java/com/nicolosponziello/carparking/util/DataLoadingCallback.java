package com.nicolosponziello.carparking.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nicolosponziello.carparking.MainActivity;

public class DataLoadingCallback implements Callback {
    private Context context;

    public DataLoadingCallback(Context context){
        this.context = context;
    }

    @Override
    public void onSuccess() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onError() {
        Log.d("CarParking", "callback error");
    }
}
