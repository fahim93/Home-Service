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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.classes.CleaningService;

import java.util.ArrayList;

public class AdapterCleaningServiceClient extends RecyclerView.Adapter<AdapterCleaningServiceClient.MyViewHolder>{
    private ArrayList<CleaningService> cleaningServices = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mListener;

    public AdapterCleaningServiceClient(ArrayList<CleaningService> cleaningServices, Context ctx) {
        this.cleaningServices = cleaningServices;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.client_cleaning_service_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CleaningService cleaningService = cleaningServices.get(position);
        holder.mServiceName.setText(cleaningService.getCs_name());
        holder.mServiceStartingPrice.setText("Starting Price: " + String.valueOf(cleaningService.getCs_starting_price())+" ৳");
        holder.mServicePrice.setText("Service Price: " + String.valueOf(cleaningService.getCs_price())+" ৳");
        holder.mSectionServiceDetails.setId(position);
        holder.mMinusButton.setId(position);
        holder.mPlusButton.setId(position);
        holder.mValueButton.setId(position);
        holder.mAddButton.setId(position);
        holder.mRemoveButton.setId(position);
    }

    @Override
    public int getItemCount() {
        return cleaningServices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        private TextView mServiceName, mServiceStartingPrice, mServicePrice, mServiceDescription;
        private LinearLayout mSectionServiceDetails;
        private Button mMinusButton, mPlusButton, mValueButton, mAddButton, mRemoveButton;
        public MyViewHolder(View itemView) {
            super(itemView);
            mServiceName = itemView.findViewById(R.id.tv_cs_item_name);
            mServiceStartingPrice = itemView.findViewById(R.id.tv_cs_starting_price);
            mServicePrice = itemView.findViewById(R.id.tv_cs_price);
            mServiceDescription = itemView.findViewById(R.id.tv_cs_description);
            mSectionServiceDetails = itemView.findViewById(R.id.section_cs_details);
            mMinusButton = itemView.findViewById(R.id.btn_quantity_decrement);
            mPlusButton = itemView.findViewById(R.id.btn_quantity_increment);
            mValueButton = itemView.findViewById(R.id.btn_quantity_value);
//            mAddButton = itemView.findViewById(R.id.btn_cs_add);
//            mRemoveButton = itemView.findViewById(R.id.btn_cs_remove);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    mListener.OnItemClick(position, mSectionServiceDetails, mMinusButton,mPlusButton,mValueButton, mAddButton, mRemoveButton);
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
        void OnItemClick(int position, LinearLayout mLayout, Button mMinus, Button mPlus, Button mValue, Button mAdd, Button mRemove);
        void OnEditClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
