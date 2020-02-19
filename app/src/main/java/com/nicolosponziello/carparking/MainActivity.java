package com.nicolosponziello.carparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.nicolosponziello.carparking.activity.AboutActivity;
import com.nicolosponziello.carparking.activity.NewParkActivity;
import com.nicolosponziello.carparking.activity.SettingsActivity;
import com.nicolosponziello.carparking.fragments.CurrentParkingFragment;
import com.nicolosponziello.carparking.fragments.NoPosFragment;
import com.nicolosponziello.carparking.intro.IntroActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.notification.ParkingNotification;

public class MainActivity extends AppCompatActivity {

    private FrameLayout posFrame;
    private FrameLayout bottomBar;
    private Toolbar toolbar;
    private FloatingActionButton fabNewButton;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posFrame = findViewById(R.id.current_pos_fragment);
        toolbar = findViewById(R.id.toolbar);
        fabNewButton = findViewById(R.id.newPosition);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        drawerLayout.addDrawerListener(toggle);



        navigationView = findViewById(R.id.lateralMenu);
        navigationView.setNavigationItemSelectedListener( item -> {
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
            return true;
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        boolean introShown = sharedPreferences.getBoolean(getString(R.string.intro_shown), false);
        if(!introShown){
            startActivity(new Intent(this, IntroActivity.class));
        }

        fabNewButton.setOnClickListener((v -> {
            startActivity(new Intent(this, NewParkActivity.class));
        }));


        setupFragments();

        //setup channel for notification
        ParkingNotification.createChannel(this);

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
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
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
            Log.d("Data", "has active");
            fragmentManager.beginTransaction().replace(R.id.current_pos_fragment, new CurrentParkingFragment()).commit();
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.current_pos_fragment, new NoPosFragment()).commit();
        }
    }
}
