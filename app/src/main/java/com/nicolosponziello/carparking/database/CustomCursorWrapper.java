package com.nicolosponziello.carparking.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.nicolosponziello.carparking.database.DatabaseSchema.ParkTable;
import com.nicolosponziello.carparking.model.ParkingData;
import java.util.Date;
import java.util.UUID;

/**
 * wrapper per un cursore del database, in grado di ritornare un oggetto completo del model
 * segue il pattern decorator
 */
public class CustomCursorWrapper extends CursorWrapper {

    public CustomCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public ParkingData getParkingData() {
        String uuid = getString(getColumnIndex(ParkTable.Cols.FIELD_UUID));
        String address = getString(getColumnIndex(ParkTable.Cols.FIELD_ADDRESS));
        int active = getInt(getColumnIndex(ParkTable.Cols.FIELD_ACTIVE));
        String lon  = getString(getColumnIndex(ParkTable.Cols.FIELD_LONG));
        String lat = getString(getColumnIndex(ParkTable.Cols.FIELD_LAT));
        String spot = getString(getColumnIndex(ParkTable.Cols.FIELD_SPOT));
        String level = getString(getColumnIndex(ParkTable.Cols.FIELD_LEVEL));
        String note = getString(getColumnIndex(ParkTable.Cols.FIELD_NOTE));
        float cost = getFloat(getColumnIndex(ParkTable.Cols.FIELD_COST));
        long exp = getLong(getColumnIndex(ParkTable.Cols.FIELD_EXP));
        String city = getString(getColumnIndex(ParkTable.Cols.FIELD_CITY));
        long date = Long.parseLong(getString(getColumnIndex(ParkTable.Cols.FIELD_DATE)));
        String photoPath = getString(getColumnIndex(ParkTable.Cols.FIELD_PHOTO));

        ParkingData newData = new ParkingData();

        newData.setId(uuid);
        newData.setAddress(address);
        newData.setActive(active != 0);
        newData.setLongitude(lon);
        newData.setLatitude(lat);
        newData.setParkSpot(spot);
        newData.setParkLevel(level);
        newData.setNote(note);
        newData.setCost(cost);
        newData.setExpiration(exp);
        newData.setCity(city);
        newData.setDate(date);
        newData.setPhotoPath(photoPath);

        return newData;
    }
}
