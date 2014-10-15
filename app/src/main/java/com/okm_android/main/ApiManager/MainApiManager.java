package com.okm_android.main.ApiManager;

import retrofit.RestAdapter;

/**
 * Created by chen on 14-7-24.
 */
public class MainApiManager {

    public static String path = "http://www.0km.me:9000/v1";
    public static final RestAdapter restAdapter = new RestAdapter.Builder()
            .setServer(path)
            .build();


    public static abstract interface FialedInterface{
        public abstract void onSuccess(Object object);
        public abstract void onFailth(int code);
        public abstract void onOtherFaith();
        public abstract void onNetworkError();
    }
}
