package com.okm_android_agile.Utils;

import android.content.Context;

/**
 * Created by chen on 14-9-29.
 */
public class ErrorUtils {
    public static void setError(int code,Context context){
        switch (code) {
            case 401:
                ToastUtils.setToast(context, "电话号码或者密码有误");
                break;
            case 404:
                ToastUtils.setToast(context, "信息已经过期，请重新登录!");
                break;
            case 501:
                ToastUtils.setToast(context, "上传出错，请再重新上传一次!");
                break;
            case 502:
                ToastUtils.setToast(context, "上传出错，请再重新上传一次!");
                break;
            case 403:
                ToastUtils.setToast(context, "每天只有5次获取验证码的机会");
                break;
            case 405:
                ToastUtils.setToast(context, "请过一分钟再试");
                break;
            default:

                ToastUtils.setToast(context, "网络错误，请重试!");
                break;
        }
    }
}
