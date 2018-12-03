package com.example.pk.wifinotes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.pk.wifinotes.models.Network;
import com.example.pk.wifinotes.models.NetworkCategory;

public class Callbacks {

    private static final String CLIPBOARD_LABEL = "wifi_notes_network_password";
    private Context context;
    OnClickNetworkAction displayDetails;
    OnClickNetworkAction shareNetwork;
    OnClickCategoryAction shareNetworkCategory;
    private Runnable refreshView;

    public Callbacks(Context context, Runnable refreshView) {
        this.context = context;
        this.displayDetails = this::displayDetails;
        this.shareNetwork = this::shareNetwork;
        this.shareNetworkCategory = this::shareNetworkCategory;
        this.refreshView = refreshView;
    }

    private void shareNetworkCategory(NetworkCategory networkCategory) {
        Intent intent = new Intent(context, QRActivity.class);
        intent.putExtra(QRActivity.CATEGORY_NAME_PARAM, networkCategory.getCategoryName());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void shareNetwork(Network network) {
        Intent intent = new Intent(context, QRActivity.class);
        intent.putExtra(QRActivity.NETWORK_ID_PARAM, network.getId());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    private void displayDetails(Network network) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.dialog_network_details, null);

        final TextView networkName = layout.findViewById(R.id.details_network_name);
        final TextView categoryName = layout.findViewById(R.id.details_category_name);
        final TextView password = layout.findViewById(R.id.details_password);
        final TextView description = layout.findViewById(R.id.details_description);
        final Button button = layout.findViewById(R.id.button_copy);

        button.setOnClickListener((v) -> copyToClipboard(network.getPassword()));

        networkName.setText(network.getSsid());
        categoryName.setText(network.getCategory());
        password.setText(network.getPassword());
        description.setText(network.getDescription());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.setView(layout)
                .setPositiveButton(context.getString(R.string.edit), (dialogInterface, i) -> editNetwork(network))
                .create();
        dialog.show();
    }

    private void copyToClipboard(String password) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(CLIPBOARD_LABEL, password);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, R.string.toast_password_copied, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.toast_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void editNetwork(Network network) {
        EditorNetworkDialog editorNetworkDialog = new EditorNetworkDialog(context, refreshView, network);
        editorNetworkDialog.show();
    }
}
