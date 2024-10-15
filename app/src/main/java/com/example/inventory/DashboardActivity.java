package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.inventory.R;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

        // Set the segment values and colors
        float[] segmentValues = new float[]{30f, 45f, 25f}; // Adjust the percentage values
        int[] segmentColors = new int[]{
                ContextCompat.getColor(this, R.color.colorSegment1), // #00FFFF
                ContextCompat.getColor(this, R.color.colorSegment2), // #FF7B69
                ContextCompat.getColor(this, R.color.colorSegment3)  // #8C66CC
        };
        String[] segmentLabels = new String[]{"", "", ""}; // Custom labels

        arcProgressView.setSegmentValues(segmentValues);
        arcProgressView.setSegmentColors(segmentColors);
        arcProgressView.setSegmentLabels(segmentLabels); // Set the custom labels

        arcProgressView.invalidate();

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

                if(itemId == R.id.nav_profile){
                    startActivity(new Intent(DashboardActivity.this, ProfileActivity.class));
                }
                drawerLayout.close();
                return false;
            }
        });


    }
}
