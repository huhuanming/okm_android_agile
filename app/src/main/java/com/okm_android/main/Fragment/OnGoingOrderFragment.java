package com.okm_android.main.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.okm_android.main.Activity.OrderManagementDetails;
import com.okm_android.main.Adapter.OrderTestAdapter;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.WatchOrderData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ShareUtils;
import com.okm_android.main.Utils.ToastUtils;
import com.okm_android.main.Utils.TokenUtils.AccessToken;

import java.util.List;

import rx.android.concurrency.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by chen on 14-10-7.
 */
public class OnGoingOrderFragment extends Fragment {
    private View parentView;
    private ListView listView;
    private OrderTestAdapter adapter;
    private String user_id;
    private Handler handler;
    private List<WatchOrderData> list;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_ongoing_order,container,false);
        init();
        user_id=ShareUtils.getId(getActivity());
        OrderData();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        list = (List<WatchOrderData>) msg.obj;
                        if(list.size()==0)
                        {
                            ToastUtils.setToast(getActivity(),"还没有订单哦，快去下单吧～");
                        }
                        else{
                            adapter=new OrderTestAdapter(getActivity(),list,0);
                            listView.setAdapter(adapter);
                        }
                }
                super.handleMessage(msg);
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent=new Intent(getActivity(), OrderManagementDetails.class);
                intent.putExtra("order_id", list.get(position).oid);
                intent.putExtra("shipping_user",list.get(position).order_info.shipping_user);
                intent.putExtra("phone_number",list.get(position).order_info.phone_number);
                intent.putExtra("shipping_address",list.get(position).order_info.shipping_address);
                intent.putExtra("updated_at",list.get(position).updated_at);
                intent.putExtra("shipping_at",list.get(position).created_at);
                startActivity(intent);
            }
        });
        return parentView;
    }

    private void init(){
        listView = (ListView)parentView.findViewById(R.id.ongoing_order_listview);
    }
    public void OrderData()
    {
        AccessToken accessToken = new AccessToken(ShareUtils.getToken(getActivity()),ShareUtils.getKey(getActivity()));
        getOrderData(user_id, accessToken.accessToken(), 0+"", new MainApiManager.FialedInterface() {
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

    private void getOrderData(String user_id,String access_token,String is_finished, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.watchOrderData(user_id,access_token,is_finished).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<WatchOrderData>>() {
                    @Override
                    public void call(List<WatchOrderData> watchOrderDatas) {
                        fialedInterface.onSuccess(watchOrderDatas);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable,fialedInterface);
                    }
                });
    }
}
