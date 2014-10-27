package com.okm_android.main.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.okm_android.main.Model.RestaurantComment;
import com.okm_android.main.R;
import com.okm_android.main.Utils.DateUtils;
import com.okm_android.main.Utils.UploadImage.Image;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chen on 14-10-7.
 */
public class SortingAdapter extends BaseAdapter {
    //	内部类实现BaseAdapter  ，自定义适配器
    private Context context;
    private ArrayList<String> menuEntries;
    private ArrayList<Integer> menuImage;
    private List<RestaurantComment> list;
    public SortingAdapter(Context context,List<RestaurantComment> list)
    {
        this.context = context;
        this.list = list;
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
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_sorting_item, null);
            holder = new ViewHolder();
//            holder.textView = (TextView) convertView
//                    .findViewById(R.id.activity_menu_text);
//            holder.img = (ImageView)convertView.findViewById(R.id.activity_menu_img);
            holder.img1=(ImageView)convertView.findViewById(R.id.image1);
            holder.img2=(ImageView)convertView.findViewById(R.id.image2);
            holder.img3=(ImageView)convertView.findViewById(R.id.image3);
            holder.img4=(ImageView)convertView.findViewById(R.id.image4);
            holder.img5=(ImageView)convertView.findViewById(R.id.image5);
            holder.tvName=(TextView)convertView.findViewById(R.id.comment_name);
            holder.tvComment=(TextView)convertView.findViewById(R.id.tv_comment);
            holder.tvTime=(TextView)convertView.findViewById(R.id.time);
            holder.tvTitle=(TextView)convertView.findViewById(R.id.tv_title);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.textView.setText(menuEntries.get(position));
//        holder.img.setImageResource(menuImage.get(position));
        String s = list.get(position).point.toString();
        if (s.equals("1")) {
            holder.img1.setImageResource(R.drawable.star_on);
            holder.img2.setImageResource(R.drawable.star_down);
            holder.img3.setImageResource(R.drawable.star_down);
            holder.img4.setImageResource(R.drawable.star_down);
            holder.img5.setImageResource(R.drawable.star_down);
        } else if (s.equals("2")) {
            holder.img1.setImageResource(R.drawable.star_on);
            holder.img2.setImageResource(R.drawable.star_on);
            holder.img3.setImageResource(R.drawable.star_down);
            holder.img4.setImageResource(R.drawable.star_down);
            holder.img5.setImageResource(R.drawable.star_down);
        } else if (s.equals("3")) {
            holder.img1.setImageResource(R.drawable.star_on);
            holder.img2.setImageResource(R.drawable.star_on);
            holder.img3.setImageResource(R.drawable.star_on);
            holder.img4.setImageResource(R.drawable.star_down);
            holder.img5.setImageResource(R.drawable.star_down);
        } else if (s.equals("4")) {
            holder.img1.setImageResource(R.drawable.star_on);
            holder.img2.setImageResource(R.drawable.star_on);
            holder.img3.setImageResource(R.drawable.star_on);
            holder.img4.setImageResource(R.drawable.star_on);
            holder.img5.setImageResource(R.drawable.star_down);
        } else if (s.equals("5")) {
            holder.img1.setImageResource(R.drawable.star_on);
            holder.img2.setImageResource(R.drawable.star_on);
            holder.img3.setImageResource(R.drawable.star_on);
            holder.img4.setImageResource(R.drawable.star_on);
            holder.img5.setImageResource(R.drawable.star_on);
        }
        holder.tvName.setText(list.get(position).author);
        holder.tvTitle.setText(list.get(position).title);
        holder.tvTime.setText(DateUtils.getMonthTime(list.get(position).created_at));
        holder.tvComment.setText(list.get(position).comment);
        return convertView;
    }


    class ViewHolder {
        TextView tvTime;
        TextView tvTitle;
        TextView tvComment;
        TextView tvName;
        ImageView img1;
        ImageView img2;
        ImageView img3;
        ImageView img4;
        ImageView img5;
    }
}
