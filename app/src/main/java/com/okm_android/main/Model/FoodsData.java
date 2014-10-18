package com.okm_android.main.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by hu on 14-10-16.
 */
@Table(name = "Food")
public class FoodsData extends Model implements Serializable {
    @Column(name = "Count")
    public int count;

    @Column(name = "Food_price")
    public String food_price;

    @Column(name = "Food_id")
    public String food_id;

    @Column(name = "Food_name")
    public String food_name;

    @Column(name = "Sold_number")
    public String sold_number;

    @Column(name = "Updated_at")
    public String updated_at;

    @Column(name = "IsHave")
    public boolean isHave = false;
}
