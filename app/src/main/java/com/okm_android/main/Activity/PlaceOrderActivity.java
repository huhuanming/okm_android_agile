package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.okm_android.main.R;
import com.okm_android.main.Utils.ShareUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by QYM on 14-10-8.
 */
public class PlaceOrderActivity extends FragmentActivity {
    final String[] fragments={
        "com.okm_android.main.Fragment.OrderChooseFragment",
        "com.okm_android.main.Fragment.OrderDetailFragment"
    };
    @InjectView(R.id.btn_order_choose)RadioButton orderChoose;
    @InjectView(R.id.segmented2)SegmentedGroup segmentedGroup;
    @InjectView(R.id.btn_order_detail)RadioButton orderDetail;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_food);
        ButterKnife.inject(this);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        segmentedGroup.setTintColor(getResources().getColor(R.color.bbutton_info_edge), Color.WHITE);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data",getIntent().getExtras().getSerializable("data"));
        bundle.putSerializable("allprice",getIntent().getExtras().getString("allprice"));
        getIntent().putExtras(bundle);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_order_choose:
                        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.frame_order, Fragment.instantiate(PlaceOrderActivity.this, fragments[0]));
                        tx.commit();
                        break;
                    case R.id.btn_order_detail:
                        if(ShareUtils.getId(PlaceOrderActivity.this).equals(""))
                        {
                            Intent intent = new Intent();
                            intent.setClass(PlaceOrderActivity.this,LoginRegisterActivity.class);
                            startActivity(intent);
                        }
                        else {
                            FragmentTransaction tx2 = getSupportFragmentManager().beginTransaction();
                            tx2.replace(R.id.frame_order, Fragment.instantiate(PlaceOrderActivity.this, fragments[1]));
                            tx2.commit();
                        }
                        break;
                }
            }
        });

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.frame_order, Fragment.instantiate(PlaceOrderActivity.this, fragments[0]));
        tx.commit();
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
}