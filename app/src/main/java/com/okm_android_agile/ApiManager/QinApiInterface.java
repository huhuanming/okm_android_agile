package com.okm_android_agile.ApiManager;


import com.okm_android_agile.Model.RestaurantMenu;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by QYM on 14-10-11.
 */
public class QinApiInterface {
    public interface ApiManagerVerificationCode {
        @GET("/restaurants/{restaurant_id}/menus")
        List<RestaurantMenu> RestaurantFood(@Path("restaurant_id") String restaurant_id);
    }
}
