package com.okm_android.main.Model;

import java.util.List;

/**
 * Created by hu on 14-10-14.
 */
public class RestaurantDetailsBackData {
    public String rid;
    public String name;
    public String avatar;
    public String phone_number;
    public Restaurant_address restaurant_address;
    public Restaurant_status restaurant_status;
    public List<Restaurant_type> restaurant_type;
    public static class Restaurant_address {
        public String address;
        public String radius;
    }

    public static class Restaurant_status {
        public String start_shipping_fee;
        public String shipping_time;
        public String shipping_fee;
        public String board;
    }

    public static class Restaurant_type{
        public Restaurant_type_name restaurant_type_name;
        public class Restaurant_type_name{
            public String type_name;
        }

    }
}
