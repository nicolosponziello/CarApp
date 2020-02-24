package com.nicolosponziello.carparking.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.nicolosponziello.carparking.database.CustomCursorWrapper;
import com.nicolosponziello.carparking.database.DatabaseHelper;
import com.nicolosponziello.carparking.database.DatabaseSchema;
import com.nicolosponziello.carparking.database.FirebaseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Manager con pattern singleton per gestire i dati dei parcheggi nell'applicazione
 */
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
        this.parkingData = new ArrayList<>();
    }

    public static ParkManager getInstance(Context context) {
        if(instance == null){
            instance = new ParkManager(context);
        }
        return instance;
    }

    /**
     * aggiunge un nuovo pezzo di dati
     * @param data
     */
    public void addParkingData(ParkingData data){
        FirebaseHandler.getInstance(context).saveData(data);
    }

    /**
     * aggiorna sul database l'istanza passata come argomento
     * @param data
     */
    public void updateParking(ParkingData data){
        FirebaseHandler.getInstance(context).updateData(data);
    }

    /**
     * ottieni uno specifo ParkingData in base all'UUID
     * @param id del parking data da trovare
     * @return il parking data o null
     */
    public ParkingData getParkingData(String id){
        Log.d("app", "looking for " + id);
        for(ParkingData data : parkingData) {
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

    /**
     * crea il contentvalues del parking data
     * @param data
     * @return content values
     */
    public static ContentValues getContentValues(ParkingData data) {
        ContentValues values = new ContentValues();
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_ACTIVE, data.isActive());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_LEVEL, data.getParkLevel());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_ADDRESS, data.getAddress());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_CITY, data.getCity());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_COST, data.getCost());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_DATE, data.getDate());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_EXP, data.getExpiration());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_LAT, data.getLatitude());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_LONG, data.getLongitude());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_NOTE, data.getNote());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_PHOTO, data.getPhotoPath());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_SPOT, data.getParkSpot());
        values.put(DatabaseSchema.ParkTable.Cols.FIELD_UUID, data.getId());

        return values;
    }

    /**
     * crea un cursor per l'elaborazione dei risultati di una query sul database
     * @param where
     * @param whereArgs
     * @return
     */
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
        currentParking.setActive(false);
        updateParking(currentParking);

        currentParking = null;
    }


    public void deleteParkingData(String id){
        FirebaseHandler.getInstance(context).deleteData(id);
    }

    public List<ParkingData> getParkingData(){
        return this.parkingData;
    }

    public void completeAddData(ParkingData data) {
        if(!parkingData.stream().map(d -> d.getId()).anyMatch(id -> id.equals(data.getId()))){
            parkingData.add(data);
        }
        if(data.isActive()){
            currentParking = getParkingData(data.getId());
        }
    }

    public void completeDeleteData(String id){
        if(this.currentParking != null && this.currentParking.getId().equals(id)){
            this.currentParking = null;
        }
        ParkingData toDelete = getParkingData(id);
        parkingData.remove(toDelete);
        Log.d("store", parkingData.toString());
    }

    public void reset(Context context){
        instance = new ParkManager(context);
    }

}
