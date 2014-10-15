package com.okm_android_agile.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.okm_android_agile.R;
import com.okm_android_agile.Utils.AddObserver.NotificationCenter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by QYM on 14-10-9.
 */
public class FoodMenuAdapter extends BaseAdapter {
    final int TYPE_food = 2;
    final int VIEW_TYPE = 0;
    final int TYPE_Menu = 1;
    String typeName;
    Context mContext;
    LinearLayout linearLayout = null;
    LayoutInflater inflater;
    private List<Integer> typeLong = new ArrayList<Integer>();
    private List<Map<String,String>> listItems=new ArrayList<Map<String,String>>();
    public FoodMenuAdapter(Context context,List<Map<String,String>> list,List<Integer> menuLong){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        listItems=list;
        typeLong=menuLong;
    }
    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public int getItemViewType(int position) {
        for(int i=0;i<typeLong.size();i++)
        {
            if(position==typeLong.get(i))
                return TYPE_Menu;
        }
        return TYPE_food;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent)
    {
        final viewHolder1 holder1;
        final viewHolder2 holder2;
        int type = getItemViewType(position);
        switch(type)
        {
            case TYPE_Menu:
            {
                convertView = inflater.inflate(R.layout.food_group_item, parent, false);
                holder1 = new viewHolder1();
                holder1.menuName = (TextView)convertView.findViewById(R.id.food_group_name);
                Log.e("convertView = ", "NULL TYPE_menu");
                convertView.setTag(holder1);
                holder1.menuName.setText(listItems.get(position).get("menuName").toString());
            }break;
            case TYPE_food:
            {
                convertView = inflater.inflate(R.layout.food_menu_item, parent, false);
                holder2 = new viewHolder2();
                holder2.foodName = (TextView) convertView.findViewById(R.id.food_menu_name);
                holder2.foodPrice = (TextView) convertView.findViewById(R.id.food_price);
                holder2.saleCount = (TextView) convertView.findViewById(R.id.show_sale_count);
                holder2.addButton = (RelativeLayout) convertView.findViewById(R.id.count_add);
                holder2.subButton = (RelativeLayout) convertView.findViewById(R.id.count_sub);
                holder2.oneCount = (RelativeLayout) convertView.findViewById(R.id.one_food_count);
                holder2.Count = (TextView) convertView.findViewById(R.id.count);
                convertView.setTag(holder2);
                Log.e("convertView = ", "NULL TYPE_food");
                holder2.foodName.setText(listItems.get(position).get("foodName"));
                holder2.foodPrice.setText(listItems.get(position).get("foodPrice"));
                holder2.saleCount.setText("月售"+listItems.get(position).get("monthSale")+"份");

                holder2.addButton.setTag(position);
                holder2.addButton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v){
                        int count= Integer.valueOf(holder2.Count.getText().toString())+1;
                        holder2.Count.setText(count+"");
                        if(Integer.valueOf(holder2.Count.getText().toString())>0)
                        {
                            holder2.subButton.setVisibility(View.VISIBLE);
                            holder2.oneCount.setVisibility(View.VISIBLE);
                        }
                        NotificationCenter.getInstance().postNotification("AddFoodPrice",listItems.get(position).toString());
                        NotificationCenter.getInstance().postNotification("AddFoodCount");
                    }
                });
                holder2.subButton.setTag(position);
                holder2.subButton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v){
                        int count= Integer.valueOf(holder2.Count.getText().toString())-1;
                        holder2.Count.setText(count+"");
                        if(Integer.valueOf(holder2.Count.getText().toString())<=0)
                        {
                            holder2.subButton.setVisibility(View.INVISIBLE);
                            holder2.oneCount.setVisibility(View.INVISIBLE);
                        }
                        NotificationCenter.getInstance().postNotification("SubFoodPrice",listItems.get(position).toString());
                        NotificationCenter.getInstance().postNotification("SubFoodCount");
                    }
                });

            }break;
        }
        return convertView;
    }
    class viewHolder1{
        public TextView menuName;
    }
    class viewHolder2{
        public TextView foodName;
        public TextView foodPrice;
        public TextView saleCount;
        public TextView Count;
        public RelativeLayout addButton;
        public RelativeLayout subButton;
        public RelativeLayout oneCount;
    }

}
