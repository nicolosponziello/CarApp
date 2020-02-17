package com.nicolosponziello.carparking.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.fragments.NewParkFragment;

public class NewParkActivity extends AppCompatActivity {

    public static final String BUNDLE_LAT = "LAT";
    public static final String BUNDLE_LONG = "LONG";

    private static final int INTERNET_PERMISSION = 0;
    private static final int FINE_LOCATION_PERMISSION = 1;
    private static final int COARSE_LOCATION_PERMISSION = 2;

    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_park_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();


        /*
        check if app has permission for location services.
        if not, ask user for them
         */
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            Log.e("CarParking", "INTERNET permission not granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, INTERNET_PERMISSION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("CarParking", "COARSE LOCATION permission not granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_PERMISSION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("CarParking", "FINE LOCATION permission not granted");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
                    Log.d("CarParking", "Location null");
                }
            }
        });
        locationTask.addOnFailureListener(this, (location) -> {
            Log.d("CarParking", "location failed");
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
