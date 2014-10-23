package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.okm_android.main.Adapter.SearchAdapter;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.SearchBackData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by chen on 14-9-28.
 */
public class SearchActivity extends Activity {
    double geoLat = 0.0;
    double geoLng = 0.0;
    private SearchAdapter adapter;
    private Handler handler;
    private List<SearchBackData> searchBackDatas = new ArrayList<SearchBackData>();
    private List<Map<String,String>> listItem;
    private SearchAdapter mAdapter;
    @InjectView(R.id.search_listview)ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        //显示actionbar上面的返回键
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        geoLat=intent.getDoubleExtra("Lat",geoLat);
        geoLng=intent.getDoubleExtra("Lng", geoLng);
        listView.setAdapter(adapter);
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS: {
                        List<SearchBackData> list = (List<SearchBackData>) msg.obj;
                        if (list.size() > 0) {
                            searchBackDatas.clear();
                            searchBackDatas.addAll(list);
                            mAdapter = new SearchAdapter(SearchActivity.this, list);
                            listView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                        }
                        else{

                            ToastUtils.setToast(SearchActivity.this,"sorry，没有找到你想要的T_T");
                        }
                    }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem search = menu.findItem(R.id.search);
//        search.collapseActionView();
        //是搜索框默认展开
        search.expandActionView();

// Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        final EditText editText = (EditText) searchView.findViewById(id);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                SearchData(editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                SearchData(editText.getText().toString());
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                SearchActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void SearchData(String searchFoodName)
    {
        getSearchDta(geoLng+"", geoLat+"", searchFoodName, new MainApiManager.FialedInterface() {
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
                ErrorUtils.setError(code, SearchActivity.this);
//                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onOtherFaith() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(SearchActivity.this, "发生错误");
            }

            @Override
            public void onNetworkError() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(SearchActivity.this, "网络错误");
            }
        });
    }

    private void getSearchDta(String longitude,String latitude,String food_name, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.searchBackData(longitude, latitude, food_name).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<SearchBackData>>() {
                    @Override
                    public void call(List<SearchBackData> searchBackDatas) {
                        fialedInterface.onSuccess(searchBackDatas);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable,fialedInterface);
                    }
                });
    }
}
