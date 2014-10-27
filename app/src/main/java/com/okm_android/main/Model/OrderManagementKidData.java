package com.okm_android.main.Model;

/**
 * Created by qym on 14-10-27.
 */
public class OrderManagementKidData {
    public String count;
    public String total_price;
    public String actual_total_price;
    public Food food;
    public static class Food{
        public String food_name;
        public String shop_price;
    }
}