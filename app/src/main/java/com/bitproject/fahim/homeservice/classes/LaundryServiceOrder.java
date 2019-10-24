package com.bitproject.fahim.homeservice.classes;

public class LaundryServiceOrder {
    //private String order_id, laundry_service_id,laundry_service_item, laundry_service_item_type, laundry_type, quantity, price;
    private String item_name, laundry_type;
    private int quantity;
    private double price;

    public LaundryServiceOrder() {
    }

    public LaundryServiceOrder(String item_name, String laundry_type, int quantity, double price) {
        this.item_name = item_name;
        this.laundry_type = laundry_type;
        this.quantity = quantity;
        this.price = price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getLaundry_type() {
        return laundry_type;
    }

    public void setLaundry_type(String laundry_type) {
        this.laundry_type = laundry_type;
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
