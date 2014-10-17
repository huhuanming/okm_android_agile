package com.okm_android.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.okm_android.main.Model.FoodsData;
import com.okm_android.main.R;
import com.okm_android.main.Utils.AddObserver.NotificationCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by QYM on 14-10-9.
 */
public class FoodMenuAdapter extends BaseAdapter {
    final int TYPE_food = 2;
    final int TYPE_Menu = 1;
    Context mContext;
    LayoutInflater inflater;
    private List<Integer> typeLong;
    private ArrayList<FoodsData> listItems;
    private List<String> typeName;
    public FoodMenuAdapter(Context context,ArrayList<FoodsData> foods,List<Integer> menuLong,List<String> typeName){
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.listItems=foods;
        this.typeLong=menuLong;
        this.typeName = typeName;
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
        int num = typeLong.size();
        for(int i = 0;i < num; i++)
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
//                convertView.setTag(holder1);
                holder1.menuName.setText(typeName.get(position));
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
//                convertView.setTag(holder2);
                holder2.foodName.setText(listItems.get(position).food_name);
                holder2.foodPrice.setText(listItems.get(position).food_price);
                holder2.saleCount.setText(listItems.get(position).sold_number);
                if(listItems.get(position).count > 0)
                {
                    holder2.subButton.setVisibility(View.VISIBLE);
                    holder2.oneCount.setVisibility(View.VISIBLE);
                    holder2.addButton.setBackgroundResource(R.drawable.food_menu_founder_add_style);
                    holder2.Count.setText(listItems.get(position).count+"");
                }
//                holder2.addButton.setTag(position);
                holder2.addButton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v){
                        listItems.get(position).count = listItems.get(position).count + 1;
                        holder2.Count.setText(listItems.get(position).count+"");
                        if(listItems.get(position).count > 0)
                        {
                            holder2.subButton.setVisibility(View.VISIBLE);
                            holder2.oneCount.setVisibility(View.VISIBLE);
                            holder2.addButton.setBackgroundResource(R.drawable.food_menu_flat_add_style);
                            listItems.get(position).isHave = true;
                        }
                        NotificationCenter.getInstance().postNotification("AddFoodPrice",listItems.get(position).food_price);
                        NotificationCenter.getInstance().postNotification("AddFoodCount");
                    }
                });
//                holder2.subButton.setTag(position);
                holder2.subButton.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v){
                        listItems.get(position).count = listItems.get(position).count - 1;
                        holder2.Count.setText(listItems.get(position).count+"");
                        if(listItems.get(position).count <= 0)
                        {
                            holder2.subButton.setVisibility(View.INVISIBLE);
                            holder2.oneCount.setVisibility(View.INVISIBLE);
                            holder2.addButton.setBackgroundResource(R.drawable.food_menu_founder_add_style);
                            listItems.get(position).isHave = false;
                        }
                        NotificationCenter.getInstance().postNotification("SubFoodPrice",listItems.get(position).food_price);
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
