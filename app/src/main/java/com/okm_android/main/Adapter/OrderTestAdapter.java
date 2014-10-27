package com.okm_android.main.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okm_android.main.Model.WatchOrderData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 14-10-7.
 */
public class OrderTestAdapter extends BaseAdapter{
    //	内部类实现BaseAdapter  ，自定义适配器
    private Context context;
    private List<WatchOrderData> list = new ArrayList<WatchOrderData>();
    private int flag;
    public OrderTestAdapter(Context context,List<WatchOrderData> list,int flag)
    {
        this.context = context;
        this.list=list;
        this.flag=flag;
    }


    @Override
    public int getViewTypeCount() {
        //包含有两个视图，所以返回值为2
        return 1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_order_item, null);
            holder = new ViewHolder();
            holder.order_name = (TextView) convertView.findViewById(R.id.order_name);
            holder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            holder.user_address = (TextView) convertView.findViewById(R.id.user_address);
            holder.user_number = (TextView) convertView.findViewById(R.id.phone_number);
            holder.user_price = (TextView) convertView.findViewById(R.id.order_item_price);
            holder.time= (TextView) convertView.findViewById(R.id.order_time);
//            holder.img = (ImageView)convertView.findViewById(R.id.activity_menu_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.order_name.setText(list.get(position).order_info.shipping_user+"的订单");
        if(flag==0) {
            Log.e("ssssa",list.get(position).created_at);
            holder.time.setText(DateUtils.getMonthTime(list.get(position).created_at));
        }
        else{
            holder.time.setText(DateUtils.getMonthTime(list.get(position).updated_at));
            Log.e("ssss",list.get(position).updated_at);
        }

        holder.user_name.setText(list.get(position).order_info.shipping_user);
        holder.user_price.setText("￥"+list.get(position).order_info.actual_total_price);
        holder.user_number.setText(list.get(position).order_info.phone_number);
        holder.user_address.setText(list.get(position).order_info.shipping_address);
//        holder.textView.setText(menuEntries.get(position));
//        holder.img.setImageResource(menuImage.get(position));

        return convertView;
    }

    class ViewHolder {
        TextView user_name;
        TextView user_number;
        TextView order_name;
        TextView user_address;
        TextView user_price;
        TextView time;
    }
}
