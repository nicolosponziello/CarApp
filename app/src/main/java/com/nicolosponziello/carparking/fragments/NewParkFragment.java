package com.nicolosponziello.carparking.fragments;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.FullImageActivity;
import com.nicolosponziello.carparking.activity.NewParkActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.model.ParkingData;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NewParkFragment extends Fragment {

    private static final int REQUEST_PHOTO = 2;
    public static final String PHOTO_EXTRA = "photo_extra";

    private TextView coordinateValue;
    private ImageView photoView;
    private ImageButton addPhotoBtn;
    private String photoFilePath;
    private Button addTimeBtn;
    private ParkingData newParking;
    private LinearLayout parkimeterLayout;
    private Switch parkimeterSwitch;
    private Button saveBtn;
    private Button cancelBtn;
    private EditText addressInput, levelInput, spotInput, costInput, noteInput;


    private long expTime;
    private String lat, lon;
    private String address, level, spot, note;
    private float cost;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_park_fragment, container, false);

        photoView = view.findViewById(R.id.park_photo);
        addPhotoBtn = view.findViewById(R.id.addPhotoBtn);
        addTimeBtn = view.findViewById(R.id.expButton);
        parkimeterLayout = view.findViewById(R.id.parkimeterData);
        parkimeterSwitch = view.findViewById(R.id.parkimeterSwitch);
        saveBtn = view.findViewById(R.id.saveBtn);
        cancelBtn = view.findViewById(R.id.cancelBtn);
        addressInput = view.findViewById(R.id.addressValue);
        levelInput = view.findViewById(R.id.levelValue);
        spotInput = view.findViewById(R.id.placeValue);
        costInput = view.findViewById(R.id.costValue);
        noteInput = view.findViewById(R.id.noteValue);

        parkimeterLayout.setVisibility(View.GONE);
        parkimeterSwitch.setOnCheckedChangeListener((v, checked)->{
            if(checked){
                parkimeterLayout.setVisibility(View.VISIBLE);
            }else{
                parkimeterLayout.setVisibility(View.GONE);
            }
        });
        coordinateValue = view.findViewById(R.id.coordinateValue);
        lat = getArguments().getString(NewParkActivity.BUNDLE_LAT);
        lon = getArguments().getString(NewParkActivity.BUNDLE_LONG);
        coordinateValue.setText("( " + lat + " ; " + lon + " )");

        newParking = new ParkingData();
        newParking.setId(UUID.randomUUID());

        addPhotoBtn.setOnClickListener(v -> {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(pictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
                File photoFile = null;
                try {
                    photoFile = createImageFile(newParking.getId());
                }catch (IOException e){
                    Log.d("CarParking", "Error creating temp file");
                }
                if(photoFile != null){
                    Uri photoUri = FileProvider.getUriForFile(getActivity(), "com.nicolosponziello.android.carparking.fileprovider", photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    photoFilePath = photoFile.getAbsolutePath();
                    startActivityForResult(pictureIntent, REQUEST_PHOTO);
                }
            }
        });
        //show dialog to setup exp time
        addTimeBtn.setOnClickListener(v ->{
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if(view.isShown()){
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        addTimeBtn.setText(hourOfDay + ":" + minute);
                        expTime = calendar.getTimeInMillis();
                    }
                }
            };
            TimePickerDialog dialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Material_Dialog, listener, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
            dialog.show();
        });

        photoView.setOnClickListener(v -> {
            if(photoFilePath != null && photoFilePath != ""){
                Intent intent = new Intent(getContext(), FullImageActivity.class);
                intent.putExtra(PHOTO_EXTRA, photoFilePath);
                startActivity(intent);
            }
        });



        //cancel btn
        cancelBtn.setOnClickListener(v -> {
            getActivity().finish();
        });
        //save btn
        saveBtn.setOnClickListener(v -> {
            note = noteInput.getText().toString();
            address = addressInput.getText().toString();
            level = levelInput.getText().toString();
            spot = spotInput.getText().toString();
            cost = Float.valueOf(costInput.getText().toString());

            newParking.setNote(note);
            newParking.setLongitude(lon);
            newParking.setLatitude(lat);
            newParking.setDate(new Date());
            newParking.setParkLevel(level);
            newParking.setCost(cost);
            newParking.setParkSpot(spot);
            newParking.setActive(true);
            newParking.setAddress(address);
            newParking.setPhotoPath(photoFilePath);

            ParkManager.getInstance(getActivity()).addParkingData(newParking);
            ParkManager.getInstance(getActivity()).setCurrentParking(newParking);
            Log.d("DATA", newParking.toString());
            getActivity().finish();
        });
        return view;
    }

    private File createImageFile(UUID id) throws IOException {
        String imageName = id.toString();
        File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageName,
                ".jpg",
                dir
        );
        photoFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_PHOTO){
            if(resultCode == Activity.RESULT_OK){
                File f = new File(photoFilePath);
                Uri uri = Uri.fromFile(f);
                photoView.setImageURI(uri);
            }
        }
    }
}
