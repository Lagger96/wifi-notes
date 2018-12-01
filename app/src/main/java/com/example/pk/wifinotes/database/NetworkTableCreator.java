package com.example.pk.wifinotes.database;

import android.database.sqlite.SQLiteDatabase;

public class NetworkTableCreator {
    public static void onUpgrade(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NETOWORK;");
        onCreate(sqLiteDatabase);
    }

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE NETWORK (NETWORK_ID " + SQLiteDataTypes.INTEGER + " PRIMARY KEY AUTOINCREMENT, SSID " + SQLiteDataTypes.TEXT
                + ", PASSWORD " + SQLiteDataTypes.TEXT + ", DESCRIPTION " + SQLiteDataTypes.TEXT + ", CATEGORY " + SQLiteDataTypes.TEXT + ");");
        insertTestData(sqLiteDatabase);
    }

    private static void insertTestData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("INSERT INTO NETWORK (SSID, PASSWORD , DESCRIPTION , CATEGORY ) VALUES ('SIEC1', '123', 'Siec pierwsza', 'Kategoria1')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK (SSID, PASSWORD , DESCRIPTION , CATEGORY ) VALUES ('SIEC2', '123', 'Siec druga', 'Kategoria1')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK (SSID, PASSWORD , DESCRIPTION , CATEGORY ) VALUES ('SIEC3', '123', 'Siec trzecia', 'Kategoria2')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK (SSID, PASSWORD , DESCRIPTION , CATEGORY ) VALUES ('SIEC4', '123', 'Siec czwarta', 'Kategoria2')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK (SSID, PASSWORD , DESCRIPTION , CATEGORY ) VALUES ('SIEC5', '123', 'Siec piąta', 'Kategoria3')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK (SSID, PASSWORD , DESCRIPTION , CATEGORY ) VALUES ('SIEC6', '123', 'Siec szósta', 'Kategoria3')");
    }
}
