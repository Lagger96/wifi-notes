package com.example.pk.wifinotes;

import android.app.AlertDialog;
import android.content.Context;

public class AddNetworkDialog extends AlertDialog {

    protected AddNetworkDialog(Context context) {
        super(context);
        setTitle(R.string.add_network);
    }
}
