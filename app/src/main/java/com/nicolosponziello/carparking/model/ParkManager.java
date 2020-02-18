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
        this.dbHelper = new DatabaseHelper(c);
        this.context = c.getApplicationContext();
        database = dbHelper.getWritableDatabase();
        this.parkingData = getParkingData();
        this.currentParking = getCurrentParkingFromDB();

        Log.d("ParkManager", "Instatiatin data " + parkingData.size());
    }

    public static ParkManager getInstance(Context context) {
        if(instance == null){
            instance = new ParkManager(context);
        }
        return instance;
    }

    public void addParkingData(ParkingData data){
        ContentValues values = getContentValues(data);
        database.insert(DatabaseSchema.ParkTable.TABLE_NAME, null, values);
    }

    public void updateParking(ParkingData data){
        String targetUUID = data.getId().toString();
        ContentValues values = getContentValues(data);

        database.update(DatabaseSchema.ParkTable.TABLE_NAME, values, DatabaseSchema.ParkTable.Cols.FIELD_UUID + " = ?", new String[] {targetUUID});
    }

    public void setParkingData(List<ParkingData> list){
        this.parkingData = list;
    }

    public List<ParkingData> getParkingData() {
        List<ParkingData> tmp = new ArrayList<>();
        CustomCursorWrapper cursor = queryParking(null, null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                ParkingData data = cursor.getParkingData();
                if(data.isActive()){
                    setCurrentParking(data);
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
        for(ParkingData data : parkingData) {
            if(data.getId() == id){
                return data;
            }
        }
        return null;
    }

    public void setCurrentParking(ParkingData currentParking) {
        Log.d("ParkManager", "setCurrentParking");
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

    private ParkingData getCurrentParkingFromDB() {
        CustomCursorWrapper cursorWrapper = queryParking(DatabaseSchema.ParkTable.Cols.FIELD_ACTIVE + " = ?", new String[]{"true"});
        try {
            if(cursorWrapper.getCount() == 0){
                return null;
            }
            cursorWrapper.moveToFirst();
            return cursorWrapper.getParkingData();
        }finally {
            cursorWrapper.close();
        }
    }
}
