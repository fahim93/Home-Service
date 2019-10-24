package com.bitproject.fahim.homeservice.fragments.client;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.activities.MainActivity;
import com.bitproject.fahim.homeservice.adapters.AdapterCSOrderItem;
import com.bitproject.fahim.homeservice.adapters.AdapterCleaningServiceClient;
import com.bitproject.fahim.homeservice.adapters.AdapterLSOrderItem;
import com.bitproject.fahim.homeservice.adapters.AdapterServiceProvider;
import com.bitproject.fahim.homeservice.classes.CSOrder;
import com.bitproject.fahim.homeservice.classes.CSOrderItem;
import com.bitproject.fahim.homeservice.classes.CleaningService;
import com.bitproject.fahim.homeservice.classes.LSOrder;
import com.bitproject.fahim.homeservice.classes.LSOrderItem;
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

public class ClientCleaningServiceFragment extends Fragment implements DatePickerListener,AdapterServiceProvider.OnItemClickListener{
    private Button mSelectScheduleButton;

    private Button mAddRoomCleaning, mAddBathroomCleaning, mAddFridgeCleaning, mAddCeillingFanCleaning;
    private Button mRemoveRoomCleaning, mRemoveBathroomCleaning, mRemoveFridgeCleaning, mRemoveCeillingFanCleaning;
    private ArrayList<CSOrderItem>orderItems;
    private final String room = "Room Basic Cleaning";
    private final String bathroom = "Bathroom Basic Cleaning";
    private final String ceiling_fan = "Ceiling Fan Cleaning";
    private final String fridge = "Fridge Cleaning";

    private String selectedDate = "";
    private String service_start_time = "";

