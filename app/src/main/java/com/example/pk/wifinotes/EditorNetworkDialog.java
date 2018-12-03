package com.example.pk.wifinotes;

import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.pk.wifinotes.database.DbHelper;
import com.example.pk.wifinotes.models.Network;

public class EditorNetworkDialog extends AlertDialog {

    public EditorNetworkDialog(Context context, Runnable callback, Network network) {
        super(context);
        setTitle(R.string.edit_network);

        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.dialog_network_adder, null);
        setView(layout);

        EditText ssidText = layout.findViewById(R.id.ssid);
        EditText passwordText = layout.findViewById(R.id.password);
        EditText categoryText = layout.findViewById(R.id.category);
        EditText descriptionText = layout.findViewById(R.id.description);

        ssidText.setText(network.getSsid());
        passwordText.setText(network.getPassword());
        categoryText.setText(network.getCategory());
        descriptionText.setText(network.getDescription());

        ssidText.setInputType(InputType.TYPE_NULL);

        setButton(BUTTON_POSITIVE, context.getString(R.string.edit), (dialog, which) -> {});
        setOnShowListener(dialog -> getButton(BUTTON_POSITIVE).setOnClickListener(view -> {
            String ssid = network.getSsid();
            String password = passwordText.getText().toString();
            String category = categoryText.getText().toString();
            String description = descriptionText.getText().toString();

            if (password.equals("")) {
                Toast.makeText(context, context.getString(R.string.password_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            Network modifiedNetwork = new Network(null, ssid, password, description, category);

            DataManager dataManager = new DataManager(DbHelper.getInstance(getContext()).getWritableDatabase());
            if(dataManager.updateNetwork(modifiedNetwork)) {
                Toast.makeText(context, context.getString(R.string.network_edit_successful), Toast.LENGTH_SHORT).show();
                //callback.run();
                dismiss();
            } else {
                //Toast.makeText(context, context.getString(R.string.network_edit_failure), Toast.LENGTH_SHORT).show();
            }
        }));
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel), (dialog, which) -> {});
    }
}
