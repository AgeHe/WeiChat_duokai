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
import java.util.HashMap;
import java.util.List;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;


/**
 * Created by zhanglinkai on 2017/3/19.
 * 功能:
 */

public class AddTalkAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String,Object>> list;
    private clearclick click;
    public AddTalkAdapter(Context context,List<HashMap<String,Object>> list,clearclick click){
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
            view= LayoutInflater.from(context).inflate(R.layout.layout_addtalk_list_item,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        Picasso.with(context).load(new File(list.get(position).get("icon").toString())).resize(100,100).into(holder.addtalk_list_item_icon);
        //holder.addtalk_list_item_icon.setImageBitmap(BitmapFactory.decodeFile(list.get(position).get("icon").toString()));

        StaticMethod.changeStrWithEmoji(context,list.get(position).get("bcontext").toString(),holder.addtalk_list_item_context);
        holder.add_talk_list_item_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.clear(position);
            }
        });



        return view;
    }

    class ViewHolder{
        private ImageView addtalk_list_item_icon;
        private TextView addtalk_list_item_context;
        private ImageView add_talk_list_item_clear;
        public ViewHolder(View view){
            addtalk_list_item_icon=(ImageView)view.findViewById(R.id.addtalk_list_item_icon);
            addtalk_list_item_context=(TextView)view.findViewById(R.id.addtalk_list_item_context);
            add_talk_list_item_clear=(ImageView)view.findViewById(R.id.add_talk_list_item_clear);
        }
    }

    public interface clearclick{
        void clear(int position);
    }
}
