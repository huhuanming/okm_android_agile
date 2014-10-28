package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.UploadBackData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ShareUtils;
import com.okm_android.main.Utils.ToastUtils;
import com.okm_android.main.Utils.TokenUtils.AccessToken;

import rx.android.concurrency.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by qym on 14-10-24.
 */
public class AddCommentActivity extends Activity{
    private CheckBox checkBox1,checkBox2,checkBox3,checkBox4,checkBox5;
    private EditText ettitle,etcomment;
    private String restaurant_id;
    private int point;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent=getIntent();
        restaurant_id=intent.getStringExtra("r_id");
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        init();
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setFalse();
                checkBox1.setChecked(true);
            }
        });
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setFalse();
                checkBox1.setChecked(true);
                checkBox2.setChecked(true);
            }
        });
        checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setFalse();
                checkBox1.setChecked(true);
                checkBox2.setChecked(true);
                checkBox3.setChecked(true);
            }
        });
        checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setFalse();
                checkBox1.setChecked(true);
                checkBox2.setChecked(true);
                checkBox3.setChecked(true);
                checkBox4.setChecked(true);
            }
        });
        checkBox5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setFalse();
                checkBox1.setChecked(true);
                checkBox2.setChecked(true);
                checkBox3.setChecked(true);
                checkBox4.setChecked(true);
                checkBox5.setChecked(true);
            }
        });
    }
    private void init()
    {
        checkBox1=(CheckBox)findViewById(R.id.checkbox1);
        checkBox2=(CheckBox)findViewById(R.id.checkbox2);
        checkBox3=(CheckBox)findViewById(R.id.checkbox3);
        checkBox4=(CheckBox)findViewById(R.id.checkbox4);
        checkBox5=(CheckBox)findViewById(R.id.checkbox5);
        ettitle=(EditText)findViewById(R.id.et_comment_title);
        etcomment=(EditText)findViewById(R.id.et_comment);
    }
    private void setFalse()
    {
        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);
        checkBox5.setChecked(false);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_comment, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                AddCommentActivity.this.finish();
                break;
            case R.id.menu_send:
            {
                CommentData();
            }break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void CommentData()
    {
        String title,comment;
        title=ettitle.getText().toString();
        comment=etcomment.getText().toString();
        if(checkBox5.isChecked()){point=5;}
        else if(checkBox4.isChecked()){point=4;}
        else if(checkBox3.isChecked()){point=3;}
        else if(checkBox2.isChecked()){point=2;}
        else point=1;
        AccessToken accessToken = new AccessToken(ShareUtils.getToken(this),ShareUtils.getKey(this));
        updateCommentDta(restaurant_id, accessToken.accessToken(),title,comment,point+"", new MainApiManager.FialedInterface() {
            @Override
            public void onSuccess(Object object) {
                ToastUtils.setToast(AddCommentActivity.this, "评论成功");
                finish();
            }

            @Override
            public void onFailth(int code) {
                ErrorUtils.setError(code, AddCommentActivity.this);
//                progressbar.setVisibility(View.GONE);
            }

            @Override
            public void onOtherFaith() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(AddCommentActivity.this, "发生错误");
            }

            @Override
            public void onNetworkError() {
//                progressbar.setVisibility(View.GONE);
                ToastUtils.setToast(AddCommentActivity.this, "网络错误");
            }
        });
    }
    private void updateCommentDta(String restaurant_id,String access_token,String title,String comment,String point, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.uploadBackDataComment(restaurant_id, access_token, title, comment,point).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UploadBackData>() {
                    @Override
                    public void call(UploadBackData uploadBackData) {
                        fialedInterface.onSuccess(uploadBackData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable,fialedInterface);
                    }
                });
    }
}
