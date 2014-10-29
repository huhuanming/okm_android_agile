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
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;

import butterknife.ButterKnife;
import butterknife.InjectView;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by chen on 14-10-7.
 */
public class SortingActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout swipeRefreshLayout;
    private String restaurant_id;
    final String[] fragments = {
            "com.okm_android.main.Fragment.TimeSortingFragment",
            "com.okm_android.main.Fragment.ScoreSortingFragment"
    };

    @InjectView(R.id.sorting_segmented)SegmentedGroup segmentedGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        Intent intent = this.getIntent();
        restaurant_id=intent.getStringExtra("restaurant_id");
        getIntent().putExtra("Restaurant_id", restaurant_id);
        ButterKnife.inject(this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);
        //显示actionbar上面的返回键
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        NotificationCenter.getInstance().addObserver("SortStopFlash",this,"SortStopFlash");
        segmentedGroup.setTintColor(getResources().getColor(R.color.bbutton_info_edge), Color.WHITE);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId)
                {
                    case R.id.sorting_time_segmentbutton:
                        swipeRefreshLayout.setRefreshing(true);
                        getIntent().putExtra("Restaurant_id", restaurant_id);
                        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.sorting_fragment, Fragment.instantiate(SortingActivity.this, fragments[0]));
                        tx.commit();
                        break;
                    case R.id.sorting_score_segmentbutton:
                        swipeRefreshLayout.setRefreshing(true);
                        getIntent().putExtra("Restaurant_id", restaurant_id);
                        FragmentTransaction tx2 = getSupportFragmentManager().beginTransaction();
                        tx2.replace(R.id.sorting_fragment, Fragment.instantiate(SortingActivity.this, fragments[1]));
                        tx2.commit();
                        break;
                }
            }
        });
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.sorting_fragment, Fragment.instantiate(SortingActivity.this, fragments[0]));
        tx.commit();
    }

    public void SortStopFlash(){
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                SortingActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
