package com.example.inventory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class InventoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InventoryAdapter inventoryAdapter;
    private List<InventoryItem> inventoryList = new ArrayList<>();
    private InventoryDBHelper dbHelper;
    private BarChartView barChartView;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        barChartView = findViewById(R.id.barChartView);

        // Firebase authentication setup
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userDatabaseName = "inventory_" + userId + ".db";
            dbHelper = new InventoryDBHelper(this, userDatabaseName);
        }

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.inventoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        inventoryAdapter = new InventoryAdapter(inventoryList, this::showEditItemDialog);
        recyclerView.setAdapter(inventoryAdapter);

        Button addButton = findViewById(R.id.addInventoryButton);
        Button auditButton = findViewById(R.id.auditButton);

        loadInventory();




        // Add button click listener to add items with custom name and quantity
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemDialog();
            }
        });

        // Audit button click listener to edit inventory items
        auditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadInventory(); // Refresh inventory
                Toast.makeText(InventoryActivity.this, "Audit mode: Tap an item to edit or delete.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateBarChart() {
        ArrayList<Integer> barValues = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (InventoryItem item : inventoryList) {
            barValues.add(item.getQuantity());
            labels.add(item.getName());
        }

        // Set the updated data to the BarChartView
        barChartView.setBarData(barValues, labels);
    }




    private void showAddItemDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_inventory_item, null);
        builder.setView(dialogView);

        final EditText itemNameEditText = dialogView.findViewById(R.id.itemNameEditText);
        final EditText itemQuantityEditText = dialogView.findViewById(R.id.itemQuantityEditText);
        Button addButton = dialogView.findViewById(R.id.addButton);

        AlertDialog dialog = builder.create();

        addButton.setOnClickListener(v -> {
            String name = itemNameEditText.getText().toString().trim();
            String quantityStr = itemQuantityEditText.getText().toString().trim();

            if (!name.isEmpty() && !quantityStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);
                addItemToInventory(name, quantity);
                loadInventory();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a valid name and quantity", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void showEditItemDialog(InventoryItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_inventory_item, null);
        builder.setView(dialogView);

        final EditText itemNameEditText = dialogView.findViewById(R.id.itemNameEditText);
        final EditText itemQuantityEditText = dialogView.findViewById(R.id.itemQuantityEditText);
        Button updateButton = dialogView.findViewById(R.id.updateButton);
        Button deleteButton = dialogView.findViewById(R.id.deleteButton);

        itemNameEditText.setText(item.getName());
        itemQuantityEditText.setText(String.valueOf(item.getQuantity()));

        AlertDialog dialog = builder.create();

        updateButton.setOnClickListener(v -> {
            String name = itemNameEditText.getText().toString().trim();
            String quantityStr = itemQuantityEditText.getText().toString().trim();

            if (!name.isEmpty() && !quantityStr.isEmpty()) {
                int quantity = Integer.parseInt(quantityStr);
                dbHelper.updateInventoryItem(item.getId(), name, quantity);
                loadInventory();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a valid name and quantity", Toast.LENGTH_SHORT).show();
            }
        });

        deleteButton.setOnClickListener(v -> {
            dbHelper.deleteInventoryItem(item.getId());
            loadInventory();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void addItemToInventory(String name, int quantity) {
        dbHelper.addRawMaterial(name, quantity);
    }

    private void loadInventory() {
        inventoryList.clear();
        inventoryList.addAll(dbHelper.getAllInventoryItems());
        inventoryAdapter.notifyDataSetChanged();
        updateBarChart(); // Refresh the chart with updated data
    }
    }

