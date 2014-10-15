package com.okm_android.main.Model;

/**
 * Created by hu on 14-10-10.
 */
public class RestaurantBackData {
    public String rid;
    public String name;
    public String avatar;
    public Status status;

    public static class Status{
        public String start_shipping_fee;
        public String shipping_time;
    }
}
