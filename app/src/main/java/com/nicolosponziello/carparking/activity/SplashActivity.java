package com.nicolosponziello.carparking.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.nicolosponziello.carparking.intro.IntroActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.util.DataLoadingCallback;
import com.nicolosponziello.carparking.util.SplashLoadingCallback;
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

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        boolean introShown = sharedPreferences.getBoolean(getString(R.string.intro_shown), false);
        //se l'intro non è mai stata mostrata (es. primo avvio) mostra l'activity
        if(!introShown){
            Intent intent = new Intent(this, IntroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
            finishAffinity();
        }else {

            if (Utils.isUserLogged()) {
                //load data and go to main activity
                FirebaseHandler.getInstance(this).getData(new SplashLoadingCallback(this));
                Log.d("CarParking", "already logged");

            } else {
                //go to login
                Intent intent = new Intent(this, LoginRegistrationActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                startActivity(intent);
                finishAffinity();
            }
        }
    }


    public void showLoadingAnimation(){
        this.animationOverlay.setVisibility(View.VISIBLE);
    }
    public void stopLoadingAnimation(){
        this.animationOverlay.setVisibility(View.GONE);
    }
}
