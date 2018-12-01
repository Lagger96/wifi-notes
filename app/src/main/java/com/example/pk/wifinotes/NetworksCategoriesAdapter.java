package com.example.pk.wifinotes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.pk.wifinotes.models.NetworkCategory;

import java.util.List;

public class NetworksCategoriesAdapter extends RecyclerView.Adapter<NetworksCategoriesAdapter.NetworksCategoriesViewHolder> {

    private List<NetworkCategory> categories;
    private Context context;
    private RecyclerView.Adapter adapter;
    private Callbacks callbacks;

    public static class NetworksCategoriesViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryName;
        Button buttonShareCategory;
        RecyclerView rvNetworks;

        public NetworksCategoriesViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            buttonShareCategory = itemView.findViewById(R.id.button_share);
            rvNetworks = itemView.findViewById(R.id.rv_networks_in_category);
            rvNetworks.setHasFixedSize(true);
        }
    }

    public NetworksCategoriesAdapter(List<NetworkCategory> categories, Callbacks callbacks) {
        this.categories = categories;
        this.callbacks = callbacks;
    }

    @Override
    public NetworksCategoriesAdapter.NetworksCategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View networksCategoriesView = inflater.inflate(R.layout.item_network_category, parent, false);

        return new NetworksCategoriesAdapter.NetworksCategoriesViewHolder(networksCategoriesView);
    }

    @Override
    public void onBindViewHolder(NetworksCategoriesViewHolder holder, int position) {
        final NetworkCategory category = categories.get(position);
        holder.tvCategoryName.setText(category.getCategoryName());
        holder.buttonShareCategory.setOnClickListener((v) -> callbacks.shareNetworkCategory.onClick(category));

        holder.rvNetworks.setLayoutManager(new LinearLayoutManager(context));

        adapter = new SavedNetworksAdapter(category.getNetworks(), callbacks,false);
        holder.rvNetworks.setAdapter(adapter);
    }

    @Override
    public int getItemCount() { return categories.size(); }
}
