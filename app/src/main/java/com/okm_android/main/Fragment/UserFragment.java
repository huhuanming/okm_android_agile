package com.okm_android.main.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.okm_android.main.R;
import com.okm_android.main.Utils.ShareUtils;
import com.okm_android.main.Utils.ToastUtils;

/**
 * Created by chen on 14-9-22.
 */
public class UserFragment extends Fragment {
    private View parentView;
    private RelativeLayout login_exit;
    private TextView login_above_text;
    private TextView login_below_text;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_user, container, false);

        return parentView;
    }

    private void init(){
        login_exit = (RelativeLayout)parentView.findViewById(R.id.fragment_user_login_exit);
        login_above_text = (TextView)parentView.findViewById(R.id.fragment_user_login_above_text);
        login_below_text = (TextView)parentView.findViewById(R.id.fragment_user_login_below_text);
        if(ShareUtils.getId().equals(""))
        {
            setInVisible();
        }
        else
        {
            setVisible();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        initListener();
    }

    private void initListener(){
        login_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtils.deleteTokenKey();
                setInVisible();
                ToastUtils.setToast(getActivity(),"成功退出");
            }
        });
    }

    private void setInVisible(){
        login_exit.setVisibility(View.INVISIBLE);
        login_above_text.setVisibility(View.INVISIBLE);
        login_below_text.setVisibility(View.INVISIBLE);
    }
    private void setVisible(){
        login_exit.setVisibility(View.VISIBLE);
        login_above_text.setVisibility(View.VISIBLE);
        login_below_text.setVisibility(View.VISIBLE);
    }
}
