package com.okm_android.main.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.okm_android.main.Adapter.AddressAdapter;
import com.okm_android.main.R;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenu;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenuCreator;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenuItem;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 14-9-22.
 */
public class TruckFragment extends Fragment {
    private View parentView;
    private List<Map<String,String>> mAppList;
    private AddressAdapter mAdapter;
    private SwipeMenuListView mListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_address, container, false);

        mListView = (SwipeMenuListView) parentView.findViewById(R.id.listView);
        mAppList=get_search();
        mAdapter=new AddressAdapter(getActivity(),mAppList);
        mListView.setAdapter(mAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0x9a, 0xd2,0x68)));
                // set item width
                openItem.setWidth(dp2px(67));
                // set item title
                openItem.setTitle("默认");
                // set item title fontsize
                openItem.setTitleSize(20);
                // set item title font color
                openItem.setTitleColor(Color.rgb(0xff, 0xff,0xff));
                // order_choose_founder_add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xe6,0x2e, 0x2e)));
                // set item width
                deleteItem.setWidth(dp2px(67));
                // set a icon
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(20);
                // set item title font color
                deleteItem.setTitleColor(Color.rgb(0xff, 0xff, 0xff));
                // order_choose_founder_add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        return parentView;
    }
    private List<Map<String, String>> get_search()   //从服务器里加载数据
    {
        //ContentResolver resolver=mContext.getContentResolver();
        List<Map<String,String>> list=new ArrayList<Map<String, String>>();
        Map<String,String> map;
        for(int i=0;i<4;i++)
        {
            map=new HashMap<String, String>();
            map.put("name", "秦源懋");
            map.put("number","18200272893");
            map.put("address", "西南交大");
            list.add(map);
        }
        return list;
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
