package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.okm_android.main.R;
import com.okm_android.main.View.Button.BootstrapButton;

/**
 * Created by qym on 14-10-17.
 */
public class OrderNoteReturn extends Activity{
    private EditText noteWrite;
    private BootstrapButton toSure;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_note_write);
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        noteWrite = (EditText) findViewById(R.id.et_order_note);
        toSure = (BootstrapButton) findViewById(R.id.btn_sure);
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
