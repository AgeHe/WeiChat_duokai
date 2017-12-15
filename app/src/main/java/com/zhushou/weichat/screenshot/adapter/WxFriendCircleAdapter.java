package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/4/10.
 * 功能:
 */

public class WxFriendCircleAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> list;
    private itemClick click;
    public WxFriendCircleAdapter(Context context,List<Map<String,Object>> list,itemClick click){
        this.context=context;
        this.list=list;
        this.click=click;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.layout_wx_friendcircle_list_item,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        holder.wx_friendcircle_item_name.setText(list.get(position).get("name").toString());
        String des=list.get(position).get("context").toString();
        if (des.isEmpty()){
            holder.wx_friendcircle_item_des.setText(R.string.mymiaoshuneirong);
        }else{
            holder.wx_friendcircle_item_des.setText(des);
        }
        Picasso.with(context).load(new File(list.get(position).get("image").toString())).resize(100,100).into(holder.wx_friendcircle_item_icon);

        holder.wx_friendcircle_item_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.itemclick(position);
            }
        });

        return view;
    }
    class ViewHolder{
        private ImageView wx_friendcircle_item_icon;
        private TextView wx_friendcircle_item_name;
        private TextView wx_friendcircle_item_des;
        private ImageView wx_friendcircle_item_clear;
        public ViewHolder(View view){
            wx_friendcircle_item_icon=(ImageView)view.findViewById(R.id.wx_friendcircle_item_icon);
            wx_friendcircle_item_name=(TextView)view.findViewById(R.id.wx_friendcircle_item_name);
            wx_friendcircle_item_des=(TextView)view.findViewById(R.id.wx_friendcircle_item_des);
            wx_friendcircle_item_clear=(ImageView)view.findViewById(R.id.wx_friendcircle_item_clear);
        }
    }
    public interface itemClick{
        void itemclick(int p);
    }
}
