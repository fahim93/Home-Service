package com.bitproject.fahim.homeservice.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class FireDB {
    public static final DatabaseReference DB_ROOT_REF = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference CLIENTS = DB_ROOT_REF.child("clients");
    public static final DatabaseReference SERVICE_PROVIDERS = DB_ROOT_REF.child("service_providers");
    public static final DatabaseReference ADMINS = DB_ROOT_REF.child("admins");
    public static final DatabaseReference LAUNDRY_SERVICES = DB_ROOT_REF.child("laundry_services");
    public static final DatabaseReference SP_SECRET_KEYS = DB_ROOT_REF.child("sp_secret_keys");
    public static final DatabaseReference AD_SECRET_KEYS = DB_ROOT_REF.child("ad_secret_keys");
    public static final DatabaseReference LS_ORDERS = DB_ROOT_REF.child("ls_orders");
    public static final DatabaseReference LS_ORDER_ITEMS = DB_ROOT_REF.child("ls_order_items");
    public static final DatabaseReference CLEANING_SERVICES = DB_ROOT_REF.child("cleaning_services");
    public static final DatabaseReference CS_ORDERS = DB_ROOT_REF.child("cs_orders");
    public static final DatabaseReference CS_ORDER_ITEMS = DB_ROOT_REF.child("cs_order_items");
    public static final DatabaseReference COOKING_PRICES = DB_ROOT_REF.child("cooking_prices");
    public static final DatabaseReference COOKING_SERVICE_ORDER = DB_ROOT_REF.child("cooking_service_order");
    public static final DatabaseReference SP_ACCOUNTS = DB_ROOT_REF.child("sp_accounts");
    public static final DatabaseReference ADMIN_ACCOUNTS = DB_ROOT_REF.child("admin_accounts");
    public static final DatabaseReference SP_RATINGS = DB_ROOT_REF.child("sp_ratings");

}
