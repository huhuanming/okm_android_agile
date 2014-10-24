package com.okm_android.main.ApiManager;

import com.okm_android.main.Model.AddressAddData;
import com.okm_android.main.Model.AddressData;
import com.okm_android.main.Model.DefaultAddressData;
import com.okm_android.main.Model.RestaurantMenu;
import com.okm_android.main.Model.SearchBackData;
import com.okm_android.main.Model.UploadBackData;
import com.okm_android.main.Model.WatchOrderData;

import java.util.List;

import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
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
        DefaultAddressData defaultAddressData(@Path("user_id") String user_id,@Query("access_token")String access_token);
    }
    public interface ApiManagerPustAddress{
        @POST("/users/{user_id}/addresses")
        AddressAddData addressAddData(@Path("user_id")String user_id,@Query("access_token") String access_token,@Query("shipping_user") String shipping_user,@Query("shipping_address") String shipping_address,@Query("phone_number")String phone_number);
    }
    public interface ApiManagerAddAddress{
        @GET("/users/{user_id}/addresses")
        List<AddressData> AddressData(@Path("user_id")String user_id,@Query("access_token")String access_token);
    }
    public interface ApiManagerDeleteAddress{
        @DELETE("/users/{user_id}/addresses/{address_id}")
        UploadBackData uploadBackData(@Path("user_id")String user_id,@Path("address_id")String address_id,@Query("access_token")String access_token);
    }
    public interface ApiManagerSetDefaultAddress{
        @PUT("/users/{user_id}/addresses/{address_id}/is_default")
        UploadBackData uploadBackDataDef(@Path("user_id")String user_id,@Path("address_id")String address_id,@Query("access_token")String access_token);
    }
    public interface ApiManagerEditAddress{
        @PUT("/users/{user_id}/addresses/{address_id}")
        UploadBackData uploadBackDataEdit(@Path("user_id")String user_id,@Path("address_id")String address_id,@Query("access_token")String access_token,@Query("shipping_user") String shipping_user,@Query("shipping_address") String shipping_address,@Query("phone_number")String phone_number);
    }
    public interface ApiManagerSearchFood{
        @GET("/restaurants/search_food")
        List<SearchBackData> searchBackData(@Query("longitude")String longitude,@Query("latitude")String latitude,@Query("food_name")String food_name);
    }
    public interface ApiManagerGetOrder{
        @GET("/users/{user_id}/orders")
        List<WatchOrderData> watchOrderData(@Path("user_id")String user_id,@Query("access_token")String access_token,@Query("is_finished")String is_finished);
    }
}
