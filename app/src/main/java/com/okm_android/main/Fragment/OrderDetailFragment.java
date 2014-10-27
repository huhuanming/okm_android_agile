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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.okm_android.main.Activity.AddAddressActivity;
import com.okm_android.main.Activity.LoginRegisterActivity;
import com.okm_android.main.Activity.OrderAddressActivity;
import com.okm_android.main.Activity.OrderNoteReturn;
import com.okm_android.main.ApiManager.ChenApiManager;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.DefaultAddressData;
import com.okm_android.main.Model.FoodsData;
import com.okm_android.main.Model.RestaurantOrderBackData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.JsonUtils;
import com.okm_android.main.Utils.ShareUtils;
import com.okm_android.main.Utils.ToastUtils;
import com.okm_android.main.Utils.TokenUtils.AccessToken;
import com.okm_android.main.View.Button.BootstrapButton;
import com.okm_android.main.View.Dialog.TimePickerDialog.RadialPickerLayout;
import com.okm_android.main.View.Dialog.TimePickerDialog.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by QYM on 14-10-8.
 */
public class OrderDetailFragment extends Fragment implements TimePickerDialog.OnTimeSetListener{
    private View parentView;
    private Handler handler;
    private TextView userName;
    private TextView userNumber;
    private TextView userAddress;
    private TextView showOrderNote;
    private String user_id;
    private ImageView moreAddress;
    private RelativeLayout moreNote;
    private TextView showTime;
    private CheckBox cbTicket1,cbTicket2,cbSendNow,cbPayOnline,cbPayface;
    private RelativeLayout changeAddress;
    private BootstrapButton sureToPlaceOrder;
    private RelativeLayout moreTime;
    private String rid = "";
    private List<FoodsData> list = new ArrayList<FoodsData>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_order_detail, container, false);
        getActivity().invalidateOptionsMenu();
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        rid = getActivity().getIntent().getExtras().getString("Restaurant_id");
        init();
        user_id = ShareUtils.getId(getActivity());
        if(user_id==null)
        {
            Intent intent = new Intent(getActivity(), LoginRegisterActivity.class);
            startActivity(intent);
        }
        DefaultAddressData();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        DefaultAddressData defaultAddressData = (DefaultAddressData)msg.obj;
                        if(defaultAddressData != null)
                        {
                            userName.setText(defaultAddressData.shipping_user);
                            userNumber.setText(defaultAddressData.phone_number);
                            userAddress.setText(defaultAddressData.shipping_address);
                        }
                        else{
                            Intent intent = new Intent(getActivity(), AddAddressActivity.class);
                            startActivityForResult(intent,2);
                        }
                        break;
                    case Constant.MSG_GETMESSAGE:
                        ToastUtils.setToast(getActivity(),"下单成功");
                        getActivity().finish();
                        break;
                }
                super.handleMessage(msg);
            }

        };
        //下单数据接口
        sureToPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.size() == 0)
                {
                    ToastUtils.setToast(getActivity(),"还没有选择菜品哟～");
                }
                else
                {
                    PostOrder();
                }
            }
        });
        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderAddressActivity.class);
                startActivityForResult(intent,2);
            }
        });

        moreAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderAddressActivity.class);
                startActivityForResult(intent,2);
            }
        });
        moreNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),OrderNoteReturn.class);
                startActivityForResult(intent,1);
            }
        });
        cbPayface.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cbPayOnline.setChecked(false);
                }else{
                    cbPayOnline.setChecked(true);
                }
            }
        });

        cbPayOnline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cbPayface.setChecked(false);
                }else{
                    cbPayface.setChecked(true);
                }
            }
        });
        cbPayface.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cbPayOnline.setChecked(false);
                }else{
                    cbPayOnline.setChecked(true);
                }
            }
        });

        /*cbTicket1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cbTicket2.setChecked(false);
                }else{
                    cbTicket2.setChecked(true);
                }
            }
        });
        cbTicket2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    cbTicket1.setChecked(false);
                }else{
                    cbTicket1.setChecked(true);
                }
            }
        });*/
        final Calendar calendar = Calendar.getInstance();
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
        cbSendNow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    showTime.setText(null);
                    moreTime.setEnabled(false);
                }

                else
                {
                    timePickerDialog.show(getFragmentManager(),"");
                    moreTime.setEnabled(true);
                }
            }
        });

        moreTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                timePickerDialog.show(getFragmentManager(),"");
            }
        });
        return parentView;
    }

    private void init(){
        userName = (TextView) parentView.findViewById(R.id.address_name);
        userNumber = (TextView) parentView.findViewById(R.id.address_number);
        userAddress = (TextView) parentView.findViewById(R.id.address_address);
        moreAddress = (ImageView) parentView.findViewById(R.id.order_address_change);
        moreNote = (RelativeLayout) parentView.findViewById(R.id.setting_update);
        moreTime = (RelativeLayout) parentView.findViewById(R.id.setting_app_mark);
        showOrderNote = (TextView) parentView.findViewById(R.id.show_order_note);
        showTime = (TextView) parentView.findViewById(R.id.show_time);
        cbTicket1 = (CheckBox) parentView.findViewById(R.id.cb_ticket1);
        cbTicket2 = (CheckBox) parentView.findViewById(R.id.cb_ticket2);
        cbSendNow = (CheckBox) parentView.findViewById(R.id.cb_send_now);
        cbPayOnline = (CheckBox) parentView.findViewById(R.id.cb_pay_online);
        cbPayface = (CheckBox) parentView.findViewById(R.id.cb_pay_faces);
        sureToPlaceOrder = (BootstrapButton) parentView.findViewById(R.id.sure_to_placeorder);
        changeAddress = (RelativeLayout) parentView.findViewById(R.id.rl_changeaddress);
        list = new Select().from(FoodsData.class).execute();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case 221:{
                    userName.setText(data.getExtras().getString("name"));
                    userAddress.setText(data.getExtras().getString("address"));
                    userNumber.setText(data.getExtras().getString("number"));
                }
                break;
            case 222:{
                    showOrderNote.setText(data.getExtras().getString("note"));
                }break;
        }
    }
    public void DefaultAddressData()
    {
        AccessToken accessToken = new AccessToken(ShareUtils.getToken(getActivity()),ShareUtils.getKey(getActivity()));
        getUserData(user_id,accessToken.accessToken(), new MainApiManager.FialedInterface() {
            @Override
            public void onSuccess(Object object) {
                NotificationCenter.getInstance().postNotification("setPlaceOrderSwipeFlase");
                // 获取一个Message对象，设置what为1
                Message msg = Message.obtain();
                msg.obj = object;
                msg.what = Constant.MSG_SUCCESS;
                // 发送这个消息到消息队列中
                handler.sendMessage(msg);
            }

            @Override
            public void onFailth(int code) {
                NotificationCenter.getInstance().postNotification("setPlaceOrderSwipeFlase");
                ErrorUtils.setError(code, getActivity());
//                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onOtherFaith() {
                NotificationCenter.getInstance().postNotification("setPlaceOrderSwipeFlase");
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(getActivity(), "发生错误");
            }

            @Override
            public void onNetworkError() {
                NotificationCenter.getInstance().postNotification("setPlaceOrderSwipeFlase");
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(getActivity(), "网络错误");
            }
        });
    }

    private void getUserData(String user_id,String access_token, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.defaultAddressData(user_id,access_token).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DefaultAddressData>() {
                    @Override
                    public void call(DefaultAddressData defaultAddressData) {
                        fialedInterface.onSuccess(defaultAddressData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable,fialedInterface);
                    }
                });
    }

    public void PostOrder()
    {
        NotificationCenter.getInstance().postNotification("setPlaceOrderSwipeTrue");
        String jsonString = JsonUtils.setOrderJson(list);
        String shipping_user = userName.getText().toString();
        String shipping_address = userAddress.getText().toString();
        String phone_number = userNumber.getText().toString();
        AccessToken accessToken = new AccessToken(ShareUtils.getToken(getActivity()),ShareUtils.getKey(getActivity()));
        setOrder(rid,accessToken.accessToken(), jsonString,"0","0", shipping_user,shipping_address
                ,phone_number, new MainApiManager.FialedInterface() {
            @Override
            public void onSuccess(Object object) {
                NotificationCenter.getInstance().postNotification("setPlaceOrderSwipeFlase");
                // 获取一个Message对象，设置what为1
                Message msg = Message.obtain();
                msg.obj = object;
                msg.what = Constant.MSG_GETMESSAGE;
                // 发送这个消息到消息队列中
                handler.sendMessage(msg);
            }

            @Override
            public void onFailth(int code) {
                NotificationCenter.getInstance().postNotification("setPlaceOrderSwipeFlase");
                ErrorUtils.setError(code, getActivity());
//                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onOtherFaith() {
                NotificationCenter.getInstance().postNotification("setPlaceOrderSwipeFlase");
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(getActivity(), "发生错误");
            }

            @Override
            public void onNetworkError() {
                NotificationCenter.getInstance().postNotification("setPlaceOrderSwipeFlase");
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(getActivity(), "网络错误");
            }
        });
    }

    private void setOrder(String restaurant_id, String access_token, String foods,
                          String ship_type, String order_type, String shipping_user,
                          String shipping_address, String phone_number, final MainApiManager.FialedInterface fialedInterface)
    {
        ChenApiManager.RestaurantsOrders(restaurant_id, access_token,
                foods, ship_type, order_type, shipping_user, shipping_address
                , phone_number).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<RestaurantOrderBackData>() {
                    @Override
                    public void call(RestaurantOrderBackData restaurantOrderBackData) {
                        fialedInterface.onSuccess(restaurantOrderBackData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable,fialedInterface);
                    }
                });
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        showTime.setText(hourOfDay+":"+minute);
        if(showTime.getText()!=null){
            cbSendNow.setChecked(false);
        }
    }
}