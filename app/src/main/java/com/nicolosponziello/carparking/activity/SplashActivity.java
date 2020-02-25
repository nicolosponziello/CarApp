package com.nicolosponziello.carparking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.style.WanderingCubes;
import com.google.android.gms.tasks.Task;
import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.database.FirebaseHandler;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.util.DataLoadingCallback;
import com.nicolosponziello.carparking.util.Utils;

public class SplashActivity extends AppCompatActivity {
    private ProgressBar loadingAnimation;
    private LinearLayout animationOverlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        loadingAnimation = findViewById(R.id.spinAnimation);
        animationOverlay = findViewById(R.id.animationOverlay);

        //setup animazione caricamento
        WanderingCubes cubes = new WanderingCubes();
        loadingAnimation.setIndeterminateDrawable(cubes);
        animationOverlay.setVisibility(View.VISIBLE);

        if(Utils.isUserLogged()) {
            //load data and go to main activity
           FirebaseHandler.getInstance(this).getData(new DataLoadingCallback(this));
        }else{
            //go to login
            stopLoadingAnimation();
            Intent intent = new Intent(this, LoginRegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


    public void showLoadingAnimation(){
        this.animationOverlay.setVisibility(View.VISIBLE);
    }
    public void stopLoadingAnimation(){
        this.animationOverlay.setVisibility(View.GONE);
    }
}
