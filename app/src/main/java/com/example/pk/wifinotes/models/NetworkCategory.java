package com.example.pk.wifinotes.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class NetworkCategory {
    private String categoryName;
    private List<Network> networks;

    public NetworkCategory(String categoryName, List<Network> networks) {
        this.categoryName = categoryName;
        this.networks = networks;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public void addNetworkToCategory(Network network) {
        networks.add(network);
    }

    public String toJSONString() {
        JSONArray jsonArray = new JSONArray();

        for (Network network : networks) {
            jsonArray.put(network.toJSON());
        }

        JSONObject mainJsonObject = new JSONObject();

        try {
            mainJsonObject.put("categoryName", categoryName);
            mainJsonObject.put("networks", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mainJsonObject.toString();
    }

    public static int compareCategories(NetworkCategory firstCategory, NetworkCategory secondCategory) {
        return firstCategory.categoryName.compareToIgnoreCase(secondCategory.categoryName);
    }
}
