package com.example.pk.wifinotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.example.pk.wifinotes.database.DbHelper;
import com.example.pk.wifinotes.models.Network;

public class NetworksActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networks);
        dbHelper = DbHelper.getInstance(this);
        dataManager = new DataManager(dbHelper.getWritableDatabase());
        testDataManagerFunctions();
    }

    private void testDataManagerFunctions() {
        dataManager.addNetwork(new Network(null, "TEST", "123", "test", "test"));
        Log.i("Categories", dataManager.getNetworkCategories().toString());
        Log.i("Networks", dataManager.getNetworks().toString());
        Log.i("Category1:", dataManager.getNetworkCategory("Kategoria1").toString());
        Log.i("Net with id: 1", dataManager.getNetwork(1).toString());
        dataManager.updateNetwork(new Network(1, "UPDATED", "123", "UPDATED", "test"));
        Log.i("Updated net with id: 1", dataManager.getNetwork(1).toString());
    }
}
