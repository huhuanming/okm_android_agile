package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;
import com.okm_android.main.Utils.ShareUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by QYM on 14-10-8.
 */
public class PlaceOrderActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener{
    final String[] fragments={
        "com.okm_android.main.Fragment.OrderChooseFragment",
        "com.okm_android.main.Fragment.OrderDetailFragment"
    };
    private Fragment currentFragment;
    private Fragment[] hidefragments = new Fragment[]{null,null};
    @InjectView(R.id.btn_order_choose)RadioButton orderChoose;
    @InjectView(R.id.segmented2)SegmentedGroup segmentedGroup;
    @InjectView(R.id.btn_order_detail)RadioButton orderDetail;
    @InjectView(R.id.swipe_container)SwipeRefreshLayout swipeRefreshLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_food);
        ButterKnife.inject(this);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);

        NotificationCenter.getInstance().addObserver("goToDetailFragment", this, "goToDetailFragment");
        NotificationCenter.getInstance().addObserver("setPlaceOrderSwipeFlase", this, "setPlaceOrderSwipeFlase");

        segmentedGroup.setTintColor(getResources().getColor(R.color.bbutton_info_edge), Color.WHITE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",getIntent().getExtras().getSerializable("data"));
        bundle.putSerializable("allprice",getIntent().getExtras().getString("allprice"));
        getIntent().putExtras(bundle);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Fragment fragment = null;
                switch (checkedId) {

                    case R.id.btn_order_choose:
                        if(hidefragments[0] == null)
                        {
                            fragment = Fragment.instantiate(PlaceOrderActivity.this, fragments[0]);
                            hidefragments[0] = fragment;
                        }
                        FragmentTransaction transactionone = getSupportFragmentManager().beginTransaction();
                        if (!hidefragments[0].isAdded()) {    // 先判断是否被add过
                            transactionone.hide(currentFragment).add(R.id.frame_order, hidefragments[0]).commit(); // 隐藏当前的fragment，add下一个到Activity中
                        } else {
                            transactionone.hide(currentFragment).show(hidefragments[0]).commit(); // 隐藏当前的fragment，显示下一个
                        }
                        currentFragment = hidefragments[0];
                        break;
                    case R.id.btn_order_detail:
                        if(ShareUtils.getId(PlaceOrderActivity.this).equals(""))
                        {
                            Intent intent = new Intent();
                            intent.setClass(PlaceOrderActivity.this,LoginRegisterActivity.class);
                            startActivity(intent);
                        }
                        else {

                            if(hidefragments[1] == null)
                            {
                                fragment = Fragment.instantiate(PlaceOrderActivity.this, fragments[1]);
                                hidefragments[1] = fragment;
                            }
                            FragmentTransaction transactiontwo = getSupportFragmentManager().beginTransaction();
                            if (!hidefragments[1].isAdded()) {    // 先判断是否被add过
                                transactiontwo.hide(currentFragment).add(R.id.frame_order, hidefragments[1]).commit(); // 隐藏当前的fragment，add下一个到Activity中
                            } else {
                                transactiontwo.hide(currentFragment).show(hidefragments[1]).commit(); // 隐藏当前的fragment，显示下一个
                            }
                            currentFragment = hidefragments[1];
                        }
                        break;
                }
            }
        });

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        Fragment fragment = Fragment.instantiate(PlaceOrderActivity.this, fragments[0]);
        tx.add(R.id.frame_order,fragment).commit();
        currentFragment = fragment;
        hidefragments[0] = fragment;
    }
    public void goToDetailFragment(){
        orderDetail.setChecked(true);
    }

    public void setPlaceOrderSwipeFlase(){
        swipeRefreshLayout.setRefreshing(false);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                PlaceOrderActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}