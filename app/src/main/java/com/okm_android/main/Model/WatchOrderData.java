package com.okm_android.main.Model;

/**
 * Created by qym on 14-10-24.
 */
public class WatchOrderData {
    public String oid;
    public String order_sign;
    public String created_at;
    public String updated_at;
    public Restaurant restaurant;
    public class Restaurant{
        public String rid;
        public String restaurant_name;
        public String phone_number;
    }
    public Order_info order_info;
    public class Order_info{
        public String shipping_user;
        public String shipping_address;
        public String phone_number;
        public String total_price;
        public String actual_total_price;
        public String order_type;
    }

}
