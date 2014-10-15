package com.okm_android_agile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.okm_android_agile.R;

import java.util.List;

/**
 * Created by hu on 14-10-9.
 */
public class PositionSearchAdapter extends BaseAdapter {
    //	内部类实现BaseAdapter  ，自定义适配器
    private Context context;
    private List<PoiItem> poiItems;

    public PositionSearchAdapter(Context context,List<PoiItem> poiItems)
    {
        this.context = context;
        this.poiItems = poiItems;
    }


    @Override
    public int getViewTypeCount() {
        //包含有两个视图，所以返回值为2
        return 1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return poiItems.size() ;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_position_search_item, null);
            holder = new ViewHolder();
            holder.activity_position_search_item_name = (TextView) convertView
                    .findViewById(R.id.activity_position_search_item_name);
            holder.activity_position_search_item_vicinity = (TextView)convertView.findViewById(R.id.activity_position_search_item_vicinity);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.activity_position_search_item_name.setText(poiItems.get(position).getTitle());
        holder.activity_position_search_item_vicinity.setText(poiItems.get(position).getSnippet());

        return convertView;
    }

    class ViewHolder {
        TextView activity_position_search_item_name;
        TextView activity_position_search_item_vicinity;
    }
}
