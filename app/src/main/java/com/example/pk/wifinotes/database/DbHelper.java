package com.example.pk.wifinotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "wifinotes.db";
    private static DbHelper dbHelperInstance;

    public static synchronized DbHelper getInstance(Context context) {
        if (dbHelperInstance == null) {
            dbHelperInstance = new DbHelper(context);
        }
        return dbHelperInstance;
    }

    private DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        NetworkTableCreator.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        NetworkTableCreator.onUpgrade(db);
    }
}
