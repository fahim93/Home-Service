package com.bitproject.fahim.homeservice.fragments.admin;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.adapters.AdapterCleaningService;
import com.bitproject.fahim.homeservice.classes.CleaningService;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CleaningServiceFragment extends Fragment implements AdapterCleaningService.OnItemClickListener{
    private RecyclerView mCleaningServicesRV;
    private AdapterCleaningService mAdapter;
    private ArrayList<CleaningService>cleaningServices;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_cleaning_service_layout, container, false);
        mCleaningServicesRV = v.findViewById(R.id.rv_cleaning_services);
        cleaningServices = new ArrayList<>();

        mCleaningServicesRV.setHasFixedSize(true);
        mCleaningServicesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCleaningServicesRV.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new AdapterCleaningService(cleaningServices,getActivity());
        mAdapter.setOnItemClickListener(CleaningServiceFragment.this);

        FireDB.CLEANING_SERVICES.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cleaningServices.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        CleaningService cleaningService = ds.getValue(CleaningService.class);
                        cleaningServices.add(cleaningService);
                    }
                    mCleaningServicesRV.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cleaning Service");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.add_button,menu);
        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add){
            openAddServiceDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openAddServiceDialog(){
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.admin_add_cleaning_service_layout);
        dialog.setTitle("Add New Cleaning Service");
        final EditText mServiceNameET = dialog.findViewById(R.id.et_cleaning_service_name);
        final EditText mServiceStartingPriceET = dialog.findViewById(R.id.et_cleaning_service_starting_price);
        final EditText mServicePriceET = dialog.findViewById(R.id.et_cleaning_service_price);
        final EditText mServiceDescriptionET = dialog.findViewById(R.id.et_cleaning_service_description);
        final Button mAddServiceBtn = dialog.findViewById(R.id.btn_add_cleaning_service);

        mAddServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mServiceNameET.getText().toString().trim().isEmpty()){
                    mServiceNameET.setError("This Field Cannot Be Blank");
                    mServiceNameET.requestFocus();
                    return;
                }
                if (mServiceDescriptionET.getText().toString().trim().isEmpty()){
                    mServiceDescriptionET.setError("This Field Cannot Be Blank");
                    mServiceDescriptionET.requestFocus();
                    return;
                }
                if (mServiceStartingPriceET.getText().toString().trim().isEmpty()){
                    mServiceStartingPriceET.setError("This Field Cannot Be Blank");
                    mServiceStartingPriceET.requestFocus();
                    return;
                }
                if (mServicePriceET.getText().toString().trim().isEmpty()){
                    mServicePriceET.setError("This Field Cannot Be Blank");
                    mServicePriceET.requestFocus();
                    return;
                }
                String cs_name = mServiceNameET.getText().toString().trim();
                String cs_description = mServiceDescriptionET.getText().toString().trim();
                double cs_starting_price = Double.parseDouble(mServiceStartingPriceET.getText().toString().trim());
                double cs_price = Double.parseDouble(mServicePriceET.getText().toString().trim());
                addCleaningService(cs_name, cs_description, cs_starting_price,cs_price);
                dialog.dismiss();


            }
        });
        dialog.show();
    }

    private void addCleaningService(String cs_name, String cs_description, double cs_starting_price, double cs_price){
        String cs_id = FireDB.CLEANING_SERVICES.push().getKey();
        CleaningService cleaningService = new CleaningService(cs_id, cs_name, cs_description,cs_starting_price,cs_price);
        FireDB.CLEANING_SERVICES.child(cs_id).setValue(cleaningService)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(), "Service Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void OnItemClick(int position, LinearLayout mLayout) {
        CleaningService cs = cleaningServices.get(position);
        if (mLayout.getId() == position){
            if (mLayout.getVisibility()==View.GONE){
                mLayout.setVisibility(View.VISIBLE);
            }else {
                mLayout.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public void OnEditClick(int position) {

    }
}
