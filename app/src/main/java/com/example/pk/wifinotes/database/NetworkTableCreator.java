package com.example.pk.wifinotes.database;

import android.database.sqlite.SQLiteDatabase;

public class NetworkTableCreator {
    public static void onUpgrade(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS NETOWORK;");
        onCreate(sqLiteDatabase);
    }

    public static void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE NETOWORK (NETWORK_ID " + SQLiteDataTypes.INTEGER + " PRIMARY KEY AUTOINCREMENT, SSID " + SQLiteDataTypes.TEXT
                + ", PASSWORD " + SQLiteDataTypes.TEXT + ", DESCRIPTION " + SQLiteDataTypes.TEXT + ", CATEGORY " + SQLiteDataTypes.TEXT + ");");
        insertTestData(sqLiteDatabase);
    }

    private static void insertTestData(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("INSERT INTO NETWORK VALUES ('SIEC1', '123', 'Siec pierwsza', 'Categoria1')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK VALUES ('SIEC2', '123', 'Siec druga', 'Categoria1')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK VALUES ('SIEC3', '123', 'Siec trzecia', 'Categoria2')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK VALUES ('SIEC4', '123', 'Siec czwarta', 'Categoria2')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK VALUES ('SIEC5', '123', 'Siec piąta', 'Categoria3')");
        sqLiteDatabase.execSQL("INSERT INTO NETWORK VALUES ('SIEC6', '123', 'Siec szósta', 'Categoria3')");
    }
}
