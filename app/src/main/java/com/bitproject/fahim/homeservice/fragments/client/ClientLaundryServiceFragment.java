package com.bitproject.fahim.homeservice.fragments.client;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.bitproject.fahim.homeservice.adapters.AdapterLSOrderItem;
import com.bitproject.fahim.homeservice.adapters.AdapterServiceProvider;
import com.bitproject.fahim.homeservice.classes.LSOrder;
import com.bitproject.fahim.homeservice.classes.LSOrderItem;
import com.bitproject.fahim.homeservice.classes.Rating;
import com.bitproject.fahim.homeservice.classes.ServiceProvider;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.bitproject.fahim.homeservice.classes.LaundryService;
import com.github.jhonnyx2012.horizontalpicker.DatePickerListener;
import com.github.jhonnyx2012.horizontalpicker.HorizontalPicker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClientLaundryServiceFragment extends Fragment implements DatePickerListener, AdapterServiceProvider.OnItemClickListener {
    private TextView mMensWearTV, mWomensWearTV, mKidsWearTV, mHouseholdsAndAccessoriesTV;
    private RecyclerView mMensWearRV, mWomensWearRV, mKidsWearRV, mHouseholdsAndAccessoriesRV;
    private Button mMensWearAddButton, mWomensWearAddButton, mKidsWearAddButton, mHouseholdsAndAccessoriesAddButton;
    private Button mMensWearRemoveButton, mWomensWearRemoveButton, mKidsWearRemoveButton, mHouseholdsAndAccessoriesRemoveButton;
    private Button mAddMoreMensWear,mAddMoreWomensWear,mAddMoreKidsWear,mAddMoreHouseholdsAndAccessories;
    private LinearLayout mDetailsPaneMensWear,mDetailsPaneWomensWear,mDetailsPaneKidsWear,mDetailsPaneHouseholdsAndAccessories;
    private Button mSelectSchedule;
    private ArrayList<LSOrderItem> mensWearList;
    private ArrayList<LSOrderItem> womensWearList;
    private ArrayList<LSOrderItem> kidsWearList;
    private ArrayList<LSOrderItem> householdsAndAccessoriesList;
    private AdapterLSOrderItem mAdapterLSOrderItem,mAdapterWomensWear,mAdapterKidsWear,mAdapterHouseholsAndAccessories;
    private AdapterServiceProvider mSPAdapter;
//    private RecyclerView mAvailableServiceProviderRV;
    private ArrayList<ServiceProvider> serviceProviders;
    private ArrayList<LSOrderItem>lsOrderItemList;

    private String selectedDate = "";
    private String service_start_time = "";

    private FirebaseFirestore mFireStore;
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.client_laundry_services_layout, container, false);
        mFireStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mMensWearTV = v.findViewById(R.id.tv_mens_wear);
        mWomensWearTV = v.findViewById(R.id.tv_womens_wear);
        mKidsWearTV = v.findViewById(R.id.tv_kids_wear);
        mHouseholdsAndAccessoriesTV = v.findViewById(R.id.tv_households_and_accessories);

        mMensWearAddButton = v.findViewById(R.id.btn_add_mens_wear);
        mWomensWearAddButton = v.findViewById(R.id.btn_add_womens_wear);
        mKidsWearAddButton = v.findViewById(R.id.btn_add_kids_wear);
        mHouseholdsAndAccessoriesAddButton = v.findViewById(R.id.btn_add_households_and_accessories);

        mMensWearRemoveButton = v.findViewById(R.id.btn_remove_mens_wear);
        mWomensWearRemoveButton = v.findViewById(R.id.btn_remove_womens_wear);
        mKidsWearRemoveButton = v.findViewById(R.id.btn_remove_kids_wear);
        mHouseholdsAndAccessoriesRemoveButton = v.findViewById(R.id.btn_remove_households_and_accessories);

        mDetailsPaneMensWear = v.findViewById(R.id.details_pane_mens_wear);
        mDetailsPaneWomensWear = v.findViewById(R.id.details_pane_womens_wear);
        mDetailsPaneKidsWear = v.findViewById(R.id.details_pane_kids_wear);
        mDetailsPaneHouseholdsAndAccessories = v.findViewById(R.id.details_pane_households_and_accessories);

        mAddMoreMensWear = v.findViewById(R.id.btn_add_more_mens_wear);
        mAddMoreWomensWear = v.findViewById(R.id.btn_add_more_womens_wear);
        mAddMoreKidsWear = v.findViewById(R.id.btn_add_more_kids_wear);
        mAddMoreHouseholdsAndAccessories = v.findViewById(R.id.btn_add_more_households_and_accessories);

        mensWearList = new ArrayList<>();
        womensWearList = new ArrayList<>();
        kidsWearList = new ArrayList<>();
        householdsAndAccessoriesList = new ArrayList<>();
        lsOrderItemList = new ArrayList<>();

        mMensWearRV = v.findViewById(R.id.rv_mens_wear);
        mWomensWearRV = v.findViewById(R.id.rv_womens_wear);
        mKidsWearRV = v.findViewById(R.id.rv_kids_wear);
        mHouseholdsAndAccessoriesRV = v.findViewById(R.id.rv_households_and_accessories);

        mMensWearRV.setHasFixedSize(true);
        mMensWearRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMensWearRV.setItemAnimator(new DefaultItemAnimator());


        mWomensWearRV.setHasFixedSize(true);
        mWomensWearRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWomensWearRV.setItemAnimator(new DefaultItemAnimator());


        mKidsWearRV.setHasFixedSize(true);
        mKidsWearRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mKidsWearRV.setItemAnimator(new DefaultItemAnimator());


        mHouseholdsAndAccessoriesRV.setHasFixedSize(true);
        mHouseholdsAndAccessoriesRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHouseholdsAndAccessoriesRV.setItemAnimator(new DefaultItemAnimator());


        mAdapterLSOrderItem = new AdapterLSOrderItem(mensWearList, getActivity());
        mAdapterWomensWear = new AdapterLSOrderItem(womensWearList, getActivity());
        mAdapterKidsWear = new AdapterLSOrderItem(kidsWearList, getActivity());
        mAdapterHouseholsAndAccessories = new AdapterLSOrderItem(householdsAndAccessoriesList, getActivity());

