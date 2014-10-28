package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.AddressAddData;
import com.okm_android.main.Model.UploadBackData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ShareUtils;
import com.okm_android.main.Utils.ToastUtils;
import com.okm_android.main.Utils.TokenUtils.AccessToken;

import rx.android.concurrency.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by qym on 14-10-17.
 */
public class AddAddressActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{
    EditText addName,addAddress,addNumber;
    String user_id;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int flagAdd=0;
    private String address_id;
    private LinearLayout delLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = this.getIntent();
        flagAdd=intent.getIntExtra("SetAddOrChange",flagAdd);
        addName = (EditText) findViewById(R.id.et_addname);
        addNumber = (EditText) findViewById(R.id.et_addnumber);
        addAddress = (EditText) findViewById(R.id.et_addaddress);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        delLayout = (LinearLayout) findViewById(R.id.ll_delete);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);
        user_id = ShareUtils.getId(this);
        if(flagAdd == 1) // 加载从上一界面传来的数据
        {
            addName.setText(intent.getStringArrayListExtra("addressData").get(0).toString());
            addNumber.setText(intent.getStringArrayListExtra("addressData").get(1).toString());
            addAddress.setText(intent.getStringArrayListExtra("addressData").get(2).toString());
            address_id = intent.getStringArrayListExtra("addressData").get(3).toString();
            delLayout.setVisibility(View.VISIBLE);
        }
        delLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteAddressData();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                AddAddressActivity.this.finish();
                break;
            case R.id.menu_ok:
            {
                if(flagAdd==0) //添加地址数据到数据库
                {
                    PustAddress();
                    finish();
                }
                else{  //修改地址到数据库
                    EditAddressData(address_id);
                }
            }break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_address, menu);
        return true;
    }

    private void PustAddress()
    {
        String shipping_user=addName.getText().toString();
        String shipping_address=addAddress.getText().toString();
        String phone_number=addNumber.getText().toString();
        if(shipping_user.equals(""))
        {
            ToastUtils.setToast(this, "请输入收货人");
        }
        else if(shipping_address.equals(""))
        {
            ToastUtils.setToast(this,"请输入收货地址");
        }
        else {
            if (phone_number.equals("")) {
                ToastUtils.setToast(this, "请输入联系方式");
            } else {
                AccessToken accessToken = new AccessToken(ShareUtils.getToken(AddAddressActivity.this),ShareUtils.getKey(AddAddressActivity.this));
                swipeRefreshLayout.setEnabled(true);
                swipeRefreshLayout.setRefreshing(true);
                addAddress(user_id,accessToken.accessToken(),shipping_user, shipping_address, phone_number, new MainApiManager.FialedInterface() {
                    @Override
                    public void onSuccess(Object object) {
                        ToastUtils.setToast(AddAddressActivity.this, "地址添加成功");
                        NotificationCenter.getInstance().postNotification("OrderAddressFlash");
                        NotificationCenter.getInstance().postNotification("ReFlashAddress");
                       // AddAddressActivity.this.finish();
                    }

                    @Override
                    public void onFailth(int code) {
                        ErrorUtils.setError(code, AddAddressActivity.this);
                    }

                    @Override
                    public void onOtherFaith() {
                        ToastUtils.setToast(AddAddressActivity.this, "发生错误");
                    }

                    @Override
                    public void onNetworkError() {
                        ToastUtils.setToast(AddAddressActivity.this, "网络错误");
                    }
                });
            }
        }
    }
    private void addAddress(String user_id,String access_token ,String shipping_user,String shipping_address, String phone_number,final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.addressAddData(user_id,access_token,shipping_user, shipping_address, phone_number).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<AddressAddData>() {
                    @Override
                    public void call(AddressAddData addressAddData) {
                        fialedInterface.onSuccess(addressAddData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable,fialedInterface);
                    }
                });
    }
    public void EditAddressData(String address_id)
    {
        String shipping_user=addName.getText().toString();
        String shipping_address=addAddress.getText().toString();
        String phone_number=addNumber.getText().toString();
        if(shipping_user.equals(""))
        {
            ToastUtils.setToast(this, "请输入收货人");
        }
        else if(shipping_address.equals(""))
        {
            ToastUtils.setToast(this,"请输入收货地址");
        }
        else if (phone_number.equals("")) {
            ToastUtils.setToast(this, "请输入联系方式");
        } else {
            swipeRefreshLayout.setRefreshing(true);
            AccessToken accessToken = new AccessToken(ShareUtils.getToken(this), ShareUtils.getKey(this));
            EditAddressDta(user_id, address_id, accessToken.accessToken(),shipping_user, shipping_address, phone_number, new MainApiManager.FialedInterface() {
                @Override
                public void onSuccess(Object object) {
                    ToastUtils.setToast(AddAddressActivity.this, "修改成功");
                    NotificationCenter.getInstance().postNotification("ReFlashAddress");
                    finish();
                }

                @Override
                public void onFailth(int code) {
                    ErrorUtils.setError(code, AddAddressActivity.this);
//                progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onOtherFaith() {
//                progressbar.setVisibility(View.GONE);
                    ToastUtils.setToast(AddAddressActivity.this, "发生错误");
                }

                @Override
                public void onNetworkError() {
//                progressbar.setVisibility(View.GONE);
                    ToastUtils.setToast(AddAddressActivity.this, "网络错误");
                }
            });
        }
    }
    private void EditAddressDta(String user_id,String address_id,String access_token,String shipping_user,String shipping_address, String phone_number, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.uploadBackDataEdit(user_id, address_id, access_token,shipping_user, shipping_address, phone_number).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UploadBackData>() {
                    @Override
                    public void call(UploadBackData uploadBackData) {
                        fialedInterface.onSuccess(uploadBackData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable, fialedInterface);
                    }
                });
    }
    public void DeleteAddressData()
    {
        AccessToken accessToken = new AccessToken(ShareUtils.getToken(this),ShareUtils.getKey(this));
        deleteAddressDta(user_id, address_id, accessToken.accessToken(), new MainApiManager.FialedInterface() {
            @Override
            public void onSuccess(Object object) {
                ToastUtils.setToast(AddAddressActivity.this, "删除成功");
                NotificationCenter.getInstance().postNotification("ReFlashAddress");
                finish();
            }

            @Override
            public void onFailth(int code) {
                ErrorUtils.setError(code, AddAddressActivity.this);
//                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onOtherFaith() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(AddAddressActivity.this, "发生错误");
            }

            @Override
            public void onNetworkError() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(AddAddressActivity.this, "网络错误");
            }
        });
    }
    private void deleteAddressDta(String user_id,String address_id,String access_token, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.uploadBackData(user_id, address_id,access_token).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UploadBackData>() {
                    @Override
                    public void call(UploadBackData uploadBackData) {
                        fialedInterface.onSuccess(uploadBackData);
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
