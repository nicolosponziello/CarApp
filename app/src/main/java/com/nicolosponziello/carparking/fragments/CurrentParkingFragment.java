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
import com.nicolosponziello.carparking.MainActivity;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.DetailActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.model.ParkingData;
import com.nicolosponziello.carparking.notification.NotifManager;
import com.nicolosponziello.carparking.util.Const;
import com.nicolosponziello.carparking.util.Utils;
import java.io.File;
import java.util.Date;

/**
 * fragment che contiene lo stato del parcheggio corrente
 */
public class CurrentParkingFragment extends Fragment {

    private MaterialCardView card;
    private ParkingData parkingData;
    private TextView cityLabel, addressLabel, dateLabel;
    private ImageView photoCard;
    private ImageButton goBtn, shareBtn;

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
        goBtn = view.findViewById(R.id.goBtn);
        shareBtn = view.findViewById(R.id.shareBtn);

        //imposta le textview
        if(parkingData.getCity() != null && parkingData.getCity() != ""){
            cityLabel.setText(parkingData.getCity());
        }

        addressLabel.setText(parkingData.getAddress());

        Date date = new Date();
        date.setTime(parkingData.getDate());
        dateLabel.setText(Utils.formatDate(date));

        //se non è stata impostata una foto nascondi l'image view
        if(parkingData.getPhotoPath().size() == 0){
            photoCard.setVisibility(View.GONE);
        }else{
            Log.d("CarParking photo", parkingData.getPhotoPath().size() + "");
            File f = new File(parkingData.getPhotoPath().get(0));
            Uri uri = Uri.fromFile(f);
            photoCard.setImageURI(uri);
        }


        card.setOnClickListener(v -> {
            //apri i dettagli
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(Const.DETAIL_EXTRA, parkingData.getId());
            startActivity(intent);
        });

        goBtn.setOnClickListener(v -> {
            //apri google map in modalità navigazione a piedi
            Uri activityUri = Uri.parse("google.navigation:q=" + parkingData.getLatitude()+","+parkingData.getLongitude() +
                    "&mode=w");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, activityUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        });

        shareBtn.setOnClickListener(v -> {
            //condividi un link a maps con le coordinate impostate
            Uri toShareData = Utils.getMapsUrlFromLocation(Double.valueOf(parkingData.getLatitude()), Double.valueOf(parkingData.getLongitude()));

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, toShareData.toString());
            shareIntent.setType("text/plain"); //specifica il tipo di dato condiviso

            //apre il chooser per la condivisione
            Intent send = Intent.createChooser(shareIntent, null);
            startActivity(send);
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
