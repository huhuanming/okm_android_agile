package com.okm_android_agile.Activity;

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

import com.okm_android_agile.R;
import com.okm_android_agile.Utils.AddObserver.NotificationCenter;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by QYM on 14-10-10.
 */
public class FoodMenuActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener{
    final String[] fragments={
            "com.okm_android_agile.Fragment.FoodMenuFragment",
            "com.okm_android_agile.Fragment.ShopDetailFragment"
    };
    RadioButton foodMenu;
    SegmentedGroup segmentedGroup;
    RadioButton shopDetail;
    public String rid=null;
    private SwipeRefreshLayout swipeRefreshLayout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_message);

        Intent intent = this.getIntent();
        final Bundle bundle = intent.getExtras();
        rid=bundle.getString("rid");
        getIntent().putExtra("Restaurant_id", rid);
        //ButterKnife.inject(this);
        foodMenu=(RadioButton)findViewById(R.id.rbtn_food_menu);
        segmentedGroup=(SegmentedGroup)findViewById(R.id.shop_message_segmented);
        shopDetail=(RadioButton)findViewById(R.id.rbtn_shop_detail);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
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
                switch (checkedId)
                {
                    case R.id.rbtn_food_menu:
                        getIntent().putExtra("Restaurant_id", rid);
                        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.shop_message_fragment, Fragment.instantiate(FoodMenuActivity.this, fragments[0]));
                        tx.commit();
                        break;
                    case R.id.rbtn_shop_detail:
                        swipeRefreshLayout.setRefreshing(true);
                        getIntent().putExtra("Restaurant_id", rid);
                        FragmentTransaction tx2 = getSupportFragmentManager().beginTransaction();
                        tx2.replace(R.id.shop_message_fragment, Fragment.instantiate(FoodMenuActivity.this, fragments[1]));
                        tx2.commit();

                        break;
                }
            }
        });

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.shop_message_fragment, Fragment.instantiate(FoodMenuActivity.this, fragments[0]));
        tx.commit();

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