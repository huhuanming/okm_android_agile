package com.okm_android.main.Utils;

import com.okm_android.main.Model.FoodsData;

import java.util.List;

/**
 * Created by hu on 14-10-18.
 */
public class JsonUtils {
    public static String setOrderJson(List<FoodsData> list){
        int num = list.size();
        String jsonString = "[";
        for(int i = 0; i < num; i++)
        {
            jsonString = jsonString + "{\"fid\":\""+ list.get(i).food_id + "\",\"count\":\""
                    + list.get(i).count + "\"}";
            if((i + 1) != num)
            {
                jsonString = jsonString + ",";
            }
        }
        return jsonString;
    }
}
