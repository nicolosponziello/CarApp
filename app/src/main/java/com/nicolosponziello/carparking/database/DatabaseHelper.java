package com.nicolosponziello.carparking.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table " + DatabaseConsts.TABLE_NAME +
                " ( _id integer primary key autoincrement," +
                DatabaseConsts.FIELD_ACTIVE + " bool," +
                DatabaseConsts.FIELD_CITY + " text," +
                DatabaseConsts.FIELD_LONG + " text," +
                DatabaseConsts.FIELD_LAT + " text," +
                DatabaseConsts.FIELD_COST + " text," +
                DatabaseConsts.FIELD_EXP + " text," +
                DatabaseConsts.FIELD_PHOTO + " byte," +
                DatabaseConsts.FIELD_NOTE + " text," +
                DatabaseConsts.FIELD_LEVEL + " text," +
                DatabaseConsts.FIELD_SPOT + " text," +
                DatabaseConsts.FIELD_ADDRESS + " text" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
