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

    private static final int FINE_LOCATION_PERMISSION = 1;
    private static final int EXTERNAL_STORAGE_PERMISSION = 2;

    private FusedLocationProviderClient locationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_park_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        /*
        check if app has permission for location services.
        if not, ask user for them
         */
        if(!hasPermissions()){
            if(!hasLocationPermissions()){
                requesLocationPermissions();
            }
            if(!hasStoragePermission()){
                requestStoragePermission();
            }
        }else{
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //we have the best permission for localization
                Log.d(getClass().getSimpleName(), "Location permissions granted");
            }else{
                finish();
            }
        }
        if(requestCode == EXTERNAL_STORAGE_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d(getClass().getSimpleName(), "External Storage permission");
            }else{
                finish();
            }
        }
    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean hasLocationPermissions(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            return true;
        }
        return false;
    }

    private boolean hasStoragePermission(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }

    private void requesLocationPermissions(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_PERMISSION);
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION);
    }

    private boolean hasPermissions(){
        return hasLocationPermissions() && hasStoragePermission();
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
