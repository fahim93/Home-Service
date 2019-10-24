package com.bitproject.fahim.homeservice.classes;

import java.util.ArrayList;

public class LSOrder {
    private String order_id, client_id, sp_id, service_category, order_date, service_start_date, service_start_time, address, order_status,order_cancelable;
    private double service_cost, delivery_charge, total_payable;

    public LSOrder() {
    }

    public LSOrder(String order_id, String client_id, String sp_id, String service_category, String order_date, String service_start_date, String service_start_time, String address, String order_status, String order_cancelable, double service_cost, double delivery_charge, double total_payable) {
        this.order_id = order_id;
        this.client_id = client_id;
        this.sp_id = sp_id;
        this.service_category = service_category;
        this.order_date = order_date;
        this.service_start_date = service_start_date;
        this.service_start_time = service_start_time;
        this.address = address;
        this.order_status = order_status;
        this.order_cancelable = order_cancelable;
        this.service_cost = service_cost;
        this.delivery_charge = delivery_charge;
        this.total_payable = total_payable;
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

    public String getService_start_time() {
        return service_start_time;
    }

    public void setService_start_time(String service_start_time) {
        this.service_start_time = service_start_time;
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

    public double getService_cost() {
        return service_cost;
    }

    public void setService_cost(double service_cost) {
        this.service_cost = service_cost;
    }

    public double getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(double delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public double getTotal_payable() {
        return total_payable;
    }

    public void setTotal_payable(double total_payable) {
        this.total_payable = total_payable;
    }
}
