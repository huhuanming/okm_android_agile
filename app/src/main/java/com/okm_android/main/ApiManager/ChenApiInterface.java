package com.okm_android.main.ApiManager;

import com.okm_android.main.Model.CommentBackData;
import com.okm_android.main.Model.RegisterBackData;
import com.okm_android.main.Model.RestaurantBackData;
import com.okm_android.main.Model.RestaurantDetailsBackData;
import com.okm_android.main.Model.RestaurantOrderBackData;
import com.okm_android.main.Model.RestaurantTypeData;
import com.okm_android.main.Model.ShakeDetailBackData;
import com.okm_android.main.Model.UploadBackData;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by chen on 14-7-28.
 */
public class ChenApiInterface {
    public interface ApiManagerVerificationCode {
        @GET("/users/mobile_verification_code")
        UploadBackData getVerificationCode(@Query("phone_number") String phone_number);
    }

    public interface ApiManagerCreateUser {
        @POST("/users")
        RegisterBackData createUser(@Query("phone_number") String phone_number, @Query("password") String password, @Query("encryption_code") String encryption_code);
    }

    public interface ApiManagerLogin {
        @POST("/users/login")
        RegisterBackData login(@Query("username") String username, @Query("password") String password);
    }

    public interface ApiManagerLoginByOauth {
        @POST("/users/login_by_oauth")
        RegisterBackData loginByOauth(@Query("uid") String uid, @Query("oauth_token") String oauth_token, @Query("oauth_type") String oauth_type);
    }

    public interface ApiManagerRestaurantsList {
        @GET("/restaurants")
        List<RestaurantBackData> RestaurantsList(@Query("latitude") String latitude, @Query("longitude") String longitude, @Query("page") String page);
    }

    public interface ApiManagerRestaurantsTypes {
        @GET("/restaurant_types")
        List<RestaurantTypeData> RestaurantsTypes();
    }

    public interface ApiManagerRestaurantsOrders {
        @POST("/restaurants/{restaurant_id}/orders")
        RestaurantOrderBackData RestaurantsOrders(@Path("restaurant_id") String restaurant_id, @Query("access_token") String access_token,
                                                  @Query("foods") String foods, @Query("ship_type") String ship_type,
                                                  @Query("order_type") String order_type, @Query("shipping_user") String shipping_user,
                                                  @Query("shipping_address") String shipping_address, @Query("phone_number") String phone_number);
    }

    public interface ApiManagerRestaurantDetails {
        @GET("/restaurants/{restaurant_id}")
        RestaurantDetailsBackData RestaurantDetails(@Path("restaurant_id") String restaurant_id);
    }

    public interface ApiManagerGetComments {
        @GET("/{restaurant_id}/comments")
        CommentBackData GetComments(@Path("restaurant_id") String restaurant_id,@Query("cid")String cid,@Query("order")String order);
    }

    public interface ApiManagerShakeFood {
        @GET("/restaurants/shake_to_food")
        ShakeDetailBackData getShakeFood(@Query("longitude")String longitude,@Query("latitude")String latitude);
    }
}
