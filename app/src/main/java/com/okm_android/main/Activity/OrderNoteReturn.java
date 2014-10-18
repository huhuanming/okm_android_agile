package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.okm_android.main.ApiManager.ChenApiManager;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.AddressAddData;
import com.okm_android.main.Model.RegisterBackData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.ToastUtils;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by qym on 14-10-17.
 */
public class OrderNoteReturn extends Activity{
    EditText noteWrite;
    RelativeLayout toSure;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_note_write);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        noteWrite = (EditText) findViewById(R.id.et_order_note);
        toSure = (RelativeLayout) findViewById(R.id.btn_sure);
        toSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("note",noteWrite.getText().toString());
                intent.putExtras(bundle);
                setResult(222,intent);
                finish();
            }
        });
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                OrderNoteReturn.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
