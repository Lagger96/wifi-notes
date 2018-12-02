package com.example.pk.wifinotes;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.Toast;
import com.example.pk.wifinotes.database.DbHelper;
import com.example.pk.wifinotes.models.Network;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworksActivity extends AppCompatActivity {

    private SavedNetworksFragment savedNetworksFragment = new SavedNetworksFragment();
    private NetworksCategoriesFragment networksCategoriesFragment = new NetworksCategoriesFragment();
    private DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupAddButton();
        setupImportButton();

        ViewPager viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        dataManager = new DataManager(DbHelper.getInstance(this).getWritableDatabase());
    }

    private void setupImportButton() {
        Button importButton = findViewById(R.id.button_import);
        importButton.setOnClickListener((v) -> importNetwork());
    }

    private void importNetwork() {
        startQRScanner();
    }

    private void setupAddButton() {
        Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener((v) -> addNetwork());
    }

    private void addNetwork() {
        Toast.makeText(this, "Add network", Toast.LENGTH_SHORT).show();
    }

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(savedNetworksFragment, getString(R.string.saved_networks));
        adapter.addFragment(networksCategoriesFragment, getString(R.string.networks_categories));
        viewPager.setAdapter(adapter);
    }

    private void startQRScanner() {
        new IntentIntegrator(this).setCaptureActivity(QRScan.class).setOrientationLocked(false).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, R.string.cancelled, Toast.LENGTH_LONG).show();
            } else {
                parseJSON(result.getContents());
                refreshViews();
                Toast.makeText(this, R.string.succes_import , Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void refreshViews() {
        savedNetworksFragment.notifyDataChanged();
        networksCategoriesFragment.notifyDataChanged();
    }

    private void parseJSON(String contents) {
        try {
            JSONObject toImport = new JSONObject(contents);
            if (toImport.has("categoryName")) {
                importCategory(toImport);
            } else {
                importNetwork(toImport, null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void importCategory(JSONObject toImport) throws JSONException {
        String category = toImport.getString("categoryName");
        JSONArray networkList = toImport.getJSONArray("networks");

        for (int i = 0; i < networkList.length(); i++) {
            importNetwork(networkList.getJSONObject(i), category);
        }
    }

    private void importNetwork(JSONObject toImport, String category) throws JSONException {
        Network newNetwork = new Network(null, toImport.getString("ssid"), toImport.getString("password"), "", category);

        Network existingNetwork = dataManager.getNetworkBySSID(newNetwork.getSsid());

        if (existConflict(newNetwork, existingNetwork)) {
            showConflictDialog(newNetwork, existingNetwork);
        } else {
            dataManager.addNetwork(newNetwork);
        }
    }

    private void showConflictDialog(Network newNetwork, Network existingNetwork) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.conflict_title));
        alertDialog.setMessage(getString(R.string.update_password_text) + newNetwork.getSsid());
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.no), (dialog, which) -> dialog.dismiss());
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), (dialog, which) -> {
            existingNetwork.updatePassword(newNetwork.getPassword());
            dataManager.updateNetwork(existingNetwork);
        });
        alertDialog.show();
    }

    private boolean existConflict(Network newNetwork, Network existingNetwork) {
        return existingNetwork != null && !existingNetwork.getPassword().equals(newNetwork.getPassword());
    }
}
