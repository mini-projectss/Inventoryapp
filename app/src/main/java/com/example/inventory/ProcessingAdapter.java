package com.example.inventory;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProcessingAdapter extends RecyclerView.Adapter<ProcessingAdapter.ProcessingViewHolder> {
    private List<InventoryItem> items;

    public ProcessingAdapter(List<InventoryItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ProcessingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_processing, parent, false);
        return new ProcessingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessingViewHolder holder, int position) {
        InventoryItem item = items.get(position);
        holder.nameTextView.setText(item.getName());
        holder.quantityTextView.setText("Required: " + item.getQuantity());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ProcessingViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, quantityTextView;

        public ProcessingViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.processingItemName);
            quantityTextView = itemView.findViewById(R.id.processingItemQuantity);
        }
    }
}
