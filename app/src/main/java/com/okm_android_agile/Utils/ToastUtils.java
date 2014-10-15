package com.okm_android_agile.Utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by chen on 14-8-16.
 */
public class ToastUtils {
    public static void setToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
