package com.okm_android_agile.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by QYM on 14-10-13.
 */
public class FoodDataResolve {
    private List<Integer> typeLong = new ArrayList<Integer>();
    private List<Food> foods = new ArrayList<Food>();
    public class Food
    {
        public String food_price;
        public String food_id;
        public String food_name;
        public String sold_number;
        public String updated_at;
    }
    private List<String> typeName=new ArrayList<String>();
    int typeNameCount=0;
    Food food;

    public FoodDataResolve(List<RestaurantMenu> restaurantMenus) {
        typeLong.add(0);
//        while (restaurantMenus.iterator().hasNext()) {
//            j = 0;
//            typeName.add(restaurantMenus.get(i).type_name);
//            while (restaurantMenus.get(i).foods.iterator().hasNext()) {
//                food = new Food();
//                food.food_name = restaurantMenus.get(i).foods.get(j).food_name;
//                food.food_price = restaurantMenus.get(i).foods.get(j).food_price;
//                food.sold_number = restaurantMenus.get(i).foods.get(j).food_status.sold_number;
//                foods.add(food);
//                j++;
//            }
//            i++;
//            int typeLongNext=typeLong.get(i);
//            typeNameCount=typeNameCount+j;
//            typeLongNext=typeLongNext+j+1;
//            typeLong.add(typeLongNext);
//        }
        int num = restaurantMenus.size();
        int i;
        for(i = 0; i < num;i++)
        {
            int j;
            typeName.add(restaurantMenus.get(i).type_name.toString());
            for(j = 0; j < restaurantMenus.get(i).foods.size(); j++)
            {
                food = new Food();
                food.food_name = restaurantMenus.get(i).foods.get(j).food_name;
                food.food_price = restaurantMenus.get(i).foods.get(j).shop_price;
                food.sold_number = restaurantMenus.get(i).foods.get(j).food_status.sold_number;
                foods.add(food);
                typeNameCount++;
            }
            int typeLongNext=typeLong.get(i);
            typeNameCount++;
            typeLongNext=typeLongNext+j+1;
            typeLong.add(typeLongNext);
        }
        typeLong.remove(i);
    }
    public List<String> getTypeName()
    {
        return typeName;
    }
    public List<Food> getFoods()
    {
        return foods;
    }
    public List<Integer> getTypeLong()
    {
        return typeLong;
    }
    public int getTypeNameCount()
    {
        return typeNameCount;
    }
}
