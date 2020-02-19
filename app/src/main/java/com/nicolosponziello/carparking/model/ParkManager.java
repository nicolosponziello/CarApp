package com.nicolosponziello.carparking.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.nicolosponziello.carparking.database.CustomCursorWrapper;
import com.nicolosponziello.carparking.database.DatabaseHelper;
import com.nicolosponziello.carparking.database.DatabaseSchema;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ParkManager {

    private static ParkManager instance;

    private List<ParkingData> parkingData;
    private ParkingData currentParking;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private Context context;


    private ParkManager(Context c){
        Log.d("Data", "inst ParkManager");
        this.dbHelper = new DatabaseHelper(c);
        this.context = c.getApplicationContext();
        database = dbHelper.getWritableDatabase();
        this.parkingData = getParkingData();
    }

    public static ParkManager getInstance(Context context) {
        if(instance == null){
            instance = new ParkManager(context);
        }
        return instance;
    }

    public void addParkingData(ParkingData data){
        ContentValues values = getContentValues(data);
        this.parkingData.add(data);
        database.insert(DatabaseSchema.ParkTable.TABLE_NAME, null, values);
    }

    public void updateParking(ParkingData data){
        String targetUUID = data.getId().toString();
        ContentValues values = getContentValues(data);

        database.update(DatabaseSchema.ParkTable.TABLE_NAME, values, DatabaseSchema.ParkTable.Cols.FIELD_UUID + " = ?", new String[] {targetUUID});
    }

    public List<ParkingData> getParkingData() {
        List<ParkingData> tmp = new ArrayList<>();
        CustomCursorWrapper cursor = queryParking(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                ParkingData data = cursor.getParkingData();
                Log.d("Data", "Found "+ data.getId().toString() + "-"+data.isActive());
                if(data.isActive()){
                    Log.d("Data", "executes");
                    this.currentParking = data;
                }
                tmp.add(data);
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return tmp;
    }

    public ParkingData getParkingData(UUID id){
        Log.d("app", "looking for " + id.toString());
        for(ParkingData data : parkingData) {
            Log.d("app", "analizing " + data.getId().toString());
            if(data.getId().equals(id)){
                return data;
            }
        }
        return null;
    }

    public void setCurrentParking(ParkingData currentParking) {
        Log.d("Data", "setCurrentParking");
        this.currentParking = currentParking;
    }

    public ParkingData getCurrentParking() {
        return currentParking;
    }

    public boolean hasActiveParking(){
        return this.currentParking != null;
    }

    private static ContentValues getContentValues(ParkingData data) {
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_ACTIVE, data.isActive());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_LEVEL, data.getParkLevel());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_ADDRESS, data.getAddress());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_CITY, data.getCity());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_COST, data.getCost());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_DATE, data.getDate().toString());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_EXP, data.getExpiration());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_LAT, data.getLatitude());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_LONG, data.getLongitude());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_NOTE, data.getNote());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_PHOTO, data.getPhotoPath());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_SPOT, data.getParkSpot());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_UUID, data.getId().toString());

        return values;
    }

    private CustomCursorWrapper queryParking(String where, String[] whereArgs){
        Cursor cursor = database.query(
                DatabaseSchema.ParkTable.TABLE_NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null
        );
        return new CustomCursorWrapper(cursor);
    }

    public void setDoneParking(){
        this.currentParking.setActive(false);
        updateParking(this.currentParking);

        this.currentParking = null;
        this.parkingData = getParkingData();
    }


    public void deleteParkingData(UUID id){
        if(this.currentParking.getId().equals(id)){
            this.currentParking = null;
        }
        database.delete(DatabaseSchema.ParkTable.TABLE_NAME, DatabaseSchema.ParkTable.Cols.FIELD_UUID + " = ?", new String[]{id.toString()});
        this.parkingData = getParkingData();
    }
}
