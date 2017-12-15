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
 * Created by zhanglinkai on 2017/4/6.
 * 功能:
 */

public class WxMultiTalkInfosetAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> list;
    private addItem click;
    public WxMultiTalkInfosetAdapter(Context context,List<Map<String,String>> list,addItem click){
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
            view= LayoutInflater.from(context).inflate(R.layout.layout_multi_talk_infoset_gridview_item,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        if (list.get(position).get("image").isEmpty()){
            holder.multi_talk_infoset_gridview_item_icon.setImageResource(R.mipmap.add);
        }else {
            Picasso.with(context).load(new File(list.get(position).get("image"))).resize(100, 100).into(holder.multi_talk_infoset_gridview_item_icon);
        }
        holder.multi_talk_infoset_gridview_item_name.setText(list.get(position).get("name"));

        holder.multi_talk_infoset_gridview_item_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==list.size()-1){
                    click.additem();
                }
            }
        });

        return view;
    }

    class ViewHolder{
        private ImageView multi_talk_infoset_gridview_item_icon;
        private TextView multi_talk_infoset_gridview_item_name;
        public ViewHolder(View view){
            multi_talk_infoset_gridview_item_icon=(ImageView)view.findViewById(R.id.multi_talk_infoset_gridview_item_icon);
            multi_talk_infoset_gridview_item_name=(TextView)view.findViewById(R.id.multi_talk_infoset_gridview_item_name);
        }
    }
    public interface addItem{
        void additem();
    }
}
