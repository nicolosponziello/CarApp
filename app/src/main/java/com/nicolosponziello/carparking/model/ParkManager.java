package com.nicolosponziello.carparking.model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.nicolosponziello.carparking.database.DatabaseConsts;
import com.nicolosponziello.carparking.database.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ParkManager {

    private static ParkManager instance;

    private List<ParkingData> parkingData;
    private ParkingData currentParking;
    private DatabaseHelper dbHelper;
    private Context context;


    private ParkManager(Context context){
        this.parkingData = new ArrayList<>();
        this.dbHelper = new DatabaseHelper(context);
        this.context = context;
    }

    public static ParkManager getInstance(Context context) {
        if(instance == null){
            instance = new ParkManager(context);
        }
        return instance;
    }

    public void init(){
        // read database to search for latest parking data
        // and parse
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseConsts.TABLE_NAME, null, null, null, null, null, null, null);
        List<ParkingData> tempData = new ArrayList<>();
        while(cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndex(DatabaseConsts.FIELD_ID));
            String city = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_CITY));
            String lat = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_LAT));
            String lon = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_LONG));
            String cost = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_COST));
            String exp = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_EXP));
            boolean active = Boolean.getBoolean(cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_ACTIVE)));
            byte[] photo = cursor.getBlob(cursor.getColumnIndex(DatabaseConsts.FIELD_PHOTO));
            String note = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_NOTE));
            String level = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_LEVEL));
            String spot = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_SPOT));
            String address = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_ADDRESS));
            String date = cursor.getString(cursor.getColumnIndex(DatabaseConsts.FIELD_DATE));

            ParkingData newData = new ParkingData();
            newData.setId(id);
            newData.setCity(city);
            newData.setLatitude(lat);
            newData.setLongitude(lon);
            newData.getParkimeter().setCost(Float.valueOf(cost));
            newData.getParkimeter().setExipiration(exp);
            newData.setPhotoBlob(photo);
            newData.setNote(note);
            newData.setParkLevel(level);
            newData.setParkSpot(spot);
            newData.setAddress(address);
            newData.setDate(date);
            newData.setActive(active);

            if(active){
                this.currentParking = newData;
            }
            parkingData.add(newData);
        }
        cursor.close();
    }

    public void setParkingData(List<ParkingData> list){
        this.parkingData = list;
    }

    public List<ParkingData> getParkingData() {
        return parkingData;
    }

    public void setCurrentParking(ParkingData currentParking) {
        this.currentParking = currentParking;
    }

    public ParkingData getCurrentParking() {
        return currentParking;
    }

    public boolean hasActiveParking(){
        return this.currentParking != null;
    }

}
