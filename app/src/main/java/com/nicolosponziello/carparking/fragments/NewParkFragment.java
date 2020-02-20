package com.nicolosponziello.carparking.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import com.nicolosponziello.carparking.R;
import com.nicolosponziello.carparking.activity.FullImageActivity;
import com.nicolosponziello.carparking.activity.NewParkActivity;
import com.nicolosponziello.carparking.model.ParkManager;
import com.nicolosponziello.carparking.model.ParkingData;
import com.nicolosponziello.carparking.notification.NotifManager;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * fragment per creare un nuovo parcheggio
 */
public class NewParkFragment extends Fragment {

    private static final int REQUEST_PHOTO = 2;
    public static final String PHOTO_EXTRA = "photo_extra";
    private static final int EXTERNAL_STORAGE_PERMISSION = 2;

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

        //di default nascondi il parchimetro
        parkimeterLayout.setVisibility(View.GONE);
        parkimeterSwitch.setOnCheckedChangeListener((v, checked)->{
            if(checked){
                parkimeterLayout.setVisibility(View.VISIBLE);
            }else{
                parkimeterLayout.setVisibility(View.GONE);
            }
        });
        coordinateValue = view.findViewById(R.id.coordinateValue);

        //ottieni le coordinate dall'intent
        lat = getArguments().getString(NewParkActivity.BUNDLE_LAT);
        lon = getArguments().getString(NewParkActivity.BUNDLE_LONG);
        coordinateValue.setText("( " + lat + " ; " + lon + " )");

        //crea un nuovo pezzo di dati ParkingData
        newParking = new ParkingData();
        newParking.setId(UUID.randomUUID());

        //bottone per aprire la fotocamera
        addPhotoBtn.setOnClickListener(v -> {
            if(!hasStoragePermission()){
                requestStoragePermission();
            }else{
                takePicture();
            }
        });

        //mostra il dialog per la scadenza
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
            //se è stato impostato un path per il file della foto salvato, al click della foto mostrala su una activity più grande
            if(photoFilePath != null && photoFilePath != ""){
                Intent intent = new Intent(getContext(), FullImageActivity.class);
                intent.putExtra(PHOTO_EXTRA, photoFilePath);
                startActivity(intent);
            }
        });



        //cancel btn
        cancelBtn.setOnClickListener(v -> {
            //annulla tutto
            getActivity().finish();
        });

        //salva
        saveBtn.setOnClickListener(v -> {
            //controlla quali dati sono stati messi dall'utente
            if(noteInput.getText().length() > 0){
                note = noteInput.getText().toString();
                newParking.setNote(note);
            }
            if(addressInput.getText().length() > 0) {
                address = addressInput.getText().toString();
                newParking.setAddress(address);
            }else{
                //ottieni indirizzo automaticamente
                address = getAddressFromCoord(Double.valueOf(lat), Double.valueOf(lon));
                newParking.setAddress(address);
            }
            if(levelInput.getText().length() > 0) {
                level = levelInput.getText().toString();
                newParking.setParkLevel(level);
            }

            if(spotInput.getText().length() > 0){
                spot = spotInput.getText().toString();
                newParking.setParkSpot(spot);
            }

            if(costInput.getText().length() > 0){
                cost = Float.valueOf(costInput.getText().toString());
                newParking.setCost(cost);
            }

            if(expTime != 0){
                newParking.setExpiration(expTime);
                setupAlarm();
            }

            newParking.setLongitude(lon);
            newParking.setLatitude(lat);
            newParking.setDate(new Date());
            newParking.setActive(true);
            newParking.setPhotoPath(photoFilePath);
            //ottieni la città automaticamente
            newParking.setCity(getCityFromCoord(Float.valueOf(lat), Float.valueOf(lon)));

            ParkManager.getInstance(getActivity()).addParkingData(newParking);
            ParkManager.getInstance(getActivity()).setCurrentParking(newParking);

            getActivity().finish();
        });
        return view;
    }

    /**
     * crea un file immagine temporaneo in cui salvare la foto ottenuta dalla fotocamera
     * @param id
     * @return file temporaneo
     * @throws IOException
     */
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

    /**
     * apri la fotocamera per ottenere una foto
     */
    private void takePicture(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            File photoFile = null;
            try {
                //crea nuovo file temporaneo
                photoFile = createImageFile(newParking.getId());
            }catch (IOException e){
                Log.d("CarParking", "Error creating temp file");
            }
            if(photoFile != null){
                //usa il FileProvider per ottenere l'uri del file temporaneo
                Uri photoUri = FileProvider.getUriForFile(getActivity(), "com.nicolosponziello.android.carparking.fileprovider", photoFile);
                //aggiorna l'image view per mostrare la foto ottenuta tramite result
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                photoFilePath = photoFile.getAbsolutePath();
                startActivityForResult(pictureIntent, REQUEST_PHOTO);
            }
        }
    }

    /**
     * chiamata quando si risponde alla richiesta del permesso per lo storage esterno in cui salvare le foto
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("perm", "onRequestPermissionsResult: ");
        if(requestCode == EXTERNAL_STORAGE_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                takePicture();
            }
        }
    }

    private boolean hasStoragePermission(){
        if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            return true;
        }
        return false;
    }


    private void requestStoragePermission(){
        requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION);
    }

    /**
     * chiamata quando termina l'activity della fotocamera per ottenere la foto
     * @param requestCode
     * @param resultCode
     * @param data
     */
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

    /**
     * ottieni la città tramite il Geocoder
     * @param lat
     * @param lon
     * @return
     */
    private String getCityFromCoord(float lat, float lon){
        Geocoder geocoder = new Geocoder(getActivity(), Locale.ITALY);
        String res = null;
        try {
            List<Address> list = geocoder.getFromLocation(lat, lon, 1);
            if(list.size() > 0){
                res = list.get(0).getLocality();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Ottieni l'indirizzo automaticamente
     * @param lat
     * @param lon
     * @return
     */
    private String getAddressFromCoord(double lat, double lon){
        Geocoder geocoder = new Geocoder(getActivity(), Locale.ITALY);
        String res = null;
        try {
            List<Address> list = geocoder.getFromLocation(lat, lon, 1);
            if(list.size() > 0){
                res = list.get(0).getAddressLine(0);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        String[] tmp = res.split(",");
        //solo via e numero civico
        return tmp[0] + " " + tmp[1];
    }

    /**
     * quando si salva la posizione avvia un'allarme per una notifica di scadenza parcheggio
     */
    private void setupAlarm(){
        boolean enabled = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(getString(R.string.enable_notif_key), false);
        if(enabled) {
            AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(getContext().ALARM_SERVICE);
            //Intent intent = new Intent(getContext(), AlarmReceiver.class);
            //PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), REQUEST_ALARM, intent, 0);

            int before = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.notif_key), "0"));
            int millis = before * 60 * 1000; //minutes -> millis
            long target = newParking.getExpiration() - millis;
            if(target < new Date().getTime()){
                target = newParking.getExpiration();
            }
            Log.d("Alarm", "Setting alarm to " + target);
            //alarmManager.setExact(NotifManager.RTC_WAKEUP, target, pendingIntent);
            NotifManager.getInstance().setupAlarm(alarmManager, target, getActivity());
        }
    }
}
