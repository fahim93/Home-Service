package com.bitproject.fahim.homeservice.fragments.client;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.activities.MainActivity;
import com.bitproject.fahim.homeservice.adapters.AdapterServiceProvider;
import com.bitproject.fahim.homeservice.classes.CookingPrice;
import com.bitproject.fahim.homeservice.classes.CookingServiceOrder;
import com.bitproject.fahim.homeservice.classes.Rating;
import com.bitproject.fahim.homeservice.classes.ServiceProvider;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClientCookingServiceFragment extends Fragment implements AdapterView.OnItemSelectedListener,DatePickerListener,AdapterServiceProvider.OnItemClickListener {
    private RadioGroup mGenderOfCook;
    private RadioButton rb;
    private Spinner mServiceType, mRangeOfMembers;
    private TextView mPayableAmount;
    private Button mSelectSchedule;
    private CardView mSectionPayableAmount;

    private String mGender = "";
    private String mRange = "";
    private String mType = "";
    private double mAmount = 0;
    private String selectedDate = "";
    private String service_start_time = "";

    private AdapterServiceProvider mSPAdapter;
    private ArrayList<ServiceProvider> serviceProviders;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.client_cooking_service_layout,container,false);
        mAuth = FirebaseAuth.getInstance();
        mGenderOfCook = v.findViewById(R.id.rg_gender_of_cook);
        mServiceType = v.findViewById(R.id.spn_service_type);
        mRangeOfMembers = v.findViewById(R.id.spn_number_of_members);
        mPayableAmount = v.findViewById(R.id.tv_cooking_service_cost);
        mSelectSchedule = v.findViewById(R.id.btn_select_schedule);
        mSectionPayableAmount = v.findViewById(R.id.section_cooking_service_cost);
        serviceProviders = new ArrayList<>();
        mSPAdapter = new AdapterServiceProvider(serviceProviders,getActivity());
        mSPAdapter.setOnItemClickListener(ClientCookingServiceFragment.this);
        mServiceType.setOnItemSelectedListener(this);
        mRangeOfMembers.setOnItemSelectedListener(this);

        mSectionPayableAmount.setVisibility(View.GONE);
        mSelectSchedule.setEnabled(false);
        mSelectSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedGenderID = mGenderOfCook.getCheckedRadioButtonId();
                rb = v.findViewById(selectedGenderID);
                mGender = rb.getText().toString();
                openSchedulingDialog();

            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cooking Service");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selectedRange = mRangeOfMembers.getSelectedItemPosition();
        int selectedType = mServiceType.getSelectedItemPosition();
        if (selectedRange > 0 && selectedType > 0){
            mRange = mRangeOfMembers.getSelectedItem().toString();
            mType = mServiceType.getSelectedItem().toString();
            mSelectSchedule.setEnabled(true);
            getPayableAmount(mType, mRange, selectedType, selectedRange);
        }
        else{
            mSelectSchedule.setEnabled(false);
            mSectionPayableAmount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getPayableAmount(final String type, final String range, final int tid, final int rid){
        FireDB.COOKING_PRICES.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CookingPrice cp = dataSnapshot.getValue(CookingPrice.class);
                if (rid == 1 && tid == 1){
                    mPayableAmount.setText(String.valueOf(cp.getOneToFourDaily()));
                    mAmount = cp.getOneToFourDaily();
                }
                if (rid == 1 && tid == 2){
                    mPayableAmount.setText(String.valueOf(cp.getOneToFourMonthly()));
                    mAmount = cp.getOneToFourMonthly();
                }
                if (rid == 2 && tid == 1){
                    mPayableAmount.setText(String.valueOf(cp.getFiveToTenDaily()));
                    mAmount = cp.getFiveToTenDaily();
                }
                if (rid == 2 && tid == 2){
                    mPayableAmount.setText(String.valueOf(cp.getFiveToTenMonthly()));
                    mAmount = cp.getFiveToTenMonthly();
                }
                mSectionPayableAmount.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void openSchedulingDialog(){
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.dialog_select_schedule);
        dialog.setTitle("Select Service Schedule");
        HorizontalPicker mDatePicker = dialog.findViewById(R.id.datePicker);
        final RadioGroup mTimeSlotGroup = dialog.findViewById(R.id.rg_time_slot_group);
        Button mConfirmSchedule = dialog.findViewById(R.id.btn_confirm_schedule);
        mDatePicker.setListener(this).init();
        mDatePicker.setBackgroundColor(Color.LTGRAY);
        mDatePicker.setDate(new DateTime());
        mConfirmSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String timeSlot = "";
                int selectedTimeSlotId = mTimeSlotGroup.getCheckedRadioButtonId();
                if (selectedTimeSlotId != -1){
                    RadioButton mSelectedTimeSlot = dialog.findViewById(selectedTimeSlotId);
                    service_start_time = mSelectedTimeSlot.getText().toString();
                }
                if (service_start_time.equals("")){
                    Toast.makeText(getActivity(), "Please Select a Time Slot", Toast.LENGTH_SHORT).show();
                    return;
                }
                showAvailableProviders(selectedDate, service_start_time);
//                Toast.makeText(getActivity(), "Date: " + selectedDate + "\nTime: " + timeSlot, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void showAvailableProviders(final String selectedDate, final String service_start_time) {
        final Dialog dialog=new Dialog(getActivity(),android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.available_ls_provider_list);
        dialog.setTitle("Available Service Providers");
        final RecyclerView mAvailableServiceProviderRV = dialog.findViewById(R.id.rv_available_ls_provider);
        mAvailableServiceProviderRV.setHasFixedSize(true);
        mAvailableServiceProviderRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAvailableServiceProviderRV.setItemAnimator(new DefaultItemAnimator());

        Query query = FireDB.SERVICE_PROVIDERS.orderByChild("service_type").equalTo("Cooking");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceProviders.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        final ServiceProvider serviceProvider = ds.getValue(ServiceProvider.class);
                        if (serviceProvider.getGender().equals(mGender)){
                            Query query1 = FireDB.COOKING_SERVICE_ORDER.orderByChild("sp_id").equalTo(serviceProvider.getSp_id());
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChildren()){
                                        int a = 0;
                                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                                            CookingServiceOrder cookingServiceOrder = ds.getValue(CookingServiceOrder.class);
                                            if (cookingServiceOrder.getService_start_date().equals(selectedDate)
                                                    && cookingServiceOrder.getService_time().equals(service_start_time)){
                                                a = 0;
                                                break;
                                            }else if (cookingServiceOrder.getOrder_status().equals("Running")
                                                    && cookingServiceOrder.getService_time().equals(service_start_time)) {
                                                a = 1;
                                            }else
                                            {
                                                a = 1;
                                            }
                                        }
                                        if (a == 1){
                                            serviceProviders.add(serviceProvider);
                                        }
                                    }
                                    else{
                                        serviceProviders.add(serviceProvider);
                                    }
                                    mAvailableServiceProviderRV.setAdapter(mSPAdapter);
                                    mSPAdapter.notifyDataSetChanged();
                                    dialog.show();

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void viewSPDetails(final String sp_id, String name) {
        final Dialog dialog=new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.service_provider_job_info);
        dialog.setTitle(name);
        final TextView tvCompletedJob = dialog.findViewById(R.id.tv_lsp_completed_job);
        final TextView tvRejectedJob = dialog.findViewById(R.id.tv_lsp_rejected_job);
        final TextView tvSeccessRatio = dialog.findViewById(R.id.tv_lsp_success_ratio);
        final RatingBar ratingBar = dialog.findViewById(R.id.rating_bar_lsp_order);
        Button placeOrder = dialog.findViewById(R.id.btn_ls_place_order);
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
        Query query1 =FireDB.SP_RATINGS.orderByChild("sp_id").equalTo(sp_id);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    int total_rate = 0;
                    float total_rating_value = 0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        Rating rating = ds.getValue(Rating.class);
                        total_rate = total_rate + 1;
                        total_rating_value = total_rating_value + rating.getRating_value();
                    }
                    ratingBar.setRating(total_rating_value/total_rate);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                placeOrder(sp_id);
            }
        });
        dialog.show();
    }

    private void placeOrder(final String sp_id) {
        final Dialog dialog=new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.cooking_order_details);
        dialog.setTitle("Confirm Your Order");

        final TextView tvGender = dialog.findViewById(R.id.tv_gender_of_cook);
        final TextView tvRange = dialog.findViewById(R.id.tv_range_of_members);
        final TextView tvType = dialog.findViewById(R.id.tv_service_type);
        final TextView tvTotalPayable = dialog.findViewById(R.id.tv_total_payable);
        final EditText etHouseNo = dialog.findViewById(R.id.et_client_house_no);
        final EditText etZipCode = dialog.findViewById(R.id.et_client_zip_code);
        final Spinner spnArea = dialog.findViewById(R.id.spn_area);
        Button btnSubmitOrder = dialog.findViewById(R.id.btn_submit_order);

        tvGender.setText(mGender);
        tvRange.setText(mRange);
        tvType.setText(mType);
        tvTotalPayable.setText(String.valueOf(mAmount)+ " ৳");
        btnSubmitOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String house_no = etHouseNo.getText().toString().trim();
                String zipCode = etZipCode.getText().toString().trim();
                int areaPosition = spnArea.getSelectedItemPosition();
                if (house_no.equals("")){
                    etHouseNo.setError("House No Cannot Be Blank");
                    etHouseNo.requestFocus();
                    return;
                }else if (zipCode.equals("")){
                    etZipCode.setError("Zip Code Cannot Be Blank");
                    etZipCode.requestFocus();
                    return;
                }else if(areaPosition < 1){
                    Toast.makeText(getActivity(), "Select Your Area", Toast.LENGTH_LONG).show();
                    return;
                }
                String address = house_no + ", " + zipCode + ", " + spnArea.getSelectedItem().toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                String order_date = dateFormat.format(date);
                String order_status = "Pending";
                saveOrders(sp_id,order_date, selectedDate, service_start_time, address,order_status,mAmount);

            }
        });
        dialog.show();
    }

    private void saveOrders(String sp_id, String order_date, String selectedDate, String service_start_time, String address, String order_status, double mAmount) {
        String service_category = "Cooking";
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.show();

        String client_id = mAuth.getCurrentUser().getUid();
        String order_id = FireDB.COOKING_SERVICE_ORDER.push().getKey();
        CookingServiceOrder cso = new CookingServiceOrder(order_id,client_id, sp_id, service_category, mGender, mRange, mType, order_date, selectedDate, service_start_time, address, order_status,"False", mAmount);
        FireDB.COOKING_SERVICE_ORDER.child(order_id).setValue(cso)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), "Thanks! Order Has Been Sent Successfully.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onDateSelected(DateTime dateSelected) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String fullDate = dateSelected.toString();
        String[] parts = fullDate.split("T");
        selectedDate = parts[0];
        String part2 = parts[1];
    }

    @Override
    public void OnItemClick(int position) {
        ServiceProvider serviceProvider = serviceProviders.get(position);
        viewSPDetails(serviceProvider.getSp_id(), serviceProvider.getName());
    }
}
