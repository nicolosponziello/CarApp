package com.nicolosponziello.carparking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.FrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.nicolosponziello.carparking.activity.AboutActivity;
import com.nicolosponziello.carparking.activity.HistoryActivity;
import com.nicolosponziello.carparking.activity.LoginRegistrationActivity;
import com.nicolosponziello.carparking.activity.NewParkActivity;
import com.nicolosponziello.carparking.activity.SettingsActivity;
import com.nicolosponziello.carparking.fragments.CurrentParkingFragment;
import com.nicolosponziello.carparking.fragments.NoPosFragment;
import com.nicolosponziello.carparking.intro.IntroActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.notification.NotifManager;
import com.nicolosponziello.carparking.notification.ParkingNotification;

public class MainActivity extends AppCompatActivity {

    private static final int FINE_LOCATION_PERMISSION = 1;

    private FrameLayout posFrame;
    private FrameLayout bottomBar;
    private Toolbar toolbar;
    private FloatingActionButton fabNewButton, fabDoneButton;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private Button logoutBtn;
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Intent intent = new Intent(this, LoginRegistrationActivity.class);
            startActivity(intent);
            finish();
        }
        posFrame = findViewById(R.id.current_pos_fragment);
        toolbar = findViewById(R.id.toolbar);
        fabNewButton = findViewById(R.id.newPosition);
        fabDoneButton = findViewById(R.id.doneBtn);
        drawerLayout = findViewById(R.id.drawer_layout);
        logoutBtn = findViewById(R.id.logoutDrawerBtn);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //hamburgher setup
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

        drawerLayout.addDrawerListener(toggle);

        logoutBtn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginRegistrationActivity.class);
            startActivity(intent);
            finish();
        });

        navigationView = findViewById(R.id.lateralMenu);
        //imposta le callback per il menu del drawer
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
        //se l'intro non è mai stata mostrata (es. primo avvio) mostra l'activity
        if(!introShown){
            startActivity(new Intent(this, IntroActivity.class));
        }

        fabNewButton.setOnClickListener((v -> {
            //se l'app non ha i permessi di localizzazione, chiedili prima di attivate l'intent
            if(!hasLocationPermissions()){
                requesLocationPermissions();
            }else{
                startActivity(new Intent(this, NewParkActivity.class));
            }
        }));

        fabDoneButton.setOnClickListener(v -> {
            ParkManager.getInstance(this).setDoneParking();
            setupView();
            NotifManager.getInstance().stopAlarm();
        });

        //setup view
        setupView();

        //setup channel per notifiche
        ParkingNotification.createChannel(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //aggiorna la view
        setupView();
    }

    /**
     * chimata quando la richiesta dei permessi è completata
     * @param requestCode identifica la richiesta
     * @param permissions permessi
     * @param grantResults risultati per ogni permesso
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //we have the best permission for localization
                startActivity(new Intent(this, NewParkActivity.class));
            }
        }
    }

    /**
     * Imposta la schermata in base ai dati di ParkManager
     */
    public void setupView(){
        if(ParkManager.getInstance(this).hasActiveParking()){
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.current_pos_fragment, new CurrentParkingFragment()).commit();
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.current_pos_fragment, new NoPosFragment()).commit();
        }
        //hide fab button if a parking is active
        if(ParkManager.getInstance(this).hasActiveParking()){
            fabNewButton.hide();
            fabDoneButton.show();
        }else{
            fabNewButton.show();
            fabDoneButton.hide();
        }
    }

    /**
     * controlla che l'utente abbia dato i permessi di localizzazione all'applicazione
     * @return true se concessi, false altrimenti
     */
    private boolean hasLocationPermissions(){
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * richiede il permesso per la localizzazione FINE (più precisa di COARSE)
     */
    private void requesLocationPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
    }
}
