package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.view.NewGridView;
import com.zhushou.weichat.screenshot.view.Newlistview;

/**
 * Created by zhanglinkai on 2017/4/11.
 * 功能:
 */

public class WxFriendCirclePreviewItemAdapter extends BaseAdapter implements WxFriendCircleAddAdapter.itemClick{
    private Context context;
    private List<Map<String,Object>> list;
    private WxFriendCircleAddAdapter addAdapter;
    private WxFrienfdclircleCommitAdapter commitAdapter;
    private itemClick click;
    public WxFriendCirclePreviewItemAdapter(Context context,List<Map<String,Object>> list,itemClick click){

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
        final ViewHolder holder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.layout_wxfriendcircle_preview_list_item,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }

        String icon=list.get(position).get("image").toString();
        String name=list.get(position).get("name").toString();
        String c=list.get(position).get("context").toString();
        String time=list.get(position).get("time").toString();
        String location=list.get(position).get("location").toString();
        String zan=list.get(position).get("zan").toString();
        List<Map<String,String>> commitlist= (List<Map<String, String>>) list.get(position).get("commit");
        List<String> imagelist= (List<String>) list.get(position).get("list");

        Picasso.with(context).load(new File(icon)).resize(100,100).into(holder.wxfriendcircle_preview_list_item_icon);
        holder.wxfriendcircle_preview_list_item_name.setText(name);
        if (c.isEmpty()){
            holder.wxfriendcircle_preview_list_context_tv.setVisibility(View.GONE);
        }else{
            holder.wxfriendcircle_preview_list_context_tv.setVisibility(View.VISIBLE);
            holder.wxfriendcircle_preview_list_context_tv.setText(c);
        }

        if (location.equals(context.getString(R.string.weitianxie))){
            holder.wxfriendcircle_preview_list_location_tv.setVisibility(View.GONE);
        }else{
            holder.wxfriendcircle_preview_list_location_tv.setVisibility(View.VISIBLE);
            holder.wxfriendcircle_preview_list_location_tv.setText(location);
        }
        holder.wxfriendcircle_preview_list_time_tv.setText(time);

        addAdapter=new WxFriendCircleAddAdapter(context,imagelist,this);
        holder.wxfriendcircle_preview_list_item_gv.setAdapter(addAdapter);

        if (zan.isEmpty()&&commitlist.size()==0){
            holder.zan_commit_layout.setVisibility(View.GONE);
        }else {
            holder.zan_commit_layout.setVisibility(View.VISIBLE);
        }
        if (zan.isEmpty()){
            holder.zan_image.setVisibility(View.GONE);
            holder.wxriendcircle_preview_item_zan.setVisibility(View.GONE);
        }else{
            holder.zan_image.setVisibility(View.VISIBLE);
            holder.wxriendcircle_preview_item_zan.setVisibility(View.VISIBLE);
            holder.mm.setVisibility(View.GONE);
            holder.wxriendcircle_preview_item_zan.setText("     "+zan);
        }
        if (!zan.isEmpty()&&commitlist.size()!=0){
            holder.mm.setVisibility(View.VISIBLE);
        }else{
            holder.mm.setVisibility(View.GONE);
        }
        if (commitlist.size()==0){
            holder.wxriendcircle_preview_item_list.setVisibility(View.GONE);
        }else{
            holder.wxriendcircle_preview_item_list.setVisibility(View.VISIBLE);
            commitAdapter=new WxFrienfdclircleCommitAdapter(context,commitlist,new WxFrienfdclircleCommitAdapter.commitClear() {
                @Override
                public void itemclick(int p) {
                    click.commitCler(position,p);
                }
            });
            holder.wxriendcircle_preview_item_list.setAdapter(commitAdapter);
        }



        holder.wxfriendcircle_preview_list_commit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.connitClick(v,position);
            }
        });

        return view;
    }

    @Override
    public void clearItem(int p) {

    }

    @Override
    public void addItem() {

    }

    class ViewHolder{

        private ImageView wxfriendcircle_preview_list_item_icon;
        private TextView wxfriendcircle_preview_list_item_name;
        private NewGridView wxfriendcircle_preview_list_item_gv;
        private TextView wxfriendcircle_preview_list_context_tv;
        private TextView wxfriendcircle_preview_list_location_tv;
        private TextView wxfriendcircle_preview_list_time_tv;
        private ImageView wxfriendcircle_preview_list_commit_iv;
        private TextView wxriendcircle_preview_item_zan;
        private Newlistview wxriendcircle_preview_item_list;
        private RelativeLayout zan_commit_layout;
        private View mm;
        private ImageView zan_image;

        public ViewHolder(View view){
            wxfriendcircle_preview_list_item_icon=(ImageView)view.findViewById(R.id.wxfriendcircle_preview_list_item_icon);
            wxfriendcircle_preview_list_item_name=(TextView)view.findViewById(R.id.wxfriendcircle_preview_list_item_name);
            wxfriendcircle_preview_list_item_gv=(NewGridView)view.findViewById(R.id.wxfriendcircle_preview_list_item_gv);
            wxfriendcircle_preview_list_context_tv=(TextView)view.findViewById(R.id.wxfriendcircle_preview_list_context_tv);
            wxfriendcircle_preview_list_location_tv=(TextView)view.findViewById(R.id.wxfriendcircle_preview_list_location_tv);
            wxfriendcircle_preview_list_time_tv=(TextView)view.findViewById(R.id.wxfriendcircle_preview_list_time_tv);
            wxfriendcircle_preview_list_commit_iv=(ImageView)view.findViewById(R.id.wxfriendcircle_preview_list_commit_iv);
            wxriendcircle_preview_item_zan=(TextView)view.findViewById(R.id.wxriendcircle_preview_item_zan);
            mm=view.findViewById(R.id.mm);
            wxriendcircle_preview_item_list=(Newlistview)view.findViewById(R.id.wxriendcircle_preview_item_list);
            zan_commit_layout=(RelativeLayout)view.findViewById(R.id.zan_commit_layout);
            zan_image=(ImageView)view.findViewById(R.id.zan_image);
        }
    }
    public interface itemClick{
        void connitClick(View v,int p);
        void commitCler(int p1,int p2);
    }
}
