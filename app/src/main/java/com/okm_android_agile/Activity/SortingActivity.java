package com.okm_android_agile.Activity;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.RadioGroup;

import com.okm_android_agile.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by chen on 14-10-7.
 */
public class SortingActivity extends FragmentActivity {

    final String[] fragments = {
            "com.okm_android_agile.Fragment.TimeSortingFragment",
            "com.okm_android_agile.Fragment.ScoreSortingFragment"
    };

    @InjectView(R.id.sorting_segmented)SegmentedGroup segmentedGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting);
        ButterKnife.inject(this);

        //显示actionbar上面的返回键
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        segmentedGroup.setTintColor(getResources().getColor(R.color.bbutton_info_edge), Color.WHITE);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId)
                {
                    case R.id.sorting_time_segmentbutton:
                        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.sorting_fragment, Fragment.instantiate(SortingActivity.this, fragments[0]));
                        tx.commit();
                        break;
                    case R.id.sorting_score_segmentbutton:
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
}
