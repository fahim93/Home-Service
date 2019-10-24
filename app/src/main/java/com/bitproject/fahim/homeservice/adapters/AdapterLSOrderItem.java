package com.bitproject.fahim.homeservice.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.classes.LSOrderItem;

import java.util.ArrayList;

public class AdapterLSOrderItem extends RecyclerView.Adapter<AdapterLSOrderItem.MyViewHolder> {
    private ArrayList<LSOrderItem> mensWearList;
    private Context ctx;

    public AdapterLSOrderItem(ArrayList<LSOrderItem> mensWearList, Context ctx) {
        this.mensWearList = mensWearList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ls_order_items_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LSOrderItem lsOrderItem = mensWearList.get(position);
        holder.mItemName.setText(lsOrderItem.getItem_name());
        holder.mQuantity.setText(String.valueOf(lsOrderItem.getQuantity()));
        holder.mPrice.setText(String.valueOf(lsOrderItem.getPrice())+" ৳");
        holder.mType.setText(lsOrderItem.getService_type());
        holder.mItemType.setText(lsOrderItem.getItem_type());
    }

    @Override
    public int getItemCount() {
        return mensWearList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView mItemName, mQuantity, mPrice, mType, mItemType;
        public MyViewHolder(View itemView) {
            super(itemView);
            mItemName = itemView.findViewById(R.id.tv_ls_item_name);
            mQuantity = itemView.findViewById(R.id.tv_ls_quantity);
            mPrice = itemView.findViewById(R.id.tv_ls_price);
            mType = itemView.findViewById(R.id.tv_ls_type);
            mItemType = itemView.findViewById(R.id.tv_ls_item_type);
        }
    }
}
