package com.okm_android_agile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okm_android_agile.R;

import java.util.ArrayList;

/**
 * Created by chen on 14-10-7.
 */
public class SortingAdapter extends BaseAdapter {
    //	内部类实现BaseAdapter  ，自定义适配器
    private Context context;
    private ArrayList<String> menuEntries;
    private ArrayList<Integer> menuImage;

    public SortingAdapter(Context context)
    {
        this.context = context;
    }


    @Override
    public int getViewTypeCount() {
        //包含有两个视图，所以返回值为2
        return 1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 5;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_sorting_item, null);
            holder = new ViewHolder();
//            holder.textView = (TextView) convertView
//                    .findViewById(R.id.activity_menu_text);
//            holder.img = (ImageView)convertView.findViewById(R.id.activity_menu_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.textView.setText(menuEntries.get(position));
//        holder.img.setImageResource(menuImage.get(position));

        return convertView;
    }

    class ViewHolder {
        TextView textView;
        ImageView img;
    }
}
