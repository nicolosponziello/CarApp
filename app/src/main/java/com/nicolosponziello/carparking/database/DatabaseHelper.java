package com.nicolosponziello.carparking.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.nicolosponziello.carparking.database.DatabaseSchema.ParkTable;

/**
 * helper per l'accesso al database
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "database.db";
    private Context context;
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //crea il database se non presente
        String query = "create table " + ParkTable.TABLE_NAME +
                " ( _id integer primary key autoincrement," +
                ParkTable.Cols.FIELD_ACTIVE + ", " +
                ParkTable.Cols.FIELD_UUID + ", " +
                ParkTable.Cols.FIELD_CITY + ", " +
                ParkTable.Cols.FIELD_LONG + ", " +
                ParkTable.Cols.FIELD_LAT + ", " +
                ParkTable.Cols.FIELD_COST + ", " +
                ParkTable.Cols.FIELD_EXP + ", " +
                ParkTable.Cols.FIELD_PHOTO + ", " +
                ParkTable.Cols.FIELD_NOTE + ", " +
                ParkTable.Cols.FIELD_LEVEL + ", " +
                ParkTable.Cols.FIELD_SPOT + ", " +
                ParkTable.Cols.FIELD_DATE + ", " +
                ParkTable.Cols.FIELD_ADDRESS + ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}
