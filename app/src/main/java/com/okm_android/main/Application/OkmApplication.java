package com.okm_android.main.Application;

import android.app.Application;

import com.activeandroid.ActiveAndroid;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by hu on 14-10-18.
 */
public class OkmApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        ActiveAndroid.initialize(this);

    }
    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
