package com.example.inventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class DashboardActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton menu_button;
    NavigationView nav_view;

    private ArcProgressView arcProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        setContentView(R.layout.activity_dashboard);

        drawerLayout = findViewById(R.id.drawerLayout);
        menu_button = findViewById(R.id.menu_button);
        nav_view = findViewById(R.id.nav_view);

        // Find the ArcProgressView from the layout
        arcProgressView = findViewById(R.id.arcProgressView);

        // Update the ArcProgressView with inventory data
        updateDashboardGraph();

        menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_profile) {
                    startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                } else if (itemId == R.id.nav_inventory) {
                    startActivity(new Intent(DashboardActivity.this, InventoryActivity.class));
                } else if (itemId == R.id.nav_supplies) {
                    startActivity(new Intent(DashboardActivity.this, SupplyActivity.class));
                } else if (itemId == R.id.nav_audit) {
                    startActivity(new Intent(DashboardActivity.this, AuditActivity.class));
                } else if (itemId == R.id.nav_processing) {
                    startActivity(new Intent(DashboardActivity.this, ProcessingActivity.class));
                }

                drawerLayout.close();
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateDashboardGraph();
    }

    private void updateDashboardGraph() {
        SharedPreferences prefs = getSharedPreferences("InventoryTotals", MODE_PRIVATE);

        // Uncomment these lines after testing with hard-coded values
        int rawMaterialsTotal = prefs.getInt("rawMaterialsTotal", 10); // Test with 10
        int processingItemsTotal = prefs.getInt("processingItemsTotal", 30); // Test with 30
        int finishedGoodsTotal = prefs.getInt("finishedGoodsTotal", 60); // Test with 60

        // Update ArcProgressView with test totals
        arcProgressView.setRawMaterialsProgress(rawMaterialsTotal);
        arcProgressView.setProcessingItemsProgress(processingItemsTotal);
        arcProgressView.setFinishedGoodsProgress(finishedGoodsTotal);

        arcProgressView.invalidate(); // Ensure the view is redrawn
    }

}
