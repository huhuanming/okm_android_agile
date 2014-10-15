package com.okm_android.main.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.okm_android.main.Activity.FoodMenuActivity;
import com.okm_android.main.Activity.MenuActivity;
import com.okm_android.main.Activity.SearchActivity;
import com.okm_android.main.Activity.ShakeActivity;
import com.okm_android.main.Adapter.FragmentHomeAdapter;
import com.okm_android.main.ApiManager.ChenApiManager;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.Model.RestaurantBackData;
import com.okm_android.main.Model.RestaurantTypeData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by chen on 14-9-22.
 */
public class HomeFragment extends Fragment implements ViewPager.OnPageChangeListener,MenuActivity.MenuActionbarItemClick,SwipeRefreshLayout.OnRefreshListener {
    private View parentView;

    private List<ImageView> imagelist;
    //    private TextView tv;
    private LinearLayout point_group;
    private int preEnablePositon = 0; // 前一个被选中的点的索引位置 默认情况下为0
    //    private String[] imagemiaoshu = { "巩俐不低俗，我就不能低俗", "朴树又回来了，再唱经典老歌引万人大合唱",
//            "揭秘北京电影如何升级", "乐视网TV版大派送", "热血屌丝的反杀" };
    private ViewPager viewPager;
    private boolean isStop = false;  //是否停止子线程  不会停止

    private String[] sorting = {"默认排序", "距离排序", "价格排序"};
    private String[] shop = {"全部商家"};
    private List<String> shoplist = new ArrayList<String>();
    private Spinner spinner_sorting;
    private Spinner spinner_shop;

    private ListView listview;
    private FragmentHomeAdapter adapter;
    private ArrayAdapter<String> adapter_shop;

    private int page = 0;
    private Handler handler;

    private HashMap<String, String> map = null;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView textone;
    private TextView texttwo;

    private List<RestaurantBackData> restaurantBackDatas = new ArrayList<RestaurantBackData>();


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_home, container, false);
        listview = (ListView) parentView.findViewById(R.id.fragment_home_listview);
        swipeRefreshLayout = (SwipeRefreshLayout) parentView.findViewById(R.id.swipe_container);
        textone = (TextView) parentView.findViewById(R.id.fragment_home_textone);
        texttwo = (TextView) parentView.findViewById(R.id.fragment_home_texttwo);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);
        adapter = new FragmentHomeAdapter(getActivity(), restaurantBackDatas);
        listview.setAdapter(adapter);

        NotificationCenter.getInstance().addObserver("restaurant", this, "restaurantData");

        init();
        initSpinner();
        restaurantType();

        handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch (msg.what) {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        if (map.get("type").equals("1")) {
                            restaurantBackDatas.clear();
                            map.put("type", "0");
                        }
                        List<RestaurantBackData> list = (List<RestaurantBackData>) msg.obj;
                        if (list.size() != 0) {
                            textone.setVisibility(View.VISIBLE);
                            texttwo.setVisibility(View.VISIBLE);
                            restaurantBackDatas.addAll(list);

                        }
                        adapter.notifyDataSetChanged();
                        break;
                    case Constant.MSG_TYPESUCCESS:
                        List<RestaurantTypeData> typelist = (List<RestaurantTypeData>) msg.obj;
                        int num = typelist.size();
                        if (num != 0) {
                            shoplist.clear();
                            for (int i = 0; i < num; i++) {
                                shoplist.add(typelist.get(i).restaurant_type_name);
                            }
                            adapter_shop.notifyDataSetChanged();
                        }
                        break;
                }
                super.handleMessage(msg);
            }

        };

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("rid", restaurantBackDatas.get(position).rid);
                intent.putExtras(bundle);
                intent.setClass(getActivity(), FoodMenuActivity.class);
                startActivity(intent);
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                //判断是否滚动到底部
                if (absListView.getLastVisiblePosition() == absListView.getCount() - 1) {
                    page++;
                    restaurantData(map);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int ItemCount, int totalItemCount) {

            }
        });

        return parentView;
    }

    private void initSpinner() {
        spinner_shop = (Spinner) parentView.findViewById(R.id.spinner_shop);
        spinner_sorting = (Spinner) parentView.findViewById(R.id.spinner_sorting);
        shoplist.add("全部商家");
        ArrayAdapter<String> adapter_sorting = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sorting);
        //设置下拉列表的风格
        adapter_sorting.setDropDownViewResource(R.layout.spinner_dropdown_item_print);

        //将adapter 添加到spinner中
        spinner_sorting.setAdapter(adapter_sorting);

        adapter_shop = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, shoplist);
        //设置下拉列表的风格
        adapter_shop.setDropDownViewResource(R.layout.spinner_dropdown_item_print);

        //将adapter 添加到spinner中
        spinner_shop.setAdapter(adapter_shop);

    }

    //添加banner
    private void init() {
        viewPager = (ViewPager) parentView.findViewById(R.id.viewpager);
        point_group = (LinearLayout) parentView.findViewById(R.id.ll_point_group);
//        tv = (TextView) parentView.findViewById(R.id.tv_image_miaoshu);
        imagelist = new ArrayList<ImageView>();
        int[] imageIDs = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e,};
        ImageView iv;
        View view;
        LayoutParams params;
        for (int id : imageIDs) {
            iv = new ImageView(getActivity());
            iv.setBackgroundResource(id);
            imagelist.add(iv);
            // 每循环一次添加一个点到现形布局中
            view = new View(getActivity());
            view.setBackgroundResource(R.drawable.point_background);
            params = new LayoutParams(12, 12);
            params.leftMargin = 5;
            view.setEnabled(false);
            view.setLayoutParams(params);
            point_group.addView(view); // 向线性布局中添加“点”
        }

        viewPager.setAdapter(new MyAdapter());
        viewPager.setOnPageChangeListener(this);

