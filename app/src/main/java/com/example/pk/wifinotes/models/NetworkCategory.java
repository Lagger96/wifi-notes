package com.example.pk.wifinotes.models;

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

    @Override
    public String toString() {
        return "NetworkCategory{" +
                "categoryName='" + categoryName + '\'' +
                ", networks=" + networks +
                '}';
    }
}
