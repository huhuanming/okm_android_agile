package com.okm_android.main.Fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.okm_android.main.Activity.LoginRegisterActivity;
import com.okm_android.main.Adapter.InstantOrderAdapter;
import com.okm_android.main.Model.FoodsData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;
import com.okm_android.main.Utils.ShareUtils;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenu;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenuCreator;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenuItem;
import com.okm_android.main.View.ListView.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;


/**
 * Created by QYM on 14-10-8.
 */
public class OrderChooseFragment extends Fragment{
    private View parentView;
    private InstantOrderAdapter mAdapter=null;
    private SwipeMenuListView mListView;
    private String allPrice = "";
    private ArrayList<FoodsData> list = new ArrayList<FoodsData>();
    private RelativeLayout allPriceLayout;
    private TextView allPriceText;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView=inflater.inflate(R.layout.fragment_order_choose, container, false);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        getActivity().invalidateOptionsMenu();
        NotificationCenter.getInstance().addObserver("addAllPrice", this, "addAllPrice");
        NotificationCenter.getInstance().addObserver("subAllPrice", this, "subAllPrice");
        init();
        initData();
        mListView = (SwipeMenuListView) parentView.findViewById(R.id.order_choose_listview);
        mAdapter=new InstantOrderAdapter(getActivity(),list);
        mListView.setAdapter(mAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
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
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                new Delete().from(FoodsData.class).where("Food_id = ?",list.get(position).food_id).execute();
                list.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        return parentView;
    }

    private void init(){
        allPriceLayout = (RelativeLayout)parentView.findViewById(R.id.fragment_order_choose_allprice_layout);
        allPriceText = (TextView)parentView.findViewById(R.id.fragment_order_choose_allprice_text);

        allPriceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ShareUtils.getToken(getActivity()).equals(""))
                {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginRegisterActivity.class);
                    startActivity(intent);
                }
                else
                {
                    NotificationCenter.getInstance().postNotification("goToDetailFragment");
                    Fragment newFragment = new OrderDetailFragment();
                    FragmentTransaction transaction =getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_order,newFragment);
                    transaction.commit();
                }
            }
        });
    }

    private void initData(){
        ArrayList<FoodsData> foodlist = new ArrayList<FoodsData>();
             foodlist   = (ArrayList<FoodsData>)getActivity().getIntent().getExtras().getSerializable("data");
        allPrice = getActivity().getIntent().getExtras().getString("allprice");
        int num = foodlist.size();
        for(int i = 0; i < num; i++)
        {
            if(foodlist.get(i).isHave)
            {
                list.add(foodlist.get(i));
            }
        }
        allPriceText.setText("总价: "+allPrice+"元");
        new Delete().from(FoodsData.class).execute();
        //存数据库
        ActiveAndroid.beginTransaction();
        try {
            int len = list.size();
            for (int i = 0; i < len; i++) {
                list.get(i).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public void addAllPrice(String price){
        float totalPrice = Float.parseFloat(allPrice) + Float.parseFloat(price);
        allPrice = totalPrice+"";
        allPriceText.setText("总价: "+allPrice+"元");
    }

    public void subAllPrice(String price){
        float totalPrice = Float.parseFloat(allPrice) - Float.parseFloat(price);
        allPrice = totalPrice+"";
        allPriceText.setText("总价: "+allPrice+"元");
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
