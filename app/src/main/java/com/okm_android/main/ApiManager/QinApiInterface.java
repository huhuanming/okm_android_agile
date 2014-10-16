package com.okm_android.main.ApiManager;

import com.okm_android.main.Model.DefaultAddressData;
import com.okm_android.main.Model.RestaurantMenu;
import com.okm_android.main.Model.UploadBackData;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by QYM on 14-10-11.
 */
public class QinApiInterface {
    public interface ApiManagerVerificationCode {
        @GET("/restaurants/{restaurant_id}/menus")
        List<RestaurantMenu> RestaurantFood(@Path("restaurant_id") String restaurant_id);
    }
    public interface ApiManagerDefaultAddress{
        @GET("/users/{user_id}/addresses/default_address")
        DefaultAddressData defaultAddressData(@Path("user_id") String user_id);
    }
}
