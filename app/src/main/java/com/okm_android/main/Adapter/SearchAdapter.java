package com.okm_android.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okm_android.main.Model.SearchBackData;
import com.okm_android.main.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chen on 14-9-29.
 */
public class SearchAdapter extends BaseAdapter {

    //	内部类实现BaseAdapter  ，自定义适配器
    private Context context;
    private ArrayList<String> menuEntries;
    private ArrayList<Integer> menuImage;
    private List<SearchBackData> list;
    public SearchAdapter(Context context,List<SearchBackData> list)
    {
        this.context = context;
        this.list=list;
    }


    @Override
    public int getViewTypeCount() {
        //包含有两个视图，所以返回值为2
        return 2;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_search_item, null);
            holder = new ViewHolder();
            holder.food_name=(TextView)convertView.findViewById(R.id.food_name);
            holder.restaurant_name=(TextView)convertView.findViewById(R.id.restaurant_name);
//
//           holder.textView = (TextView) convertView
//                    .findViewById(R.id.activity_menu_text);
 //           holder.img = (ImageView)convertView.findViewById(R.id.activity_menu_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.textView.setText(menuEntries.get(position));
//        holder.img.setImageResource(menuImage.get(position));
        holder.restaurant_name.setText(list.get(position).restaurant_name);
        holder.food_name.setText(list.get(position).food_name);

        return convertView;
    }

    class ViewHolder {
        TextView food_name;
        TextView restaurant_name;
        ImageView img;
    }
}
