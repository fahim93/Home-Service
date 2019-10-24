package com.bitproject.fahim.homeservice.fragments.admin;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.adapters.AdapterServiceProviderAdmin;
import com.bitproject.fahim.homeservice.classes.CSOrder;
import com.bitproject.fahim.homeservice.classes.CookingServiceOrder;
import com.bitproject.fahim.homeservice.classes.LSOrder;
import com.bitproject.fahim.homeservice.classes.ServiceProvider;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageServiceProviderFragment extends Fragment implements AdapterServiceProviderAdmin.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private AdapterServiceProviderAdmin mAdapter;
    private ArrayList<ServiceProvider> serviceProviders;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_manage_service_provider_layout, container, false);
        mRecyclerView = v.findViewById(R.id.rv_service_provider_list_admin);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        serviceProviders = new ArrayList<>();
        mAdapter = new AdapterServiceProviderAdmin(serviceProviders,getActivity());
        mAdapter.setOnItemClickListener(this);
        getServiceProviders();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Manage Service Provider");
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
            getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.admin_content_container, new AddServiceProviderFragment())
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnItemClick(int position) {
        ServiceProvider sp = serviceProviders.get(position);
        viewSPDetails(sp.getSp_id(), sp.getName(), sp.getGender(), sp.getEmail(), sp.getPhone(), sp.getService_type());
    }

    private void getServiceProviders() {
        FireDB.SERVICE_PROVIDERS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceProviders.clear();
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        ServiceProvider sp = ds.getValue(ServiceProvider.class);
                        serviceProviders.add(sp);
                    }
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void viewSPDetails(final String sp_id, String name, String gender, String email, final String phone, String service_category){
        final Dialog dialog=new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.admin_service_provider_info);
        dialog.setTitle("Service Provider Info");

        final TextView tvName= dialog.findViewById(R.id.tv_sp_name_admin);
        final TextView tvGender = dialog.findViewById(R.id.tv_sp_gender_admin);
        final TextView tvEmail = dialog.findViewById(R.id.tv_sp_email_admin);
        final TextView tvPhoneNumber = dialog.findViewById(R.id.tv_sp_phone_admin);
        ImageView ivCall = dialog.findViewById(R.id.iv_sp_call_admin);
        final TextView tvCompletedJob = dialog.findViewById(R.id.tv_sp_completed_job_admin);
        final TextView tvRejectedJob = dialog.findViewById(R.id.tv_sp_rejected_job_admin);
        final TextView tvSeccessRatio = dialog.findViewById(R.id.tv_sp_success_ratio_admin);
        RatingBar ratingBar = dialog.findViewById(R.id.rating_bar_sp_admin);

        tvName.setText(name);
        tvGender.setText(gender);
        tvEmail.setText(email);
        tvPhoneNumber.setText(phone);
        if (service_category.equals("Laundry")){
            Query query = FireDB.LS_ORDERS.orderByChild("sp_id").equalTo(sp_id);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()){
                        int completedJob = 0;
                        int rejectedJob = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            LSOrder lsOrder = ds.getValue(LSOrder.class);
                            if (lsOrder.getOrder_status().equals("Completed")){
                                completedJob = completedJob + 1;
                            }
                            else if(lsOrder.getOrder_status().equals("Rejected")){
                                rejectedJob = rejectedJob + 1;
                            }
                        }
                        double successRatio = 0.0;
                        if (completedJob != 0){
                            successRatio = ((completedJob + rejectedJob) * 100) / completedJob;
                        }
                        tvSeccessRatio.setText(String.valueOf(successRatio) + " %");
                        tvCompletedJob.setText(String.valueOf(completedJob));
                        tvRejectedJob.setText(String.valueOf(rejectedJob));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if (service_category.equals("Cleaning")){
            Query query = FireDB.CS_ORDERS.orderByChild("sp_id").equalTo(sp_id);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()){
                        int completedJob = 0;
                        int rejectedJob = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            CSOrder csOrder = ds.getValue(CSOrder.class);
                            if (csOrder.getOrder_status().equals("Completed")){
                                completedJob = completedJob + 1;
                            }
                            else if(csOrder.getOrder_status().equals("Rejected")){
                                rejectedJob = rejectedJob + 1;
                            }
                        }
                        double successRatio = 0.0;
                        if (completedJob != 0){
                            successRatio = ((completedJob + rejectedJob) * 100) / completedJob;
                        }
                        tvSeccessRatio.setText(String.valueOf(successRatio) + " %");
                        tvCompletedJob.setText(String.valueOf(completedJob));
                        tvRejectedJob.setText(String.valueOf(rejectedJob));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if (service_category.equals("Cooking")){
            Query query = FireDB.COOKING_SERVICE_ORDER.orderByChild("sp_id").equalTo(sp_id);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()){
                        int completedJob = 0;
                        int rejectedJob = 0;
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            CookingServiceOrder cso = ds.getValue(CookingServiceOrder.class);
                            if (cso.getOrder_status().equals("Completed")){
                                completedJob = completedJob + 1;
                            }
                            else if(cso.getOrder_status().equals("Rejected")){
                                rejectedJob = rejectedJob + 1;
                            }
                        }
                        double successRatio = 0.0;
                        if (completedJob != 0){
                            successRatio = ((completedJob + rejectedJob) * 100) / completedJob;
                        }
                        tvSeccessRatio.setText(String.valueOf(successRatio) + " %");
                        tvCompletedJob.setText(String.valueOf(completedJob));
                        tvRejectedJob.setText(String.valueOf(rejectedJob));

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phone.equals("")){
                    call(phone);
                }
            }
        });

        dialog.show();
    }

    private void call(String number) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 456);
            return;
        }
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
