package com.okm_android.main.Fragment;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.okm_android.main.Activity.PlaceOrderActivity;
import com.okm_android.main.Adapter.FoodMenuAdapter;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.FoodDataResolve;
import com.okm_android.main.Model.RestaurantMenu;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by QYM on 14-10-9.
 */
public class FoodMenuFragment extends Fragment{
    private View parentView;
    public FoodMenuAdapter foodMenuAdapter;
    public String rid;
    private Handler handler;
    private List<RestaurantMenu> restaurantMenus = new ArrayList<RestaurantMenu>();
    ListView listView;
    FoodDataResolve foodDataResolve;
    TextView allFoodCount;
    TextView allFoodPrice;
    RelativeLayout relativeLayout_placeOrder;
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_food_menu, container, false);
        getActivity().invalidateOptionsMenu();
        rid= getActivity().getIntent().getStringExtra("Restaurant_id");
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        relativeLayout_placeOrder=(RelativeLayout)parentView.findViewById(R.id.place_order);
        allFoodCount = (TextView) parentView.findViewById(R.id.show_food_count);
        allFoodPrice = (TextView) parentView.findViewById(R.id.show_food_price);
        listView = (ListView)parentView.findViewById(R.id.food_menu_listView);
        restaurantData();

        NotificationCenter.getInstance().addObserver("AddFoodCount",this,"AddFoodCount");
        NotificationCenter.getInstance().addObserver("SubFoodCount",this,"SubFoodCount");
        NotificationCenter.getInstance().addObserver("AddFoodPrice",this,"AddFoodPrice");
        NotificationCenter.getInstance().addObserver("SubFoodPrice",this,"SubFoodPrice");

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        List<RestaurantMenu> list = (List<RestaurantMenu>) msg.obj;
                        if(list.size() > 0)
                        {
                            restaurantMenus.clear();
                            restaurantMenus.addAll(list);
                            foodDataResolve=new FoodDataResolve(restaurantMenus);
                            foodMenuAdapter = new FoodMenuAdapter(getActivity(),foodDataResolve.getFoods(),foodDataResolve.getTypeLong(),foodDataResolve.getTypeName());
                            listView.setAdapter(foodMenuAdapter);
                            foodMenuAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                super.handleMessage(msg);
            }

        };

        relativeLayout_placeOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity() ,PlaceOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", foodDataResolve.getFoods());
                bundle.putString("allprice",allFoodPrice.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return parentView;
    }

    public void AddFoodCount()
    {
        allFoodCount.setText(Integer.valueOf((String) allFoodCount.getText())+1+"");
    }
    public void SubFoodCount()
    {
        allFoodCount.setText(Integer.valueOf((String)allFoodCount.getText())-1+"");
    }
    public void AddFoodPrice(String foodPrice)
    {
        allFoodPrice.setText(Float.valueOf((String) allFoodPrice.getText()) + Float.valueOf(foodPrice) + "");
    }
    public void SubFoodPrice(String foodPrice)
    {
        allFoodPrice.setText(Float.valueOf((String)allFoodPrice.getText()) - Float.valueOf(foodPrice)+"");
    }

    public void restaurantData()
    {
        getRestaurantDta(rid, new MainApiManager.FialedInterface() {
            @Override
            public void onSuccess(Object object) {
                // 获取一个Message对象，设置what为1
                Message msg = Message.obtain();
                msg.obj = object;
                msg.what = Constant.MSG_SUCCESS;
                // 发送这个消息到消息队列中
                handler.sendMessage(msg);
            }

            @Override
            public void onFailth(int code) {
                ErrorUtils.setError(code, getActivity());
//                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onOtherFaith() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(getActivity(), "发生错误");
            }

            @Override
            public void onNetworkError() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(getActivity(), "网络错误");
            }
        });
    }

    private void getRestaurantDta(String restaurant_id, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.RestaurantFood(restaurant_id).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RestaurantMenu>>() {
                    @Override
                    public void call(List<RestaurantMenu> restaurantMenus) {
                        fialedInterface.onSuccess(restaurantMenus);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if(throwable.getClass().getName().toString().indexOf("RetrofitError") != -1) {
                            retrofit.RetrofitError e = (retrofit.RetrofitError) throwable;
                            if(e.isNetworkError())
                            {
                                fialedInterface.onNetworkError();

                            }
                            else {
                                fialedInterface.onFailth(e.getResponse().getStatus());
                            }
                        }
                        else{
                            fialedInterface.onOtherFaith();
                        }
                    }
                });
    }
}


