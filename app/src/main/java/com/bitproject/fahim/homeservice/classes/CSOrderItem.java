package com.bitproject.fahim.homeservice.classes;

public class CSOrderItem {
    private String order_id, order_service_id, service_name;
    private int quantity;
    private double total_price;

    public CSOrderItem() {
    }

    public CSOrderItem(String service_name, int quantity, double total_price) {
        this.service_name = service_name;
        this.quantity = quantity;
        this.total_price = total_price;
    }

    public CSOrderItem(String order_id, String order_service_id, String service_name, int quantity, double total_price) {
        this.order_id = order_id;
        this.order_service_id = order_service_id;
        this.service_name = service_name;
        this.quantity = quantity;
        this.total_price = total_price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_service_id() {
        return order_service_id;
    }

    public void setOrder_service_id(String order_service_id) {
        this.order_service_id = order_service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
}
