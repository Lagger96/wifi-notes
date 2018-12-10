package com.example.pk.wifinotes;


import android.util.Log;
import com.example.pk.wifinotes.models.Network;

import java.util.*;

public class NetworkStatusSetter {
    private static String activeNetworkSsid;
    private static Set<String> nearbyNetworksSsids;
    public static final int STATUS_DEFAULT = 0;
    public static final int STATUS_ACTIVE = 2;
    public static final int STATUS_NEARBY = 1;

    private static class SortByStatus implements Comparator<Network> {
        public int compare(Network a, Network b) {
            return b.getStatus() - a.getStatus();
        }
    }

    public static void setActiveNetworkSsid(String activeNetworkSsid) {
        NetworkStatusSetter.activeNetworkSsid = activeNetworkSsid;
    }

    public static void setNearbyNetworksSsids(Set<String> nearbyNetworksSsids) {
        NetworkStatusSetter.nearbyNetworksSsids = nearbyNetworksSsids;
    }

    public static List<Network> setStatus(List<Network> networks) {
        for (Network network : networks) {
            if (nearbyNetworksSsids.contains(network.getSsid())) {
                network.setStatus(STATUS_NEARBY);
            } else {
                network.setStatus(STATUS_DEFAULT);
            }
            if (activeNetworkSsid != null && activeNetworkSsid.equals(network.getSsid())) {
                network.setStatus(STATUS_ACTIVE);
            }
        }
        Collections.sort(networks, new SortByStatus());
        return networks;
    }
}
