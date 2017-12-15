package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/4/7.
 * 功能:
 */

public class MultiTalkSelectPeopleDialogAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> list;
    private itemClick click;
    public MultiTalkSelectPeopleDialogAdapter(Context context,List<Map<String,String>> list,itemClick click){
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
            view= LayoutInflater.from(context).inflate(R.layout.layout_multi_talk_selectpeople_dialog_item,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        Picasso.with(context).load(new File(list.get(position).get("image"))).resize(100,100).into(holder.multi_talk_sp_dialog_item_iv);
        holder.multi_talk_sp_dialog_item_tv.setText(list.get(position).get("name"));
        if (position==0){
            holder.multi_talk_sp_dialog_item_tag.setVisibility(View.VISIBLE);
        }else{
            holder.multi_talk_sp_dialog_item_tag.setVisibility(View.GONE);
        }

        holder.multi_talk_sp_dialog_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.itemclick(position);
            }
        });

        return view;
    }
    class ViewHolder{
        private LinearLayout multi_talk_sp_dialog_item_layout;
        private ImageView multi_talk_sp_dialog_item_iv;
        private TextView multi_talk_sp_dialog_item_tv;
        private TextView multi_talk_sp_dialog_item_tag;
        public ViewHolder(View view){
            multi_talk_sp_dialog_item_layout=(LinearLayout)view.findViewById(R.id.multi_talk_sp_dialog_item_layout);
            multi_talk_sp_dialog_item_iv=(ImageView)view.findViewById(R.id.multi_talk_sp_dialog_item_iv);
            multi_talk_sp_dialog_item_tv=(TextView)view.findViewById(R.id.multi_talk_sp_dialog_item_tv);
            multi_talk_sp_dialog_item_tag=(TextView)view.findViewById(R.id.multi_talk_sp_dialog_item_tag);
        }
    }
    public interface itemClick{
        void itemclick(int p);
    }
}
