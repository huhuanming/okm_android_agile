package com.okm_android.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.okm_android.main.Model.OrderManagementKidData;
import com.okm_android.main.R;

import java.util.List;

/**
 * Created by qym on 14-10-27.
 */
public class OrderManagementDetailAdapter extends BaseAdapter {
    //	内部类实现BaseAdapter  ，自定义适配器
    private Context context;
    private List<OrderManagementKidData> list;

    public OrderManagementDetailAdapter(Context context,List<OrderManagementKidData> kids_list)
    {
        this.context = context;
        this.list = kids_list;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_management_details_item, null);
            holder = new ViewHolder();
            holder.order_management_item_details_item_name = (TextView) convertView
                    .findViewById(R.id.order_management_item_details_item_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.order_management_item_details_item_name.setText(list.get(position).food.food_name+"*"+list.get(position).count);

        return convertView;
    }

    class ViewHolder {
        TextView order_management_item_details_item_name;
    }
}