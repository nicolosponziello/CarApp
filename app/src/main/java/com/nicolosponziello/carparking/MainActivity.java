package com.nicolosponziello.carparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nicolosponziello.carparking.activity.AboutActivity;
import com.nicolosponziello.carparking.activity.NewParkActivity;
import com.nicolosponziello.carparking.activity.SettingsActivity;
import com.nicolosponziello.carparking.fragments.CurrentParkingFragment;
import com.nicolosponziello.carparking.fragments.NoPosFragment;
import com.nicolosponziello.carparking.intro.IntroActivity;
import com.nicolosponziello.carparking.model.ParkManager;

public class MainActivity extends AppCompatActivity {

    private FrameLayout posFrame;
    private FrameLayout bottomBar;
    private Toolbar toolbar;
    private FloatingActionButton fabNewButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posFrame = findViewById(R.id.current_pos_fragment);
        bottomBar = findViewById(R.id.bottom_bar_frag);
        toolbar = findViewById(R.id.toolbar);
        fabNewButton = findViewById(R.id.newPosition);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        boolean introShown = sharedPreferences.getBoolean(getString(R.string.intro_shown), false);
        if(!introShown){
            startActivity(new Intent(this, IntroActivity.class));
        }

        fabNewButton.setOnClickListener((v -> {
            startActivity(new Intent(this, NewParkActivity.class));
        }));

        setSupportActionBar(toolbar);

        setupFragments();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupFragments(){
        if(ParkManager.getInstance(this).hasActiveParking()){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.current_pos_fragment, new CurrentParkingFragment()).commit();
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.current_pos_fragment, new NoPosFragment()).commit();
        }
    }
}
