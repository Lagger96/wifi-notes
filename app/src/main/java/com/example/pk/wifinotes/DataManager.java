package com.example.pk.wifinotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.example.pk.wifinotes.DAO.NetworkDAO;
import com.example.pk.wifinotes.database.DbHelper;

public class DataManager {

    private SQLiteDatabase database;
    private NetworkDAO networkDAO;

    private DataManager(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        database = dbHelper.getWritableDatabase();
        networkDAO = new NetworkDAO(database);
    }

}
