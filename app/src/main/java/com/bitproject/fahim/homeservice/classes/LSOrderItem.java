package com.bitproject.fahim.homeservice.classes;

public class LSOrderItem {
    private String order_id, order_item_id, item_name, item_type, service_type;
    private int quantity;
    private double price;

    public LSOrderItem() {
    }

    public LSOrderItem(String item_name, String item_type, String service_type, int quantity, double price) {
        this.item_name = item_name;
        this.item_type = item_type;
        this.service_type = service_type;
        this.quantity = quantity;
        this.price = price;
    }

    public LSOrderItem(String order_id, String order_item_id, String item_name, String item_type, String service_type, int quantity, double price) {
        this.order_id = order_id;
        this.order_item_id = order_item_id;
        this.item_name = item_name;
        this.item_type = item_type;
        this.service_type = service_type;
        this.quantity = quantity;
        this.price = price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_item_id() {
        return order_item_id;
    }

    public void setOrder_item_id(String order_item_id) {
        this.order_item_id = order_item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