//        // 初始化图片描述和哪一个点被选中
//        tv.setText(imagemiaoshu[0]);
        point_group.getChildAt(0).setEnabled(true);

        // 初始化viewpager的默认position.MAX_value的一半
        int index = (Integer.MAX_VALUE / 2)
                - ((Integer.MAX_VALUE / 2) % imagelist.size());
        viewPager.setCurrentItem(index); // 设置当前viewpager选中的pager页
        // ，会触发OnPageChangeListener中的onPageSelected方法

        imagelist.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.setToast(getActivity(), "0");
            }
        });
        imagelist.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.setToast(getActivity(), "1");
            }
        });
        imagelist.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.setToast(getActivity(), "2");
            }
        });
        imagelist.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.setToast(getActivity(), "3");
            }
        });
        imagelist.get(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.setToast(getActivity(), "4");
            }
        });

        // 开启线程无限自动移动
        Thread myThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!isStop) {
                    //每个两秒钟发一条消息到主线程，更新viewpager界面
                    SystemClock.sleep(3000);
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            // 此方法在主线程中执行
                            int newindex = viewPager.getCurrentItem() + 1;
                            viewPager.setCurrentItem(newindex);
                        }
                    });
                }
            }
        });
        myThread.start(); // 用来更细致的划分  比如页面失去焦点时候停止子线程恢复焦点时再开启

    }

    public void restaurantData(HashMap<String, String> map) {
        this.map = map;
        if (this.map.get("type").equals("1")) {
            textone.setVisibility(View.GONE);
            texttwo.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(true);
        }
        getRestaurantData(map.get("geoLat"), map.get("geoLng"), page + "", new MainApiManager.FialedInterface() {
            @Override
            public void onSuccess(Object object) {
                swipeRefreshLayout.setRefreshing(false);

                // 获取一个Message对象，设置what为1
                Message msg = Message.obtain();
                msg.obj = object;
                msg.what = Constant.MSG_SUCCESS;
                // 发送这个消息到消息队列中
                handler.sendMessage(msg);
            }

            @Override
            public void onFailth(int code) {
                swipeRefreshLayout.setRefreshing(false);
                ErrorUtils.setError(code, getActivity());
            }

            @Override
            public void onOtherFaith() {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.setToast(getActivity(), "发生错误");
            }

            @Override
            public void onNetworkError() {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.setToast(getActivity(), "网络错误");
            }
        });
    }

    private void getRestaurantData(String latitude, String longitude, String page, final MainApiManager.FialedInterface fialedInterface) {
        ChenApiManager.RestaurantsList(latitude, longitude, page).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RestaurantBackData>>() {
                    @Override
                    public void call(List<RestaurantBackData> restaurantBackDatas) {
                        fialedInterface.onSuccess(restaurantBackDatas);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (throwable.getClass().getName().toString().indexOf("RetrofitError") != -1) {
                            retrofit.RetrofitError e = (retrofit.RetrofitError) throwable;
                            if (e.isNetworkError()) {
                                fialedInterface.onNetworkError();

                            } else {
                                fialedInterface.onFailth(e.getResponse().getStatus());
                            }
                        } else {
                            fialedInterface.onOtherFaith();
                        }
                    }
                });
    }

    public void restaurantType() {
        getRestaurantType(new MainApiManager.FialedInterface() {
            @Override
            public void onSuccess(Object object) {

                // 获取一个Message对象，设置what为1
                Message msg = Message.obtain();
                msg.obj = object;
                msg.what = Constant.MSG_TYPESUCCESS;
                // 发送这个消息到消息队列中
                handler.sendMessage(msg);
            }

            @Override
            public void onFailth(int code) {
                ErrorUtils.setError(code, getActivity());
            }

            @Override
            public void onOtherFaith() {
                ToastUtils.setToast(getActivity(), "发生错误");
            }

            @Override
            public void onNetworkError() {
                ToastUtils.setToast(getActivity(), "网络错误");
            }
        });
    }

    private void getRestaurantType(final MainApiManager.FialedInterface fialedInterface) {
        ChenApiManager.RestaurantsTypes().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RestaurantTypeData>>() {
                    @Override
                    public void call(List<RestaurantTypeData> restaurantTypeDatas) {
                        fialedInterface.onSuccess(restaurantTypeDatas);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                        if (throwable.getClass().getName().toString().indexOf("RetrofitError") != -1) {
                            retrofit.RetrofitError e = (retrofit.RetrofitError) throwable;
                            if (e.isNetworkError()) {
                                fialedInterface.onNetworkError();

                            } else {
                                fialedInterface.onFailth(e.getResponse().getStatus());
                            }
                        } else {
                            fialedInterface.onOtherFaith();
                        }
                    }
                });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }


    //banner的适配器
    class MyAdapter extends PagerAdapter {

        /**
         * 销毁对象
         *
         * @param position 将要被销毁对象的索引位置
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imagelist.get(position % imagelist.size()));
        }

        /**
         * 初始化一个对象
         *
         * @param position 将要被创建的对象的索引位置
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 先把对象添加到viewpager中，再返回当前对象
            container.addView(imagelist.get(position % imagelist.size()));
            return imagelist.get(position % imagelist.size());
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Integer.MAX_VALUE;
        }

        /**
         * 复用对象 true 复用对象 false 用的是object
         */
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {
        // 取余后的索引
        int newPositon = position % imagelist.size();

//        // 根据索引设置图片的描述
//        tv.setText(imagemiaoshu[newPositon]);

        // 把上一个点设置为被选中
        point_group.getChildAt(preEnablePositon).setEnabled(false);

        // 根据索引设置那个点被选中
        point_group.getChildAt(newPositon).setEnabled(true);

        preEnablePositon = newPositon;

    }

    @Override
    public void onResume() {
        super.onResume();
        isStop = false;
        MenuActivity.menuActionbarItemClick = this;
    }

    @Override
    public void onPause() {
        super.onPause();
        isStop = true;
        MenuActivity.menuActionbarItemClick = null;
    }


    @Override
    public void onClick(int id) {
        switch (id) {
            case R.id.menu_search: {
                Intent intent = new Intent();
                intent.setClass(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
            break;
            case R.id.menu_shake: {
                Intent intent = new Intent();
                intent.setClass(getActivity(), ShakeActivity.class);
                startActivity(intent);
            }
            break;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onDestroy() {
        isStop = true;
        super.onDestroy();

    }
}