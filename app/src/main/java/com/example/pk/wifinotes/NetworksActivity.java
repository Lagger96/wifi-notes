package com.example.pk.wifinotes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.pk.wifinotes.database.DbHelper;

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
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                importNetwork();
            }
        });
    }

    private void importNetwork() {
        Toast.makeText(this, "Import network", Toast.LENGTH_SHORT).show();
    }

    private void setupAddButton() {
        Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNetwork();
            }
        });
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
}
