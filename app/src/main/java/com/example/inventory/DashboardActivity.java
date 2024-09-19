package com.example.inventory;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity extends AppCompatActivity {

    private ArcProgressView arcProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        setContentView(R.layout.activity_dashboard);

        // Find the ArcProgressView from the layout
        arcProgressView = findViewById(R.id.arcProgressView);



        // Set the segment values and colors
        float[] segmentValues = new float[]{30f, 45f, 25f}; // Adjust the percentage values
        int[] segmentColors = new int[]{
                getResources().getColor(R.color.colorSegment1), // #00FFFF
                getResources().getColor(R.color.colorSegment2), // #FF7B69
                getResources().getColor(R.color.colorSegment3)  // #8C66CC
        };
        String[] segmentLabels = new String[]{"", "", ""}; // Custom labels

        arcProgressView.setSegmentValues(segmentValues);
        arcProgressView.setSegmentColors(segmentColors);
        arcProgressView.setSegmentLabels(segmentLabels); // Set the custom labels

        arcProgressView.invalidate();
    }
}



