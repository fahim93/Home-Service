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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.classes.SPAccount;

import java.util.ArrayList;
import java.util.List;

public class AdapterSPAccount extends RecyclerView.Adapter<AdapterSPAccount.MyViewHolder>{
    private List<SPAccount> spAccounts;
    private List<SPAccount> spAccountsFull;
    private Context ctx;
    private OnItemClickListener mListener;

    public AdapterSPAccount(List<SPAccount> spAccounts, Context ctx) {
        this.spAccounts = spAccounts;
        spAccountsFull = new ArrayList<>(spAccounts);
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_sp_accounts_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SPAccount spAccount = spAccounts.get(position);

        holder.mName.setText(spAccount.getName());
        holder.mEmail.setText(spAccount.getEmail());
        holder.mDue.setText(String.valueOf(spAccount.getDue_balance())+" ৳");
        holder.mPaid.setText(String.valueOf(spAccount.getPaid_balance())+" ৳");

    }

    @Override
    public int getItemCount() {
        return spAccounts.size();
    }

//    @Override
//    public Filter getFilter() {
//        return filterData;
//    }
//
//    private Filter filterData = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            List<SPAccount> filteredList = new ArrayList<>();
//            if (constraint == null || constraint.length() == 0){
//                filteredList.addAll(spAccountsFull);
//            }else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//                for (SPAccount spAccount : spAccountsFull){
//                    if (spAccount.getName().toLowerCase().contains(filterPattern)){
//                        filteredList.add(spAccount);
//                    }
//                }
//            }
//            FilterResults results = new FilterResults();
//            results.values = filteredList;
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            spAccounts.clear();
//            spAccounts.addAll((List)results.values);
//            notifyDataSetChanged();
//        }
//    };

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        private TextView mName, mEmail, mDue, mPaid;
        public MyViewHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_sp_name);
            mEmail = itemView.findViewById(R.id.tv_sp_email);
            mDue = itemView.findViewById(R.id.tv_sp_due);
            mPaid = itemView.findViewById(R.id.tv_sp_paid);
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
            MenuItem add_payment = menu.add(Menu.NONE,1,1,"Add Payment");

            add_payment.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null){
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION){
                    switch (item.getItemId()){
                        case 1:
                            mListener.OnPaymentClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public  interface OnItemClickListener{
        void OnItemClick(int position);
        void OnPaymentClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
}
