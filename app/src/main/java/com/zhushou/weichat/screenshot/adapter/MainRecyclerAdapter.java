package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import com.zhushou.weichat.R;

/**
 * Created by zhanglinkai on 2017/3/23.
 * 功能:
 */

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<HashMap<String,Object>> list;
    private mainClick click;
    public MainRecyclerAdapter(Context context,List<HashMap<String,Object>> list,mainClick click){
        this.context=context;
        this.list=list;
        this.click=click;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.layout_main_recycler_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.main_recycle_item_iv.setImageResource((Integer) list.get(position).get("icon"));
        holder.main_recycle_item_tv.setText(list.get(position).get("txt").toString());

        String txt=list.get(position).get("txt").toString();
        if (txt.indexOf(R.string.pyq)!=-1||
                txt.indexOf(R.string.wodeqianbao)!=-1||
                txt.indexOf(context.getString(R.string.xindepy))!=-1||
                txt.indexOf(context.getString(R.string.lingqianmingxi))!=-1||
                txt.indexOf(context.getString(R.string.shipinliaotian))!=-1||
                txt.indexOf(context.getString(R.string.yuyinliaotian))!=-1||txt.indexOf(context.getString(R.string.weixin))!=-1){

            holder.main_recycle_item_layout.setBackgroundResource(R.drawable.green_shape);
            holder.main_recycle_item_viptag.setVisibility(View.GONE);
        }

        if (txt.indexOf(context.getString(R.string.zfb))!=-1||txt.indexOf(R.string.yuebao)!=-1){
            holder.main_recycle_item_layout.setBackgroundResource(R.drawable.blue_shape);
            holder.main_recycle_item_viptag.setVisibility(View.GONE);
        }
        if (txt.indexOf(context.getString(R.string.qq))!=-1){
            holder.main_recycle_item_layout.setBackgroundResource(R.drawable.asgreen_shape);
            holder.main_recycle_item_viptag.setVisibility(View.GONE);
        }

        if ((Boolean) list.get(position).get("vip")){
            holder.main_recycle_item_layout.setBackgroundResource(R.drawable.black_shape);
            holder.main_recycle_item_viptag.setVisibility(View.VISIBLE);
        }

        holder.main_recycle_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.itemclick(list.get(position).get("txt").toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout main_recycle_item_layout;
        private ImageView main_recycle_item_iv;
        private ImageView main_recycle_item_viptag;
        private TextView main_recycle_item_tv;
        public ViewHolder(View view) {
            super(view);
            main_recycle_item_layout=(RelativeLayout)view.findViewById(R.id.main_recycle_item_layout);
            main_recycle_item_iv=(ImageView)view.findViewById(R.id.main_recycle_item_iv);
            main_recycle_item_viptag=(ImageView)view.findViewById(R.id.main_recycle_item_viptag);
            main_recycle_item_tv=(TextView)view.findViewById(R.id.main_recycle_item_tv);
        }
    }

    public interface mainClick{
        void itemclick(String s);
    }
}
