package com.bitproject.fahim.homeservice.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.classes.CSOrderItem;

import java.util.ArrayList;

public class AdapterCSOrderItem extends RecyclerView.Adapter<AdapterCSOrderItem.MyViewHolder> {
    private ArrayList<CSOrderItem> csOrderItems;
    private Context ctx;

    public AdapterCSOrderItem(ArrayList<CSOrderItem> csOrderItems, Context ctx) {
        this.csOrderItems = csOrderItems;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cs_order_items_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CSOrderItem csOrderItem = csOrderItems.get(position);
        holder.mItemName.setText(csOrderItem.getService_name());
        holder.mQuantity.setText(String.valueOf(csOrderItem.getQuantity()));
        holder.mSubTotal.setText(String.valueOf(csOrderItem.getTotal_price())+" ৳");
    }

    @Override
    public int getItemCount() {
        return csOrderItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mItemName, mQuantity, mSubTotal;
        public MyViewHolder(View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.tv_cs_item_name);
            mQuantity = itemView.findViewById(R.id.tv_cs_quantity);
            mSubTotal = itemView.findViewById(R.id.tv_cs_sub_total);
        }
    }
}
