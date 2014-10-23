package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.okm_android.main.Adapter.AddressAdapter;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.AddressData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ShareUtils;
import com.okm_android.main.Utils.ToastUtils;
import com.okm_android.main.Utils.TokenUtils.AccessToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by qym on 14-10-21.
 */
public class OrderAddressActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private List<Map<String,String>> mAppList;
    private AddressAdapter mAdapter;
    private ListView mListView;
    private TextView name,number,address;
    private SwipeRefreshLayout swipeRefreshLayout;
    Handler handler;
    String user_id;
    private List<AddressData> addressDatas = new ArrayList<AddressData>();
    List<Map<String,String>> listItem;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_address);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mListView = (ListView) findViewById(R.id.listView);
        name = (TextView) findViewById(R.id.address_name);
        number = (TextView) findViewById(R.id.address_number);
        address = (TextView) findViewById(R.id.address_address);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);
        user_id = ShareUtils.getId(this);
        AddressData();
        NotificationCenter.getInstance().addObserver("OrderAddressFlash",this,"OrderAddressFlash");
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
                        Map<String,String> map;
                        if(list.size() > 0)
                        {
                            addressDatas.clear();
                            addressDatas.addAll(list);
                            listItem = new ArrayList<Map<String, String>>();
                            for(int i=0;i<addressDatas.size();i++){
                                    map = new HashMap<String, String>();
                                    map.put("name",addressDatas.get(i).shipping_user);
                                    map.put("address",addressDatas.get(i).shipping_address);
                                    map.put("number",addressDatas.get(i).phone_number);
                                    map.put("address_id",addressDatas.get(i).address_id);
                                    listItem.add(map);
                            }
                            mAdapter = new AddressAdapter(OrderAddressActivity.this,listItem);
                            mListView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("name",listItem.get(position).get("name"));
                bundle.putString("address",listItem.get(position).get("number"));
                bundle.putString("number",listItem.get(position).get("address"));
                intent.putExtras(bundle);
                setResult(221, intent);
                finish();
            }
        });
    }

    public void OrderAddressFlash()
    {
        AddressData();
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                OrderAddressActivity.this.finish();
                break;
            case R.id.menu_order_address_add:
            {
                Intent intent = new Intent(OrderAddressActivity.this,AddAddressActivity.class);
                intent.putExtra("SetAddOrChange",0);
                startActivityForResult(intent,2);
            }break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_order_address_add, menu);
        return true;
    }
    public void AddressData()
    {
        AccessToken accessToken = new AccessToken(ShareUtils.getToken(this),ShareUtils.getKey(this));
        getAddressDta(user_id, accessToken.accessToken(), new MainApiManager.FialedInterface() {
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
                ErrorUtils.setError(code, OrderAddressActivity.this);
//                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onOtherFaith() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(OrderAddressActivity.this, "发生错误");
            }

            @Override
            public void onNetworkError() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(OrderAddressActivity.this, "网络错误");
            }
        });
    }
    private void getAddressDta(String user_id,String access_token, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.addressData(user_id, access_token).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AddressData>>() {
                    @Override
                    public void call(List<AddressData> addressDatas) {
                        fialedInterface.onSuccess(addressDatas);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable,fialedInterface);
                    }
                });
    }
    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
