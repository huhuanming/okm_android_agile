package com.okm_android.main.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.okm_android.main.Adapter.SortingAdapter;
import com.okm_android.main.ApiManager.MainApiManager;
import com.okm_android.main.ApiManager.QinApiManager;
import com.okm_android.main.Model.RestaurantComment;
import com.okm_android.main.R;
import com.okm_android.main.Utils.Constant;
import com.okm_android.main.Utils.ErrorUtils;
import com.okm_android.main.Utils.ToastUtils;

import java.util.List;

import rx.android.concurrency.AndroidSchedulers;
import rx.util.functions.Action1;

/**
 * Created by chen on 14-10-7.
 */
public class ScoreSortingFragment extends Fragment{
    private View parentView;
    private ListView listView;
    private SortingAdapter adapter;
    private Handler handler;
    private String restaurant_id;
    private int count=10;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_scoresorting,container,false);
        init();
        restaurant_id=getActivity().getIntent().getStringExtra("Restaurant_id");
        CommentData();
        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub
                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        List<RestaurantComment> list = (List<RestaurantComment>) msg.obj;
                        if(list.size() > 0)
                        {
                            adapter=new SortingAdapter(getActivity(),list);
                            listView.setAdapter(adapter);
                        }
                        else{
                            ToastUtils.setToast(getActivity(), "暂时没有评价哦～");
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
        listView.setAdapter(adapter);
        return parentView;
    }

    private void init(){
        listView = (ListView)parentView.findViewById(R.id.score_sorting_listview);
    }

    public void CommentData()
    {
        //   AccessToken accessToken = new AccessToken(ShareUtils.getToken(getActivity()),ShareUtils.getKey(getActivity()));
        getCommentDta(restaurant_id, 0+"", count + "", 1 + "", new MainApiManager.FialedInterface() {
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

    private void getCommentDta(String restaurant_id,String cid,String count,String order, final MainApiManager.FialedInterface fialedInterface)
    {
        QinApiManager.getComment(restaurant_id, cid, count, order).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<RestaurantComment>>() {
                    @Override
                    public void call(List<RestaurantComment> restaurantComments) {
                        fialedInterface.onSuccess(restaurantComments);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.SetThrowable(throwable, fialedInterface);
                    }
                });
    }
}
