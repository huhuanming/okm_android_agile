package com.okm_android.main.Application;

import android.app.Application;
import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.okm_android.main.R;

import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by hu on 14-10-18.
 */
public class OkmApplication extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        ActiveAndroid.initialize(this);

        OkmApplication.context = getApplicationContext();

        CustomPushNotificationBuilder builder = new CustomPushNotificationBuilder(this,
                R.layout.customer_notitfication_layout, R.id.icon, R.id.title, R.id.text);  // 指定定制的 Notification Layout
        builder.statusBarDrawable = R.drawable.lingjiaxiaochu;      // 指定最顶层状态栏小图标
        builder.layoutIconDrawable = R.drawable.lingjiaxiaochu;   // 指定下拉状态栏时显示的通知图标
        JPushInterface.setPushNotificationBuilder(2, builder);
    }

    public static Context getAppContext() {
        return OkmApplication.context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ActiveAndroid.dispose();
    }
}
