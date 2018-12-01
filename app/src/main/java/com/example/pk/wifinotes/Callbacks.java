package com.example.pk.wifinotes;

import android.content.Context;
import android.widget.Toast;
import com.example.pk.wifinotes.models.Network;
import com.example.pk.wifinotes.models.NetworkCategory;

public class Callbacks {

    Context context;
    OnClickNetworkAction displayDetails;
    OnClickNetworkAction shareNetwork;
    OnClickCategoryAction shareNetworkCategory;

    public Callbacks(Context context) {
        this.context = context;
        this.displayDetails = this::displayDetails;
        this.shareNetwork = this::shareNetwork;
        this.shareNetworkCategory = this::shareNetworkCategory;
    }

    private void shareNetworkCategory(NetworkCategory networkCategory) {
        Toast.makeText(context, "Category share", Toast.LENGTH_SHORT).show();
    }

    private void shareNetwork(Network network) {
        Toast.makeText(context, "Network share", Toast.LENGTH_SHORT).show();
    }

    private void displayDetails(Network network) {
        Toast.makeText(context, "Network details", Toast.LENGTH_SHORT).show();
    }
}
