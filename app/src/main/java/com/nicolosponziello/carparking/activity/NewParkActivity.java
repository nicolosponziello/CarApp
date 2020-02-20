package com.nicolosponziello.carparking.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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
import com.nicolosponziello.carparking.model.ParkManager;

public class NewParkActivity extends AppCompatActivity {

    public static final String BUNDLE_LAT = "LAT";
    public static final String BUNDLE_LONG = "LONG";

    private static final int EXTERNAL_STORAGE_PERMISSION = 2;

    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_park_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        if(isLocationEnabled()){
            Task<Location> locationTask = locationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(this, new OnSuccessListener<Location>() {

                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        Log.d("CarParking", String.valueOf(location.getLatitude() + " " + location.getLongitude()));
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
            //ask user to enable location in settings
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void requestNewLocation(){
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(0);
        locationRequest.setNumUpdates(1);
        locationRequest.setFastestInterval(0);

        locationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

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
