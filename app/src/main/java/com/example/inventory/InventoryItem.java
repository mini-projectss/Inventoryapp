package com.example.inventory;

public class InventoryItem {
    private int id;         // New field for item ID
    private String name;
    private int quantity;

    // Constructor with id
    public InventoryItem(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
