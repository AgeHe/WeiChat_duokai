package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.view.MyImageView;

/**
 * Created by zhanglinkai on 2017/4/10.
 * 功能:
 */

public class WxFriendCircleAddAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private itemClick click;
    private int iniphotowidth=550;
    public WxFriendCircleAddAdapter(Context context,List<String> list,itemClick click){
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
    public void notifys(){
        this.notifyDataSetChanged();
    }
    HashMap<Integer, View> localAppMap = new HashMap<Integer, View>();
    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (localAppMap.get(position)==null){
            view= LayoutInflater.from(context).inflate(R.layout.layout_wx_friendcircle_add_gridview_item,parent,false);
            holder=new ViewHolder(view);
            localAppMap.put(position,view);
            view.setTag(holder);
        }else{
            view=localAppMap.get(position);
            holder=(ViewHolder)view.getTag();
        }
        if (list.get(position).isEmpty()){
            holder.friendcircle_add_gridview_item_iv.setImageResource(R.mipmap.add);
        }else {

            if (list.size() == 1) {
                Bitmap bitmap= BitmapFactory.decodeFile(list.get(position));
                float width=bitmap.getWidth();
                float height=bitmap.getHeight();
                float w=width/height;
                if (w<1&&w>0.7){
                    w=1;
                }
                Log.e("ttt", "getView: "+w );
                holder.friendcircle_add_gridview_item_iv.setVisibility(View.GONE);
                holder.friendcircle_add_gridview_item_matchiv.setVisibility(View.VISIBLE);
                view.setLayoutParams(new GridView.LayoutParams((int) (iniphotowidth*w), iniphotowidth));
                view.setBackgroundColor(context.getResources().getColor(R.color.blue));
                Picasso.with(context).load(new File(list.get(position))).fit().into(holder.friendcircle_add_gridview_item_matchiv);

            } else {
                holder.friendcircle_add_gridview_item_iv.setVisibility(View.VISIBLE);
                holder.friendcircle_add_gridview_item_matchiv.setVisibility(View.GONE);
                Picasso.with(context).load(new File(list.get(position))).resize(300, 300).centerCrop().into(holder.friendcircle_add_gridview_item_iv);

            }
        }
        holder.friendcircle_add_gridview_item_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position==list.size()-1){
                    click.addItem();
                }else{
                    click.clearItem(position);
                }
            }
        });


        return view;
    }
    class ViewHolder{
        private MyImageView friendcircle_add_gridview_item_iv;
        private ImageView friendcircle_add_gridview_item_matchiv;
        public ViewHolder(View view){
            friendcircle_add_gridview_item_iv=(MyImageView)view.findViewById(R.id.friendcircle_add_gridview_item_iv);
            friendcircle_add_gridview_item_matchiv=(ImageView)view.findViewById(R.id.friendcircle_add_gridview_item_matchiv);
        }
    }
    public interface itemClick{
        void clearItem(int p);
        void addItem();
    }
}
