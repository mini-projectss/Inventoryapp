package com.example.inventory;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class SupplyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Enable back button

        toolbar.setNavigationOnClickListener(v -> finish()); // Close the activity when back button is clicked
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Close the activity on navigation up
        return true;
    }
}