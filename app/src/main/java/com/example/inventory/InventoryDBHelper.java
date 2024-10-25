package com.example.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class InventoryDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "inventory.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_RAW_MATERIALS = "rawMaterials";
    public static final String TABLE_PROCESSING_ITEMS = "processingItems";
    public static final String TABLE_FINISHED_GOODS = "finishedGoods";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";

    public InventoryDBHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
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
