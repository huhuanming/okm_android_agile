package com.okm_android_agile.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.okm_android_agile.Adapter.SortingAdapter;
import com.okm_android_agile.R;

/**
 * Created by chen on 14-10-7.
 */
public class TimeSortingFragment extends Fragment {
    private View parentView;
    private ListView listView;
    private SortingAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_timesorting,container,false);
        init();
        listView.setAdapter(adapter);
        return parentView;
    }

    private void init(){
        adapter = new SortingAdapter(getActivity());
        listView = (ListView)parentView.findViewById(R.id.time_sorting_listview);
    }
}
