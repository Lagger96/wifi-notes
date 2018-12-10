package com.example.pk.wifinotes;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pk.wifinotes.database.DbHelper;
import com.example.pk.wifinotes.models.Network;

public class AdderNetworkDialog extends AlertDialog {

    public AdderNetworkDialog(Context context, Runnable refreshViews, @Nullable Network network) {
        super(context);
        setTitle(R.string.add_network);

        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_network_adder, null);
        setView(layout);

        EditText ssidText = layout.findViewById(R.id.ssid);
        if (network != null) {
            ssidText.setText(network.getSsid());
            ssidText.setInputType(InputType.TYPE_NULL);
            ssidText.setFocusable(false);
        }

        setButton(BUTTON_POSITIVE, context.getString(R.string.add), (dialog, which) -> {});
        setOnShowListener(dialog -> getButton(BUTTON_POSITIVE).setOnClickListener(view -> {
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

            Network newNetwork = new Network(null, ssid, password, description, category);

            DataManager dataManager = new DataManager(DbHelper.getInstance(getContext()).getWritableDatabase(), getContext());
            if (dataManager.addNetwork(newNetwork)) {
                Toast.makeText(context, context.getString(R.string.network_save_successful), Toast.LENGTH_SHORT).show();
                refreshViews.run();
                dismiss();
            } else {
                Toast.makeText(context, context.getString(R.string.network_save_failure), Toast.LENGTH_SHORT).show();
            }
        }));
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), (dialog, which) -> {});
    }
}
