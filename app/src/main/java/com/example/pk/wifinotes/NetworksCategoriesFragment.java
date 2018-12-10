package com.example.pk.wifinotes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.pk.wifinotes.database.DbHelper;
import com.example.pk.wifinotes.models.NetworkCategory;

import java.util.ArrayList;
import java.util.List;

public class NetworksCategoriesFragment extends Fragment {
    private static final String TAG = "NetworkCategoriesFragment";

    private RecyclerView recyclerView;
    private ConstraintLayout noCategoriesInfo;
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

        noCategoriesInfo = view.findViewById(R.id.no_categories_info);
        recyclerView = view.findViewById(R.id.rv_network_categories);
        recyclerView.setHasFixedSize(true);

        setVisibility();

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NetworksCategoriesAdapter(categories, new Callbacks(getContext(), this::refreshViews));
        recyclerView.setAdapter(adapter);
    }

    private void setVisibility() {
        noCategoriesInfo.setVisibility(categories.size() > 0 ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(categories.size() > 0 ? View.VISIBLE : View.GONE);
    }

    public void notifyDataChanged() {
        if (categories != null && dataManager != null && adapter != null) {
            categories.clear();
            categories.addAll(dataManager.getNetworkCategories());
            adapter.notifyDataSetChanged();
            setVisibility();
        }
    }

    public void refreshViews() {
        ((NetworksActivity) getActivity()).refreshViews();
    }
}