    private AdapterServiceProvider mSPAdapter;
    private ArrayList<ServiceProvider>serviceProviders;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.client_cleaning_services_layout,container,false);
        mAddRoomCleaning = v.findViewById(R.id.btn_add_room_basic_cleaning);
        mAddBathroomCleaning = v.findViewById(R.id.btn_add_bathroom_cleaning);
        mAddCeillingFanCleaning = v.findViewById(R.id.btn_add_ceiling_fan_cleaning);
        mAddFridgeCleaning = v.findViewById(R.id.btn_add_fridge_cleaning);
        mRemoveRoomCleaning = v.findViewById(R.id.btn_remove_room_basic_cleaning);
        mRemoveBathroomCleaning = v.findViewById(R.id.btn_remove_bathroom_cleaning);
        mRemoveCeillingFanCleaning = v.findViewById(R.id.btn_remove_ceiling_fan_cleaning);
        mRemoveFridgeCleaning = v.findViewById(R.id.btn_remove_fridge_cleaning);
        mSelectScheduleButton = v.findViewById(R.id.btn_select_schedule);
        serviceProviders = new ArrayList<>();
        mSPAdapter = new AdapterServiceProvider(serviceProviders,getActivity());
        mSPAdapter.setOnItemClickListener(ClientCleaningServiceFragment.this);
        orderItems = new ArrayList<>();
        if (orderItems.isEmpty()){
            mSelectScheduleButton.setEnabled(false);
        }
        mAddRoomCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCSItemDialog(room, mAddRoomCleaning, mRemoveRoomCleaning);
            }
        });
        mAddBathroomCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCSItemDialog(bathroom, mAddBathroomCleaning, mRemoveBathroomCleaning);
            }
        });
        mAddCeillingFanCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCSItemDialog(ceiling_fan, mAddCeillingFanCleaning, mRemoveCeillingFanCleaning);
            }
        });
        mAddFridgeCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCSItemDialog(fridge, mAddFridgeCleaning, mRemoveFridgeCleaning);
            }
        });
        mRemoveRoomCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CSOrderItem orderItem : orderItems){
                    if (orderItem.getService_name().equals(room)){
                        orderItems.remove(orderItem);
                    }
                }
                mRemoveRoomCleaning.setVisibility(View.GONE);
                mAddRoomCleaning.setVisibility(View.VISIBLE);
                if (orderItems.isEmpty()){
                    mSelectScheduleButton.setEnabled(false);
                }

            }
        });
        mRemoveBathroomCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CSOrderItem orderItem : orderItems){
                    if (orderItem.getService_name().equals(bathroom)){
                        orderItems.remove(orderItem);
                    }
                }
                mRemoveBathroomCleaning.setVisibility(View.GONE);
                mAddBathroomCleaning.setVisibility(View.VISIBLE);
                if (orderItems.isEmpty()){
                    mSelectScheduleButton.setEnabled(false);
                }
            }
        });
        mRemoveCeillingFanCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CSOrderItem orderItem : orderItems){
                    if (orderItem.getService_name().equals(ceiling_fan)){
                        orderItems.remove(orderItem);
                    }
                }
                mRemoveCeillingFanCleaning.setVisibility(View.GONE);
                mAddCeillingFanCleaning.setVisibility(View.VISIBLE);
                if (orderItems.isEmpty()){
                    mSelectScheduleButton.setEnabled(false);
                }
            }
        });
        mRemoveFridgeCleaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CSOrderItem orderItem : orderItems){
                    if (orderItem.getService_name().equals(fridge)){
                        orderItems.remove(orderItem);
                    }
                }
                mRemoveFridgeCleaning.setVisibility(View.GONE);
                mAddFridgeCleaning.setVisibility(View.VISIBLE);
                if (orderItems.isEmpty()){
                    mSelectScheduleButton.setEnabled(false);
                }
            }
        });
        mSelectScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSchedulingDialog();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cleaning Services");
    }

    private void openCSItemDialog(final String service_name, final Button addButton, final Button removeButton){
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.client_cleaning_service_item_layout);
        dialog.setTitle(service_name);
        final TextView starting_price = dialog.findViewById(R.id.tv_cs_starting_price);
        final TextView price = dialog.findViewById(R.id.tv_cs_price);
        final TextView totalServiceCharge = dialog.findViewById(R.id.tv_cs_total_service_charge);
        final Button minus = dialog.findViewById(R.id.btn_quantity_decrement);
        Button plus = dialog.findViewById(R.id.btn_quantity_increment);
        final Button value = dialog.findViewById(R.id.btn_quantity_value);
        Button add = dialog.findViewById(R.id.btn_add_service);
        Button cancel = dialog.findViewById(R.id.btn_cancel_service);
        int qv = Integer.parseInt(value.getText().toString());
        if (qv == 1){
            minus.setEnabled(false);
        }
        Query query = FireDB.CLEANING_SERVICES.orderByChild("cs_name").equalTo(service_name);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    CleaningService cs = ds.getValue(CleaningService.class);
                    starting_price.setText(String.valueOf(cs.getCs_starting_price()));
                    price.setText(String.valueOf(cs.getCs_price()));
                    int qv = Integer.parseInt(value.getText().toString());
                    totalServiceCharge.setText(String.valueOf((qv * cs.getCs_price()) + cs.getCs_starting_price()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qv = Integer.parseInt(value.getText().toString());
                qv = qv - 1;
                value.setText(String.valueOf(qv));
                double service_price = Double.parseDouble(price.getText().toString());
                double start_price = Double.parseDouble(starting_price.getText().toString());
                totalServiceCharge.setText(String.valueOf((qv * service_price) + start_price));
                if (qv == 1){
                    minus.setEnabled(false);
                }

            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qv = Integer.parseInt(value.getText().toString());
                qv = qv + 1;
                value.setText(String.valueOf(qv));
                double service_price = Double.parseDouble(price.getText().toString());
                double start_price = Double.parseDouble(starting_price.getText().toString());
                totalServiceCharge.setText(String.valueOf((qv * service_price) + start_price));
                minus.setEnabled(true);
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(value.getText().toString());
                double total_price = Double.parseDouble(totalServiceCharge.getText().toString());
                CSOrderItem orderItem = new CSOrderItem(service_name, quantity, total_price);
                orderItems.add(orderItem);
                addButton.setVisibility(View.GONE);
                removeButton.setVisibility(View.VISIBLE);
                mSelectScheduleButton.setEnabled(true);
                dialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
                ShowAvailableProviders(selectedDate, service_start_time);
//                Toast.makeText(getActivity(), "Date: " + selectedDate + "\nTime: " + timeSlot, Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private void ShowAvailableProviders(final String selectedDate, final String service_start_time) {
        final Dialog dialog=new Dialog(getActivity(),android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.available_ls_provider_list);
        dialog.setTitle("Available Service Providers");
        final RecyclerView mAvailableServiceProviderRV = dialog.findViewById(R.id.rv_available_ls_provider);
        mAvailableServiceProviderRV.setHasFixedSize(true);
        mAvailableServiceProviderRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAvailableServiceProviderRV.setItemAnimator(new DefaultItemAnimator());

        Query query = FireDB.SERVICE_PROVIDERS.orderByChild("service_type").equalTo("Cleaning");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceProviders.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        final ServiceProvider serviceProvider = ds.getValue(ServiceProvider.class);
                        //String spId = ds.child("sp_id").getValue(String.class);
                        Query query1 = FireDB.CS_ORDERS.orderByChild("sp_id").equalTo(serviceProvider.getSp_id());
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()){
                                    int a = 0;
                                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                                        CSOrder csOrder = ds.getValue(CSOrder.class);
                                        if (csOrder.getService_start_date().equals(selectedDate)
                                                && csOrder.getService_start_time().equals(service_start_time)){
                                            a = 0;
                                            break;
                                        }else{
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void viewSPDetails(final String sp_id, String name){
        final Dialog dialog=new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.service_provider_job_info);
        dialog.setTitle(name);
        final TextView tvCompletedJob = dialog.findViewById(R.id.tv_lsp_completed_job);
        final TextView tvRejectedJob = dialog.findViewById(R.id.tv_lsp_rejected_job);
        final TextView tvSeccessRatio = dialog.findViewById(R.id.tv_lsp_success_ratio);
        final RatingBar ratingBar = dialog.findViewById(R.id.rating_bar_lsp_order);
        Button placeOrder = dialog.findViewById(R.id.btn_ls_place_order);
        Query query = FireDB.CS_ORDERS.orderByChild("sp_id").equalTo(sp_id);
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
        Query query1 = FireDB.SP_RATINGS.orderByChild("sp_id").equalTo(sp_id);
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
        dialog.setContentView(R.layout.cs_order_details);
        dialog.setTitle("Confirm Your Order");
//        double deliveryCharge = 30;
        double total_price = 0;
        if (!orderItems.isEmpty()){
            for (CSOrderItem cso : orderItems){
                total_price = total_price + cso.getTotal_price();
            }
        }
        AdapterCSOrderItem adapterCSOrderItem = new AdapterCSOrderItem(orderItems,getActivity());
        RecyclerView orderListView = dialog.findViewById(R.id.rv_cs_order_list);
        final TextView tvTotalPayable = dialog.findViewById(R.id.tv_cs_total_payable);
        final EditText etHouseNo = dialog.findViewById(R.id.et_cs_client_house_no);
        final EditText etZipCode = dialog.findViewById(R.id.et_cs_client_zip_code);
        final Spinner spnArea = dialog.findViewById(R.id.spn_cs_area);
        Button btnSubmitOrder = dialog.findViewById(R.id.btn_submit_cs_order);

        orderListView.setHasFixedSize(true);
        orderListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderListView.setItemAnimator(new DefaultItemAnimator());

        orderListView.setAdapter(adapterCSOrderItem);
        adapterCSOrderItem.notifyDataSetChanged();

        tvTotalPayable.setText(String.valueOf(total_price)+ " ৳");
        final double total_payable =  total_price;
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
                saveOrders(sp_id,order_date, selectedDate, service_start_time, address,order_status,total_payable);

            }
        });
        dialog.show();
    }

    private void saveOrders(String sp_id, String order_date, String selectedDate, String service_start_time, String address, String order_status, double total_payable) {
        String service_category = "Cleaning";
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.show();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String client_id = firebaseAuth.getCurrentUser().getUid();
        final String order_id = FireDB.CS_ORDERS.push().getKey();
        CSOrder csOrder = new CSOrder(order_id,client_id,sp_id,service_category,order_date,selectedDate,service_start_time,address,order_status,"False",total_payable);
        FireDB.CS_ORDERS.child(order_id).setValue(csOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!orderItems.isEmpty()){
                            for (CSOrderItem cso : orderItems){
                                String order_item_id = FireDB.CS_ORDER_ITEMS.push().getKey();
                                CSOrderItem csOrderItem = new CSOrderItem(order_id, order_item_id, cso.getService_name(), cso.getQuantity(),cso.getTotal_price());
                                FireDB.CS_ORDER_ITEMS.child(order_item_id).setValue(csOrderItem);
                            }
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Thanks! Your Order Has Been Done Successfully.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                        }

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
