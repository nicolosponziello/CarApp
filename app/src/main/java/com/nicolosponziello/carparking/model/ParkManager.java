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
        this.parkingData = getParkingData();
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
        //i content values permettono di gestire più agilmente la scrittura su database
        ContentValues values = getContentValues(data);
        this.parkingData.add(data);
        database.insert(DatabaseSchema.ParkTable.TABLE_NAME, null, values);
    }

    /**
     * aggiorna sul database l'istanza passata come argomento
     * @param data
     */
    public void updateParking(ParkingData data){
        String targetUUID = data.getId().toString();
        ContentValues values = getContentValues(data);

        database.update(DatabaseSchema.ParkTable.TABLE_NAME, values, DatabaseSchema.ParkTable.Cols.FIELD_UUID + " = ?", new String[] {targetUUID});
    }

    /**
     * leggi dal database tutti i dati salvati
     * @return lista dei parking data
     */
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

    /**
     * ottieni uno specifo ParkingData in base all'UUID
     * @param id del parking data da trovare
     * @return il parking data o null
     */
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

    /**
     * crea il contentvalues del parking data
     * @param data
     * @return content values
     */
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
        this.currentParking.setActive(false);
        updateParking(this.currentParking);

        this.currentParking = null;
        this.parkingData = getParkingData();
    }


    public void deleteParkingData(UUID id){
        if(this.currentParking != null && this.currentParking.getId().equals(id)){
            this.currentParking = null;
        }
        database.delete(DatabaseSchema.ParkTable.TABLE_NAME, DatabaseSchema.ParkTable.Cols.FIELD_UUID + " = ?", new String[]{id.toString()});
        ParkingData toDelete = getParkingData(id);
        this.parkingData.remove(toDelete);
    }
}
