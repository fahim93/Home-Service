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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.classes.CleaningService;

import java.util.ArrayList;

public class AdapterCleaningService extends RecyclerView.Adapter<AdapterCleaningService.MyViewHolder>{
    private ArrayList<CleaningService> cleaningServices = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mListener;

    public AdapterCleaningService(ArrayList<CleaningService> cleaningServices, Context ctx) {
        this.cleaningServices = cleaningServices;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_cleaning_service_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CleaningService cleaningService = cleaningServices.get(position);
        holder.mServiceName.setText(cleaningService.getCs_name());
        holder.mServiceStartingPrice.setText("Starting Price: " + String.valueOf(cleaningService.getCs_starting_price())+" ৳");
        holder.mServicePrice.setText("Price: " + String.valueOf(cleaningService.getCs_price())+" ৳");
        holder.mServiceDescription.setText(cleaningService.getCs_description());
        holder.mSectionServiceDetails.setId(position);
    }

    @Override
    public int getItemCount() {
        return cleaningServices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        private TextView mServiceName, mServiceStartingPrice, mServicePrice, mServiceDescription;
        private LinearLayout mSectionServiceDetails;
        public MyViewHolder(View itemView) {
            super(itemView);
            mServiceName = itemView.findViewById(R.id.tv_cs_item_name);
            mServiceStartingPrice = itemView.findViewById(R.id.tv_cs_starting_price);
            mServicePrice = itemView.findViewById(R.id.tv_cs_price);
            mServiceDescription = itemView.findViewById(R.id.tv_cs_description);
            mSectionServiceDetails = itemView.findViewById(R.id.section_cs_details);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnItemClick(position, mSectionServiceDetails);
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

    public interface OnItemClickListener{
        void OnItemClick(int position, LinearLayout mLayout);
        void OnEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
