package com.example.pk.wifinotes;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class AddNetworkDialog extends AlertDialog {

    public AddNetworkDialog(Context context) {
        super(context);
        setTitle(R.string.add_network);

        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.add_network_dialog, null);
        setView(layout);

        layout.findViewById(R.id.ssid);
        layout.findViewById(R.id.password);
        layout.findViewById(R.id.category);
        layout.findViewById(R.id.description);

        setButton(BUTTON_POSITIVE, context.getString(R.string.add),
                (dialog, which) -> Toast.makeText(context, "ADD", Toast.LENGTH_SHORT).show());
        setButton(BUTTON_NEGATIVE, context.getString(R.string.cancel),
                (dialog, which) -> Toast.makeText(context, "CANCEL", Toast.LENGTH_SHORT).show());
    }
}
