package com.example.inventory;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProcessingAdapter adapter;
    private InventoryDBHelper dbHelper;
    private List<InventoryItem> selectedItems;
    private Handler handler;

    private EditText finishedGoodNameEditText, productionTimeEditText;
    private Button addItemButton, startProductionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        dbHelper = new InventoryDBHelper(this, "inventory_" + FirebaseAuth.getInstance().getCurrentUser().getUid() + ".db");

        finishedGoodNameEditText = findViewById(R.id.finishedGoodName);
        productionTimeEditText = findViewById(R.id.productionTime);
        addItemButton = findViewById(R.id.addProcessingItemButton);
        startProductionButton = findViewById(R.id.startProductionButton);

        recyclerView = findViewById(R.id.processingRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedItems = new ArrayList<>();
        adapter = new ProcessingAdapter(selectedItems);
        recyclerView.setAdapter(adapter);

        addItemButton.setOnClickListener(v -> showAddItemDialog());
        startProductionButton.setOnClickListener(v -> startProduction());

        handler = new Handler();
    }

    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_processing_item, null);
        builder.setView(dialogView);

        final EditText itemNameEditText = dialogView.findViewById(R.id.processingItemName);
        final EditText itemQuantityEditText = dialogView.findViewById(R.id.processingItemQuantity);
        Button addItemButton = dialogView.findViewById(R.id.addProcessingDialogButton);

        AlertDialog dialog = builder.create();

        addItemButton.setOnClickListener(v -> {
            String itemName = itemNameEditText.getText().toString().trim();
            String quantityStr = itemQuantityEditText.getText().toString().trim();

            if (!itemName.isEmpty() && !quantityStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);

                InventoryItem item = dbHelper.getInventoryItemByName(itemName);
                if (item != null) {
                    item.setQuantity(quantity);
                    selectedItems.add(item);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Item not found in inventory", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please enter valid item name and quantity", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void startProduction() {
        String finishedGoodName = finishedGoodNameEditText.getText().toString().trim();
        String productionTimeStr = productionTimeEditText.getText().toString().trim();

        if (!finishedGoodName.isEmpty() && !productionTimeStr.isEmpty()) {
            int productionTime = Integer.parseInt(productionTimeStr) * 3600000; // convert hours to milliseconds

            Map<Integer, Integer> requiredItems = new HashMap<>();
            for (InventoryItem item : selectedItems) {
                requiredItems.put(item.getId(), item.getQuantity());
            }

            handler.postDelayed(() -> {
                boolean success = dbHelper.createFinishedGood(finishedGoodName, requiredItems);
                if (success) {
                    Toast.makeText(this, finishedGoodName + " production completed", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "Insufficient inventory for production", Toast.LENGTH_SHORT).show();
                }
            }, productionTime);

            Toast.makeText(this, "Production started for " + finishedGoodName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please enter finished good name and production time", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
