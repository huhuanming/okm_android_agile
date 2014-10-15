package com.okm_android_agile.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.okm_android_agile.R;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by chen on 14-9-22.
 */
public class OrderFragment extends Fragment {
    private View parentView;
    private SegmentedGroup segmentedGroup;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_order,container,false);

        init();

        segmentedGroup.setTintColor(getResources().getColor(R.color.bbutton_info_edge), Color.WHITE);
        segmentedGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId)
                {
                    case R.id.sorting_time_segmentbutton:
                        FragmentTransaction tx = getActivity().getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.order_fragment, Fragment.instantiate(getActivity(), fragments[0]));
                        tx.commit();
                        break;
                    case R.id.sorting_score_segmentbutton:
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
    }
}
