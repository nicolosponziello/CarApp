package com.nicolosponziello.carparking.intro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.google.firebase.auth.FirebaseAuth;
import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.LoginRegistrationActivity;

/**
 * intro dell'applicazione creata con la libreria AppIntro
 */
public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //prima pagina
        SliderPage page1 = new SliderPage();
        page1.setTitle("Il tuo assistente per gestire i parcheggi!");
        page1.setDescription("Dimentichi dove hai parcheggiato l'auto?\nNon ti ricordi quando scade il parchimetro?");
        page1.setImageDrawable(R.drawable.ic_intro_image_2);
        page1.setBgColor(getColor(R.color.colorPrimaryDark));

        //seconda pagina
        SliderPage page2 = new SliderPage();
        page2.setTitle("Ci penso io!");
        page2.setDescription("Parcheggia senza pensieri! Ci penserò io a condurti alla tua auto!");
        page2.setImageDrawable(R.drawable.ic_intro_image_1);
        page2.setBgColor(getColor(R.color.colorPrimaryDark));

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

    /**
     * scrivi sulle shared preferences che l'intro è stata mostrata e avvia l'activity principale
     */
    private void setDone(){
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        preferences.edit().putBoolean(getString(R.string.intro_shown), true).commit();
        //se dopo l'intro l'utente non è loggato portalo all'activity di login
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginRegistrationActivity.class);
            startActivity(intent);
            finish();
        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
