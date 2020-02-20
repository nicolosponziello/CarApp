package com.nicolosponziello.carparking.database;

/**
 * schema del database dell'applicazione
 */
public class DatabaseSchema {
    public static final class ParkTable {
        public static final String TABLE_NAME = "PARKING";

        public static final class Cols {
            public static final String FIELD_UUID = "UUID";
            public static final String FIELD_CITY = "CITY";
            public static final String FIELD_LAT = "LAT";
            public static final String FIELD_LONG = "LONG";
            public static final String FIELD_COST = "COST";
            public static final String FIELD_EXP = "EXPIRATION";
            public static final String FIELD_ACTIVE = "ACTIVE";
            public static final String FIELD_PHOTO = "PHOTO";
            public static final String FIELD_NOTE = "NOTE";
            public static final String FIELD_LEVEL = "LEVEL";
            public static final String FIELD_SPOT = "SPOT";
            public static final String FIELD_ADDRESS= "ADDRESS";
            public static final String FIELD_DATE = "DATE";
        }
    }
}
