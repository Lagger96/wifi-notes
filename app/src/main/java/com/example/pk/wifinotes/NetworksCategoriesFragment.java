package com.example.pk.wifinotes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.pk.wifinotes.database.DbHelper;
import com.example.pk.wifinotes.models.Network;
import com.example.pk.wifinotes.models.NetworkCategory;

import java.util.List;

public class NetworksCategoriesFragment extends Fragment {
    private static final String TAG = "NetworkCategoriesFragment";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<NetworkCategory> categories;
    private DataManager dataManager;

    public NetworksCategoriesFragment() {
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_networks_categories, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataManager = new DataManager(DbHelper.getInstance(getContext()).getWritableDatabase());
        categories = dataManager.getNetworkCategories();

        recyclerView = view.findViewById(R.id.rv_network_categories);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NetworksCategoriesAdapter(categories, this::displayDetails, this::shareNetwork, this::shareNetworkCategory);
        recyclerView.setAdapter(adapter);
    }

    private void displayDetails(Network network) {
        Toast.makeText(getContext(), "Network details", Toast.LENGTH_SHORT).show();
    }

    private void shareNetwork(Network network) {
        Toast.makeText(getContext(), "Network share", Toast.LENGTH_SHORT).show();
    }

    private void shareNetworkCategory(NetworkCategory category) {
        Toast.makeText(getContext(), "Category share", Toast.LENGTH_SHORT).show();
    }
}
