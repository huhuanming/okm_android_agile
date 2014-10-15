package com.okm_android.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okm_android.main.R;

import java.util.List;
import java.util.Map;

public class AddressAdapter extends BaseAdapter {
    private Context context;                        //运行上下文
    private List<Map<String, String>> listItems;    //商品信息集合
    private LayoutInflater layoutInflater;

    public AddressAdapter(Context c, List<Map<String, String>> list) {
        context = c;
        layoutInflater = LayoutInflater.from(context);
        listItems = list;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.address_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.address_name);
            holder.number = (TextView) convertView.findViewById(R.id.address_number);
            holder.address = (TextView) convertView.findViewById(R.id.address_address);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        //从list对象中为子组件赋值
        holder.name.setText(listItems.get(position).get("name").toString());
        holder.number.setText(listItems.get(position).get("number").toString());
        holder.address.setText(listItems.get(position).get("address").toString());
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView number;
        TextView address;
    }
}
