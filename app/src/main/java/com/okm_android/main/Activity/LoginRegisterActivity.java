package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.okm_android.main.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by chen on 14-9-23.
 */


public class LoginRegisterActivity extends FragmentActivity {

    final String[] fragments = {
            "com.okm_android.main.Fragment.LoginFragment",
            "com.okm_android.main.Fragment.RegisterFragment"
    };

    @InjectView(R.id.login_register_segmented)SegmentedGroup segmentedGroup;
    @InjectView(R.id.login_segmentbutton)RadioButton login_segmentbutton;
    @InjectView(R.id.register_segmentbutton)RadioButton register_segmentbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        ButterKnife.inject(this);

        //显示actionbar上面的返回键
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        segmentedGroup.setTintColor(getResources().getColor(R.color.segment_color), Color.WHITE);

        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId)
                {
                    case R.id.login_segmentbutton:
                        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.login_register_fragment, Fragment.instantiate(LoginRegisterActivity.this, fragments[0]));
                        tx.commit();
                        break;
                    case R.id.register_segmentbutton:
                        FragmentTransaction tx2 = getSupportFragmentManager().beginTransaction();
                        tx2.replace(R.id.login_register_fragment, Fragment.instantiate(LoginRegisterActivity.this, fragments[1]));
                        tx2.commit();
                        break;
                }
            }
        });

        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.login_register_fragment, Fragment.instantiate(LoginRegisterActivity.this, fragments[0]));
        tx.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                setResult(500,new Intent());
                LoginRegisterActivity.this.finish();
                overridePendingTransition(0, R.anim.bottom_out);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            setResult(500,new Intent());
            LoginRegisterActivity.this.finish();
            overridePendingTransition(0, R.anim.bottom_out);
        }
        return super.onKeyDown(keyCode, event);
    }
}
