package com.okm_android.main.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYM on 14-10-13.
 */
public class FoodDataResolve {
    private List<Integer> typeLong = new ArrayList<Integer>();
    private ArrayList<FoodsData> foods = new ArrayList<FoodsData>();
    private List<String> typeName=new ArrayList<String>();
    int typeNameCount=0;

    public FoodDataResolve(List<RestaurantMenu> restaurantMenus) {
        typeLong.add(0);
        int num = restaurantMenus.size();
        for(int i = 0; i < num;i++)
        {
            foods.add(new FoodsData());
            typeName.add(restaurantMenus.get(i).type_name.toString());
            int foodsnum = restaurantMenus.get(i).foods.size();
            for(int j = 0; j < foodsnum; j++)
            {
                FoodsData food = new FoodsData();
                food.food_name = restaurantMenus.get(i).foods.get(j).food_name;
                food.food_price = restaurantMenus.get(i).foods.get(j).shop_price;
                food.sold_number = restaurantMenus.get(i).foods.get(j).food_status.sold_number;
                food.count = 0;
                foods.add(food);
                typeName.add("");
            }
            if(i+1 != num)
                typeLong.add(typeLong.get(i)+foodsnum+1);
        }
    }
    public List<String> getTypeName()
    {
        return typeName;
    }
    public ArrayList<FoodsData> getFoods()
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
