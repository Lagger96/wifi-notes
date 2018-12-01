package com.example.pk.wifinotes.database;

import android.database.sqlite.SQLiteDatabase;

public class NetworkTableCreator {
    public static void onUpgrade(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NETOWORK;");
        onCreate(sqLiteDatabase);
    }

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE NETOWORK (NETWORK_ID " + SQLiteDataTypes.INTEGER + " PRIMARY KEY, SSID " + SQLiteDataTypes.TEXT
                + ", PASSWORD " + SQLiteDataTypes.TEXT + ", DESCRIPTION " + SQLiteDataTypes.TEXT + ", CATEGORY " + SQLiteDataTypes.TEXT + ");");
    }
}
