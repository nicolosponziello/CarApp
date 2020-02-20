package com.nicolosponziello.carparking.intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.R;

public class IntroActivity extends AppIntro {
    private static final String BG_COLOR = "#010570";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SliderPage page1 = new SliderPage();
        page1.setTitle("Il tuo assistente per gestire i parcheggi!");
        page1.setDescription("Dimentichi dove hai parcheggiato l'auto?\nNon ti ricordi quando scade il parchimetro?");
        page1.setImageDrawable(R.drawable.ic_intro_image_2);
        page1.setBgColor(Color.parseColor(BG_COLOR));

        SliderPage page2 = new SliderPage();
        page2.setTitle("Ci penso io!");
        page2.setDescription("Parcheggia senza pensieri! Ci penserò io a condurti alla tua auto!");
        page2.setImageDrawable(R.drawable.ic_intro_image_1);
        page2.setBgColor(Color.parseColor(BG_COLOR));

        addSlide(AppIntroFragment.newInstance(page1));
        addSlide(AppIntroFragment.newInstance(page2));

        setDepthAnimation();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        setDone();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        setDone();
    }

    private void setDone(){
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        preferences.edit().putBoolean(getString(R.string.intro_shown), true).commit();
        startActivity(new Intent(this, MainActivity.class));
    }
}
