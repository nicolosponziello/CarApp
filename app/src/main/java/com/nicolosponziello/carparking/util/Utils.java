package com.nicolosponziello.carparking.util;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.StorageReference;
import com.nicolosponziello.carparking.model.ParkingData;

import java.sql.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class Utils {

    /**
     * formatta un tipo di dato Date in modo che sia leggibile
     * @param date
     * @return stringa della data
     */
    public static String formatDate(Date date){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ITALIAN);
        int style = DateFormat.MEDIUM;
        DateFormat df = DateFormat.getDateInstance(style, Locale.ITALIAN);
        String dateFormatted = df.format(date);
        String timeFormatted = formatter.format(date);

        return dateFormatted + " " + timeFormatted;
    }

    /**
     * crea un uri che permette di aprire l'applicazione di maps per la destinazione (lat, lon)
     * @param lat latitudine destinazione
     * @param lon longitudine destinazione
     * @return uri da condividere
     */
    public static Uri getMapsUrlFromLocation(double lat, double lon){
        Uri activityUri = Uri.parse("http://maps.google.com?daddr=" + lat + "," + lon);
        return activityUri;
    }

    public static ParkingData buildParkingData(DocumentSnapshot doc){
        String uuid = doc.getData().get("id").toString();
        String address = doc.getData().get("address").toString();
        boolean active = Boolean.parseBoolean(doc.getData().get("active").toString());
        String lon  = doc.getData().get("longitude").toString();
        String lat = doc.getData().get("latitude").toString();
        String spot = doc.getData().get("spot") != null? doc.getData().get("spot").toString():null;
        String level = doc.getData().get("level") != null? doc.getData().get("level").toString():null;
        String note = doc.getData().get("note") != null?  doc.getData().get("note").toString():null;
        float cost = Float.valueOf(doc.getData().get("cost").toString());
        long exp = Long.parseLong(doc.getData().get("expiration").toString());
        String city = doc.getData().get("city").toString();
        long date = Long.parseLong(doc.getData().get("date").toString());
        List<String> photoPath = doc.getData().get("photoPath") != null? (List<String>)doc.getData().get("photoPath"):null;

        ParkingData newData = new ParkingData();

        newData.setId(uuid);
        newData.setAddress(address);
        newData.setActive(active);
        newData.setLongitude(lon);
        newData.setLatitude(lat);
        newData.setParkSpot(spot);
        newData.setParkLevel(level);
        newData.setNote(note);
        newData.setCost(cost);
        newData.setExpiration(exp);
        newData.setCity(city);
        newData.setDate(date);
        for(String path : photoPath){
            newData.setPhotoPath(path);
        }

        return newData;
    }


    public static boolean isUserLogged(){
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public static String extractPhotoname(String path){
        String[] tmp = path.split("/");
        String name = tmp[tmp.length - 1];
        if(name.indexOf('.') > 0){
            name = name.substring(0, name.indexOf('.'));
        }
        return name;
    }

    public static <T> List<T> getListFromIterator(Iterator<T> iterator){
        List<T> res = new ArrayList<>();
        iterator.forEachRemaining(res::add);
        return res;
    }
}
