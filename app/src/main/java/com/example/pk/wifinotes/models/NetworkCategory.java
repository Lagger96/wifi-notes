package com.example.pk.wifinotes.models;

import java.util.List;

public class NetworkCategory {
    private String networkName;
    private List<Network> networks;

    public NetworkCategory(String networkName, List<Network> networks) {
        this.networkName = networkName;
        this.networks = networks;
    }

    public String getNetworkName() {
        return networkName;
    }

    public List<Network> getNetworks() {
        return networks;
    }

    public void addNetworkToCategory(Network network) {
        networks.add(network);
    }
}
