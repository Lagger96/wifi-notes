package com.example.pk.wifinotes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.pk.wifinotes.models.Network;

import java.util.List;

public class SystemsNetworksAdapter extends RecyclerView.Adapter<SystemsNetworksAdapter.SystemsNetworksViewHolder> {

    private List<Network> networks;
    private Callbacks callbacks;

    public static class SystemsNetworksViewHolder extends RecyclerView.ViewHolder {

        TextView tvNetworkName;
        TextView tvCategoryName;
        ImageView iconWifi;
        ImageView iconShare;

        public SystemsNetworksViewHolder(View itemView) {
            super(itemView);

            tvNetworkName = itemView.findViewById(R.id.tv_network_name);
            tvCategoryName = itemView.findViewById(R.id.tv_network_category);
            iconWifi = itemView.findViewById(R.id.icon_wifi);
            iconShare = itemView.findViewById(R.id.icon_share);
        }
    }

    public SystemsNetworksAdapter(List<Network> networks, Callbacks callbacks) {
        this.networks = networks;
        this.callbacks = callbacks;
    }

    @Override
    public SystemsNetworksAdapter.SystemsNetworksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View savedNetworkView = inflater.inflate(R.layout.item_saved_network, parent, false);

        return new SystemsNetworksAdapter.SystemsNetworksViewHolder(savedNetworkView);
    }

    @Override
    public void onBindViewHolder(SystemsNetworksViewHolder holder, int position) {
        final Network network = networks.get(position);
        holder.iconWifi.setImageResource(R.drawable.ic_wifi_system_24dp);
        holder.tvNetworkName.setText(network.getSsid());
        holder.tvCategoryName.setVisibility(View.GONE);
        holder.itemView.setOnClickListener((v) -> callbacks.displayDetails.onClick(network)); // TODO
        holder.iconShare.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return networks.size();
    }
}
