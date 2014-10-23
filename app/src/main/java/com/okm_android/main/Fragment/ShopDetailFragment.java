package com.okm_android.main.Fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okm_android.main.Activity.SortingActivity;
import com.okm_android.main.ApiManager.ChenApiManager;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.Model.RestaurantDetailsBackData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ToastUtils;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by QYM on 14-10-9.
 */
public class ShopDetailFragment extends Fragment{
    private View parentView;
    private String rid;
    private Handler handler;
    private RestaurantDetailsBackData data;

    private TextView shop_address;
    private TextView shop_number;
    private TextView shop_name;
    private TextView show_charact;
    private TextView less_money;
    private TextView send_money;
    private TextView less_time;
    private TextView shop_announce;
    private TextView see_evaluate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_shop_detail, container, false);
        getActivity().invalidateOptionsMenu();
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        rid= getActivity().getIntent().getStringExtra("Restaurant_id");
        init();
        restaurantDetails(rid);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case Constant.MSG_SUCCESS:
                        data = (RestaurantDetailsBackData)msg.obj;
                        initData(data);
                        break;
                }
                super.handleMessage(msg);
            }
        };
        return parentView;
    }

    private void init(){
        shop_address = (TextView)parentView.findViewById(R.id.shop_address);
        shop_number = (TextView)parentView.findViewById(R.id.shop_number);
        shop_name = (TextView)parentView.findViewById(R.id.shop_name);
        show_charact = (TextView)parentView.findViewById(R.id.show_charact);
        less_money = (TextView)parentView.findViewById(R.id.less_money);
        less_time = (TextView)parentView.findViewById(R.id.less_time);
        send_money = (TextView)parentView.findViewById(R.id.send_money);
        shop_announce = (TextView)parentView.findViewById(R.id.shop_announce);
        see_evaluate = (TextView)parentView.findViewById(R.id.see_evaluate_text);

        see_evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SortingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Restaurant_id",rid);
                startActivity(intent);
            }
        });
    }

    private void initData(RestaurantDetailsBackData data){
        shop_address.setText(data.restaurant_address.address);
        shop_number.setText(data.phone_number);
        shop_name.setText(data.name);
        less_money.setText(data.restaurant_status.start_shipping_fee);
        less_time.setText(data.restaurant_status.shipping_time);
        send_money.setText(data.restaurant_status.shipping_fee);
        shop_announce.setText(data.restaurant_status.board);
        int num = data.restaurant_type.size();
        String charact = "";
        for(int i = 0; i < num; i++)
        {
            charact = charact + data.restaurant_type.get(i).restaurant_type_name.type_name+" ";
        }
        show_charact.setText(charact);
        NotificationCenter.getInstance().postNotification("setSwiperefresh");
    }
    public void restaurantDetails(String restaurant_id) {
        getRestaurantDetails(restaurant_id, new MainApiManager.FialedInterface() {
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
                NotificationCenter.getInstance().postNotification("setSwiperefresh");
                ErrorUtils.setError(code, getActivity());
            }

            @Override
            public void onOtherFaith() {
                NotificationCenter.getInstance().postNotification("setSwiperefresh");
                ToastUtils.setToast(getActivity(), "发生错误");
            }

            @Override
            public void onNetworkError() {
                NotificationCenter.getInstance().postNotification("setSwiperefresh");
                ToastUtils.setToast(getActivity(), "网络错误");
            }
        });
    }

    private void getRestaurantDetails(String restaurant_id, final MainApiManager.FialedInterface fialedInterface) {
        ChenApiManager.RestaurantDetails(restaurant_id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RestaurantDetailsBackData>() {
                    @Override
                    public void call(RestaurantDetailsBackData restaurantDetailsBackData) {
                        fialedInterface.onSuccess(restaurantDetailsBackData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable,fialedInterface);
                    }
                });
    }
}
