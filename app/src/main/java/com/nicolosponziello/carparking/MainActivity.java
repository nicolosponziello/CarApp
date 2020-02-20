package com.nicolosponziello.carparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.nicolosponziello.carparking.activity.AboutActivity;
import com.nicolosponziello.carparking.activity.HistoryActivity;
import com.nicolosponziello.carparking.activity.NewParkActivity;
import com.nicolosponziello.carparking.activity.SettingsActivity;
import com.nicolosponziello.carparking.fragments.CurrentParkingFragment;
import com.nicolosponziello.carparking.fragments.NoPosFragment;
import com.nicolosponziello.carparking.intro.IntroActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.notification.ParkingNotification;

public class MainActivity extends AppCompatActivity {

    private static final int FINE_LOCATION_PERMISSION = 1;

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
            drawerLayout.closeDrawers();
            switch (item.getItemId()){
                case R.id.homeItem:
                    break;
                case R.id.settingsItem:
                    startActivity(new Intent(this, SettingsActivity.class));
                    break;
                case R.id.historyItem:
                    startActivity(new Intent(this, HistoryActivity.class));
                    break;
                case R.id.aboutItem:
                    startActivity(new Intent(this, AboutActivity.class));
                    break;
            }
            return true;
        });

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        boolean introShown = sharedPreferences.getBoolean(getString(R.string.intro_shown), false);
        if(!introShown){
            startActivity(new Intent(this, IntroActivity.class));
        }

        fabNewButton.setOnClickListener((v -> {
            if(!hasLocationPermissions()){
                requesLocationPermissions();
            }else{
                startActivity(new Intent(this, NewParkActivity.class));
            }
        }));


        setupView();

        //setup channel for notification
        ParkingNotification.createChannel(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupView();
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

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //we have the best permission for localization
                startActivity(new Intent(this, NewParkActivity.class));
            }
        }
    }

    public void setupView(){
        if(ParkManager.getInstance(this).hasActiveParking()){
            FragmentManager fragmentManager = getSupportFragmentManager();
            Log.d("Data", "has active");
            fragmentManager.beginTransaction().replace(R.id.current_pos_fragment, new CurrentParkingFragment()).commit();
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.current_pos_fragment, new NoPosFragment()).commit();
        }
        if(ParkManager.getInstance(this).hasActiveParking()){
            fabNewButton.hide();
        }else{
            fabNewButton.show();
        }
    }

    private boolean hasLocationPermissions(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            return true;
        }
        return false;
    }

    private void requesLocationPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
    }
}
