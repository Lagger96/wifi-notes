package com.example.pk.wifinotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pk.wifinotes.models.Network;

import java.util.List;

public class SavedNetworksAdapter extends RecyclerView.Adapter<SavedNetworksAdapter.SavedNetworksViewHolder> {

    private List<Network> networks;
    private Boolean displayCategoryName;
    private Callbacks callbacks;

    public static class SavedNetworksViewHolder extends RecyclerView.ViewHolder {

        TextView tvNetworkName;
        TextView tvCategoryName;
        ImageView iconWifi;
        ImageView iconShare;

        public SavedNetworksViewHolder(View itemView) {
            super(itemView);

            tvNetworkName = itemView.findViewById(R.id.tv_network_name);
            tvCategoryName = itemView.findViewById(R.id.tv_network_category);
            iconWifi = itemView.findViewById(R.id.icon_wifi);
            iconShare = itemView.findViewById(R.id.icon_share);
        }
    }

    public SavedNetworksAdapter(List<Network> networks, Callbacks callbacks, Boolean displayCategoryName) {
        this.networks = networks;
        this.displayCategoryName = displayCategoryName;
        this.callbacks = callbacks;
    }

    public SavedNetworksAdapter(List<Network> networks, Callbacks callbacks) {
        this(networks, callbacks, true);
    }

    @Override
    public SavedNetworksAdapter.SavedNetworksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View savedNetworkView = inflater.inflate(R.layout.item_saved_network, parent, false);

        return new SavedNetworksAdapter.SavedNetworksViewHolder(savedNetworkView);
    }

    @Override
    public void onBindViewHolder(SavedNetworksViewHolder holder, int position) {
        final Network network = networks.get(position);
        holder.iconWifi.setImageResource(getWifiIcon(network.getStatus()));
        holder.tvNetworkName.setText(network.getSsid());
        holder.tvCategoryName.setText(network.getCategory());
        if (!displayCategoryName) {
            holder.tvCategoryName.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener((v) -> callbacks.displayDetails.onClick(network));
        holder.iconShare.setOnClickListener((v) -> callbacks.shareNetwork.onClick(network));
    }

    private int getWifiIcon(int networkStatus) {
        int iconId = R.drawable.ic_wifi_default_24dp;
        switch (networkStatus) {
            case NetworkStatusSetter.STATUS_ACTIVE:
                iconId = R.drawable.ic_wifi_active_24dp;
                break;
            case NetworkStatusSetter.STATUS_NEARBY:
                iconId = R.drawable.ic_wifi_nearby_24dp;
                break;
        }
        return iconId;
    }

    @Override
    public int getItemCount() {
        return networks.size();
    }
}
