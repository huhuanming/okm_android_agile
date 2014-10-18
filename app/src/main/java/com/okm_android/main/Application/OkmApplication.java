package com.okm_android.main.Application;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

/**
 * Created by hu on 14-10-18.
 */
public class OkmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
