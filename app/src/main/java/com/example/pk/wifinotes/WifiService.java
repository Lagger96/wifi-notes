package com.example.pk.wifinotes;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.*;

public class WifiService extends Service {

    public static final String NETWORKS_STATUS_CHANGED = "network_scan_results";

    private Timer mTimer;
    private Handler mHandler = new Handler();
    private WifiManager wifiManager;

    private static Set<String> ssidSet = new HashSet<>();
    private static String activeNetworkName;


    BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if (success) {
                scanSuccess();
            } else {
                scanFailure();
            }
        }
    };

    private void scanFailure() {
    }

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();
        Set<String> ssids = new HashSet<>();
        for(ScanResult result : results) {
            ssids.add(result.SSID);
        }
        if(!ssids.equals(ssidSet)){
            ssidSet = ssids;
            emitBroadcast();
        }
    }


    public WifiService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(wifiScanReceiver, intentFilter);

        if (wifiManager.startScan()) {
            scanSuccess();
        }

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new ToastTimerTask(), 500, 5000);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimer.cancel();
        this.unregisterReceiver(wifiScanReceiver);
    }

    public String getCurrentSsid() {
        String ssid = null;
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null) {
            return null;
        }

        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                ssid = connectionInfo.getSSID();
            }
        }
        if (ssid != null && ssid.equals("<unknown ssid>")) {
            ssid = null;
        }
        return ssid;
    }

    class ToastTimerTask extends TimerTask {
        @Override
        public void run() {
            mHandler.post(() -> {
                String ssid = getCurrentSsid();
                if (ssid == null && activeNetworkName != null) {
                    activeNetworkName = null;
                    emitBroadcast();
                } else if (ssid != null && (activeNetworkName == null || !activeNetworkName.equals(ssid))) {
                    activeNetworkName = ssid;
                    emitBroadcast();
                }
            });
        }
    }

    private void emitBroadcast() {
        Intent intent = new Intent(NETWORKS_STATUS_CHANGED);
        if (activeNetworkName != null) {
            NetworkStatusSetter.setActiveNetworkSsid(activeNetworkName.substring(1, activeNetworkName.length() - 1));
        } else {
            NetworkStatusSetter.setActiveNetworkSsid(null);
        }
        if (ssidSet.size() > 0) {
            NetworkStatusSetter.setNearbyNetworksSsids(ssidSet);
        }
        sendBroadcast(intent);
    }
}
