package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.MenuItem;
import android.widget.TextView;

import com.okm_android.main.Adapter.OrderManagementDetailAdapter;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.Model.OrderManagementKidData;
import com.okm_android.main.Model.WatchOrderData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.DateUtils;
import com.okm_android.main.Utils.ShareUtils;
import com.okm_android.main.Utils.TokenUtils.AccessToken;
import com.okm_android.main.View.ListView.swipemenulistview.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by qym on 14-10-27.
 */
public class OrderManagementDetails extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private String order_id;
    private String shipping_user;
    private String phone_number;
    private String shipping_address;
    private String updated_at;
    private String shipping_at;
    private String order_remark;
    public String is_ticket;
    public String is_receipt;
    public String is_now;
    private WatchOrderData watchOrderData;
    private Handler handler;
    private OrderManagementDetailAdapter adapter;
    private List<OrderManagementKidData> dataList = new ArrayList<OrderManagementKidData>();

    @InjectView(R.id.swipe_container)SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.order_management_item_details_name)TextView order_management_item_details_name;
    @InjectView(R.id.order_management_item_details_number)TextView order_management_item_details_number;
    @InjectView(R.id.order_management_item_details_address)TextView order_management_item_details_address;
    @InjectView(R.id.order_management_item_details_gettime)TextView order_management_item_details_gettime;
    @InjectView(R.id.order_management_item_details_grid)MyGridView order_management_item_details_grid;
    @InjectView(R.id.order_note_detail)TextView order_note_detail;
    @InjectView(R.id.order_management_item_details_timetosend)TextView order_management_item_details_timetosend;
    @InjectView(R.id.order_management_item_details_provide_the_invoice)TextView order_management_item_details_provide_the_invoice;
    @InjectView(R.id.order_management_item_details_commercial_invoice )TextView order_management_item_details_commercial_invoice;

    @InjectView(R.id.order_management_details_name)TextView order_management_details_name;
    @InjectView(R.id.order_management_details_time)TextView order_management_details_time;
    @InjectView(R.id.order_management_details_money)TextView order_management_details_money;
    @InjectView(R.id.order_management_details_totalmoney)TextView order_management_details_totalmoney;

    @OnClick(R.id.order_management_item_details_finish)void order_management_item_details_finish(){
        swipeRefreshLayout.setRefreshing(true);
        //putDetailData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_management_details);
        ButterKnife.inject(this);
        //显示actionbar上面的返回键
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);
        initData();
        //getDetailData();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        initMoney();
                        adapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }

        };
    }

    private void initData(){
        Bundle bundle = getIntent().getExtras();
        order_id = bundle.getString("order_id");
        shipping_user = bundle.getString("shipping_user");
        phone_number = bundle.getString("phone_number");
        shipping_address = bundle.getString("shipping_address");
        updated_at = bundle.getString("updated_at");
        shipping_at = bundle.getString("shipping_at");
        order_remark = bundle.getString("order_remark");
        is_ticket = bundle.getString("is_ticket");
        is_receipt = bundle.getString("is_receipt");
        is_now = bundle.getString("is_now");

        if(is_now.equals("0"))
        {
            String string = "及时送";
            SpannableString sp = new SpannableString(string);
            sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            order_management_item_details_timetosend.setText(sp);
            order_management_item_details_timetosend.setTextColor(getResources().getColor(R.color.delete_text));
            order_management_item_details_gettime.setText("预约"+ DateUtils.getTime(shipping_at)+"送达");
        }
        else
        {
            String string = "预约送";
            SpannableString sp = new SpannableString(string);
            sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            order_management_item_details_gettime.setText(sp);
            order_management_item_details_gettime.setTextColor(getResources().getColor(R.color.delete_text));
        }

        if(is_receipt.equals("0"))
        {
            String string = "需要提供商用小票";
            SpannableString sp = new SpannableString(string);
            sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            order_management_item_details_commercial_invoice.setText(sp);
            order_management_item_details_commercial_invoice.setTextColor(getResources().getColor(R.color.delete_text));

        }

        if(is_ticket.equals("0"))
        {
            String string = "需要提供收银小票";
            SpannableString sp = new SpannableString(string);
            sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            order_management_item_details_provide_the_invoice.setText(sp);
            order_management_item_details_provide_the_invoice.setTextColor(getResources().getColor(R.color.delete_text));
        }

        order_management_item_details_name.setText("收货人: "+shipping_user);
        order_management_item_details_number.setText(phone_number);
        order_management_item_details_address.setText("收货地址: "+shipping_address);

        order_note_detail.setText(order_remark);

        order_management_details_name.setText(shipping_user+"的订单");
        order_management_details_time.setText(DateUtils.getMonthTime(updated_at));

        adapter = new OrderManagementDetailAdapter(OrderManagementDetails.this,dataList);
        order_management_item_details_grid.setAdapter(adapter);

    }

    private void initMoney(){
        if(dataList.size() > 0)
        {
            double num = Double.parseDouble(dataList.get(0).actual_total_price) - Double.parseDouble(dataList.get(0).total_price);
            order_management_details_money.setText("订单金额: ¥"+dataList.get(0).total_price+"   运费: ¥"+num);
            order_management_details_totalmoney.setText("订单总额: ¥"+dataList.get(0).actual_total_price);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:

                OrderManagementDetails.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onRefresh() {

    }
}