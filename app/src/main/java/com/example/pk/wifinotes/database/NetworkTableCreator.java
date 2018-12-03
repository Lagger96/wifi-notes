package com.example.pk.wifinotes.database;

import android.database.sqlite.SQLiteDatabase;

public class NetworkTableCreator {
    public static void onUpgrade(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NETWORK;");
        onCreate(sqLiteDatabase);
    }

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE NETWORK (NETWORK_ID " + SQLiteDataTypes.INTEGER
                + " PRIMARY KEY AUTOINCREMENT, SSID " + SQLiteDataTypes.TEXT + " UNIQUE, PASSWORD "
                + SQLiteDataTypes.TEXT + ", DESCRIPTION " + SQLiteDataTypes.TEXT + ", CATEGORY "
                + SQLiteDataTypes.TEXT + ");");
    }
}
