package com.example.pk.wifinotes.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DBContentProvider {
    private SQLiteDatabase mDb;

    public DBContentProvider(SQLiteDatabase db) {
        this.mDb = db;
    }

    public long insert(String tableName, ContentValues values) {
        return mDb.insert(tableName, null, values);
    }

    public int update(String tableName, ContentValues contentValues, String whereClause, String[] whereArgs) {
        return mDb.update(tableName, contentValues, whereClause, whereArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return mDb.rawQuery(sql, selectionArgs);
    }

    public void execute(String sql) {
        mDb.execSQL(sql);
    }

    protected abstract<T> T cursorToEntity(Cursor cursor);
}
