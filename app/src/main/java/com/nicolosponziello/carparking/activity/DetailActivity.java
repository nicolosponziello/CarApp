package com.nicolosponziello.carparking.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.model.ParkingData;
import com.nicolosponziello.carparking.util.Const;
import com.nicolosponziello.carparking.util.Utils;

import java.util.Date;
import java.util.UUID;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private TextView cityLabel, dateLabel, coordLabel, addrLabel,
        spotLabel, levelLabel, noteLabel, costLabel, expLabel;


    private ParkingData parkingData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        cityLabel = findViewById(R.id.cityValue);
        dateLabel = findViewById(R.id.dateValue);
        coordLabel = findViewById(R.id.coordinateValue);
        addrLabel = findViewById(R.id.addressValue);
        spotLabel = findViewById(R.id.placeValue);
        levelLabel = findViewById(R.id.levelValue);
        expLabel = findViewById(R.id.expValue);
        noteLabel = findViewById(R.id.noteValue);
        costLabel = findViewById(R.id.costValue);

        Bundle extras = getIntent().getExtras();
        String id = extras.getString(Const.DETAIL_EXTRA);

        parkingData = ParkManager.getInstance(this).getParkingData(UUID.fromString(id));

        if(parkingData.getCity() != null){
            cityLabel.setText(parkingData.getCity());
        }else{
            cityLabel.setVisibility(View.GONE);
            findViewById(R.id.cityText).setVisibility(View.GONE);
        }

        if(parkingData.getDate() != null){
            dateLabel.setText(Utils.formatDate(parkingData.getDate()));
        }else{
            dateLabel.setVisibility(View.GONE);
            findViewById(R.id.dateText).setVisibility(View.GONE);
        }

        if(parkingData.getLatitude() != null && parkingData.getLongitude() != null){
            coordLabel.setText(parkingData.getLatitude() + " - " + parkingData.getLongitude());
        }else{
            coordLabel.setVisibility(View.GONE);
            findViewById(R.id.coordText).setVisibility(View.GONE);
        }

        if(parkingData.getAddress() != null){
            addrLabel.setText(parkingData.getAddress());
        }else{
            addrLabel.setVisibility(View.GONE);
            findViewById(R.id.addressText).setVisibility(View.GONE);
        }

        if(parkingData.getParkSpot() != null){
            spotLabel.setText(parkingData.getParkSpot());
        }else{
            spotLabel.setVisibility(View.GONE);
            findViewById(R.id.placeText).setVisibility(View.GONE);
        }

        if(parkingData.getParkLevel() != null){
            levelLabel.setText(parkingData.getParkLevel());
        }else{
            levelLabel.setVisibility(View.GONE);
            findViewById(R.id.levelText).setVisibility(View.GONE);
        }

        if(parkingData.getExpiration() > 0){
            Date date = new Date(parkingData.getExpiration());
            expLabel.setText(Utils.formatDate(date));
        }else{
            expLabel.setVisibility(View.GONE);
            findViewById(R.id.expText).setVisibility(View.GONE);
        }
        if(parkingData.getNote() != null){
            noteLabel.setText(parkingData.getNote());
        }else{
            noteLabel.setVisibility(View.GONE);
            findViewById(R.id.noteText).setVisibility(View.GONE);
        }
        if(parkingData.getCost() > 0){
            costLabel.setText(String.valueOf(parkingData.getCost()));
        }else{
            costLabel.setVisibility(View.GONE);
            findViewById(R.id.costText).setVisibility(View.GONE);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lat = Double.valueOf(parkingData.getLatitude());
        double lon = Double.valueOf(parkingData.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 10));
    }
}
