package com.nicolosponziello.carparking.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.fragments.NewParkFragment;

/**
 * activity che permette di salvare una nuova posizione del parcheggio
 */
public class NewParkActivity extends AppCompatActivity {

    public static final String BUNDLE_LAT = "LAT";
    public static final String BUNDLE_LONG = "LONG";

    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_park_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //controlla che la localizzazione sia attiva nel telefono
        if(isLocationEnabled()){
            //ottieni l'ultima posizione utile
            Task<Location> locationTask = locationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(this, new OnSuccessListener<Location>() {

                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        //posizione recente ottenuta con successo
                        Bundle bundle = new Bundle();
                        bundle.putString(BUNDLE_LAT, String.valueOf(location.getLatitude()));
                        bundle.putString(BUNDLE_LONG, String.valueOf(location.getLongitude()));
                        Fragment newParkFragment = new NewParkFragment();
                        newParkFragment.setArguments(bundle);
                        fragmentManager.beginTransaction().add(R.id.frame, newParkFragment).commit();
                    }else{
                        requestNewLocation();
                    }
                }
            });
        }else{
            //apri le impostazioni per attivare la localizzazione
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            finish();
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * crea una nuova richiesta di localizzazione ed eseguila
     */
    private void requestNewLocation(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setNumUpdates(1);
        locationRequest.setFastestInterval(0);

        //un solo update
        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    /**
     * callback eseguita quando la richiesta di localizzazione termina
     */
    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location location = locationResult.getLastLocation();
            Bundle bundle = new Bundle();
            bundle.putString(BUNDLE_LAT, String.valueOf(location.getLatitude()));
            bundle.putString(BUNDLE_LONG, String.valueOf(location.getLongitude()));
            Fragment newParkFragment = new NewParkFragment();
            newParkFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().add(R.id.frame, newParkFragment).commit();
        }
    };
}
