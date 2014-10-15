package com.okm_android_agile.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.okm_android_agile.Adapter.OrderTestAdapter;
import com.okm_android_agile.R;


/**
 * Created by chen on 14-10-7.
 */
public class FinishOrderFragment extends Fragment{
    private View parentView;
    private ListView listView;
    private OrderTestAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_finish_order,container,false);
        init();
        listView.setAdapter(adapter);
        return parentView;
    }

    private void init(){
        adapter = new OrderTestAdapter(getActivity());
        listView = (ListView)parentView.findViewById(R.id.finish_order_listview);
    }
}
