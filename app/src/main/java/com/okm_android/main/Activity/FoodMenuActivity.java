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
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by QYM on 14-10-10.
 */
public class FoodMenuActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener{
    final String[] fragments={
            "com.okm_android.main.Fragment.FoodMenuFragment",
            "com.okm_android.main.Fragment.ShopDetailFragment"
    };
    private Fragment currentFragment;
    private Fragment[] hidefragments = new Fragment[]{null,null};

    public String rid=null;
    @InjectView(R.id.rbtn_food_menu)RadioButton foodMenu;
    @InjectView(R.id.shop_message_segmented)SegmentedGroup segmentedGroup;
    @InjectView(R.id.rbtn_shop_detail)RadioButton shopDetail;
    @InjectView(R.id.swipe_container)SwipeRefreshLayout swipeRefreshLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_shop_message);
        ButterKnife.inject(this);

        Intent intent = this.getIntent();
        final Bundle bundle = intent.getExtras();
        rid=bundle.getString("rid");
        getIntent().putExtra("Restaurant_id", rid);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);
        NotificationCenter.getInstance().addObserver("setSwiperefresh", this, "setSwiperefresh");
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        segmentedGroup.setTintColor(getResources().getColor(R.color.bbutton_info_edge), Color.WHITE);

        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                Fragment fragment = null;
                switch (checkedId)
                {
                    case R.id.rbtn_food_menu:
                        getIntent().putExtra("Restaurant_id", rid);
                        if(hidefragments[0] == null)
                        {
                            fragment = Fragment.instantiate(FoodMenuActivity.this, fragments[0]);
                            hidefragments[0] = fragment;
                        }
                        FragmentTransaction transactionone = getSupportFragmentManager().beginTransaction();
                        if (!hidefragments[0].isAdded()) {    // 先判断是否被add过
                            transactionone.hide(currentFragment).add(R.id.shop_message_fragment, hidefragments[0]).commit(); // 隐藏当前的fragment，add下一个到Activity中
                        } else {
                            transactionone.hide(currentFragment).show(hidefragments[0]).commit(); // 隐藏当前的fragment，显示下一个
                        }
                        currentFragment = hidefragments[0];
                        break;
                    case R.id.rbtn_shop_detail:

                        getIntent().putExtra("Restaurant_id", rid);
                        if(hidefragments[1] == null)
                        {
                            fragment = Fragment.instantiate(FoodMenuActivity.this, fragments[1]);
                            hidefragments[1] = fragment;
                        }
                        FragmentTransaction transactiontwo = getSupportFragmentManager().beginTransaction();
                        if (!hidefragments[1].isAdded()) {    // 先判断是否被add过
                            swipeRefreshLayout.setRefreshing(true);
                            transactiontwo.hide(currentFragment).add(R.id.shop_message_fragment, hidefragments[1]).commit(); // 隐藏当前的fragment，add下一个到Activity中
                        } else {
                            transactiontwo.hide(currentFragment).show(hidefragments[1]).commit(); // 隐藏当前的fragment，显示下一个
                        }
                        currentFragment = hidefragments[1];

                        break;
                }
            }
        });

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        Fragment fragment = Fragment.instantiate(FoodMenuActivity.this, fragments[0]);
        tx.add(R.id.shop_message_fragment,fragment).commit();
        currentFragment = fragment;
        hidefragments[0] = fragment;
        NotificationCenter.getInstance().addObserver("sendRid", this, rid);
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                FoodMenuActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void placeOrder(View view)
    {
        Intent intent = new Intent();
        intent.setClass(this,PlaceOrderActivity.class);
        startActivity(intent);
    }

    public void setSwiperefresh(){
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}