package com.okm_android.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.okm_android.main.Model.RestaurantBackData;
import com.okm_android.main.R;

import java.util.List;

/**
 * Created by chen on 14-9-24.
 */
public class FragmentHomeAdapter extends BaseAdapter{

    //	内部类实现BaseAdapter  ，自定义适配器
    private Context context;
    private List<RestaurantBackData> restaurantBackDatas;

    public FragmentHomeAdapter(Context context, List<RestaurantBackData> restaurantBackDatas )
    {
        this.context = context;
        this.restaurantBackDatas = restaurantBackDatas;
    }


    @Override
    public int getViewTypeCount() {
        //包含有两个视图，所以返回值为2
        return 1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return restaurantBackDatas.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final ViewHolder holder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_home_item, null);
            holder = new ViewHolder();
            holder.home_item_name = (TextView) convertView
                    .findViewById(R.id.home_item_name);
            holder.home_item_price = (TextView)convertView.findViewById(R.id.home_item_price);
            holder.home_item_time = (TextView)convertView.findViewById(R.id.home_item_time);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.home_item_name.setText(restaurantBackDatas.get(position).name);
        holder.home_item_price.setText("人均: "+restaurantBackDatas.get(position).status.start_shipping_fee+"元");
        holder.home_item_time.setText("送餐时间: "+restaurantBackDatas.get(position).status.shipping_time+"分钟");

        return convertView;
    }
    class ViewHolder {
        TextView home_item_name;
        TextView home_item_price;
        TextView home_item_time;
    }
}
