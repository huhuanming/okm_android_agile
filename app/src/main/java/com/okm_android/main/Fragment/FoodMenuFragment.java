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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<Map<String,String>> list = new ArrayList<Map<String, String>>();
    FoodDataResolve foodDataResolve;
    TextView allFoodCount;
    TextView allFoodPrice;
    RelativeLayout relativeLayout_placeOrder;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
                        if(msg.obj != null)
                        {
                            restaurantMenus.clear();
                            restaurantMenus.addAll((List<RestaurantMenu>) msg.obj);
                            foodDataResolve=new FoodDataResolve(restaurantMenus);
                            list=getSearch();//数据加载过程
                            foodMenuAdapter = new FoodMenuAdapter(getActivity(),list,foodDataResolve.getTypeLong());
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

   // public void placeOrder(View view)//处理下单，获取当前已有的一些数据并把数据封装，传到下一个Activity
 //   {
        /*List<PlaceOrder> placeOrders=new ArrayList<PlaceOrder>();
        PlaceOrder placeOrder;
        TextView foodCount,foodName,foodPrice;
 /     {
            for(int j=0;j<foodDataResolve.getTypeLong().size();j++) //效率较底下，后期需要改进
            {
                if(i==foodDataResolve.getTypeLong().get(j))
                    break ok;
            }
            RelativeLayout relativeLayout = (RelativeLayout)listView.getChildAt(i);
            foodCount = (TextView)relativeLayout.findViewById(R.id.count);
            if(Integer.valueOf(foodCount.getText().toString())==0)
                continue;
            else{
                foodName = (TextView)relativeLayout.findViewById(R.id.food_menu_name);
                foodPrice = (TextView)relativeLayout.findViewById(R.id.food_price);
                placeOrder=new PlaceOrder();
                placeOrder.foodName=foodName.getText().toString();
                placeOrder.oneFoodPrice=Float.valueOf(foodPrice.getText().toString())*Float.valueOf(foodCount.getText().toString())+"";
                placeOrder.Count=foodCount.getText().toString();
                placeOrders.add(placeOrder);
            }
        }*/

   // }
    private ArrayList<Map<String,String>> getSearch()
    {
        ArrayList<Map<String,String>> arrayList=new ArrayList<Map<String,String>>();
        Map<String,String> mapMenu;
        Map<String,String> mapFood;
        int i,j=0;
        int k=0,isMenu=0;
        for(i=0;i < foodDataResolve.getTypeNameCount(); i++)
        {
            for(int m=0;m < foodDataResolve.getTypeLong().size();m++){
                if(foodDataResolve.getTypeLong().get(m)==i)
                {
                    isMenu=1;
                    break;
                }
            }
            if(isMenu==1)
            {
                mapMenu = new HashMap<String, String>();
                mapMenu.put("menuName", foodDataResolve.getTypeName().get(k).toString());
                arrayList.add(mapMenu);
                k++;
                isMenu=0;
            }
            else{
                mapFood=new HashMap<String, String>();
                mapFood.put("foodName", foodDataResolve.getFoods().get(j).food_name);
                mapFood.put("foodPrice", foodDataResolve.getFoods().get(j).food_price);
                mapFood.put("monthSale", foodDataResolve.getFoods().get(j++).sold_number);
                arrayList.add(mapFood);
            }
        }
        return arrayList;
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


