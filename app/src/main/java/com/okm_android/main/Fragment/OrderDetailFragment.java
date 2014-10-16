package com.okm_android.main.Fragment;

import android.app.ActionBar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okm_android.main.Adapter.FoodMenuAdapter;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.DefaultAddressData;
import com.okm_android.main.Model.FoodDataResolve;
import com.okm_android.main.Model.RestaurantMenu;
import com.okm_android.main.R;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ToastUtils;

import java.util.List;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by QYM on 14-10-8.
 */
public class OrderDetailFragment extends Fragment{
    private View parentView;
    Handler handler;
    DefaultAddressData defaultAddress;
    TextView userName;
    TextView userNumber;
    TextView userAddress;
    String user_id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        getActivity().invalidateOptionsMenu();
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        userName = (TextView) parentView.findViewById(R.id.address_name);
        userNumber = (TextView) parentView.findViewById(R.id.address_number);
        userAddress = (TextView) parentView.findViewById(R.id.address_address);
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        DefaultAddressData defaultAddressData = (DefaultAddressData)msg.obj;
                        userName.setText(defaultAddressData.shipping_user);
                        userNumber.setText(defaultAddressData.phone_number);
                        userAddress.setText(defaultAddressData.shipping_address);
                        break;
                }
                super.handleMessage(msg);
            }

        };
        return parentView;
    }

    public void DefaultAddressData()
    {
        getRestaurantDta(user_id, new MainApiManager.FialedInterface() {
            @Override
            public void onSuccess(Object object) {
                // 获取一个Message对象，设置what为1
                Message msg = Message.obtain();
                msg.obj = object;
                msg.what = Constant.MSG_SUCCESS;
                // 发送这个消息到消息队列中
                handler.sendMessage(msg);
            }

            @Override
            public void onFailth(int code) {
                ErrorUtils.setError(code, getActivity());
//                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onOtherFaith() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(getActivity(), "发生错误");
            }

            @Override
            public void onNetworkError() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(getActivity(), "网络错误");
            }
        });
    }

    private void getRestaurantDta(String user_id, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.defaultAddressData(user_id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DefaultAddressData>() {
                    @Override
                    public void call(DefaultAddressData defaultAddressData) {
                        fialedInterface.onSuccess(defaultAddressData);
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
