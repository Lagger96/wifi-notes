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
import com.example.pk.wifinotes.models.Network;

import java.util.List;

public class SavedNetworksFragment extends Fragment {
    private static final String TAG = "SavedNetworksFragment";

    private RecyclerView recyclerView;
    private ConstraintLayout noNetworksInfo;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Network> networks;
    private DataManager dataManager;

    public SavedNetworksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saved_networks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataManager = new DataManager(DbHelper.getInstance(getContext()).getWritableDatabase());
        networks = dataManager.getNetworks();

        noNetworksInfo = view.findViewById(R.id.no_networks_info);
        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        setVisibility();

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new SavedNetworksAdapter(networks, new Callbacks(getContext(), this::refreshViews));
        recyclerView.setAdapter(adapter);
    }

    private void setVisibility() {
        noNetworksInfo.setVisibility(networks.size() > 0 ? View.GONE : View.VISIBLE);
        recyclerView.setVisibility(networks.size() > 0 ? View.VISIBLE : View.GONE);
    }

    public void notifyDataChanged() {
        networks.clear();
        networks.addAll(dataManager.getNetworks());
        adapter.notifyDataSetChanged();
        setVisibility();
    }

    public void refreshViews() {
        ((NetworksActivity) getActivity()).refreshViews();
    }
}
