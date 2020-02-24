package com.nicolosponziello.carparking.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.nicolosponziello.carparking.fragments.NewParkFragment.PHOTO_EXTRA;

/**
 * activity che gestisce la visualizzazione dei dettagli del parking
 */
public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int FINE_LOCATION_PERMISSION = 1;

    private TextView cityLabel, dateLabel, coordLabel, addrLabel,
        spotLabel, levelLabel, noteLabel, costLabel, expLabel, photoLabel;

    private List<ImageView> photoViewList;

    private ImageButton closeBtn;
    private ImageButton deleteBtn;
    private LinearLayout imagesLayout;

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
        closeBtn = findViewById(R.id.closeBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        photoLabel = findViewById(R.id.photoDetailLabel);
        photoViewList = new ArrayList<>();
        imagesLayout = findViewById(R.id.imagesDetail);

        if(!hasLocationPermissions()){
            requesLocationPermissions();
        }else{
            setupView();
        }


    }

    private void setupView(){
        closeBtn.setOnClickListener(v -> finish());

        Bundle extras = getIntent().getExtras();
        String id = extras.getString(Const.DETAIL_EXTRA);

        parkingData = ParkManager.getInstance(this).getParkingData(id);

        if(parkingData.getCity() != null){
            cityLabel.setText(parkingData.getCity());
        }else{
            cityLabel.setVisibility(View.GONE);
            findViewById(R.id.cityText).setVisibility(View.GONE);
        }

        if(parkingData.getDate() != 0){
            Date date = new Date();
            date.setTime(parkingData.getDate());
            dateLabel.setText(Utils.formatDate(date));
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

        if(parkingData.getPhotoPath() == null || parkingData.getPhotoPath().size() == 0){
            Log.d("Photo", "error");
            photoLabel.setVisibility(View.GONE);

        }else{
            Log.d("Photo", parkingData.getPhotoPath().toString());
            for(String path : parkingData.getPhotoPath()){
                File f = new File(path);
                Uri uri = Uri.fromFile(f);
                ImageView tmp = (ImageView) getLayoutInflater().inflate(R.layout.image_view, imagesLayout, false);
                tmp.setBackgroundDrawable(null);
                photoViewList.add(tmp);
                imagesLayout.addView(tmp);
                tmp.setImageURI(uri);
                tmp.setOnClickListener(v -> {
                    if(path != null && path != ""){
                        Intent intent = new Intent(this, FullImageActivity.class);
                        intent.putExtra(PHOTO_EXTRA, path);
                        startActivity(intent);
                    }
                });
            }
        }
        deleteBtn.setOnClickListener(v -> {
            ParkManager.getInstance(this).deleteParkingData(parkingData.getId());
            finish();
        });
    }

    /**
     * callback che viene chiamata quando la mappa è pronta
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        double lat = Double.valueOf(parkingData.getLatitude());
        double lon = Double.valueOf(parkingData.getLongitude());
        //imposta un marker alla posizione del parcheggio
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lon)));
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 60));
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == FINE_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //we have the best permission for localization
                setupView();
            }else {
                finish();
            }
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
