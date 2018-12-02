package com.example.pk.wifinotes;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pk.wifinotes.database.DbHelper;
import com.example.pk.wifinotes.models.Network;

public class AdderNetworkDialog extends AlertDialog {

    public AdderNetworkDialog(Context context, Runnable callback) {
        super(context);
        setTitle(R.string.add_network);

        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_network_adder, null);
        setView(layout);

        setButton(BUTTON_POSITIVE, context.getString(R.string.add), (dialog, which) -> {});
        setOnShowListener(dialog -> getButton(BUTTON_POSITIVE).setOnClickListener(view -> {
            EditText ssidText = layout.findViewById(R.id.ssid);
            EditText passwordText = layout.findViewById(R.id.password);
            EditText categoryText = layout.findViewById(R.id.category);
            EditText descriptionText = layout.findViewById(R.id.description);

            String ssid = ssidText.getText().toString();
            String password = passwordText.getText().toString();
            String category = categoryText.getText().toString();
            String description = descriptionText.getText().toString();

            if (ssid.equals("") || password.equals("")) {
                Toast.makeText(context, context.getString(R.string.ssid_or_password_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            Network network = new Network(null, ssid, password, description, category);

            DataManager dataManager = new DataManager(DbHelper.getInstance(getContext()).getWritableDatabase());
            if(dataManager.addNetwork(network)) {
                Toast.makeText(context, context.getString(R.string.network_save_successful), Toast.LENGTH_SHORT).show();
                callback.run();
                dismiss();
            } else {
                Toast.makeText(context, context.getString(R.string.network_save_failure), Toast.LENGTH_SHORT).show();
            }
        }));
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), (dialog, which) -> {});
    }
}
