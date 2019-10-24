package com.bitproject.fahim.homeservice.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.classes.LaundryService;

import java.util.ArrayList;

public class AdapterLaundryService extends RecyclerView.Adapter<AdapterLaundryService.MyViewHolder>{
    private ArrayList<LaundryService> laundry_services = new ArrayList<>();
    private Context ctx;
    private OnItemClickListener mListener;

    public AdapterLaundryService(ArrayList<LaundryService> laundry_services, Context ctx) {
        this.laundry_services = laundry_services;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.laundry_service_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LaundryService laundryService = laundry_services.get(position);
        holder.mLuandryItemName.setText(laundryService.getLaundry_service_item());
    }

    @Override
    public int getItemCount() {
        return laundry_services.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        private TextView mLuandryItemName;
        public MyViewHolder(View itemView) {
            super(itemView);
            mLuandryItemName = itemView.findViewById(R.id.tv_ls_item_name);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
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

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem edit = menu.add(Menu.NONE,1,1,"Edit");

            edit.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.OnEditClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }

    public  interface OnItemClickListener{
        void OnItemClick(int position);
        void OnEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