//        mAvailableServiceProviderRV.setHasFixedSize(true);
//        mAvailableServiceProviderRV.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mAvailableServiceProviderRV.setItemAnimator(new DefaultItemAnimator());
        serviceProviders = new ArrayList<>();
        mSPAdapter = new AdapterServiceProvider(serviceProviders, getActivity());
        mSPAdapter.setOnItemClickListener(ClientLaundryServiceFragment.this);

        mSelectSchedule = v.findViewById(R.id.btn_select_schedule);
        if (mensWearList.isEmpty() && womensWearList.isEmpty() && kidsWearList.isEmpty() && householdsAndAccessoriesList.isEmpty()){
            mSelectSchedule.setEnabled(false);
        }
        else {
            mSelectSchedule.setEnabled(true);
        }


        //Text View Click Listeners
        mMensWearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailsPaneMensWear.getVisibility()==View.GONE){
                    mDetailsPaneMensWear.setVisibility(View.VISIBLE);
                    mMensWearRV.setAdapter(mAdapterLSOrderItem);
                    if (mensWearList.isEmpty()){
                        mAddMoreMensWear.setVisibility(View.GONE);
                    }
                }else{
                    mDetailsPaneMensWear.setVisibility(View.GONE);
                }
            }
        });
        mWomensWearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailsPaneWomensWear.getVisibility()==View.GONE){
                    mDetailsPaneWomensWear.setVisibility(View.VISIBLE);
                    mWomensWearRV.setAdapter(mAdapterWomensWear);
                    if (womensWearList.isEmpty()){
                        mAddMoreWomensWear.setVisibility(View.GONE);
                    }
                }else{
                    mDetailsPaneWomensWear.setVisibility(View.GONE);
                }
            }
        });
        mKidsWearTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailsPaneKidsWear.getVisibility()==View.GONE){
                    mDetailsPaneKidsWear.setVisibility(View.VISIBLE);
                    mKidsWearRV.setAdapter(mAdapterKidsWear);
                    if (kidsWearList.isEmpty()){
                        mAddMoreKidsWear.setVisibility(View.GONE);
                    }
                }else{
                    mDetailsPaneKidsWear.setVisibility(View.GONE);
                }
            }
        });
        mHouseholdsAndAccessoriesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDetailsPaneHouseholdsAndAccessories.getVisibility()==View.GONE){
                    mDetailsPaneHouseholdsAndAccessories.setVisibility(View.VISIBLE);
                    mHouseholdsAndAccessoriesRV.setAdapter(mAdapterHouseholsAndAccessories);
                    if (householdsAndAccessoriesList.isEmpty()){
                        mAddMoreHouseholdsAndAccessories.setVisibility(View.GONE);
                    }
                }else{
                    mDetailsPaneHouseholdsAndAccessories.setVisibility(View.GONE);
                }
            }
        });

        //Add button Click Listeners
        mMensWearAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = "Men's Wear";
                openAddServiceDialog(serviceType, mMensWearAddButton, mMensWearRemoveButton);
            }
        });
        mWomensWearAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = "Women's Wear";
                openAddServiceDialog(serviceType, mWomensWearAddButton, mWomensWearRemoveButton);

            }
        });
        mKidsWearAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = "Kids Wear";
                openAddServiceDialog(serviceType, mKidsWearAddButton, mKidsWearRemoveButton);
            }
        });
        mHouseholdsAndAccessoriesAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = "Households and Accessories";
                openAddServiceDialog(serviceType, mHouseholdsAndAccessoriesAddButton, mHouseholdsAndAccessoriesRemoveButton);
            }
        });

        //Remove button Click Listeners
        mMensWearRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mensWearList.clear();
                mMensWearRemoveButton.setVisibility(View.GONE);
                mMensWearAddButton.setVisibility(View.VISIBLE);
                mDetailsPaneMensWear.setVisibility(View.GONE);
                if (mensWearList.isEmpty() && womensWearList.isEmpty() && kidsWearList.isEmpty() && householdsAndAccessoriesList.isEmpty()){
                    mSelectSchedule.setEnabled(false);
                }
            }
        });
        mWomensWearRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                womensWearList.clear();
                mWomensWearRemoveButton.setVisibility(View.GONE);
                mWomensWearAddButton.setVisibility(View.VISIBLE);
                mDetailsPaneWomensWear.setVisibility(View.GONE);
                if (mensWearList.isEmpty() && womensWearList.isEmpty() && kidsWearList.isEmpty() && householdsAndAccessoriesList.isEmpty()){
                    mSelectSchedule.setEnabled(false);
                }
            }
        });
        mKidsWearRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kidsWearList.clear();
                mKidsWearRemoveButton.setVisibility(View.GONE);
                mKidsWearAddButton.setVisibility(View.VISIBLE);
                mDetailsPaneKidsWear.setVisibility(View.GONE);
                if (mensWearList.isEmpty() && womensWearList.isEmpty() && kidsWearList.isEmpty() && householdsAndAccessoriesList.isEmpty()){
                    mSelectSchedule.setEnabled(false);
                }
            }
        });
        mHouseholdsAndAccessoriesRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                householdsAndAccessoriesList.clear();
                mHouseholdsAndAccessoriesRemoveButton.setVisibility(View.GONE);
                mHouseholdsAndAccessoriesAddButton.setVisibility(View.VISIBLE);
                mDetailsPaneHouseholdsAndAccessories.setVisibility(View.GONE);
                if (mensWearList.isEmpty() && womensWearList.isEmpty() && kidsWearList.isEmpty() && householdsAndAccessoriesList.isEmpty()){
                    mSelectSchedule.setEnabled(false);
                }
            }
        });

        mAddMoreMensWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = "Men's Wear";
                openAddServiceDialog(serviceType, mMensWearAddButton, mMensWearRemoveButton);
            }
        });
        mAddMoreWomensWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = "Women's Wear";
                openAddServiceDialog(serviceType, mWomensWearAddButton, mWomensWearRemoveButton);
            }
        });
        mAddMoreKidsWear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = "Kids Wear";
                openAddServiceDialog(serviceType, mKidsWearAddButton, mKidsWearRemoveButton);
            }
        });
        mAddMoreHouseholdsAndAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String serviceType = "Households and Accessories";
                openAddServiceDialog(serviceType, mHouseholdsAndAccessoriesAddButton, mHouseholdsAndAccessoriesRemoveButton);
            }
        });

        mSelectSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScheduleDialog();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Laundry Services");
    }

    public void openAddServiceDialog(final String item_type, final Button add, final Button remove){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_laundry_service_item_layout, null);
        dialogBuilder.setView(dialogView);
        final Spinner mServiceItemSpinner = dialogView.findViewById(R.id.spinner_laundry_service_items);
        final RadioGroup mLaundryServiceTypeGroup = dialogView.findViewById(R.id.rg_laundry_type_group);
        final Button mQuantityDecrement = dialogView.findViewById(R.id.btn_quantity_decrement);
        final Button mQuantityIncrement = dialogView.findViewById(R.id.btn_quantity_increment);
        final Button mQuantityValue = dialogView.findViewById(R.id.btn_quantity_value);
        final Button mOkButton = dialogView.findViewById(R.id.btn_laundry_service_dialog_ok);
        final Button mCancelButton = dialogView.findViewById(R.id.btn_laundry_service_dialog_cancel);
        final TextView wash_and_iron = dialogView.findViewById(R.id.tv_wash_and_iron_price);
        final TextView dry_clean = dialogView.findViewById(R.id.tv_dry_clean_price);
        final TextView only_iron = dialogView.findViewById(R.id.tv_only_iron_price);
        final ArrayList<LaundryService> serviceItems = new ArrayList<>();
        final ArrayAdapter<LaundryService> adapter =
                new ArrayAdapter<LaundryService>(getActivity(),  android.R.layout.simple_spinner_dropdown_item, serviceItems);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);


        Query query = FireDB.LAUNDRY_SERVICES.orderByChild("laundry_service_item_type").equalTo(item_type);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceItems.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    //String itemType = ds.child("laundry_service_item").getValue(String.class);
                    LaundryService laundryService = ds.getValue(LaundryService.class);
                    serviceItems.add(laundryService);
                    //serviceItems.add(itemType);
                }
                mServiceItemSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mServiceItemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LaundryService laundryService = adapter.getItem(position);

                wash_and_iron.setText(String.valueOf(laundryService.getPrice_wash_and_iron()));
                dry_clean.setText(String.valueOf(laundryService.getPrice_dry_clean()));
                only_iron.setText(String.valueOf(laundryService.getPrice_only_iron()));


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mQuantityValue.setText("1");
        int qv = Integer.parseInt(mQuantityValue.getText().toString());
        if (qv==1){
            mQuantityDecrement.setEnabled(false);
        }
        dialogBuilder.setTitle(item_type);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        mQuantityDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qv = Integer.parseInt(mQuantityValue.getText().toString());
                if (qv==1){
                    mQuantityDecrement.setEnabled(false);
                }else{
                    qv = qv - 1;
                    mQuantityValue.setText(""+qv);
                }

            }
        });
        mQuantityIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuantityDecrement.setEnabled(true);
                int qv = Integer.parseInt(mQuantityValue.getText().toString());
                qv = qv + 1;
                mQuantityValue.setText(""+qv);
            }
        });

        mOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item_name = mServiceItemSpinner.getSelectedItem().toString();
                int qv = Integer.parseInt(mQuantityValue.getText().toString());
                double price = 0;
                String mServiceItemType = "";
                int selectedTypeId = mLaundryServiceTypeGroup.getCheckedRadioButtonId();
                if (selectedTypeId != -1){
                    RadioButton mLaundryServiceItemTypeRadioButton = dialogView.findViewById(selectedTypeId);
                    mServiceItemType = mLaundryServiceItemTypeRadioButton.getText().toString();
                    if (mServiceItemType.equals("Wash and Iron")){
                        price = Double.parseDouble(wash_and_iron.getText().toString());
                    }else if (mServiceItemType.equals("Dry Clean")){
                        price = Double.parseDouble(dry_clean.getText().toString());
                    }else if (mServiceItemType.equals("Only Iron")){
                        price = Double.parseDouble(only_iron.getText().toString());
                    }
                }
                double t_price = price * qv;
                LSOrderItem laundryServiceOrder = new LSOrderItem(item_name, item_type,mServiceItemType,qv,t_price);
                if (item_type.equals("Men's Wear")){
                    mensWearList.add(laundryServiceOrder);
                    mMensWearRV.setAdapter(mAdapterLSOrderItem);
                    mDetailsPaneMensWear.setVisibility(View.VISIBLE);
                    mAddMoreMensWear.setVisibility(View.VISIBLE);
                }else if (item_type.equals("Women's Wear")){
                    womensWearList.add(laundryServiceOrder);
                    mWomensWearRV.setAdapter(mAdapterWomensWear);
                    mDetailsPaneWomensWear.setVisibility(View.VISIBLE);
                    mAddMoreWomensWear.setVisibility(View.VISIBLE);
                }else if (item_type.equals("Kids Wear")){
                    kidsWearList.add(laundryServiceOrder);
                    mKidsWearRV.setAdapter(mAdapterKidsWear);
                    mDetailsPaneKidsWear.setVisibility(View.VISIBLE);
                    mAddMoreKidsWear.setVisibility(View.VISIBLE);
                }else if (item_type.equals("Households and Accessories")){
                    householdsAndAccessoriesList.add(laundryServiceOrder);
                    mHouseholdsAndAccessoriesRV.setAdapter(mAdapterHouseholsAndAccessories);
                    mDetailsPaneHouseholdsAndAccessories.setVisibility(View.VISIBLE);
                    mAddMoreHouseholdsAndAccessories.setVisibility(View.VISIBLE);
                }
                if (mensWearList.isEmpty() && womensWearList.isEmpty() && kidsWearList.isEmpty() && householdsAndAccessoriesList.isEmpty()){
                    mSelectSchedule.setEnabled(false);
                }
                else {
                    mSelectSchedule.setEnabled(true);
                }


                add.setVisibility(View.GONE);
                remove.setVisibility(View.VISIBLE);

                alertDialog.dismiss();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    private void openScheduleDialog(){
        final Dialog dialog=new Dialog(getActivity(),android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_select_schedule);
        dialog.setTitle("Select Schedule");
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

    private void ShowAvailableProviders(final String service_start_date, final String selected_time){
        final Dialog dialog=new Dialog(getActivity(),android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.available_ls_provider_list);
        dialog.setTitle("Available Service Providers");
        final RecyclerView mAvailableServiceProviderRV = dialog.findViewById(R.id.rv_available_ls_provider);
        mAvailableServiceProviderRV.setHasFixedSize(true);
        mAvailableServiceProviderRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAvailableServiceProviderRV.setItemAnimator(new DefaultItemAnimator());

        Query query = FireDB.SERVICE_PROVIDERS.orderByChild("service_type").equalTo("Laundry");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceProviders.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        final ServiceProvider serviceProvider = ds.getValue(ServiceProvider.class);
                        //String spId = ds.child("sp_id").getValue(String.class);
                        Query query1 = FireDB.LS_ORDERS.orderByChild("sp_id").equalTo(serviceProvider.getSp_id());
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChildren()){
                                    int a = 0;
                                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                                        LSOrder lsOrder = ds.getValue(LSOrder.class);
                                        if (lsOrder.getService_start_date().equals(service_start_date)
                                                && lsOrder.getService_start_time().equals(selected_time)){
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
//        if (serviceProviders.isEmpty()){
//            Toast.makeText(getActivity(), "Any Service Providers Are Not Available On Your Selected Schedule", Toast.LENGTH_LONG).show();
//            return;
//        }
//        else {
//
//
//        }

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

    private void placeOrder(final String sp_id){
        final Dialog dialog=new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.ls_order_details);
        dialog.setTitle("Confirm Your Order");
        double deliveryCharge = 30;
        double serviceCost = 0;
        if (!mensWearList.isEmpty()){
            lsOrderItemList.addAll(mensWearList);
        }
        if (!womensWearList.isEmpty()){
            lsOrderItemList.addAll(womensWearList);
        }
        if (!kidsWearList.isEmpty()){
            lsOrderItemList.addAll(kidsWearList);
        }
        if (!householdsAndAccessoriesList.isEmpty()){
            lsOrderItemList.addAll(householdsAndAccessoriesList);
        }
        if (!lsOrderItemList.isEmpty()){
            for (LSOrderItem lso : lsOrderItemList){
                serviceCost = serviceCost + lso.getPrice();
            }
        }
        AdapterLSOrderItem adapterLSOrderItem = new AdapterLSOrderItem(lsOrderItemList,getActivity());
        RecyclerView orderListView = dialog.findViewById(R.id.rv_ls_order_list);
        TextView tvServiceCost = dialog.findViewById(R.id.tv_ls_service_cost);
        TextView tvDeliveryCharge = dialog.findViewById(R.id.tv_ls_delivery_charge);
        final TextView tvTotalPayable = dialog.findViewById(R.id.tv_ls_total_payable);
        final EditText etHouseNo = dialog.findViewById(R.id.et_ls_client_house_no);
        final EditText etZipCode = dialog.findViewById(R.id.et_ls_client_zip_code);
        final Spinner spnArea = dialog.findViewById(R.id.spn_ls_area);
        Button btnSubmitOrder = dialog.findViewById(R.id.btn_submit_ls_order);

        orderListView.setHasFixedSize(true);
        orderListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderListView.setItemAnimator(new DefaultItemAnimator());

        orderListView.setAdapter(adapterLSOrderItem);
        adapterLSOrderItem.notifyDataSetChanged();

        tvServiceCost.setText(String.valueOf(serviceCost)+ " ৳");
        tvDeliveryCharge.setText(String.valueOf(deliveryCharge)+ " ৳");
        final double total_payable = serviceCost + deliveryCharge;
        tvTotalPayable.setText(String.valueOf(total_payable)+ " ৳");

        final double delivery_charge = deliveryCharge;
        final double service_cost = serviceCost;
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
                saveOrders(sp_id,order_date, selectedDate, service_start_time, address,order_status,service_cost,delivery_charge, total_payable);

            }
        });
        dialog.show();
    }

    private void saveOrders(String sp_id, String order_date, String service_start_date, String service_start_time, String address, String order_status, double service_cost, double delivery_charge, double total_payable){
        String service_category = "Laundry";
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Processing");
        dialog.show();
        final String client_id = mAuth.getCurrentUser().getUid();
        final String order_id = FireDB.LS_ORDERS.push().getKey();
        LSOrder lsOrder = new LSOrder(order_id,client_id,sp_id,service_category,order_date,service_start_date,service_start_time,address,order_status,"False",service_cost,delivery_charge,total_payable);
        FireDB.LS_ORDERS.child(order_id).setValue(lsOrder)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        if (!lsOrderItemList.isEmpty()){
                            for (LSOrderItem lso : lsOrderItemList){
                                String order_item_id = FireDB.LS_ORDER_ITEMS.push().getKey();
                                LSOrderItem lsOrderItem = new LSOrderItem(order_id, order_item_id, lso.getItem_name(), lso.getItem_type(), lso.getService_type(), lso.getQuantity(), lso.getPrice());
                                FireDB.LS_ORDER_ITEMS.child(order_item_id).setValue(lsOrderItem);
                            }
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Thanks! Your Order Has Been Done Successfully.", Toast.LENGTH_LONG).show();
                            String token_id = FirebaseInstanceId.getInstance().getToken();
                            Map<String, Object>tokenMap = new HashMap<>();
                            tokenMap.put("token_id",token_id);
                            mFireStore.collection("Clients").document(client_id).update(tokenMap);
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
