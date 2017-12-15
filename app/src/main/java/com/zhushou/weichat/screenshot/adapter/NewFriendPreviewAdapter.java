package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
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

public class NewFriendPreviewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,Object>> list;
    public NewFriendPreviewAdapter(Context context,List<Map<String,Object>> list){
        this.context=context;
        this.list=list;
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
            view= LayoutInflater.from(context).inflate(R.layout.layout_newfriend_preview_list_item,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        Picasso.with(context).load(new File(list.get(position).get("image").toString())).resize(100,100).into(holder.newfriend_preview_item_icon);
        //holder.newfriend_preview_item_icon.setImageBitmap(BitmapFactory.decodeFile(list.get(position).get("image").toString()));
        holder.newfriend_preview_item_name.setText(list.get(position).get("name").toString());
        holder.newfriend_preview_item_des.setText(list.get(position).get("des").toString());
        boolean isselect= (boolean) list.get(position).get("isfriend");
        if (isselect){
            holder.newfriend_preview_item_tv.setVisibility(View.VISIBLE);
            holder.newfriend_preview_item_btn.setVisibility(View.GONE);
        }else{
            holder.newfriend_preview_item_tv.setVisibility(View.GONE);
            holder.newfriend_preview_item_btn.setVisibility(View.VISIBLE);
        }

        return view;
    }
    class ViewHolder{
        private ImageView newfriend_preview_item_icon;
        private TextView newfriend_preview_item_name;
        private TextView newfriend_preview_item_des;
        private Button newfriend_preview_item_btn;
        private TextView newfriend_preview_item_tv;
        public ViewHolder(View view){
            newfriend_preview_item_icon=(ImageView)view.findViewById(R.id.newfriend_preview_item_icon);
            newfriend_preview_item_name=(TextView)view.findViewById(R.id.newfriend_preview_item_name);
            newfriend_preview_item_des=(TextView)view.findViewById(R.id.newfriend_preview_item_des);

            newfriend_preview_item_btn=(Button)view.findViewById(R.id.newfriend_preview_item_btn);
            newfriend_preview_item_tv=(TextView)view.findViewById(R.id.newfriend_preview_item_tv);
        }
    }
}
