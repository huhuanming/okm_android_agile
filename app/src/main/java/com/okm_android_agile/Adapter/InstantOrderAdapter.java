package com.okm_android_agile.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.okm_android_agile.R;

import java.util.List;
import java.util.Map;

/**
 * Created by QYM on 14-10-8.
 */
public class InstantOrderAdapter extends BaseAdapter {
    private Context context;                        //运行上下文
    private List<Map<String, String>> listItems;    //商品信息集合
    private LayoutInflater layoutInflater;
    public InstantOrderAdapter(Context c,List<Map<String,String>> list)
    {
        context=c;
        layoutInflater = LayoutInflater.from(context);
        listItems=list;
    }
    @Override
    public int getCount() {
        return listItems.size();
    }
    @Override
    public Map<String,String> getItem(int position) {
        return listItems.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
       // if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.order_food_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.food_name);
            holder.number = (TextView) convertView.findViewById(R.id.food_count);
            holder.money = (TextView) convertView.findViewById(R.id.money);
            /*//得到条目中的子组件
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        //从list对象中为子组件赋值*/
        holder.name.setText(listItems.get(position).get("name").toString());
        holder.number.setText(listItems.get(position).get("number").toString());
        holder.money.setText(listItems.get(position).get("money").toString());
        return convertView;
    }
    class ViewHolder {
        TextView name;
        TextView number;
        TextView money;
    }
}
