package com.okm_android_agile.Model;

import java.util.List;

/**
 * Created by QYM on 14-10-11.
 */
public class RestaurantMenu {
    public String type_name;
    public List<Food> foods;
    public static class Food
    {
        public String shop_price;
        public String fid;
        public String food_name;
        public Food_status food_status;
        public class Food_status
        {
            public String sold_number;
            public String updated_at;
        }

        public String getFormatShopPrice(){
            String  price = new  String();

            return price;
        }
    }
}
