package com.okm_android.main.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by chen on 14-9-22.
 */
public class OrderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    private View parentView;
    private SegmentedGroup segmentedGroup;

    private SwipeRefreshLayout swipeRefreshLayout;
    final String[] fragments = {
            "com.okm_android.main.Fragment.OnGoingOrderFragment",
            "com.okm_android.main.Fragment.FinishOrderFragment"
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    public void StopFlash()
    {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_order,container,false);
        NotificationCenter.getInstance().addObserver("StopFlash",this,"StopFlash");
        init();

        segmentedGroup.setTintColor(getResources().getColor(R.color.bbutton_info_edge), Color.WHITE);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId)
                {
                    case R.id.order_segmentbutton:
                        swipeRefreshLayout.setEnabled(true);
                        swipeRefreshLayout.setRefreshing(true);
                        FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.order_fragment, Fragment.instantiate(getActivity(), fragments[0]));
                        tx.commit();
                        break;
                    case R.id.order_finish_segmentbutton:
                        swipeRefreshLayout.setEnabled(true);
                        swipeRefreshLayout.setRefreshing(true);
                        FragmentTransaction tx2 = getActivity().getSupportFragmentManager().beginTransaction();
                        tx2.replace(R.id.order_fragment, Fragment.instantiate(getActivity(), fragments[1]));
                        tx2.commit();
                        break;
                }
            }
        });

        FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.order_fragment, Fragment.instantiate(getActivity(), fragments[0]));
        tx.commit();
        return parentView;
    }

    private void init(){
        segmentedGroup = (SegmentedGroup)parentView.findViewById(R.id.order_segmented);

        swipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
