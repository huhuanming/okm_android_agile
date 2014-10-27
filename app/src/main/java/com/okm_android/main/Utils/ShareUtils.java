package com.okm_android.main.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.okm_android.main.Application.OkmApplication;

/**
 * Created by chen on 14-8-20.
 */
public class ShareUtils {

    public static String getKey(Context context) {
        SharedPreferences TokenShared = context.getSharedPreferences("usermessage", 0);
        return TokenShared.getString("key", "");
    }

    public static String getToken(Context context) {
        SharedPreferences TokenShared = context.getSharedPreferences("usermessage", 0);
        return TokenShared.getString("token", "");
    }

    public static void deleteTokenKey(Context context) {
        SharedPreferences Shared = context.getSharedPreferences("usermessage", 0);
        SharedPreferences.Editor editor = Shared.edit();
        editor.clear().commit();
    }

    public static String getId(Context context) {
        SharedPreferences TokenShared = context.getSharedPreferences("usermessage", 0);
        return TokenShared.getString("id", "");
    }

    public static String getKey() {
        SharedPreferences TokenShared = OkmApplication.getAppContext().getSharedPreferences("usermessage", 0);
        return TokenShared.getString("key", "");
    }

    public static String getToken() {
        SharedPreferences TokenShared = OkmApplication.getAppContext().getSharedPreferences("usermessage", 0);
        return TokenShared.getString("token", "");
    }

    public static void deleteTokenKey() {
        SharedPreferences Shared = OkmApplication.getAppContext().getSharedPreferences("usermessage", 0);
        SharedPreferences.Editor editor = Shared.edit();
        editor.clear().commit();
    }

    public static String getId() {
        SharedPreferences TokenShared = OkmApplication.getAppContext().getSharedPreferences("usermessage", 0);
        return TokenShared.getString("id", "");
    }
}
