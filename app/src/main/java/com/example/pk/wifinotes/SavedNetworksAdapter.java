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
    private OnClickNetworkAction displayDetailsAction;
    private OnClickNetworkAction shareNetworkAction;
    private Boolean displayCategoryName;

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

    public SavedNetworksAdapter(List<Network> networks, OnClickNetworkAction displayDetailsAction, OnClickNetworkAction shareNetworkAction, Boolean displayCategoryName) {
        this.networks = networks;
        this.displayDetailsAction = displayDetailsAction;
        this.shareNetworkAction = shareNetworkAction;
        this.displayCategoryName = displayCategoryName;
    }

    public SavedNetworksAdapter(List<Network> networks, OnClickNetworkAction displayDetailsAction, OnClickNetworkAction shareNetworkAction) {
        this(networks, displayDetailsAction, shareNetworkAction, true);
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
        holder.tvNetworkName.setText(network.getSsid());
        holder.tvCategoryName.setText(network.getCategory());
        if (!displayCategoryName) {
            holder.tvCategoryName.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener((v) -> displayDetailsAction.onClick(network));
        holder.iconShare.setOnClickListener((v) -> shareNetworkAction.onClick(network));
    }

    @Override
    public int getItemCount() {
        return networks.size();
    }
}
