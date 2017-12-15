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
 * Created by zhanglinkai on 2017/3/31.
 * 功能:
 */

public class WxNewFriendAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> list;
    private itemClear cick;
    public WxNewFriendAdapter(Context context,List<Map<String,Object>> list,itemClear cick){
        this.context=context;
        this.list=list;
        this.cick=cick;
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
            view= LayoutInflater.from(context).inflate(R.layout.layout_newfriend_list_item,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        Picasso.with(context).load(new File(list.get(position).get("image").toString())).resize(100,100).into(holder.newfriend_item_iv);
        //holder.newfriend_item_iv.setImageBitmap(BitmapFactory.decodeFile(list.get(position).get("image").toString()));
        holder.newfriend_item_name.setText(list.get(position).get("name").toString());
        holder.newfriend_item_des.setText(list.get(position).get("des").toString());
        holder.newfriend_item_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cick.itemClick(position);
            }
        });

        return view;
    }
    class ViewHolder{
        private ImageView newfriend_item_iv;
        private TextView newfriend_item_name;
        private TextView newfriend_item_des;
        private ImageView newfriend_item_clear;
        public ViewHolder(View view){
            newfriend_item_iv=(ImageView)view.findViewById(R.id.newfriend_item_iv);
            newfriend_item_name=(TextView)view.findViewById(R.id.newfriend_item_name);
            newfriend_item_des=(TextView)view.findViewById(R.id.newfriend_item_des);
            newfriend_item_clear=(ImageView)view.findViewById(R.id.newfriend_item_clear);
        }
    }
    public interface itemClear{
        void itemClick(int p);
    }
}
