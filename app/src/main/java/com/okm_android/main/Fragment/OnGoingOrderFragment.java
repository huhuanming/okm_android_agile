package com.okm_android.main.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.okm_android.main.Adapter.OrderTestAdapter;
import com.okm_android.main.R;

/**
 * Created by chen on 14-10-7.
 */
public class OnGoingOrderFragment extends Fragment {
    private View parentView;
    private ListView listView;
    private OrderTestAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_ongoing_order,container,false);
        init();

        listView.setAdapter(adapter);
        return parentView;
    }

    private void init(){
        adapter = new OrderTestAdapter(getActivity());
        listView = (ListView)parentView.findViewById(R.id.ongoing_order_listview);
    }
}
