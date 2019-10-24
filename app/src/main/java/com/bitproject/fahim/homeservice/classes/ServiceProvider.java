package com.bitproject.fahim.homeservice.classes;

import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceProvider {
    private String sp_id, name, gender, address, phone, email, service_type;

    public ServiceProvider() {
    }

    public ServiceProvider(String sp_id, String name, String gender, String address, String phone, String email, String service_type) {
        this.sp_id = sp_id;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.service_type = service_type;
    }

    public String getSp_id() {
        return sp_id;
    }

    public void setSp_id(String sp_id) {
        this.sp_id = sp_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public ArrayList<ServiceProvider> getServiceProviders(){
        final ArrayList<ServiceProvider>spList = new ArrayList<>();
        FireDB.SERVICE_PROVIDERS.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot ds :dataSnapshot.getChildren()){
                        ServiceProvider sp = ds.getValue(ServiceProvider.class);
                        spList.add(sp);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return spList;
    }
}
