package com.bitproject.fahim.homeservice.fragments.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitproject.fahim.homeservice.R;
import com.bitproject.fahim.homeservice.adapters.AdapterLaundryService;
import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.bitproject.fahim.homeservice.classes.LaundryService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LaundryServiceFragment extends Fragment implements AdapterView.OnItemSelectedListener, AdapterLaundryService.OnItemClickListener{
    private Spinner mLaundryServiceCategorySpinner;
    private RecyclerView mLaundryServiceItems;
    private AdapterLaundryService mAdapter;
    private ArrayList<LaundryService> laundryServices;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.admin_laundry_service_layout, container, false);
        mLaundryServiceCategorySpinner = v.findViewById(R.id.spinner_laundry_categories);
        mLaundryServiceItems = v.findViewById(R.id.rv_laundry_service_items);
        laundryServices = new ArrayList<>();

        mLaundryServiceCategorySpinner.setOnItemSelectedListener(this);
        mLaundryServiceItems.setHasFixedSize(true);
        mLaundryServiceItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLaundryServiceItems.setItemAnimator(new DefaultItemAnimator());


        mAdapter = new AdapterLaundryService(laundryServices, getActivity());
        mAdapter.setOnItemClickListener(LaundryServiceFragment.this);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Laundry Services");
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
            openAddLaundryServiceItemDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String category = parent.getItemAtPosition(position).toString();
        getLaundryServiceItem(category);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void OnItemClick(int position) {
        Toast.makeText(getActivity(), "Item Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnEditClick(int position) {

    }

    public void getLaundryServiceItem(String category){
        Query query = null;
        if(category.equals("All")){
            query = FireDB.LAUNDRY_SERVICES;
        }else{
            query = FireDB.LAUNDRY_SERVICES.orderByChild("laundry_service_item_type").equalTo(category);
        }

        if (query != null){
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    laundryServices.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                        LaundryService laundryService = ds.getValue(LaundryService.class);
                        laundryServices.add(laundryService);
                    }

                    mLaundryServiceItems.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void openAddLaundryServiceItemDialog(){
        final Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault);
        dialog.setContentView(R.layout.admin_add_laundry_service_layout);
        dialog.setTitle("Add Service Item");
        final Spinner mItemTypeSpinner = dialog.findViewById(R.id.spinner_laundry_categories_add);
        final EditText mItemNameEditText = dialog.findViewById(R.id.et_ls_item_name);
        final EditText mItemPriceWashAndIronEditText = dialog.findViewById(R.id.et_ls_price_wash_and_iron);
        final EditText mItemPriceDryCleanEditText = dialog.findViewById(R.id.et_ls_price_dry_clean);
        final EditText mItemPriceOnlyIronEditText = dialog.findViewById(R.id.et_ls_price_only_iron);
        final Button mSaveItemButton = dialog.findViewById(R.id.btn_save_ls_item);
        mSaveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemTypeSpinner.getSelectedItemPosition() < 1){
                    Toast.makeText(getActivity(), "Please a Laundry Service Type.", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(mItemNameEditText.getText().toString().trim())){
                    mItemNameEditText.setError("Field can't be blank");
                    mItemNameEditText.requestFocus();
                }
                else if (TextUtils.isEmpty(mItemPriceWashAndIronEditText.getText().toString().trim())){
                    mItemPriceWashAndIronEditText.setError("Field can't be blank");
                    mItemPriceWashAndIronEditText.requestFocus();
                }
                else if (TextUtils.isEmpty(mItemPriceDryCleanEditText.getText().toString().trim())){
                    mItemPriceDryCleanEditText.setError("Field can't be blank");
                    mItemPriceDryCleanEditText.requestFocus();
                }
                else if (TextUtils.isEmpty(mItemPriceOnlyIronEditText.getText().toString().trim())){
                    mItemPriceOnlyIronEditText.setError("Field can't be blank");
                    mItemPriceOnlyIronEditText.requestFocus();
                }
                else{
                    String item_type = mItemTypeSpinner.getSelectedItem().toString();
                    String item_name = mItemNameEditText.getText().toString().trim();
                    double price_wash_and_iron = Double.parseDouble(mItemPriceWashAndIronEditText.getText().toString().trim());
                    double price_dry_clean = Double.parseDouble(mItemPriceDryCleanEditText.getText().toString().trim());
                    double price_only_iron = Double.parseDouble(mItemPriceOnlyIronEditText.getText().toString().trim());

                    String id = FireDB.LAUNDRY_SERVICES.push().getKey();
                    LaundryService laundryService = new LaundryService(id,item_name,item_type,price_wash_and_iron,price_dry_clean,price_only_iron);
                    FireDB.LAUNDRY_SERVICES.child(id).setValue(laundryService);
                    Toast.makeText(getActivity(), "Laundry Service Item Added Successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}
