package com.bitproject.fahim.homeservice.fragments.service_provider;

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
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.adapters.AdapterCSOrderItem;
import com.bitproject.fahim.homeservice.adapters.AdapterCleaningOrderListClient;
import com.bitproject.fahim.homeservice.adapters.AdapterCookingOrderListClient;
import com.bitproject.fahim.homeservice.adapters.AdapterLSOrderItem;
import com.bitproject.fahim.homeservice.adapters.AdapterLaundryOrderListClient;
import com.bitproject.fahim.homeservice.classes.CSOrder;
import com.bitproject.fahim.homeservice.classes.CSOrderItem;
import com.bitproject.fahim.homeservice.classes.Client;
import com.bitproject.fahim.homeservice.classes.CookingServiceOrder;
import com.bitproject.fahim.homeservice.classes.LSOrder;
import com.bitproject.fahim.homeservice.classes.LSOrderItem;
import com.bitproject.fahim.homeservice.classes.SPAccount;
import com.bitproject.fahim.homeservice.classes.ServiceProvider;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceProviderOrdersFragment extends Fragment implements AdapterLaundryOrderListClient.OnItemClickListener, AdapterCleaningOrderListClient.OnItemClickListener,AdapterCookingOrderListClient.OnItemClickListener{
    private RecyclerView mLaundryRV, mCookingRV, mCleaningRV;
    private AdapterLaundryOrderListClient mAdapterLaundry;
    private AdapterCleaningOrderListClient mAdapterCleaning;
    private AdapterCookingOrderListClient mAdapterCooking;
    private ArrayList<LSOrder> lsOrders;
    private ArrayList<CSOrder> csOrders;
    private ArrayList<CookingServiceOrder> cookingServiceOrders;
    private FirebaseAuth mAuth;
    private ArrayList<SPAccount>spAccounts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.service_provider_order_list, container, false);
        mAuth = FirebaseAuth.getInstance();
        spAccounts = new ArrayList<>();
        mLaundryRV = v.findViewById(R.id.rv_sp_laundry_order_list);
        mCleaningRV = v.findViewById(R.id.rv_sp_cleaning_order_list);
        mCookingRV = v.findViewById(R.id.rv_sp_cooking_order_list);

        lsOrders = new ArrayList<>();
        csOrders = new ArrayList<>();
        cookingServiceOrders = new ArrayList<>();

        mLaundryRV.setHasFixedSize(true);
        mLaundryRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLaundryRV.setItemAnimator(new DefaultItemAnimator());

        mCleaningRV.setHasFixedSize(true);
        mCleaningRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCleaningRV.setItemAnimator(new DefaultItemAnimator());

        mCookingRV.setHasFixedSize(true);
        mCookingRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCookingRV.setItemAnimator(new DefaultItemAnimator());

        mAdapterLaundry = new AdapterLaundryOrderListClient(lsOrders,getActivity());
        mAdapterCleaning = new AdapterCleaningOrderListClient(csOrders,getActivity());
        mAdapterCooking = new AdapterCookingOrderListClient(cookingServiceOrders,getActivity());

        mAdapterLaundry.setOnItemClickListener(this);
        mAdapterCleaning.setOnItemClickListener(this);
        mAdapterCooking.setOnItemClickListener(this);

        getOrders();

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Order List");
    }

    @Override
    public void OnItemClickCleaning(int position) {
        CSOrder csOrder = csOrders.get(position);
        getCleaningOrderDetails(
                csOrder.getOrder_id(),
                csOrder.getSp_id(),
                csOrder.getService_category(),
                csOrder.getOrder_date(),
                csOrder.getService_start_date(),
                csOrder.getService_start_time(),
                csOrder.getTotal_price(),
                csOrder.getOrder_status(),
                csOrder.getClient_id(),
                csOrder.getAddress()
        );

    }


    @Override
    public void OnItemClickCooking(int position) {
        CookingServiceOrder cookingServiceOrder = cookingServiceOrders.get(position);
        getCookingOrderDetails(
                cookingServiceOrder.getOrder_id(),
                cookingServiceOrder.getSp_id(),
                cookingServiceOrder.getService_category(),
                cookingServiceOrder.getRange_of_members(),
                cookingServiceOrder.getService_type(),
                cookingServiceOrder.getOrder_date(),
                cookingServiceOrder.getService_start_date(),
                cookingServiceOrder.getService_time(),
                cookingServiceOrder.getPayable_amount(),
                cookingServiceOrder.getOrder_status(),
                cookingServiceOrder.getClient_id(),
                cookingServiceOrder.getAddress()
        );
    }

    @Override
    public void OnItemClickLaundry(int position) {
        LSOrder lsOrder = lsOrders.get(position);
        getLaundryOrderDetails(
                lsOrder.getOrder_id(),
                lsOrder.getSp_id(),
                lsOrder.getService_category(),
                lsOrder.getOrder_date(),
                lsOrder.getService_start_date(),
                lsOrder.getService_start_time(),
                lsOrder.getTotal_payable(),
                lsOrder.getOrder_status(),
                lsOrder.getClient_id(),
                lsOrder.getAddress()
        );
    }

    private void getOrders(){
        final String sp_id =  mAuth.getCurrentUser().getUid();
        Query query = FireDB.SERVICE_PROVIDERS.orderByChild("sp_id").equalTo(sp_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ServiceProvider sp = ds.getValue(ServiceProvider.class);
                    if (sp.getService_type().equals("Laundry")){
                        getLaundryOrders(sp_id);
                    }else if (sp.getService_type().equals("Cleaning")){
                        getCleaningOrders(sp_id);
                    }else if (sp.getService_type().equals("Cooking")){
                        getCookingOrders(sp_id);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCookingOrders(String sp_id) {
        Query query = FireDB.COOKING_SERVICE_ORDER.orderByChild("sp_id").equalTo(sp_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cookingServiceOrders.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        CookingServiceOrder cookingServiceOrder = ds.getValue(CookingServiceOrder.class);
                        cookingServiceOrders.add(cookingServiceOrder);
                    }
                    mCookingRV.setAdapter(mAdapterCooking);
                    mAdapterCooking.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getLaundryOrders(String sp_id) {
        Query query = FireDB.LS_ORDERS.orderByChild("sp_id").equalTo(sp_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                csOrders.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        LSOrder lsOrder = ds.getValue(LSOrder.class);
                        lsOrders.add(lsOrder);
                    }
                    mLaundryRV.setAdapter(mAdapterLaundry);
                    mAdapterLaundry.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getCleaningOrders(String sp_id) {
        Query query = FireDB.CS_ORDERS.orderByChild("sp_id").equalTo(sp_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                csOrders.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        CSOrder csOrder = ds.getValue(CSOrder.class);
                        csOrders.add(csOrder);
                    }
                    mCleaningRV.setAdapter(mAdapterCleaning);
                    mAdapterCleaning.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    private void getLaundryOrderDetails(final String order_id, final String sp_id, String service_category, String order_date, String service_start_date, String service_start_time, final double total_payable, String order_status, String client_id, String address) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.service_provider_order_details);
        dialog.setTitle("Order Details");
        TextView tvServiceCategory = dialog.findViewById(R.id.tv_service_category);
        TextView tvOrderDate = dialog.findViewById(R.id.tv_order_date);
        TextView tvServiceStartDate = dialog.findViewById(R.id.tv_service_start_date);
        TextView tvServiceStartTime = dialog.findViewById(R.id.tv_service_start_time);
        TextView tvTotalPayable = dialog.findViewById(R.id.tv_total_payable);
        TextView tvOrderStatus = dialog.findViewById(R.id.tv_order_status);
        final TextView tvClientName = dialog.findViewById(R.id.tv_client_name);
        TextView tvClientAddress = dialog.findViewById(R.id.tv_client_address);
        final TextView tvClientPhone = dialog.findViewById(R.id.tv_client_phone);
        TextView tvViewItems = dialog.findViewById(R.id.tv_view_service_items);
        ImageView ivCall = dialog.findViewById(R.id.iv_call);
        Button btnDeny = dialog.findViewById(R.id.btn_order_deny);
        Button btnAccept = dialog.findViewById(R.id.btn_order_accept);
        Button btnReject = dialog.findViewById(R.id.btn_order_reject);
        Button btnComplete = dialog.findViewById(R.id.btn_order_complete);
        Button btnEnableCancel = dialog.findViewById(R.id.btn_enable_cancel);
        LinearLayout sectionAcceptOrDeny = dialog.findViewById(R.id.section_accept_or_deny);
        LinearLayout sectionCompleteOrReject = dialog.findViewById(R.id.section_complete_or_reject);
        if (order_status.equals("Pending")){
            sectionAcceptOrDeny.setVisibility(View.VISIBLE);
            sectionCompleteOrReject.setVisibility(View.GONE);
        }
        else if(order_status.equals("Accepted")){
            sectionAcceptOrDeny.setVisibility(View.GONE);
            sectionCompleteOrReject.setVisibility(View.VISIBLE);
            if (order_status.equals("False")){
                btnEnableCancel.setVisibility(View.VISIBLE);
            }
        }else if (order_status.equals("Rejected") || order_status.equals("Completed")){
            sectionAcceptOrDeny.setVisibility(View.GONE);
            sectionCompleteOrReject.setVisibility(View.GONE);
        }

        tvServiceCategory.setText(service_category);
        tvOrderDate.setText(order_date);
        tvServiceStartDate.setText(service_start_date);
        tvServiceStartTime.setText(service_start_time);
        tvTotalPayable.setText(String.valueOf(total_payable));
        tvOrderStatus.setText(order_status);
        tvClientAddress.setText(address);
        Query query = FireDB.CLIENTS.orderByChild("client_id").equalTo(client_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Client client = ds.getValue(Client.class);
                    tvClientName.setText(client.getName());
                    tvClientPhone.setText(client.getPhone());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvClientPhone.getText().toString();
                call(phone);
            }
        });

        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.LS_ORDERS.child(order_id).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Order Has Been Denied.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.LS_ORDERS.child(order_id).child("order_status").setValue("Accepted")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Order is Accepted", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.LS_ORDERS.child(order_id).child("order_status").setValue("Rejected")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final double comission = (total_payable * 5) / 100;
                                setRejectedComision(sp_id,comission);
                                Toast.makeText(getActivity(), "Job Has Been Rejected", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.LS_ORDERS.child(order_id).child("order_status").setValue("Completed")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final double comission = (total_payable * 15) / 100;
                                setSPAccount(sp_id,comission,total_payable);
                                Toast.makeText(getActivity(), "Job Has Been Completed.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                dialog.dismiss();
            }
        });
        tvViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLaundryOrderItems(order_id);
            }
        });
        btnEnableCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.LS_ORDERS.child(order_id).child("order_cancelable").setValue("True")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Cancellable Access Is Enabled For Client", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    dialog.show();

    }

    private void viewLaundryOrderItems(String order_id) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.service_provider_view_order_items);
        dialog.setTitle("Order Items");
        final RecyclerView recyclerView = dialog.findViewById(R.id.rv_view_order_items);
        final ArrayList<LSOrderItem>lsOrderItems = new ArrayList<>();
        final AdapterLSOrderItem adapterLSOrderItem = new AdapterLSOrderItem(lsOrderItems,getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Query query = FireDB.LS_ORDER_ITEMS.orderByChild("order_id").equalTo(order_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    LSOrderItem lsOrderItem = ds.getValue(LSOrderItem.class);
                    lsOrderItems.add(lsOrderItem);
                }
                recyclerView.setAdapter(adapterLSOrderItem);
                adapterLSOrderItem.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.show();
    }

    private void getCleaningOrderDetails(final String order_id, final String sp_id, String service_category, String order_date, String service_start_date, String service_start_time, final double total_payable, String order_status, String client_id, String address) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.service_provider_order_details);
        dialog.setTitle("Order Details");
        TextView tvServiceCategory = dialog.findViewById(R.id.tv_service_category);
        TextView tvOrderDate = dialog.findViewById(R.id.tv_order_date);
        TextView tvServiceStartDate = dialog.findViewById(R.id.tv_service_start_date);
        TextView tvServiceStartTime = dialog.findViewById(R.id.tv_service_start_time);
        TextView tvTotalPayable = dialog.findViewById(R.id.tv_total_payable);
        TextView tvOrderStatus = dialog.findViewById(R.id.tv_order_status);
        final TextView tvClientName = dialog.findViewById(R.id.tv_client_name);
        TextView tvClientAddress = dialog.findViewById(R.id.tv_client_address);
        final TextView tvClientPhone = dialog.findViewById(R.id.tv_client_phone);
        TextView tvViewItems = dialog.findViewById(R.id.tv_view_service_items);
        ImageView ivCall = dialog.findViewById(R.id.iv_call);
        Button btnDeny = dialog.findViewById(R.id.btn_order_deny);
        Button btnAccept = dialog.findViewById(R.id.btn_order_accept);
        Button btnReject = dialog.findViewById(R.id.btn_order_reject);
        Button btnComplete = dialog.findViewById(R.id.btn_order_complete);
        Button btnEnableCancel = dialog.findViewById(R.id.btn_enable_cancel);
        LinearLayout sectionAcceptOrDeny = dialog.findViewById(R.id.section_accept_or_deny);
        LinearLayout sectionCompleteOrReject = dialog.findViewById(R.id.section_complete_or_reject);
        if (order_status.equals("Pending")){
            sectionAcceptOrDeny.setVisibility(View.VISIBLE);
            sectionCompleteOrReject.setVisibility(View.GONE);
        }
        else if(order_status.equals("Accepted")){
            sectionAcceptOrDeny.setVisibility(View.GONE);
            sectionCompleteOrReject.setVisibility(View.VISIBLE);
            if (order_status.equals("False")){
                btnEnableCancel.setVisibility(View.VISIBLE);
            }
        }else if (order_status.equals("Rejected") || order_status.equals("Completed")){
            sectionAcceptOrDeny.setVisibility(View.GONE);
            sectionCompleteOrReject.setVisibility(View.GONE);
        }

        tvServiceCategory.setText(service_category);
        tvOrderDate.setText(order_date);
        tvServiceStartDate.setText(service_start_date);
        tvServiceStartTime.setText(service_start_time);
        tvTotalPayable.setText(String.valueOf(total_payable));
        tvOrderStatus.setText(order_status);
        tvClientAddress.setText(address);
        Query query = FireDB.CLIENTS.orderByChild("client_id").equalTo(client_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Client client = ds.getValue(Client.class);
                    tvClientName.setText(client.getName());
                    tvClientPhone.setText(client.getPhone());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvClientPhone.getText().toString();
                call(phone);
            }
        });

        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.CS_ORDERS.child(order_id).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Order Has Been Denied.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.CS_ORDERS.child(order_id).child("order_status").setValue("Accepted")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Order is Accepted", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.CS_ORDERS.child(order_id).child("order_status").setValue("Rejected")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final double comission = (total_payable * 5) / 100;
                                setRejectedComision(sp_id,comission);
                                Toast.makeText(getActivity(), "Job Has Been Rejected", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.CS_ORDERS.child(order_id).child("order_status").setValue("Completed")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final double comission = (total_payable * 15) / 100;
                                setSPAccount(sp_id,comission,total_payable);
                                Toast.makeText(getActivity(), "Job Has Been Completed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
        tvViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCleaningOrderItems(order_id);
            }
        });

        btnEnableCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.CS_ORDERS.child(order_id).child("order_cancelable").setValue("True")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Cancellable Access Is Enabled For Client", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    dialog.show();
    }

    private void viewCleaningOrderItems(String order_id) {
        Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.service_provider_view_order_items);
        dialog.setTitle("Order Items");
        final RecyclerView recyclerView = dialog.findViewById(R.id.rv_view_order_items);
        final ArrayList<CSOrderItem>csOrderItems = new ArrayList<>();
        final AdapterCSOrderItem adapterCSOrderItem = new AdapterCSOrderItem(csOrderItems,getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Query query = FireDB.CS_ORDER_ITEMS.orderByChild("order_id").equalTo(order_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    CSOrderItem csOrderItem = ds.getValue(CSOrderItem.class);
                    csOrderItems.add(csOrderItem);
                }
                recyclerView.setAdapter(adapterCSOrderItem);
                adapterCSOrderItem.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.show();
    }

    private void getCookingOrderDetails(final String order_id, final String sp_id, String service_category, String range_of_members, String service_type, String order_date, String service_start_date, String service_start_time, final double total_payable, String order_status, String client_id, String address) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.service_provider_order_details);
        dialog.setTitle("Order Details");
        TextView tvServiceCategory = dialog.findViewById(R.id.tv_service_category);
        TextView tvRangeOfMembers = dialog.findViewById(R.id.tv_range_of_members);
        TextView tvServiceType = dialog.findViewById(R.id.tv_service_type);
        TextView tvOrderDate = dialog.findViewById(R.id.tv_order_date);
        TextView tvServiceStartDate = dialog.findViewById(R.id.tv_service_start_date);
        TextView tvServiceStartTime = dialog.findViewById(R.id.tv_service_start_time);
        TextView tvTotalPayable = dialog.findViewById(R.id.tv_total_payable);
        TextView tvOrderStatus = dialog.findViewById(R.id.tv_order_status);
        final TextView tvClientName = dialog.findViewById(R.id.tv_client_name);
        TextView tvClientAddress = dialog.findViewById(R.id.tv_client_address);
        final TextView tvClientPhone = dialog.findViewById(R.id.tv_client_phone);
        TextView tvViewItems = dialog.findViewById(R.id.tv_view_service_items);
        ImageView ivCall = dialog.findViewById(R.id.iv_call);
        Button btnDeny = dialog.findViewById(R.id.btn_order_deny);
        Button btnAccept = dialog.findViewById(R.id.btn_order_accept);
        Button btnReject = dialog.findViewById(R.id.btn_order_reject);
        Button btnComplete = dialog.findViewById(R.id.btn_order_complete);
        Button btnEnableCancel = dialog.findViewById(R.id.btn_enable_cancel);
        LinearLayout sectionAcceptOrDeny = dialog.findViewById(R.id.section_accept_or_deny);
        LinearLayout sectionCompleteOrReject = dialog.findViewById(R.id.section_complete_or_reject);
        CardView cvRangeOfMembers = dialog.findViewById(R.id.cv_range_of_members);
        CardView cvServiceType = dialog.findViewById(R.id.cv_service_type);

        cvRangeOfMembers.setVisibility(View.VISIBLE);
        cvServiceType.setVisibility(View.VISIBLE);
        tvViewItems.setVisibility(View.INVISIBLE);

        if (order_status.equals("Pending")){
            sectionAcceptOrDeny.setVisibility(View.VISIBLE);
            sectionCompleteOrReject.setVisibility(View.GONE);
        }
        else if(order_status.equals("Accepted")){
            sectionAcceptOrDeny.setVisibility(View.GONE);
            sectionCompleteOrReject.setVisibility(View.VISIBLE);
            if (order_status.equals("False")){
                btnEnableCancel.setVisibility(View.VISIBLE);
            }
        }else if (order_status.equals("Rejected") || order_status.equals("Completed")){
            sectionAcceptOrDeny.setVisibility(View.GONE);
            sectionCompleteOrReject.setVisibility(View.GONE);
        }
        tvServiceCategory.setText(service_category);
        tvRangeOfMembers.setText(range_of_members);
        tvServiceType.setText(service_type);
        tvOrderDate.setText(order_date);
        tvServiceStartDate.setText(service_start_date);
        tvServiceStartTime.setText(service_start_time);
        tvTotalPayable.setText(String.valueOf(total_payable));
        tvOrderStatus.setText(order_status);
        tvClientAddress.setText(address);
        Query query = FireDB.CLIENTS.orderByChild("client_id").equalTo(client_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Client client = ds.getValue(Client.class);
                    tvClientName.setText(client.getName());
                    tvClientPhone.setText(client.getPhone());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvClientPhone.getText().toString();
                call(phone);
            }
        });

        btnDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.COOKING_SERVICE_ORDER.child(order_id).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Order Has Been Denied.", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.COOKING_SERVICE_ORDER.child(order_id).child("order_status").setValue("Accepted")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Order is Accepted", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.COOKING_SERVICE_ORDER.child(order_id).child("order_status").setValue("Rejected")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final double comission = (total_payable * 5) / 100;
                                setRejectedComision(sp_id,comission);
                                Toast.makeText(getActivity(), "Job Has Been Rejected", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.COOKING_SERVICE_ORDER.child(order_id).child("order_status").setValue("Completed")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                final double comission = (total_payable * 15) / 100;
                                setSPAccount(sp_id,comission,total_payable);
                                Toast.makeText(getActivity(), "Job Has Been Completed.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
        btnEnableCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.COOKING_SERVICE_ORDER.child(order_id).child("order_cancelable").setValue("True")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Cancellable Access Is Enabled For Client", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        dialog.show();
    }

    private void setSPAccount(final String sp_id, final double comission, final double total_payable){
        Query query = FireDB.SP_ACCOUNTS.orderByChild("sp_id").equalTo(sp_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()){
                    String account_id = FireDB.SP_ACCOUNTS.push().getKey();
                    SPAccount spa = new SPAccount(account_id, sp_id, comission, 0,total_payable);
                    FireDB.SP_ACCOUNTS.child(account_id).setValue(spa);

                }else {

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        SPAccount spAccount = ds.getValue(SPAccount.class);
                        SPAccount spa = new SPAccount(
                                spAccount.getAccount_id(),
                                spAccount.getSp_id(),
                                spAccount.getDue_balance() + comission,
                                spAccount.getPaid_balance(),
                                spAccount.getTotal_income() + total_payable
                        );
                        FireDB.SP_ACCOUNTS.child(spAccount.getAccount_id()).setValue(spa);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void setRejectedComision(final String sp_id, final double comission){
        Query query = FireDB.SP_ACCOUNTS.orderByChild("sp_id").equalTo(sp_id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()){
                    String account_id = FireDB.SP_ACCOUNTS.push().getKey();
                    SPAccount spa = new SPAccount(account_id, sp_id, comission, 0,0);
                    FireDB.SP_ACCOUNTS.child(account_id).setValue(spa);

                }else {

                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        SPAccount spAccount = ds.getValue(SPAccount.class);
                        SPAccount spa = new SPAccount(
                                spAccount.getAccount_id(),
                                spAccount.getSp_id(),
                                spAccount.getDue_balance() + comission,
                                spAccount.getPaid_balance(),
                                spAccount.getTotal_income()
                        );
                        FireDB.SP_ACCOUNTS.child(spAccount.getAccount_id()).setValue(spa);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
