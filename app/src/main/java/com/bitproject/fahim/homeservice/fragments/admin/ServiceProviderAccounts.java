package com.bitproject.fahim.homeservice.fragments.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.adapters.AdapterSPAccount;
import com.bitproject.fahim.homeservice.classes.AdminAccount;
import com.bitproject.fahim.homeservice.classes.SPAccount;
import com.bitproject.fahim.homeservice.classes.ServiceProvider;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ServiceProviderAccounts extends Fragment implements AdapterSPAccount.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private AdapterSPAccount mAdapter;
    private List<SPAccount> spAccounts;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_sp_accounts,container,false);
        spAccounts = new ArrayList<>();

        mRecyclerView = v.findViewById(R.id.rv_service_provider_list_for_payment);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new AdapterSPAccount(spAccounts, getActivity());
        mAdapter.setOnItemClickListener(this);
        getAccountInfo();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Service Provider Accounts");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_button,menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                mAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
    }

    private void getAccountInfo(){
        spAccounts.clear();
        FireDB.SP_ACCOUNTS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    final SPAccount spa = ds.getValue(SPAccount.class);
                    Query query = FireDB.SERVICE_PROVIDERS.orderByChild("sp_id").equalTo(spa.getSp_id());
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                ServiceProvider sp = ds.getValue(ServiceProvider.class);
                                SPAccount spAccount = new SPAccount(spa.getAccount_id(), spa.getSp_id(), spa.getDue_balance(), spa.getPaid_balance(), sp.getName(), sp.getEmail());
                                spAccounts.add(spAccount);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void OnItemClick(int position) {

    }

    @Override
    public void OnPaymentClick(int position) {
        SPAccount spa = spAccounts.get(position);
        addPayment(spa.getAccount_id(), spa.getSp_id(), spa.getDue_balance(), spa.getPaid_balance(), spa.getName(), spa.getEmail());
    }

    private void addPayment(final String account_id, String sp_id, final double due_balance, final double paid_balance, String name, String email) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.add_payment_layout);
        dialog.setTitle("Add Payment");
        TextView tvName = dialog.findViewById(R.id.tv_sp_name);
        TextView tvEmail = dialog.findViewById(R.id.tv_sp_email);
        final EditText etAmount = dialog.findViewById(R.id.et_amount);
        Button btnAdd = dialog.findViewById(R.id.btn_add_payment);

        tvName.setText(name);
        tvEmail.setText(email);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAmount.getText().toString().trim().equals("")){
                    etAmount.setError("Cannot be blank.");
                    etAmount.requestFocus();
                    return;
                }
                if (Double.parseDouble(etAmount.getText().toString().trim())<=0){
                    etAmount.setError("Amount must be greater than 0");
                    etAmount.requestFocus();
                    return;
                }
                final double amount = Double.parseDouble(etAmount.getText().toString().trim());
                double newDue = due_balance - amount;
                double newPaid = paid_balance + amount;
                FireDB.SP_ACCOUNTS.child(account_id).child("due_balance").setValue(newDue);
                FireDB.SP_ACCOUNTS.child(account_id).child("paid_balance").setValue(newPaid);
                FireDB.ADMIN_ACCOUNTS.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.hasChildren()){
                            String admin_account_id = FireDB.ADMIN_ACCOUNTS.push().getKey();
                            AdminAccount adminAccount = new AdminAccount(admin_account_id, amount);
                            FireDB.ADMIN_ACCOUNTS.child(admin_account_id).setValue(adminAccount);
                        }
                        else {
                            for (DataSnapshot ds : dataSnapshot.getChildren()){
                                AdminAccount ad = ds.getValue(AdminAccount.class);
                                double newAmount = ad.getTotal_income()+ amount;
                                FireDB.ADMIN_ACCOUNTS.child(ad.getAccount_id()).child("total_income").setValue(newAmount);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Toast.makeText(getActivity(), "Payment Added Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();

    }


}
