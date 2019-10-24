package com.bitproject.fahim.homeservice.classes;

import android.widget.Toast;

import com.bitproject.fahim.homeservice.firebase.FireDB;
import com.google.android.gms.tasks.OnSuccessListener;

public class CleaningService {
    private String cs_id, cs_name, cs_description;
    private double cs_starting_price, cs_price;

    public CleaningService() {
    }

    public CleaningService(String cs_id, String cs_name, String cs_description, double cs_starting_price, double cs_price) {
        this.cs_id = cs_id;
        this.cs_name = cs_name;
        this.cs_description = cs_description;
        this.cs_starting_price = cs_starting_price;
        this.cs_price = cs_price;
    }

    public String getCs_id() {
        return cs_id;
    }

    public void setCs_id(String cs_id) {
        this.cs_id = cs_id;
    }

    public String getCs_name() {
        return cs_name;
    }

    public void setCs_name(String cs_name) {
        this.cs_name = cs_name;
    }

    public String getCs_description() {
        return cs_description;
    }

    public void setCs_description(String cs_description) {
        this.cs_description = cs_description;
    }

    public double getCs_starting_price() {
        return cs_starting_price;
    }

    public void setCs_starting_price(double cs_starting_price) {
        this.cs_starting_price = cs_starting_price;
    }

    public double getCs_price() {
        return cs_price;
    }

    public void setCs_price(double cs_price) {
        this.cs_price = cs_price;
    }


}
