package com.bitproject.fahim.homeservice.fragments.client;

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
import android.widget.RatingBar;
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
import com.bitproject.fahim.homeservice.classes.Rating;
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

public class ClientOrdersFragment extends Fragment implements AdapterLaundryOrderListClient.OnItemClickListener, AdapterCookingOrderListClient.OnItemClickListener, AdapterCleaningOrderListClient.OnItemClickListener{
    private RecyclerView mLaundryOrderRV, mCleaningOrderRV, mCookingOrderRV;
    private AdapterCleaningOrderListClient mAdapterCleaningOrderListClient;
    private AdapterCookingOrderListClient mAdapterCookingOrderListClient;
    private AdapterLaundryOrderListClient mAdapterLaundryOrderListClient;
    private ArrayList<CookingServiceOrder> cookingServiceOrders;
    private ArrayList<CSOrder> csOrders;
    private ArrayList<LSOrder> lsOrders;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.client_order_list, container, false);
        mLaundryOrderRV = v.findViewById(R.id.rv_client_laundry_order_list);
        mCleaningOrderRV = v.findViewById(R.id.rv_client_cleaning_order_list);
        mCookingOrderRV = v.findViewById(R.id.rv_client_cooking_order_list);

        mLaundryOrderRV.setHasFixedSize(true);
        mLaundryOrderRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLaundryOrderRV.setItemAnimator(new DefaultItemAnimator());

        mCleaningOrderRV.setHasFixedSize(true);
        mCleaningOrderRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCleaningOrderRV.setItemAnimator(new DefaultItemAnimator());

        mCookingOrderRV.setHasFixedSize(true);
        mCookingOrderRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCookingOrderRV.setItemAnimator(new DefaultItemAnimator());

        cookingServiceOrders = new ArrayList<>();
        csOrders = new ArrayList<>();
        lsOrders = new ArrayList<>();

        mAdapterCookingOrderListClient = new AdapterCookingOrderListClient(cookingServiceOrders, getActivity());
        mAdapterCleaningOrderListClient = new AdapterCleaningOrderListClient(csOrders, getActivity());
        mAdapterLaundryOrderListClient = new AdapterLaundryOrderListClient(lsOrders, getActivity());

        mAdapterCookingOrderListClient.setOnItemClickListener(this);
        mAdapterCleaningOrderListClient.setOnItemClickListener(this);
        mAdapterLaundryOrderListClient.setOnItemClickListener(this);

        getOrders();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Your Orders");
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
                csOrder.getAddress(),
                csOrder.getOrder_cancelable()
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
                cookingServiceOrder.getAddress(),
                cookingServiceOrder.getOrder_cancelable()
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
                lsOrder.getAddress(),
                lsOrder.getOrder_cancelable()
        );

    }

    private void getLaundryOrderDetails(final String order_id, final String sp_id, String service_category, String order_date, String service_start_date, String service_start_time, double total_payable, String order_status, String client_id, String address, String order_cancelable) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.client_order_details);
        dialog.setTitle("Order Details");
        TextView tvServiceCategory = dialog.findViewById(R.id.tv_service_category);
        TextView tvOrderDate = dialog.findViewById(R.id.tv_order_date);
        TextView tvServiceStartDate = dialog.findViewById(R.id.tv_service_start_date);
        TextView tvServiceStartTime = dialog.findViewById(R.id.tv_service_start_time);
        TextView tvTotalPayable = dialog.findViewById(R.id.tv_total_payable);
        TextView tvOrderStatus = dialog.findViewById(R.id.tv_order_status);
        final TextView tvSPName = dialog.findViewById(R.id.tv_sp_name);
        final TextView tvSPPhone = dialog.findViewById(R.id.tv_sp_phone);
        TextView tvViewItems = dialog.findViewById(R.id.tv_view_service_items);
        ImageView ivCall = dialog.findViewById(R.id.iv_call);
        Button btnCancelOrder = dialog.findViewById(R.id.btn_cancel_order);
        Button btnGiveReview = dialog.findViewById(R.id.btn_give_review);
        if (order_cancelable.equals("True")){
            btnCancelOrder.setVisibility(View.VISIBLE);
        }else {
            btnCancelOrder.setVisibility(View.GONE);
        }
        if (order_status.equals("Completed")){
            btnGiveReview.setVisibility(View.VISIBLE);
        }
        tvServiceCategory.setText(service_category);
        tvOrderDate.setText(order_date);
        tvServiceStartDate.setText(service_start_date);
        tvServiceStartTime.setText(service_start_time);
        tvTotalPayable.setText(String.valueOf(total_payable));
        tvOrderStatus.setText(order_status);
        Query query = FireDB.SERVICE_PROVIDERS.orderByChild("sp_id").equalTo(sp_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ServiceProvider sp = ds.getValue(ServiceProvider.class);
                    tvSPName.setText(sp.getName());
                    tvSPPhone.setText(sp.getPhone());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvSPPhone.getText().toString();
                call(phone);
            }
        });

        tvViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLaundryOrderItems(order_id);
            }
        });

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.LS_ORDERS.child(order_id).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Order Has Been Cancelled.", Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new ClientOrdersFragment())
                                        .commit();
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
        btnGiveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRatingDialog(sp_id);
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

    private void getCleaningOrderDetails(final String order_id, final String sp_id, String service_category, String order_date, String service_start_date, String service_start_time, double total_price, String order_status, String client_id, String address, String order_cancelable) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.client_order_details);
        dialog.setTitle("Order Details");
        TextView tvServiceCategory = dialog.findViewById(R.id.tv_service_category);
        TextView tvOrderDate = dialog.findViewById(R.id.tv_order_date);
        TextView tvServiceStartDate = dialog.findViewById(R.id.tv_service_start_date);
        TextView tvServiceStartTime = dialog.findViewById(R.id.tv_service_start_time);
        TextView tvTotalPayable = dialog.findViewById(R.id.tv_total_payable);
        TextView tvOrderStatus = dialog.findViewById(R.id.tv_order_status);
        final TextView tvSPName = dialog.findViewById(R.id.tv_sp_name);
        final TextView tvSPPhone = dialog.findViewById(R.id.tv_sp_phone);
        TextView tvViewItems = dialog.findViewById(R.id.tv_view_service_items);
        ImageView ivCall = dialog.findViewById(R.id.iv_call);
        Button btnCancelOrder = dialog.findViewById(R.id.btn_cancel_order);
        Button btnGiveReview = dialog.findViewById(R.id.btn_give_review);
        if (order_cancelable.equals("True")){
            btnCancelOrder.setVisibility(View.VISIBLE);
        }else {
            btnCancelOrder.setVisibility(View.GONE);
        }
        if (order_status.equals("Completed")){
            btnGiveReview.setVisibility(View.VISIBLE);
        }
        tvServiceCategory.setText(service_category);
        tvOrderDate.setText(order_date);
        tvServiceStartDate.setText(service_start_date);
        tvServiceStartTime.setText(service_start_time);
        tvTotalPayable.setText(String.valueOf(total_price));
        tvOrderStatus.setText(order_status);
        Query query = FireDB.SERVICE_PROVIDERS.orderByChild("sp_id").equalTo(sp_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ServiceProvider sp = ds.getValue(ServiceProvider.class);
                    tvSPName.setText(sp.getName());
                    tvSPPhone.setText(sp.getPhone());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvSPPhone.getText().toString();
                call(phone);
            }
        });

        tvViewItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewCleaningOrderItems(order_id);
            }
        });
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.CS_ORDERS.child(order_id).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Order Has Been Cancelled.", Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new ClientOrdersFragment())
                                        .commit();
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
        btnGiveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRatingDialog(sp_id);
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

    private void getCookingOrderDetails(final String order_id, final String sp_id, String service_category, String range_of_members, String service_type, String order_date, String service_start_date, String service_time, double payable_amount, String order_status, String client_id, String address, String order_cancelable) {
        final Dialog dialog = new Dialog(getActivity(),android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.client_order_details);
        dialog.setTitle("Order Details");
        TextView tvServiceCategory = dialog.findViewById(R.id.tv_service_category);
        TextView tvRangeOfMembers = dialog.findViewById(R.id.tv_range_of_members);
        TextView tvServiceType = dialog.findViewById(R.id.tv_service_type);
        TextView tvOrderDate = dialog.findViewById(R.id.tv_order_date);
        TextView tvServiceStartDate = dialog.findViewById(R.id.tv_service_start_date);
        TextView tvServiceStartTime = dialog.findViewById(R.id.tv_service_start_time);
        TextView tvTotalPayable = dialog.findViewById(R.id.tv_total_payable);
        TextView tvOrderStatus = dialog.findViewById(R.id.tv_order_status);
        final TextView tvSPName = dialog.findViewById(R.id.tv_sp_name);
        final TextView tvSPPhone = dialog.findViewById(R.id.tv_sp_phone);
        TextView tvViewItems = dialog.findViewById(R.id.tv_view_service_items);
        ImageView ivCall = dialog.findViewById(R.id.iv_call);
        Button btnCancelOrder = dialog.findViewById(R.id.btn_cancel_order);
        Button btnGiveReview = dialog.findViewById(R.id.btn_give_review);
        CardView cvRangeOfMembers = dialog.findViewById(R.id.cv_range_of_members);
        CardView cvServiceType = dialog.findViewById(R.id.cv_service_type);

        cvRangeOfMembers.setVisibility(View.VISIBLE);
        cvServiceType.setVisibility(View.VISIBLE);
        tvViewItems.setVisibility(View.INVISIBLE);
        if (order_cancelable.equals("True")){
            btnCancelOrder.setVisibility(View.VISIBLE);
        }else {
            btnCancelOrder.setVisibility(View.GONE);
        }
        if (order_status.equals("Completed")){
            btnGiveReview.setVisibility(View.VISIBLE);
        }
        tvServiceCategory.setText(service_category);
        tvOrderDate.setText(order_date);
        tvRangeOfMembers.setText(range_of_members);
        tvServiceType.setText(service_type);
        tvServiceStartDate.setText(service_start_date);
        tvServiceStartTime.setText(service_time);
        tvTotalPayable.setText(String.valueOf(payable_amount));
        tvOrderStatus.setText(order_status);
        Query query = FireDB.SERVICE_PROVIDERS.orderByChild("sp_id").equalTo(sp_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    ServiceProvider sp = ds.getValue(ServiceProvider.class);
                    tvSPName.setText(sp.getName());
                    tvSPPhone.setText(sp.getPhone());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = tvSPPhone.getText().toString();
                call(phone);
            }
        });

        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FireDB.COOKING_SERVICE_ORDER.child(order_id).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Order Has Been Cancelled.", Toast.LENGTH_LONG).show();
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.fragment_container, new ClientOrdersFragment())
                                        .commit();
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
        btnGiveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRatingDialog(sp_id);
            }
        });
        dialog.show();
    }


    private void getOrders(){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String client_id = mAuth.getCurrentUser().getUid();

        //get laundry orders
        Query query = FireDB.LS_ORDERS.orderByChild("client_id").equalTo(client_id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        LSOrder lsOrder = ds.getValue(LSOrder.class);
                        lsOrders.add(lsOrder);
                    }
                    mLaundryOrderRV.setAdapter(mAdapterLaundryOrderListClient);
                    mAdapterLaundryOrderListClient.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get cleaning orders
        Query query2 = FireDB.CS_ORDERS.orderByChild("client_id").equalTo(client_id);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        CSOrder csOrder = ds.getValue(CSOrder.class);
                        csOrders.add(csOrder);
                    }
                    mCleaningOrderRV.setAdapter(mAdapterCleaningOrderListClient);
                    mAdapterCleaningOrderListClient.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get cooking orders
        Query query3 = FireDB.COOKING_SERVICE_ORDER.orderByChild("client_id").equalTo(client_id);
        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        CookingServiceOrder cookingServiceOrder = ds.getValue(CookingServiceOrder.class);
                        cookingServiceOrders.add(cookingServiceOrder);
                    }
                    mCookingOrderRV.setAdapter(mAdapterCookingOrderListClient);
                    mAdapterCookingOrderListClient.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void openRatingDialog(final String sp_id){
        final Dialog dlg = new Dialog(getActivity());
        dlg.setContentView(R.layout.rating_layout);
        dlg.setTitle("Rating");
        final RatingBar ratingBar = dlg.findViewById(R.id.rating_bar_client);
        Button btnSubmitRating = dlg.findViewById(R.id.btn_submit_rating);

        btnSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float ratingvalue = ratingBar.getRating();
                String rating_id = FireDB.SP_RATINGS.push().getKey();
                Rating rating = new Rating(rating_id,sp_id, ratingvalue);
                FireDB.SP_RATINGS.child(rating_id).setValue(rating)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getActivity(), "Thanks For Your Rating", Toast.LENGTH_LONG).show();
                                dlg.dismiss();
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
        dlg.show();
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
