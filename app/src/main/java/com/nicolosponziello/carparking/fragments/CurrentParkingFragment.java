package com.nicolosponziello.carparking.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.card.MaterialCardView;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.DetailActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.model.ParkingData;
import com.nicolosponziello.carparking.util.Const;
import com.nicolosponziello.carparking.util.Utils;
import java.io.File;

public class CurrentParkingFragment extends Fragment {

    private MaterialCardView card;
    private ParkingData parkingData;
    private TextView cityLabel, addressLabel, dateLabel;
    private ImageView photoCard;
    private ImageButton goBtn;
    private Button doneBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.current_parking_fragment, container, false);

        parkingData = ParkManager.getInstance(getActivity()).getCurrentParking();
        card = view.findViewById(R.id.currentCard);
        cityLabel = view.findViewById(R.id.cityCard);
        addressLabel = view.findViewById(R.id.addressCard);
        dateLabel = view.findViewById(R.id.dateCard);
        photoCard = view.findViewById(R.id.currentPhotoCard);
        doneBtn = view.findViewById(R.id.doneBtn);
        goBtn = view.findViewById(R.id.goBtn);
        if(parkingData.getCity() != null && parkingData.getCity() != ""){
            cityLabel.setText("Città: " + parkingData.getCity());
        }

        addressLabel.setText(parkingData.getAddress());

        dateLabel.setText("Data: " + Utils.formatDate(parkingData.getDate()));

        if(parkingData.getPhotoPath() == null){
            Log.d("Photo", "error");
        }
        Log.d("Photo", parkingData.getPhotoPath());
        File f = new File(parkingData.getPhotoPath());
        Uri uri = Uri.fromFile(f);

        photoCard.setImageURI(uri);

        card.setOnClickListener(v -> {
            //open details
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Const.DETAIL_EXTRA, parkingData.getId().toString());
            startActivity(intent);
        });

        goBtn.setOnClickListener(v -> {
            //start navigation
            Uri activityUri = Uri.parse("google.navigation:q=" + parkingData.getLatitude()+","+parkingData.getLongitude() +
                    "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, activityUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        doneBtn.setOnClickListener(v -> {
            ParkManager.getInstance(getActivity()).setDoneParking();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.current_pos_fragment, new NoPosFragment()).commit();
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
