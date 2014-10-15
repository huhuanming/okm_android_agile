package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.okm_android.main.Adapter.PositionSearchAdapter;
import com.okm_android.main.R;
import com.okm_android.main.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hu on 14-10-9.
 */
public class PositionSearchActivity extends Activity implements PoiSearch.OnPoiSearchListener {

    private PoiSearch.Query query;// Poi查询条件类
    private PoiResult poiResult; // poi返回的结果
    private PositionSearchAdapter adapter;
    private List<PoiItem> poiItems = new ArrayList<PoiItem>();
    private String keyword = "";
    private String city = "";

    @InjectView(R.id.position_search_listview)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_search);
        ButterKnife.inject(this);
        //显示actionbar上面的返回键
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        keyword = getIntent().getExtras().getString("keyword");
        city = getIntent().getExtras().getString("city");
        adapter = new PositionSearchAdapter(this,poiItems);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putDouble("geoLat",poiItems.get(position).getLatLonPoint().getLatitude());
                bundle.putDouble("geoLng",poiItems.get(position).getLatLonPoint().getLongitude());
                bundle.putString("title", poiItems.get(position).getTitle());
                intent.putExtras(bundle);
                setResult(201, intent);
                PositionSearchActivity.this.finish();
            }
        });
        doAroundQuery();
    }



    private void doAroundQuery(){
        int  currentPage = 0;
// 第一个参数表示搜索字符串，第二个参数表示POI搜索类型，二选其一
// 第三个参数表示POI搜索区域的编码，必设
        query = new PoiSearch.Query(keyword, "", city);
        query.setPageSize(15);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);//设置查第一页
        PoiSearch poiSearch = new PoiSearch(this,query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

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
                keyword = editText.getText().toString().trim();
                if(!keyword.equals(""))
                {
                    doAroundQuery();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
                PositionSearchActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
    // TODO Auto-generated method stub
        if(rCode == 0){
            // 搜索POI的结果
            if (result != null&&result.getQuery()!=null) {
                // 是否是同一条
                if(result.getQuery().equals(query)){
                    poiResult = result;
                    // 取得搜索到的poiitems有多少页
                    int resultPages = poiResult.getPageCount();
                    // 取得第一页的poiitem数据，页数从数字0开始
                    poiItems.clear();
                    List<PoiItem> list = poiResult.getPois();

                    if(list != null)
                    {
                        poiItems.addAll(list);
                        adapter.notifyDataSetChanged();
                    }
                    else
                    {
                        ToastUtils.setToast(PositionSearchActivity.this,"对不起,搜索无结果");
                    }
                }
            } else {
                ToastUtils.setToast(PositionSearchActivity.this,"对不起,搜索无结果");
            }
        }else{
            ToastUtils.setToast(PositionSearchActivity.this, "网络错误");
        }
    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail poiItemDetail, int i) {

    }


}
