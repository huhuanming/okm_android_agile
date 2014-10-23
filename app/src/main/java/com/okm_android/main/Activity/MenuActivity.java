package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.okm_android.main.Adapter.MenuAdapter;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuActivity extends FragmentActivity implements AMapLocationListener {

    //    final String[] menuEntries = {"店铺管理","数据报表","订单管理","菜单管理","关于我们"};
    private ArrayList<String> menuEntries = new ArrayList<String>();
    private ArrayList<Integer> menuImage = new ArrayList<Integer>();
    final String[] fragments = {
            "com.okm_android.main.Fragment.HomeFragment",
            "com.okm_android.main.Fragment.UserFragment",
            "com.okm_android.main.Fragment.OrderFragment",
            "com.okm_android.main.Fragment.TruckFragment",
            "com.okm_android.main.Fragment.SettingFragment"
    };
    private Fragment currentFragment;
    private Fragment[] hidefragments = new Fragment[]{null,null,null,null,null};
    public static abstract interface MenuActionbarItemClick{
        public abstract void onClick(int id);
    }

    //定位位置的经纬度和关键字
    double geoLat = 0.0;
    double geoLng = 0.0;
    private String keyword = "";
    private String city = "";

    private LocationManagerProxy mLocationManagerProxy;

    private ActionBarDrawerToggle drawerToggle;
    private MenuAdapter adapter;
    private ArrayAdapter<String> navigationAdapter;
    public static MenuActionbarItemClick  menuActionbarItemClick;
    private RelativeLayout relativeLayout;
    private int fragmentPositon = 0;
    String[] actions = new String[] {
            "正在定位..."
            ,"更多位置"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getActionBar().setTitle(R.string.home);

        menuEntries.add("首页");
        menuEntries.add("个人资料");
        menuEntries.add("我的订单");
        menuEntries.add("送货地址");
        menuEntries.add("设置");

        menuImage.add(R.drawable.home);
        menuImage.add(R.drawable.user);
        menuImage.add(R.drawable.order);
        menuImage.add(R.drawable.truck);
        menuImage.add(R.drawable.setting);


        final DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        final ListView navList = (ListView) findViewById(R.id.drawer);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout);
        adapter = new MenuAdapter(getActionBar().getThemedContext(),menuEntries,menuImage);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawer,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        drawer.setDrawerListener(drawerToggle);

        navList.setAdapter(adapter);
        navList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int pos,long id){
                drawer.setDrawerListener( new DrawerLayout.SimpleDrawerListener(){
                    @Override
                    public void onDrawerClosed(View drawerView){
                        super.onDrawerClosed(drawerView);
                        fragmentPositon = pos;
                        Fragment fragment = null;
                        if(hidefragments[pos] == null)
                        {
                            fragment = Fragment.instantiate(MenuActivity.this, fragments[pos]);
                            hidefragments[pos] = fragment;
                        }
//                        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

//                        tx.replace(R.id.main, fragment);
                        getActionBar().setTitle(menuEntries.get(pos));
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        if (!hidefragments[pos].isAdded()) {    // 先判断是否被add过
                            transaction.hide(currentFragment).add(R.id.main, hidefragments[pos]).commit(); // 隐藏当前的fragment，add下一个到Activity中
                        } else {
                            transaction.hide(currentFragment).show(hidefragments[pos]).commit(); // 隐藏当前的fragment，显示下一个
                        }
                        currentFragment = hidefragments[pos];

//                        tx.commit();
                    }
                });
                drawer.closeDrawer(relativeLayout);
            }
        });
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        Fragment fragment = Fragment.instantiate(MenuActivity.this, fragments[0]);
        tx.add(R.id.main,fragment).commit();
        currentFragment = fragment;
        hidefragments[0] = fragment;

        setActionbarSpinner();

        init();
    }

    private void setActionbarSpinner(){
        /** Create an array adapter to populate dropdownlist */
        navigationAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_item_print, actions);
        navigationAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item_print);
        /** Enabling dropdown list navigation for the action bar */
        getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        /** Defining Navigation listener */
        ActionBar.OnNavigationListener navigationListener = new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                switch (itemPosition)
                {
                    case 1:
                        Intent intent = new Intent();
                        intent.setClass(MenuActivity.this,PositionSearchActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("keyword", keyword);
                        bundle.putString("city", city);
                        intent.putExtras(bundle);
                        startActivityForResult(intent,201);
                        break;
                }
                return false;
            }
        };
        getActionBar().setListNavigationCallbacks(navigationAdapter, navigationListener);

    }

    /**
     * 初始化定位
     */
    private void init(){

        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        mLocationManagerProxy.setGpsEnable(false);

        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode)
        {
            case 201:
                geoLat = data.getExtras().getDouble("geoLat");
                geoLng = data.getExtras().getDouble("geoLng");
                actions[0] = data.getExtras().getString("title");
                keyword = data.getExtras().getString("title");
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("geoLat",geoLat+"");
                map.put("geoLng",geoLng+"");
                map.put("type","1");
                NotificationCenter.getInstance().postNotification("restaurant",map);
                NotificationCenter.getInstance().postNotification("GetPosition",map);
                break;
        }

        setActionbarSpinner();
    }
    //高德地图信息
    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if(amapLocation != null && amapLocation.getAMapException().getErrorCode() == 0){
            //获取位置信息
            geoLat = amapLocation.getLatitude();
            geoLng = amapLocation.getLongitude();
            actions[0] = amapLocation.getStreet();
            city = amapLocation.getCity();

            Bundle locBundle = amapLocation.getExtras();

            String desc = null;
            if (locBundle != null) {
                desc = locBundle.getString("desc");
                String[] position = desc.split(" ");
                keyword = position[position.length - 2];
            }
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("geoLat",geoLat+"");
            map.put("geoLng",geoLng+"");
            map.put("type","0");
            NotificationCenter.getInstance().postNotification("restaurant",map);
            NotificationCenter.getInstance().postNotification("GetPosition",map);
            navigationAdapter.notifyDataSetChanged();
            //移除定位请求
            mLocationManagerProxy.removeUpdates(this);
            // 销毁定位
            mLocationManagerProxy.destroy();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        //移除定位请求
        mLocationManagerProxy.removeUpdates(this);
        // 销毁定位
        mLocationManagerProxy.destroy();

    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()){
            case R.id.menu_search:{
                menuActionbarItemClick.onClick(R.id.menu_search);
            }
            break;
            case R.id.menu_shake:{
                menuActionbarItemClick.onClick(R.id.menu_shake);
            }
            break;

            case R.id.menu_add_address:{
                Intent intent = new Intent(this, AddAddressActivity.class);
                startActivity(intent);
            }
            break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menumanagement_right, menu);
        MenuItem searchMenuItem = menu.getItem(0);
        MenuItem shackMenuItem = menu.getItem(1);
        MenuItem addAddressItem = menu.getItem(2);
        addAddressItem.setVisible(false);
        searchMenuItem.setVisible(false);
        shackMenuItem.setVisible(false);
        switch (fragmentPositon){
            case 0:{
                getActionBar().setDisplayShowTitleEnabled(false);
                getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
                setActionbarSpinner();
                searchMenuItem.setVisible(true);
                shackMenuItem.setVisible(true);
                addAddressItem.setVisible(false);
                adapter.notifyDataSetChanged();
            }
            break;
            case 1:{
                getActionBar().setDisplayShowTitleEnabled(true);
                getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                getActionBar().setTitle("个人资料");
                shackMenuItem.setVisible(false);
                searchMenuItem.setVisible(false);
                adapter.notifyDataSetChanged();
            }
            break;
            case 2:{
                getActionBar().setDisplayShowTitleEnabled(true);
                getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                getActionBar().setTitle("我的订单");
                shackMenuItem.setVisible(false);
                searchMenuItem.setVisible(false);
                adapter.notifyDataSetChanged();
            }
            break;
            case 3:{
                getActionBar().setDisplayShowTitleEnabled(true);
                getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                getActionBar().setTitle("送货地址");
                shackMenuItem.setVisible(false);
                searchMenuItem.setVisible(false);
                addAddressItem.setVisible(true);
                adapter.notifyDataSetChanged();
            }
            break;
            case 4:{
                getActionBar().setDisplayShowTitleEnabled(true);
                getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
                getActionBar().setTitle("设置");
                shackMenuItem.setVisible(false);
                searchMenuItem.setVisible(false);
                adapter.notifyDataSetChanged();
            }
            break;
        }
        return true;
    }

}
