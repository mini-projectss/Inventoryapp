package com.example.inventory;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SupplyActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SupplyAdapter adapter;
    private InventoryDBHelper dbHelper;
    private List<InventoryItem> supplyItems;
    private Handler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply);

        dbHelper = new InventoryDBHelper(this, "inventory_" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".db");

        recyclerView = findViewById(R.id.supplyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        supplyItems = dbHelper.getAllInventoryItems(); // Fetch items from inventory
        for (InventoryItem item : supplyItems) {
            item.setSupplyTime(1);  // Default supply time 1 hour
            item.setSupplyQuantity(10);  // Default supply quantity 10
        }
        adapter = new SupplyAdapter(this, supplyItems, dbHelper);
        recyclerView.setAdapter(adapter);

        handler = new Handler();
        startSupplyUpdateLoop();
    }

    private void startSupplyUpdateLoop() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (InventoryItem item : supplyItems) {
                    dbHelper.incrementInventoryQuantity(item.getId(), item.getSupplyQuantity());
                }
                adapter.notifyDataSetChanged();
                handler.postDelayed(this, 3600000); // Run every hour
            }
        }, 3600000); // Initial delay of 1 hour
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
