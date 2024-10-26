package com.example.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InventoryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RAW_MATERIALS = "rawMaterials";
   

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String TABLE_PROCESSING_ITEMS = "processingItems";
    public static final String TABLE_FINISHED_GOODS = "finishedGoods";


    
    public InventoryDBHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }


    public InventoryItem getInventoryItemByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM rawMaterials WHERE name=?", new String[]{name});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0); // Retrieve the ID from the first column
            String itemName = cursor.getString(1); // Retrieve the name from the second column
            int quantity = cursor.getInt(2); // Retrieve the quantity from the third column

            InventoryItem item = new InventoryItem(id, itemName, quantity); // Use the correct constructor
            cursor.close();
            return item;
        }
        cursor.close();
        return null;
    }


    public boolean createFinishedGood(String finishedGoodName, Map<Integer, Integer> requiredItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Map.Entry<Integer, Integer> entry : requiredItems.entrySet()) {
                int itemId = entry.getKey();
                int requiredQuantity = entry.getValue();

                Cursor cursor = db.rawQuery("SELECT quantity FROM rawMaterials WHERE _id=?", new String[]{String.valueOf(itemId)});
                if (cursor.moveToFirst()) {
                    int currentQuantity = cursor.getInt(0);
                    if (currentQuantity < requiredQuantity) {
                        cursor.close();
                        db.endTransaction();
                        return false; // Not enough inventory
                    }
                    ContentValues values = new ContentValues();
                    values.put("quantity", currentQuantity - requiredQuantity);
                    db.update("rawMaterials", values, "_id=?", new String[]{String.valueOf(itemId)});
                }
                cursor.close();
            }

            ContentValues finishedGoodValues = new ContentValues();
            finishedGoodValues.put("name", finishedGoodName);
            finishedGoodValues.put("quantity", 1); // Increment by 1 as a new finished good
            db.insert("finishedGoods", null, finishedGoodValues);

            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }







    public void updateInventoryItem(int id, String name, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        db.update(TABLE_RAW_MATERIALS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteInventoryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RAW_MATERIALS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_RAW_MATERIALS = "CREATE TABLE " + TABLE_RAW_MATERIALS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_QUANTITY + " INTEGER);";
        db.execSQL(CREATE_TABLE_RAW_MATERIALS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RAW_MATERIALS);


        onCreate(db);
    }

    public void addRawMaterial(String name, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        db.insert(TABLE_RAW_MATERIALS, null, values);
        db.close();
    }

    public void updateRawMaterial(int id, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, newQuantity);
        db.update(TABLE_RAW_MATERIALS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    // InventoryDBHelper.java

    public void updateSupplyQuantity(int itemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("supply_quantity", quantity);
        db.update(TABLE_RAW_MATERIALS, values, COLUMN_ID + "=?", new String[]{String.valueOf(itemId)});
        db.close();
    }

    public void updateSupplyTime(int itemId, int time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("supply_time", time);
        db.update(TABLE_RAW_MATERIALS, values, COLUMN_ID + "=?", new String[]{String.valueOf(itemId)});
        db.close();
    }

    public void incrementInventoryQuantity(int itemId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_RAW_MATERIALS + " SET " + COLUMN_QUANTITY + " = " + COLUMN_QUANTITY + " + ? WHERE " + COLUMN_ID + " = ?";
        db.execSQL(sql, new Object[]{quantity, itemId});
        db.close();
    }

    public List<InventoryItem> getAllInventoryItems() {
        List<InventoryItem> inventoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RAW_MATERIALS,
                new String[]{COLUMN_ID, COLUMN_NAME, COLUMN_QUANTITY},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUANTITY));
                inventoryList.add(new InventoryItem(id, name, quantity));
            }
            cursor.close();
        }
        return inventoryList;
    }




}
