package com.example.pk.wifinotes.DAO;

import android.database.sqlite.SQLiteDatabase;

public class NetworkDAO {

    private SQLiteDatabase database;

    public NetworkDAO(SQLiteDatabase database) {
        this.database = database;
    }
}
