package com.okm_android_agile.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.okm_android_agile.ApiManager.ChenApiManager;
import com.okm_android_agile.ApiManager.MainApiManager;
import com.okm_android_agile.Model.RegisterBackData;
import com.okm_android_agile.R;
import com.okm_android_agile.Utils.Constant;
import com.okm_android_agile.Utils.EncodeUtils;
import com.okm_android_agile.Utils.ErrorUtils;
import com.okm_android_agile.Utils.ToastUtils;
import com.okm_android_agile.View.Button.BootstrapButton;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by chen on 14-9-23.
 */
public class LoginFragment extends Fragment{
    private View parentView;
    private RelativeLayout sinaLayout;
    private EditText login_phonenumber;
    private EditText login_password;
    private SmoothProgressBar progressbar;
    private BootstrapButton login_button;

    private SharedPreferences.Editor editor;
    private SharedPreferences mshared;

    private Handler handler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_login, container, false);
        init();
        initSina();
        initListener();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Constant.MSG_FINISH:
                        ToastUtils.setToast(getActivity(), "登录失败");
                        break;
                    case Constant.MSG_SUCCESS:
                        progressbar.setVisibility(View.VISIBLE);
                        break;
                }
            }
        };

        return parentView;
    }

    private void init(){
        login_phonenumber = (EditText)parentView.findViewById(R.id.login_phonenumber);
        login_password = (EditText)parentView.findViewById(R.id.login_password);
        progressbar = (SmoothProgressBar)parentView.findViewById(R.id.login_progressbar);
        login_button = (BootstrapButton)parentView.findViewById(R.id.login_button);
    }
    private void initSina(){
        sinaLayout = (RelativeLayout)parentView.findViewById(R.id.fragment_login_sinalayout);
        sinaLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareSDK.initSDK(getActivity(),"3361c24ca69f");
                Platform[] sina = ShareSDK.getPlatformList(getActivity());
                sina[0].setPlatformActionListener(new PlatformActionListener() {
                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> stringObjectHashMap) {
                        handler.obtainMessage(Constant.MSG_SUCCESS).sendToTarget();
                        login(platform.getDb().getUserId(),platform.getDb().getToken());
                    }

                    @Override
                    public void onError(Platform platform, int i, Throwable throwable) {
                        handler.obtainMessage(Constant.MSG_FINISH).sendToTarget();
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {

                    }
                });
                if (sina[0].isValid()) {
                    sina[0].removeAccount();
                }
                sina[0].showUser(null);
            }
        });

    }

    private void initListener(){
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    private void login()
    {
        progressbar.setVisibility(View.VISIBLE);
        String username = login_phonenumber.getText().toString().trim();
        String password = login_password.getText().toString().trim();

        if(username.equals(""))
        {
            ToastUtils.setToast(getActivity(), "请输入电话号码");
        }
        else if(password.equals(""))
        {
            ToastUtils.setToast(getActivity(),"请输入密码");
        }
        else{
            userLogin(username, EncodeUtils.MD5(EncodeUtils.MD5(password)), new MainApiManager.FialedInterface() {
                @Override
                public void onSuccess(Object object) {
                    ToastUtils.setToast(getActivity(), "登录成功");
                    RegisterBackData registerBackData = (RegisterBackData) object;
                    mshared = getActivity().getSharedPreferences("usermessage", 0);
                    editor = mshared.edit();
                    editor.putString("token", registerBackData.access_token.token);
                    editor.putString("key", registerBackData.access_token.key);
                    editor.commit();
                    progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onFailth(int code) {
                    ErrorUtils.setError(code, getActivity());
                    progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onOtherFaith() {
                    progressbar.setVisibility(View.GONE);
                    ToastUtils.setToast(getActivity(), "发生错误");
                }

                @Override
                public void onNetworkError() {
                    progressbar.setVisibility(View.GONE);
                    ToastUtils.setToast(getActivity(), "网络错误");
                }
            });
        }

    }

    private void userLogin(String phone_number,String password, final MainApiManager.FialedInterface fialedInterface)
    {
        ChenApiManager.Login(phone_number, password).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RegisterBackData>() {
                    @Override
                    public void call(RegisterBackData registerBackData) {
                        fialedInterface.onSuccess(registerBackData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if(throwable.getClass().getName().toString().indexOf("RetrofitError") != -1) {
                            retrofit.RetrofitError e = (retrofit.RetrofitError) throwable;
                            if(e.isNetworkError())
                            {
                                fialedInterface.onNetworkError();

                            }
                            else {
                                fialedInterface.onFailth(e.getResponse().getStatus());
                            }
                        }
                        else{
                            fialedInterface.onOtherFaith();
                        }
                    }
                });
    }

    private void login(String uid, String oauth_token)
    {

            sinaLogin(uid, oauth_token, "0", new MainApiManager.FialedInterface() {
                @Override
                public void onSuccess(Object object) {
                    ToastUtils.setToast(getActivity(), "登录成功");
                    RegisterBackData registerBackData = (RegisterBackData) object;
                    mshared = getActivity().getSharedPreferences("usermessage", 0);
                    editor = mshared.edit();
                    editor.putString("token", registerBackData.access_token.token);
                    editor.putString("key", registerBackData.access_token.key);
                    editor.commit();
                    progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onFailth(int code) {
                    ErrorUtils.setError(code, getActivity());
                    progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onOtherFaith() {
                    progressbar.setVisibility(View.GONE);
                    ToastUtils.setToast(getActivity(), "发生错误");
                }

                @Override
                public void onNetworkError() {
                    progressbar.setVisibility(View.GONE);
                    ToastUtils.setToast(getActivity(), "网络错误");
                }
            });
    }

    private void sinaLogin(String uid,String oauth_token,String oauth_type, final MainApiManager.FialedInterface fialedInterface)
    {
        ChenApiManager.LoginByOauth(uid, oauth_token, oauth_type).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RegisterBackData>() {
                    @Override
                    public void call(RegisterBackData registerBackData) {
                        fialedInterface.onSuccess(registerBackData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if(throwable.getClass().getName().toString().indexOf("RetrofitError") != -1) {
                            retrofit.RetrofitError e = (retrofit.RetrofitError) throwable;
                            if(e.isNetworkError())
                            {
                                fialedInterface.onNetworkError();

                            }
                            else {
                                fialedInterface.onFailth(e.getResponse().getStatus());
                            }
                        }
                        else{
                            fialedInterface.onOtherFaith();
                        }
                    }
                });
    }
}
