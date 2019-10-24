package com.bitproject.fahim.homeservice.classes;

public class CookingServiceOrder {
    private String order_id, client_id, sp_id, service_category, gender_of_cook, range_of_members, service_type, order_date, service_start_date, service_time, address, order_status, order_cancelable;
    private double payable_amount;

    public CookingServiceOrder() {
    }

    public CookingServiceOrder(String order_id, String client_id, String sp_id, String service_category, String gender_of_cook, String range_of_members, String service_type, String order_date, String service_start_date, String service_time, String address, String order_status, String order_cancelable, double payable_amount) {
        this.order_id = order_id;
        this.client_id = client_id;
        this.sp_id = sp_id;
        this.service_category = service_category;
        this.gender_of_cook = gender_of_cook;
        this.range_of_members = range_of_members;
        this.service_type = service_type;
        this.order_date = order_date;
        this.service_start_date = service_start_date;
        this.service_time = service_time;
        this.address = address;
        this.order_status = order_status;
        this.order_cancelable = order_cancelable;
        this.payable_amount = payable_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getSp_id() {
        return sp_id;
    }

    public void setSp_id(String sp_id) {
        this.sp_id = sp_id;
    }

    public String getService_category() {
        return service_category;
    }

    public void setService_category(String service_category) {
        this.service_category = service_category;
    }

    public String getGender_of_cook() {
        return gender_of_cook;
    }

    public void setGender_of_cook(String gender_of_cook) {
        this.gender_of_cook = gender_of_cook;
    }

    public String getRange_of_members() {
        return range_of_members;
    }

    public void setRange_of_members(String range_of_members) {
        this.range_of_members = range_of_members;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getService_start_date() {
        return service_start_date;
    }

    public void setService_start_date(String service_start_date) {
        this.service_start_date = service_start_date;
    }

    public String getService_time() {
        return service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_cancelable() {
        return order_cancelable;
    }

    public void setOrder_cancelable(String order_cancelable) {
        this.order_cancelable = order_cancelable;
    }

    public double getPayable_amount() {
        return payable_amount;
    }

    public void setPayable_amount(double payable_amount) {
        this.payable_amount = payable_amount;
    }
}
