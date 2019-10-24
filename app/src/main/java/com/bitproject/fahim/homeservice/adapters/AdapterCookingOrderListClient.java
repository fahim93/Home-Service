package com.bitproject.fahim.homeservice.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.classes.CookingServiceOrder;

import java.util.ArrayList;

public class AdapterCookingOrderListClient extends RecyclerView.Adapter<AdapterCookingOrderListClient.MyViewHolder> {
    private ArrayList<CookingServiceOrder> cookingServiceOrders;
    private Context ctx;
    private OnItemClickListener mListener;

    public AdapterCookingOrderListClient(ArrayList<CookingServiceOrder> cookingServiceOrders, Context ctx) {
        this.cookingServiceOrders = cookingServiceOrders;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_order_list_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CookingServiceOrder cookingServiceOrder = cookingServiceOrders.get(position);

        holder.mServiceCategory.setText(cookingServiceOrder.getService_category());
        holder.mOrderDate.setText(cookingServiceOrder.getOrder_date());

    }

    @Override
    public int getItemCount() {
        return cookingServiceOrders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mServiceCategory, mOrderDate;
        public MyViewHolder(View itemView) {
            super(itemView);
            mServiceCategory = itemView.findViewById(R.id.tv_service_category);
            mOrderDate = itemView.findViewById(R.id.tv_order_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnItemClickCooking(position);
                }
            }
        }
    }
    public  interface OnItemClickListener{
        void OnItemClickCooking(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
