package com.bitproject.fahim.homeservice.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.classes.ServiceProvider;

import java.util.ArrayList;

public class AdapterServiceProviderAdmin extends RecyclerView.Adapter<AdapterServiceProviderAdmin.MyViewHolder> {
    private ArrayList<ServiceProvider> serviceProviders;
    private Context ctx;
    private OnItemClickListener mListener;

    public AdapterServiceProviderAdmin(ArrayList<ServiceProvider> serviceProviders, Context ctx) {
        this.serviceProviders = serviceProviders;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_service_provider_list_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ServiceProvider serviceProvider = serviceProviders.get(position);

        holder.mName.setText(serviceProvider.getName());
        holder.mCategory.setText(serviceProvider.getService_type());

    }

    @Override
    public int getItemCount() {
        return serviceProviders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mName, mCategory;
        public MyViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_service_provider_name);
            mCategory = itemView.findViewById(R.id.tv_service_provider_category);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnItemClick(position);
                }
            }
        }
    }
    public  interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
