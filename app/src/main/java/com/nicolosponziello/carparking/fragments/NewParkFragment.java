package com.nicolosponziello.carparking.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.NewParkActivity;

public class NewParkFragment extends Fragment implements OnMapReadyCallback {

    private TextView coordinateValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_park_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        coordinateValue = view.findViewById(R.id.coordinateValue);
        String lat = getArguments().getString(NewParkActivity.BUNDLE_LAT);
        String lon = getArguments().getString(NewParkActivity.BUNDLE_LONG);
        coordinateValue.setText("( " + lat + " ; " + lon + " )");

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng cervia = new LatLng(44.263550, 12.347682);
        googleMap.addMarker(new MarkerOptions().position(cervia).title("test"));
        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cervia));
    }
}
