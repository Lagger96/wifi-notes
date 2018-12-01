package com.example.pk.wifinotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NetworksCategoriesFragment extends Fragment {
    private static final String TAG = "NetworkCategoriesFragment";

    public NetworksCategoriesFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_networks_categories, container, false);
    }
}
