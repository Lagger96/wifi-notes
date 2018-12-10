package com.example.pk.wifinotes;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.support.v4.app.ActivityCompat;
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
    private static final String LAST_SELECTED_FRAGMENT_KEY = "last_selected_fragment";

    private SavedNetworksFragment savedNetworksFragment = new SavedNetworksFragment();
    private NetworksCategoriesFragment networksCategoriesFragment = new NetworksCategoriesFragment();
    private SystemsNetworksFragment systemsNetworksFragment = new SystemsNetworksFragment();
    private DataManager dataManager;
    private ViewPager viewPager;

    private BroadcastReceiver wifiStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshViews();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_networks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupAddButton();
        setupImportButton();
        setupLanguageButton();

        viewPager = findViewById(R.id.viewpager);
        setupViewPager();

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        dataManager = new DataManager(DbHelper.getInstance(this).getWritableDatabase(), this);

        ActivityCompat.requestPermissions(NetworksActivity.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, 1);
        registerReceiver(wifiStatusReceiver, new IntentFilter(WifiService.NETWORKS_STATUS_CHANGED));
        startService(new Intent(this, WifiService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, WifiService.class));
        this.unregisterReceiver(wifiStatusReceiver);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    private void setupImportButton() {
        Button importButton = findViewById(R.id.button_import);
        importButton.setOnClickListener(view -> importNetwork());
    }

    private void importNetwork() {
        startQRScanner();
    }

    private void setupAddButton() {
        Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(view -> addNetwork());
    }

    private void addNetwork() {
        AdderNetworkDialog adderNetworkDialog = new AdderNetworkDialog(this, this::refreshViews);
        adderNetworkDialog.show();
    }

    private void setupLanguageButton() {
        Button languageButton = findViewById(R.id.button_language);
        languageButton.setOnClickListener(view -> changeLanguage());
    }

    private void changeLanguage() {
        LocaleManager.changeLocale(this);
        saveLastSelectedFragment();

        Intent intent = getIntent();
        finish();
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void setupViewPager() {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(savedNetworksFragment, getString(R.string.saved_networks));
        adapter.addFragment(networksCategoriesFragment, getString(R.string.networks_categories));
        adapter.addFragment(systemsNetworksFragment, getString(R.string.systems_networks));
        viewPager.setAdapter(adapter);

        int lastSelectedFragment = getPreferences(MODE_PRIVATE).getInt(LAST_SELECTED_FRAGMENT_KEY, 0);
        viewPager.setCurrentItem(lastSelectedFragment);
    }

    private void startQRScanner() {
        new IntentIntegrator(this).setCaptureActivity(QRScan.class).setOrientationLocked(false).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null && result.getContents() != null) {
            parseJSON(result.getContents());
            Toast.makeText(this, R.string.success_import, Toast.LENGTH_LONG).show();
        }
    }

    public void refreshViews() {
        savedNetworksFragment.notifyDataChanged();
        networksCategoriesFragment.notifyDataChanged();
        systemsNetworksFragment.notifyDataChanged();
    }

    private void parseJSON(String contents) {
        try {
            JSONObject toImport = new JSONObject(contents);
            if (toImport.has("categoryName")) {
                importCategory(toImport);
            } else {
                importNetwork(toImport, "");
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

        if (existingNetwork != null) {
            if (existingNetwork.hasNoCategory()) {
                existingNetwork.assignToCategory(newNetwork.getCategory());
                dataManager.updateNetwork(existingNetwork);
            }

            if (existConflictOnPassword(newNetwork, existingNetwork)) {
                showConflictDialog(newNetwork, existingNetwork);
            }
        } else {
            dataManager.addNetwork(newNetwork);
        }

        refreshViews();
    }

    private void showConflictDialog(Network newNetwork, Network existingNetwork) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(getString(R.string.conflict_title));
        alertDialog.setMessage(String.format(getString(R.string.conflict_dialog_message), newNetwork.getSsid()));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), (dialog, which) -> {});
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), (dialog, which) -> updateNetwork(newNetwork, existingNetwork));
        alertDialog.show();
    }

    private void updateNetwork(Network newNetwork, Network existingNetwork) {
        existingNetwork.updatePassword(newNetwork.getPassword());
        dataManager.updateNetwork(existingNetwork);
        refreshViews();
    }

    private boolean existConflictOnPassword(Network newNetwork, Network existingNetwork) {
        return !existingNetwork.getPassword().equals(newNetwork.getPassword());
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveLastSelectedFragment();
    }

    private void saveLastSelectedFragment() {
        int lastSelectedFragment = viewPager.getCurrentItem();
        getPreferences(MODE_PRIVATE)
                .edit()
                .putInt(LAST_SELECTED_FRAGMENT_KEY, lastSelectedFragment)
                .apply();
    }
}
