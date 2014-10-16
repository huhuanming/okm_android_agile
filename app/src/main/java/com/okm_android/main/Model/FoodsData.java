package com.okm_android.main.Model;

import java.io.Serializable;

/**
 * Created by hu on 14-10-16.
 */
public class FoodsData implements Serializable {
    public int count;
    public String food_price;
    public String food_id;
    public String food_name;
    public String sold_number;
    public String updated_at;
    public boolean isHave = false;
}
