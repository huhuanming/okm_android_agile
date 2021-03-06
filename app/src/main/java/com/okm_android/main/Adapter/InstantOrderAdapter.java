package com.okm_android.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Update;
import com.okm_android.main.Model.FoodsData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;

import java.util.ArrayList;

/**
 * Created by QYM on 14-10-8.
 */
public class InstantOrderAdapter extends BaseAdapter {
    private Context context;                        //运行上下文
    private ArrayList<FoodsData> listItems;    //商品信息集合
    private LayoutInflater layoutInflater;
    public InstantOrderAdapter(Context c,ArrayList<FoodsData> list)
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
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
       if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.order_food_item, null);
            holder.name = (TextView) convertView.findViewById(R.id.food_name);
            holder.number = (TextView) convertView.findViewById(R.id.food_count);
            holder.money = (TextView) convertView.findViewById(R.id.money);
           holder.add_img = (ImageView)convertView.findViewById(R.id.order_food_item_add);
           holder.subtract_img = (ImageView)convertView.findViewById(R.id.order_food_item_subtract);
            //得到条目中的子组件
           convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        //从list对象中为子组件赋值
        if(listItems.get(position).count <= 1)
        {
            holder.subtract_img.setBackgroundResource(R.drawable.order_choose_founder_one_subtract);
            holder.subtract_img.setEnabled(false);
        }
        holder.name.setText(listItems.get(position).food_name);
        holder.number.setText(listItems.get(position).count+"");
        float money = listItems.get(position).count * Float.parseFloat(listItems.get(position).food_price);
        holder.money.setText("￥ "+money);

        holder.add_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.get(position).count = listItems.get(position).count + 1;
                holder.number.setText(listItems.get(position).count+"");
                new Update(FoodsData.class).set("Count = ?",listItems.get(position).count+"")
                        .where("Food_id = ?",listItems.get(position).food_id).execute();
                float addmoney = listItems.get(position).count * Float.parseFloat(listItems.get(position).food_price);
                holder.money.setText("￥ "+addmoney);
                if(listItems.get(position).count >= 1)
                {
                    holder.subtract_img.setBackgroundResource(R.drawable.order_choose_founder_more_subtract);
                    holder.subtract_img.setEnabled(true);
                }
                NotificationCenter.getInstance().postNotification("addAllPrice",listItems.get(position).food_price);
            }
        });

        holder.subtract_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listItems.get(position).count = listItems.get(position).count - 1;
                holder.number.setText(listItems.get(position).count+"");
                float submoney = listItems.get(position).count * Float.parseFloat(listItems.get(position).food_price);
                holder.money.setText("￥ "+submoney);
                new Update(FoodsData.class).set("Count = ?",listItems.get(position).count+"")
                        .where("Food_id = ?",listItems.get(position).food_id).execute();
                if(listItems.get(position).count <= 1)
                {
                    holder.subtract_img.setBackgroundResource(R.drawable.order_choose_founder_one_subtract);
                    holder.subtract_img.setEnabled(false);
                }
                NotificationCenter.getInstance().postNotification("subAllPrice",listItems.get(position).food_price);
            }
        });

        return convertView;
    }
    class ViewHolder {
        TextView name;
        TextView number;
        TextView money;
        ImageView add_img;
        ImageView subtract_img;
    }
}
