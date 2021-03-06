package com.okm_android.main.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.okm_android.main.ApiManager.ChenApiManager;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.Model.ShakeDetailBackData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.concurrency.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by hu on 14-10-27.
 */
public class ShakeDetailActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener{

    private ShakeDetailBackData shakeDetailBackData;
    private boolean isStop = false;
    private static final int SENSOR_SHAKE = 10;
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private String longitude;
    private String latitude;

    @InjectView(R.id.shake_detail_food_image)CircleImageView food_image;
    @InjectView(R.id.shake_detail_food_price)TextView food_price;
    @InjectView(R.id.shake_detail_food_time)TextView food_time;
    @InjectView(R.id.shake_detail_swiperefresh)SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_detail);
        ButterKnife.inject(this);

        //显示actionbar上面的返回键
        ActionBar actionBar = this.getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(false);
        //加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_blue_light,
                android.R.color.white, android.R.color.holo_blue_bright);

        shakeDetailBackData = (ShakeDetailBackData)getIntent().getExtras().getSerializable("data");
        init(shakeDetailBackData);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        // 开启线程无限自动移动
        Thread myThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (!isStop) {
                    //每个两秒钟发一条消息到主线程，更新viewpager界面
                    SystemClock.sleep(2000);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            YoYo.with(Techniques.Shake).playOn(findViewById(R.id.shake_detail_food_shaketext));
                        }
                    });
                }
            }
        });
        myThread.start(); // 用来更细致的划分  比如页面失去焦点时候停止子线程恢复焦点时再开启
    }

    private void init(ShakeDetailBackData shakeDetailBackData){
        latitude = getIntent().getExtras().getString("Lat");
        longitude = getIntent().getExtras().getString("Lng");
        food_price.setText("RMB : "+shakeDetailBackData.shop_price);
        food_time.setText("预计送货时长 : 10 - 15分钟");
        getActionBar().setTitle(shakeDetailBackData.food_name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // TODO Auto-generated method stub
        switch (item.getItemId())
        {
            //监听返回键
            case android.R.id.home:
                ShakeDetailActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isStop = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isStop = false;
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
// 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        isStop = true;
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正

            int medumValue = 19;// 三星 i9250怎么晃都不会超过20，没办法，只设置19了
            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue || Math.abs(z) > medumValue) {
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    /**
     * 动作执行
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SENSOR_SHAKE:
                    swipeRefreshLayout.setRefreshing(true);
                    shakeData();

                    break;
                case Constant.MSG_SUCCESS:
                    ShakeDetailBackData shakeDetailBackData = (ShakeDetailBackData)msg.obj;
                    swipeRefreshLayout.setRefreshing(false);
                    init(shakeDetailBackData);
                    break;
            }
        }
    };


    public void shakeData() {
        getShakeData(new MainApiManager.FialedInterface() {
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
                swipeRefreshLayout.setRefreshing(false);
                ErrorUtils.setError(code, ShakeDetailActivity.this);
            }

            @Override
            public void onOtherFaith() {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.setToast(ShakeDetailActivity.this, "发生错误");
            }

            @Override
            public void onNetworkError() {
                swipeRefreshLayout.setRefreshing(false);
                ToastUtils.setToast(ShakeDetailActivity.this, "网络错误");
            }
        });
    }

    private void getShakeData(final MainApiManager.FialedInterface fialedInterface) {
        ChenApiManager.GetShakeFood(longitude, latitude).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ShakeDetailBackData>() {
                    @Override
                    public void call(ShakeDetailBackData shakeDetailBackData) {
                        fialedInterface.onSuccess(shakeDetailBackData);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable,fialedInterface);
                    }
                });
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }
}
