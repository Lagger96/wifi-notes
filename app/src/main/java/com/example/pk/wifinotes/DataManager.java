package com.example.pk.wifinotes;

import android.database.sqlite.SQLiteDatabase;
import com.example.pk.wifinotes.DAO.NetworkDAO;
import com.example.pk.wifinotes.models.Network;
import com.example.pk.wifinotes.models.NetworkCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataManager {

    private NetworkDAO networkDAO;

    public DataManager(SQLiteDatabase sqLiteDatabase) {
        networkDAO = new NetworkDAO(sqLiteDatabase);
    }

    public List<Network> getNetworks() {
        List<Network> allNetworks = networkDAO.getAllNetworks();

        Collections.sort(allNetworks, Network::compareNetworks);

        return allNetworks;
    }

    public Network getNetwork(Integer networkId) {
        return networkDAO.getNetwork(networkId);
    }

    public List<NetworkCategory> getNetworkCategories() {
        List<NetworkCategory> categories = new ArrayList<>();

        for (String categoryName : networkDAO.getCategoriesNames()) {
            categories.add(mapToNetworkCategory(networkDAO.getNetworksByCategoryName(categoryName), categoryName));
        }

        Collections.sort(categories, NetworkCategory::compareCategories);

        return categories;
    }

    public NetworkCategory getNetworkCategory(String categoryName) {
        return mapToNetworkCategory(networkDAO.getNetworksByCategoryName(categoryName), categoryName);
    }

    public boolean addNetwork(Network network) {
        return networkDAO.saveNetwork(network);
    }

    public boolean updateNetwork(Network network) {
        return networkDAO.updateNetwork(network);
    }

    public void deleteNetwork(Integer networkId) {
        networkDAO.removeNetwork(networkId);
    }

    public Network getNetworkBySSID(String SSID) {
        return networkDAO.getNetworkBySSID(SSID);
    }

    private NetworkCategory mapToNetworkCategory(List<Network> networks, String categoryName) {
        return new NetworkCategory(categoryName, networks);
    }
}
