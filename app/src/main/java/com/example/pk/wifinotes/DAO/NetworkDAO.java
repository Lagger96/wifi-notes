package com.example.pk.wifinotes.DAO;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.example.pk.wifinotes.models.Network;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class NetworkDAO extends DBContentProvider {

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
        Network network = null;

        cursor = super.rawQuery("SELECT * FROM NETWORK WHERE NETWORK_ID = ?", selectionArgs);

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
            return super.insert("NETWORK", getContentValue()) > 0;
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
        return getAllNetworks().stream().map(Network::getCategory).collect(Collectors.toSet());
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

    @Override
    protected Network cursorToEntity(Cursor cursor) {
        Network network = null;
        int idIndex;
        int categoryIndex;
        int descriptionIndex;
        int passwordIndex;
        int ssidIndex;

        if (cursor != null) {
            idIndex = cursor.getColumnIndexOrThrow("NETWORK_ID");
            categoryIndex = cursor.getColumnIndexOrThrow("CATEGORY");
            descriptionIndex = cursor.getColumnIndexOrThrow("DESCRIPTION");
            passwordIndex = cursor.getColumnIndexOrThrow("PASSWORD");
            ssidIndex = cursor.getColumnIndexOrThrow("SSID");
            network = new Network(cursor.getInt(idIndex), cursor.getString(ssidIndex), cursor.getString(passwordIndex), cursor.getString(descriptionIndex), cursor.getString(categoryIndex));
        }

        return network;
    }

    private ContentValues getContentValue() {
        return initialValues;
    }

    private void setContentValue(Network network) {
        initialValues = new ContentValues();
        initialValues.put("CATEGORY", network.getCategory());
        initialValues.put("DESCRIPTION", network.getDescription());
        initialValues.put("PASSWORD", network.getPassword());
        initialValues.put("SSID", network.getSsid());
    }
}
