package com.nicolosponziello.carparking.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.ContactsContract;

import com.nicolosponziello.carparking.model.ParkingData;

public class DatabaseAdapter {
    private static final String LOG_TAG = DatabaseAdapter.class.getSimpleName();
    private Context context;
    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    public DatabaseAdapter(Context context){
        this.context = context;
    }

    public void open() throws SQLException {
        if(this.databaseHelper == null){
            this.databaseHelper = new DatabaseHelper(this.context);
        }
        this.database = this.databaseHelper.getWritableDatabase();
    }

    public void saveData(ParkingData data){
        ContentValues values = new ContentValues();
        values.put(DatabaseConsts.FIELD_CITY, data.getCity());
        values.put(DatabaseConsts.FIELD_LAT, data.getLatitude());
        values.put(DatabaseConsts.FIELD_LONG, data.getLongitude());
        values.put(DatabaseConsts.FIELD_COST, data.getParkimeter().getCost());
        values.put(DatabaseConsts.FIELD_EXP, data.getParkimeter().getExipiration().toString());
        values.put(DatabaseConsts.FIELD_ACTIVE, data.isActive());
        values.put(DatabaseConsts.FIELD_PHOTO, data.getPhotoBlob().toString());
        values.put(DatabaseConsts.FIELD_NOTE, data.getNote());
        values.put(DatabaseConsts.FIELD_LEVEL, data.getParkLevel());
        values.put(DatabaseConsts.FIELD_SPOT, data.getParkSpot());
        values.put(DatabaseConsts.FIELD_ADDRESS, data.getAddress());
        values.put(DatabaseConsts.FIELD_DATE, data.getDate().toString());

        try {
            database.insert(DatabaseConsts.TABLE_NAME, null, values);
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public Cursor getCursor() {
        Cursor cursor = null;
        try {
            cursor = database.query(DatabaseConsts.TABLE_NAME, null, null, null, null, null, null);
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        return cursor;
    }
}
