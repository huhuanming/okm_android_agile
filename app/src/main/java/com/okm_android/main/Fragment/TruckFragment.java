package com.okm_android.main.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.okm_android.main.Adapter.AddressAdapter;
import com.okm_android.main.Adapter.FoodMenuAdapter;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.AddressData;
import com.okm_android.main.Model.FoodDataResolve;
import com.okm_android.main.Model.RestaurantMenu;
import com.okm_android.main.R;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ShareUtils;
import com.okm_android.main.Utils.ToastUtils;
import com.okm_android.main.Utils.TokenUtils.AccessToken;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenu;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenuCreator;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenuItem;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by chen on 14-9-22.
 */
public class TruckFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private View parentView;
    private List<Map<String,String>> mAppList;
    private AddressAdapter mAdapter;
    private SwipeMenuListView mListView;
    private TextView name,number,address;
    private SwipeRefreshLayout swipeRefreshLayout;
    Handler handler;
    String user_id;
    private List<AddressData> addressDatas = new ArrayList<AddressData>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_address, container, false);

        mListView = (SwipeMenuListView) parentView.findViewById(R.id.listView);
        name = (TextView) parentView.findViewById(R.id.address_name);
        number = (TextView) parentView.findViewById(R.id.address_number);
        address = (TextView) parentView.findViewById(R.id.address_address);
        swipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);
        user_id = ShareUtils.getId(getActivity());
        restaurantData();
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem openItem = new SwipeMenuItem(
                getActivity().getApplicationContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0x9a, 0xd2,0x68)));
                openItem.setWidth(dp2px(67));
                openItem.setTitle("默认");
                openItem.setTitleSize(20);
                openItem.setTitleColor(Color.rgb(0xff, 0xff,0xff));
                menu.addMenuItem(openItem);
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                getActivity().getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xe6,0x2e, 0x2e)));
                deleteItem.setWidth(dp2px(67));
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(20);
                deleteItem.setTitleColor(Color.rgb(0xff, 0xff, 0xff));
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        swipeRefreshLayout.setRefreshing(false);
                        List<AddressData> list = (List<AddressData>) msg.obj;
                        List<Map<String,String>> listItem=new ArrayList<Map<String, String>>();
                        Map<String,String> map;
                        if(list.size() > 0)
                        {
                            addressDatas.clear();
                            addressDatas.addAll(list);
                            for(int i=0;i<addressDatas.size();i++){
                                if(addressDatas.get(i).is_default.toString().equals("1"))
                                {
                                    name.setText(addressDatas.get(i).shipping_user);
                                    address.setText(addressDatas.get(i).shipping_address);
                                    number.setText(addressDatas.get(i).phone_number);
                                }
                                else {
                                    map = new HashMap<String, String>();
                                    map.put("name",addressDatas.get(i).shipping_user);
                                    map.put("address",addressDatas.get(i).shipping_address);
                                    map.put("number",addressDatas.get(i).phone_number);
                                    listItem.add(map);
                                }
                            }
                            mAdapter = new AddressAdapter(getActivity(),listItem);
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                super.handleMessage(msg);
            }

        };
        return parentView;
    }/*
    private List<Map<Strig, String>> get_search()   //从服务器里加载数据
    {
        //ContentResolver resolver=mContext.getContentResolver();
        List<Map<String,String>> list=new ArrayList<Map<String, String>>();
        Map<String,String> map;
        for(int i=0;i<4;i++)
        {
            map=new HashMap<String, String>();
            map.put("name", "秦源懋");
            map.put("number","18200272893");
            map.put("address", "西南交大");
            list.add(map);
        }
        return list;
    }*/
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    public void restaurantData()
    {
        AccessToken accessToken = new AccessToken(ShareUtils.getToken(getActivity()),ShareUtils.getKey(getActivity()));
        getRestaurantDta(user_id,accessToken.accessToken(), new MainApiManager.FialedInterface() {
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

    private void getRestaurantDta(String user_id,String access_token, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.addressData(user_id,access_token).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AddressData>>() {
                    @Override
                    public void call(List<AddressData> addressDatas) {
                        fialedInterface.onSuccess(addressDatas);
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

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
