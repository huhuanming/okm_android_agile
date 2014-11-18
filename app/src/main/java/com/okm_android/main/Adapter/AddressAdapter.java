package com.okm_android.main.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.okm_android.main.Application.OkmApplication;
import com.okm_android.main.Model.AddressData;
import com.okm_android.main.R;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
    private List<AddressData> listItems;    //商品信息集合
    private LayoutInflater layoutInflater;

    public AddressAdapter(List<AddressData> list) {
        layoutInflater = LayoutInflater.from(OkmApplication.getAppContext());
        listItems = list;
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public AddressData getItem(int position) {
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
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        //从list对象中为子组件赋值
        holder.name.setText(listItems.get(position).shipping_user);
        holder.number.setText(listItems.get(position).phone_number);
        holder.address.setText(listItems.get(position).shipping_address);
        return convertView;
    }

    class ViewHolder {
        TextView name;
        TextView number;
        TextView address;
    }
}
