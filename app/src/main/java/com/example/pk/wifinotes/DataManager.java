package com.example.pk.wifinotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import com.example.pk.wifinotes.DAO.NetworkDAO;
import com.example.pk.wifinotes.models.Network;
import com.example.pk.wifinotes.models.NetworkCategory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataManager {

    private NetworkDAO networkDAO;
    private WifiManager wifiManager;

    public DataManager(SQLiteDatabase sqLiteDatabase, Context context) {
        networkDAO = new NetworkDAO(sqLiteDatabase);
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public List<Network> getNetworks() {
        List<Network> allNetworks = networkDAO.getAllNetworks();

        Collections.sort(allNetworks, Network::compareNetworks);

        return allNetworks;
    }

    public List<Network> getSystemNetworks() {
        List<Network> systemNetworks = new ArrayList<>();
        List<WifiConfiguration> wifiConfigurationList = wifiManager.getConfiguredNetworks();

        if (wifiConfigurationList != null) {
            for (WifiConfiguration configuredNetwork : wifiManager.getConfiguredNetworks()) {
                String ssid = configuredNetwork.SSID.substring(1, configuredNetwork.SSID.length() - 1);
                if (getNetworkBySSID(ssid) == null) {
                    systemNetworks.add(new Network(-1, ssid, null, null, null));
                }
            }
            Collections.sort(systemNetworks, Network::compareNetworks);
        }
        return systemNetworks;
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
