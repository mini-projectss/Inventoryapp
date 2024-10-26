package com.example.inventory;

public class InventoryItem {
    private int id;         // New field for item ID
    private String name;
    private int quantity;
    private int supplyTime;
    private int supplyQuantity;

    // Constructor with id
    public InventoryItem(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    // Getters and Setters for supplyTime and supplyQuantity
    public int getSupplyTime() {
        return supplyTime;
    }

    public void setSupplyTime(int supplyTime) {
        this.supplyTime = supplyTime;
    }

    public int getSupplyQuantity() {
        return supplyQuantity;
    }

    public void setSupplyQuantity(int supplyQuantity) {
        this.supplyQuantity = supplyQuantity;
    }

    public void setId(int id) {
        this.id = id;
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
