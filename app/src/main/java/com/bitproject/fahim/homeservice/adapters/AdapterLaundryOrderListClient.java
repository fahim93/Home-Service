package com.bitproject.fahim.homeservice.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.classes.LSOrder;

import java.util.ArrayList;

public class AdapterLaundryOrderListClient extends RecyclerView.Adapter<AdapterLaundryOrderListClient.MyViewHolder> {
    private ArrayList<LSOrder> lsOrders;
    private Context ctx;
    private OnItemClickListener mListener;

    public AdapterLaundryOrderListClient(ArrayList<LSOrder> lsOrders, Context ctx) {
        this.lsOrders = lsOrders;
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
        LSOrder lsOrder = lsOrders.get(position);

        holder.mServiceCategory.setText(lsOrder.getService_category());
        holder.mOrderDate.setText(lsOrder.getOrder_date());

    }

    @Override
    public int getItemCount() {
        return lsOrders.size();
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
                    mListener.OnItemClickLaundry(position);
                }
            }
        }
    }
    public  interface OnItemClickListener{
        void OnItemClickLaundry(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
