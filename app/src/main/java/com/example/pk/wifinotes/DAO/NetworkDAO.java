package com.example.pk.wifinotes.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.pk.wifinotes.models.Network;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NetworkDAO extends DBContentProvider {

    private static final String CATEGORY_COLUMN = "CATEGORY";
    private static final String DESCRIPTION_COLUMN = "DESCRIPTION";
    private static final String PASSWORD_COLUMN = "PASSWORD";
    private static final String SSID_COLUMN = "SSID";
    private static final String NETWORK_ID_COLUMN = "NETWORK_ID";
    private static final String TABLE_NAME = "NETWORK";

    private Cursor cursor;
    private ContentValues initialValues;

    public NetworkDAO(SQLiteDatabase database) {
        super(database);
    }

    public List<Network> getAllNetworks() {
        cursor = super.rawQuery("SELECT * FROM NETWORK", null);

        return getNetworksFromCursor();
    }

    public Network getNetwork(Integer networkId) {
        final String selectionArgs[] = {String.valueOf(networkId)};

        cursor = super.rawQuery("SELECT * FROM NETWORK WHERE NETWORK_ID = ?", selectionArgs);

        return getEntityFromCursor();
    }

    public void updateNetwork(Network network) {
        final String selectionArgs[] = {network.getCategory(), network.getDescription(), network.getPassword(), network.getSsid(), String.valueOf(network.getId())};

        try {
            cursor = super.rawQuery("UPDATE NETWORK SET CATEGORY = ?, DESCRIPTION = ?, PASSWORD = ?, SSID = ? WHERE NETWORK_ID = ? ", selectionArgs);
            if (cursor != null) {
                cursor.moveToFirst();
                cursor.close();
            }
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
        }
    }

    public void removeNetwork(Integer networkId) {
        super.execute("DELETE FROM NETWORK WHERE NETWORK_ID = " + String.valueOf(networkId));
    }

    public boolean saveNetwork(Network network) {
        setContentValue(network);
        try {
            return super.insert(TABLE_NAME, getContentValue()) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    public List<Network> getNetworksByCategoryName(String categoryName) {
        final String selectionArgs[] = {categoryName};

        cursor = super.rawQuery("SELECT * FROM NETWORK WHERE CATEGORY = ?", selectionArgs);

        return getNetworksFromCursor();
    }

    public Set<String> getCategoriesNames() {
        Set<String> categoriesNames = new HashSet<>();

        for (Network network : getAllNetworks()) {
            if (!network.getCategory().equals("")) {
                categoriesNames.add(network.getCategory());
            }
        }

        return categoriesNames;
    }

    private List<Network> getNetworksFromCursor() {
        List<Network> networks = new ArrayList<>();

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Network network = cursorToEntity(cursor);
                networks.add(network);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return networks;
    }

    public Network getNetworkBySSID(String SSID) {
        final String selectionArgs[] = {SSID};

        cursor = super.rawQuery("SELECT * FROM NETWORK WHERE SSID = ?", selectionArgs);

        return getEntityFromCursor();
    }

    private Network getEntityFromCursor() {
        Network network = null;
        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                network = cursorToEntity(cursor);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return network;
    }

    @Override
    protected Network cursorToEntity(Cursor cursor) {
        Network network = null;
        int idIndex;
        int categoryIndex;
        int descriptionIndex;
        int passwordIndex;
        int ssidIndex;

        if (cursor != null) {
            idIndex = cursor.getColumnIndexOrThrow(NETWORK_ID_COLUMN);
            categoryIndex = cursor.getColumnIndexOrThrow(CATEGORY_COLUMN);
            descriptionIndex = cursor.getColumnIndexOrThrow(DESCRIPTION_COLUMN);
            passwordIndex = cursor.getColumnIndexOrThrow(PASSWORD_COLUMN);
            ssidIndex = cursor.getColumnIndexOrThrow(SSID_COLUMN);
            network = new Network(cursor.getInt(idIndex), cursor.getString(ssidIndex), cursor.getString(passwordIndex), cursor.getString(descriptionIndex), cursor.getString(categoryIndex));
        }

        return network;
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    private void setContentValue(Network network) {
        initialValues = new ContentValues();
        initialValues.put(CATEGORY_COLUMN, network.getCategory());
        initialValues.put(DESCRIPTION_COLUMN, network.getDescription());
        initialValues.put(PASSWORD_COLUMN, network.getPassword());
        initialValues.put(SSID_COLUMN, network.getSsid());
    }
}
