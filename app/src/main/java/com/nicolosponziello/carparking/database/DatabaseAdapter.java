package com.nicolosponziello.carparking.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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

    //add methods for adding data
}
