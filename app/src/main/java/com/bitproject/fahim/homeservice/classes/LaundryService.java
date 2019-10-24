package com.bitproject.fahim.homeservice.classes;

public class LaundryService {
    private String laundry_service_id, laundry_service_item, laundry_service_item_type;
    private double price_wash_and_iron, price_dry_clean, price_only_iron;

    public LaundryService() {
    }

    public LaundryService(String laundry_service_id, String laundry_service_item, String laundry_service_item_type, double price_wash_and_iron, double price_dry_clean, double price_only_iron) {
        this.laundry_service_id = laundry_service_id;
        this.laundry_service_item = laundry_service_item;
        this.laundry_service_item_type = laundry_service_item_type;
        this.price_wash_and_iron = price_wash_and_iron;
        this.price_dry_clean = price_dry_clean;
        this.price_only_iron = price_only_iron;
    }

    public String getLaundry_service_id() {
        return laundry_service_id;
    }

    public void setLaundry_service_id(String laundry_service_id) {
        this.laundry_service_id = laundry_service_id;
    }

    public String getLaundry_service_item() {
        return laundry_service_item;
    }

    public void setLaundry_service_item(String laundry_service_item) {
        this.laundry_service_item = laundry_service_item;
    }

    public String getLaundry_service_item_type() {
        return laundry_service_item_type;
    }

    public void setLaundry_service_item_type(String laundry_service_item_type) {
        this.laundry_service_item_type = laundry_service_item_type;
    }

    public double getPrice_wash_and_iron() {
        return price_wash_and_iron;
    }

    public void setPrice_wash_and_iron(double price_wash_and_iron) {
        this.price_wash_and_iron = price_wash_and_iron;
    }

    public double getPrice_dry_clean() {
        return price_dry_clean;
    }

    public void setPrice_dry_clean(double price_dry_clean) {
        this.price_dry_clean = price_dry_clean;
    }

    public double getPrice_only_iron() {
        return price_only_iron;
    }

    public void setPrice_only_iron(double price_only_iron) {
        this.price_only_iron = price_only_iron;
    }

    @Override
    public String toString() {
        return laundry_service_item;
    }
}
