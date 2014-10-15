package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.okm_android.main.Adapter.SearchAdapter;
import com.okm_android.main.R;
import com.okm_android.main.Utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by chen on 14-9-28.
 */
public class SearchActivity extends Activity {

    private SearchAdapter adapter;
    @InjectView(R.id.search_listview)ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        //显示actionbar上面的返回键
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        adapter = new SearchAdapter(this);
        listView.setAdapter(adapter);
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
                ToastUtils.setToast(SearchActivity.this,editText.getText().toString());
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
                SearchActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
