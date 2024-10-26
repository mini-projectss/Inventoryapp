package com.example.inventory;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SupplyAdapter extends RecyclerView.Adapter<SupplyAdapter.SupplyViewHolder> {
    private Context context;
    private List<InventoryItem> supplyItems;
    private InventoryDBHelper dbHelper;

    public SupplyAdapter(Context context, List<InventoryItem> supplyItems, InventoryDBHelper dbHelper) {
        this.context = context;
        this.supplyItems = supplyItems;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public SupplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_supply, parent, false);
        return new SupplyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SupplyViewHolder holder, int position) {
        InventoryItem item = supplyItems.get(position);
        holder.nameTextView.setText(item.getName());
        holder.quantityEditText.setText(String.valueOf(item.getSupplyQuantity()));
        holder.timeEditText.setText(String.valueOf(item.getSupplyTime()));

        holder.quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed during text change
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int quantity = Integer.parseInt(s.toString());
                    item.setSupplyQuantity(quantity);
                    dbHelper.updateSupplyQuantity(item.getId(), quantity);
                } catch (NumberFormatException e) {
                    // Handle case where input is invalid or empty
                }
            }
        });

        holder.timeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text is changed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed during text change
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int time = Integer.parseInt(s.toString());
                    item.setSupplyTime(time);
                    dbHelper.updateSupplyTime(item.getId(), time);
                } catch (NumberFormatException e) {
                    // Handle case where input is invalid or empty
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return supplyItems.size();
    }

    public static class SupplyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        EditText quantityEditText, timeEditText;

        public SupplyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.supplyItemName);
            quantityEditText = itemView.findViewById(R.id.supplyQuantity);
            timeEditText = itemView.findViewById(R.id.supplyTime);
        }
    }
}
