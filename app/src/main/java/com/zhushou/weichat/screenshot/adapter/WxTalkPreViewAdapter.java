package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;
import com.zhushou.weichat.screenshot.view.BubbleImageView;


/**
 * Created by zhanglinkai on 2017/3/20.
 * 功能:
 */

public class WxTalkPreViewAdapter extends BaseAdapter {
    private Context context;
    private List<HashMap<String, Object>> list;
    private HashMap<Integer, View> localAppMap;
    public WxTalkPreViewAdapter(Context context,List<HashMap<String, Object>> list){
        this.context=context;
        this.list=list;
        localAppMap = new HashMap<Integer, View>();
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (localAppMap.get(position)==null){
            view= LayoutInflater.from(context).inflate(R.layout.layout_addtalk_preview_list_item,parent,false);
            holder=new ViewHolder(view);
            localAppMap.put(position,view);
            view.setTag(holder);
        }else{
            view=localAppMap.get(position);
            holder=(ViewHolder)view.getTag();
        }

        String type=list.get(position).get("type").toString();
        String sendtype=list.get(position).get("sendtype").toString();
        String time=list.get(position).get("time").toString();
        if (time.isEmpty()){
            holder.preview_time.setVisibility(View.GONE);
        }else {
            holder.preview_time.setVisibility(View.VISIBLE);
            holder.preview_time.setText(time);
        }
        if (sendtype.equals("m")){
            //right
            holder.right_preview_layout.setVisibility(View.VISIBLE);
            holder.left_preview_layout.setVisibility(View.GONE);
            Picasso.with(context).load(new File(list.get(position).get("icon").toString())).resize(100,100).into(holder.preview_txt_icon);
            //holder.preview_txt_icon.setImageBitmap(BitmapFactory.decodeFile(list.get(position).get("icon").toString()));
        }else if(sendtype.equals("h")){
            //left
            holder.right_preview_layout.setVisibility(View.GONE);
            holder.left_preview_layout.setVisibility(View.VISIBLE);
            Picasso.with(context).load(new File(list.get(position).get("icon").toString())).resize(100,100).into(holder.preview_left_txt_icon);
            //holder.preview_left_txt_icon.setImageBitmap(BitmapFactory.decodeFile(list.get(position).get("icon").toString()));
        }
        switch (type){
            case "txt":
                if (sendtype.equals("m")){
                    //right
                    holder.preview_txt_tag.setVisibility(View.GONE);
                    holder.preview_txt.setVisibility(View.VISIBLE);
                    holder.preview_photo.setVisibility(View.GONE);
                    holder.preview_voice_layout.setVisibility(View.GONE);
                    holder.preview_redpacket_layout.setVisibility(View.GONE);
                    holder.preview_transfer_layout.setVisibility(View.GONE);
                    holder.preview_right_times.setVisibility(View.GONE);
                    holder.right_redpacket_tag_layout.setVisibility(View.GONE);

                    StaticMethod.changeStrWithEmoji(context,list.get(position).get("context").toString(),holder.preview_txt);

                }else if(sendtype.equals("h")){
                    //left
                    holder.preview_left_txt_tag.setVisibility(View.GONE);
                    holder.preview_left_txt.setVisibility(View.VISIBLE);
                    holder.preview_left_photo.setVisibility(View.GONE);
                    holder.preview_left_voice_layout.setVisibility(View.GONE);
                    holder.preview_left_redpacket_layout.setVisibility(View.GONE);
                    holder.preview_left_transfer_layout.setVisibility(View.GONE);
                    holder.preview_left_times.setVisibility(View.GONE);
                    holder.left_redpacket_tag_layout.setVisibility(View.GONE);
                    StaticMethod.changeStrWithEmoji(context,list.get(position).get("context").toString(),holder.preview_left_txt);
                }


                break;
            case "photo":
                if (sendtype.equals("m")){
                    //right
                    holder.preview_txt_tag.setVisibility(View.GONE);
                    holder.preview_txt.setVisibility(View.GONE);
                    holder.preview_photo.setVisibility(View.VISIBLE);
                    holder.preview_voice_layout.setVisibility(View.GONE);
                    holder.preview_redpacket_layout.setVisibility(View.GONE);
                    holder.preview_transfer_layout.setVisibility(View.GONE);
                    holder.preview_right_times.setVisibility(View.GONE);
                    holder.right_redpacket_tag_layout.setVisibility(View.GONE);


                    Picasso.with(context).load(new File(list.get(position).get("bitmap").toString())).centerCrop().resize(250,400).into(holder.preview_photo);
                    //holder.preview_photo.setImageBitmap(BitmapFactory.decodeFile(list.get(position).get("bitmap").toString()));

                }else if(sendtype.equals("h")){
                    //left
                    holder.preview_left_txt_tag.setVisibility(View.GONE);
                    holder.preview_left_txt.setVisibility(View.GONE);
                    holder.preview_left_photo.setVisibility(View.VISIBLE);
                    holder.preview_left_voice_layout.setVisibility(View.GONE);
                    holder.preview_left_redpacket_layout.setVisibility(View.GONE);
                    holder.preview_left_transfer_layout.setVisibility(View.GONE);
                    holder.preview_left_times.setVisibility(View.GONE);
                    holder.left_redpacket_tag_layout.setVisibility(View.GONE);


                    Picasso.with(context).load(new File(list.get(position).get("bitmap").toString())).centerCrop().resize(250,400).into(holder.preview_left_photo);
                    //holder.preview_left_photo.setImageBitmap(BitmapFactory.decodeFile(list.get(position).get("bitmap").toString()));
                }


                break;
            case "voice":
                if (sendtype.equals("m")){
                    //right

                    holder.preview_txt.setVisibility(View.GONE);
                    holder.preview_photo.setVisibility(View.GONE);
                    holder.preview_voice_layout.setVisibility(View.VISIBLE);
                    holder.preview_redpacket_layout.setVisibility(View.GONE);
                    holder.preview_transfer_layout.setVisibility(View.GONE);
                    holder.preview_right_times.setVisibility(View.VISIBLE);
                    holder.right_redpacket_tag_layout.setVisibility(View.GONE);

                    holder.preview_right_times.setText(list.get(position).get("listenlength").toString()+"\"");

                    int i=Integer.valueOf(list.get(position).get("listenlength").toString());
                    if (i==1||i==2){
                        holder.preview_voice_tv.getLayoutParams().width=holder.preview_voice_tv.getMinWidth();
                    }else{
                        holder.preview_voice_tv.getLayoutParams().width=holder.preview_voice_tv.getMaxWidth()/60*i+holder.preview_voice_tv.getMinWidth();
                    }

                    boolean tag= Boolean.parseBoolean(list.get(position).get("isselectlisten").toString());
                    if (tag){
                        holder.preview_txt_tag.setVisibility(View.GONE);
                    }else{
                        holder.preview_txt_tag.setVisibility(View.VISIBLE);
                    }

                }else if(sendtype.equals("h")){
                    //left
                    holder.preview_left_txt_tag.setVisibility(View.GONE);
                    holder.preview_left_txt.setVisibility(View.GONE);
                    holder.preview_left_photo.setVisibility(View.GONE);
                    holder.preview_left_voice_layout.setVisibility(View.VISIBLE);
                    holder.preview_left_redpacket_layout.setVisibility(View.GONE);
                    holder.preview_left_transfer_layout.setVisibility(View.GONE);
                    holder.preview_left_times.setVisibility(View.VISIBLE);
                    holder.left_redpacket_tag_layout.setVisibility(View.GONE);


                    holder.preview_left_times.setText(list.get(position).get("listenlength").toString()+"\"");

                    int i=Integer.valueOf(list.get(position).get("listenlength").toString());

                    if (i==1||i==2){
                        holder.preview_left_voice_tv.getLayoutParams().width=holder.preview_voice_tv.getMinWidth();
                    }else{
                        holder.preview_left_voice_tv.getLayoutParams().width=holder.preview_voice_tv.getMaxWidth()/60*i+holder.preview_voice_tv.getMinWidth();
                    }

                    //holder.preview_left_voice_tv.getLayoutParams().width=holder.preview_left_voice_tv.getMaxWidth()/60*i+holder.preview_left_voice_tv.getMinWidth();

                    boolean tag= Boolean.parseBoolean(list.get(position).get("isselectlisten").toString());
                    if (tag){
                        holder.preview_left_txt_tag.setVisibility(View.GONE);
                    }else{
                        holder.preview_left_txt_tag.setVisibility(View.VISIBLE);
                    }
                }

                break;
            case "redpacket":
                boolean tag= Boolean.parseBoolean(list.get(position).get("isselectlisten").toString());

                if (sendtype.equals("m")){
                    //right
                    holder.preview_txt_tag.setVisibility(View.GONE);
                    holder.preview_txt.setVisibility(View.GONE);
                    holder.preview_photo.setVisibility(View.GONE);
                    holder.preview_voice_layout.setVisibility(View.GONE);
                    holder.preview_redpacket_layout.setVisibility(View.VISIBLE);
                    holder.preview_transfer_layout.setVisibility(View.GONE);
                    holder.preview_right_times.setVisibility(View.GONE);
                    holder.preview_wxhb_tv.setText(list.get(position).get("bcontext").toString());
                    holder.preview_wxhb_sub_tv.setText("查看红包");
                    if (tag){
                        String name=list.get(position).get("hname").toString();
                        holder.right_redpacket_tag_layout.setVisibility(View.VISIBLE);
                        holder.right_text_select_tag.setText(name+context.getString(R.string.lingqulnide));

                    }else{
                        holder.right_redpacket_tag_layout.setVisibility(View.GONE);
                    }


                }else if(sendtype.equals("h")){
                    //left
                    holder.preview_left_txt_tag.setVisibility(View.GONE);
                    holder.preview_left_txt.setVisibility(View.GONE);
                    holder.preview_left_photo.setVisibility(View.GONE);
                    holder.preview_left_voice_layout.setVisibility(View.GONE);
                    holder.preview_left_redpacket_layout.setVisibility(View.VISIBLE);
                    holder.preview_left_transfer_layout.setVisibility(View.GONE);
                    holder.preview_left_times.setVisibility(View.GONE);
                    holder.preview_left_wxhb_sub_tv.setText(R.string.lingquhongbao);
                    if (tag){
                        String name=list.get(position).get("hname").toString();
                        holder.left_redpacket_tag_layout.setVisibility(View.VISIBLE);
                        holder.left_text_select_tag.setText(context.getString(R.string.nilingqul)+name+context.getString(R.string.de));
                    }else{
                        holder.right_redpacket_tag_layout.setVisibility(View.GONE);
                    }

                }


                break;
            case "transfer":

                boolean select= Boolean.parseBoolean(list.get(position).get("isselectlisten").toString());
                if (sendtype.equals("m")){
                    //right
                    holder.preview_txt_tag.setVisibility(View.GONE);
                    holder.preview_txt.setVisibility(View.GONE);
                    holder.preview_photo.setVisibility(View.GONE);
                    holder.preview_voice_layout.setVisibility(View.GONE);
                    holder.preview_redpacket_layout.setVisibility(View.GONE);
                    holder.preview_transfer_layout.setVisibility(View.VISIBLE);
                    holder.preview_right_times.setVisibility(View.GONE);
                    holder.right_redpacket_tag_layout.setVisibility(View.GONE);
                    String money=list.get(position).get("money").toString();
                    if (money.indexOf(".")!=-1){
                        holder.preview_reansfer_money.setText(context.getString(R.string.renminbi)+list.get(position).get("money").toString());
                    }else{
                        holder.preview_reansfer_money.setText(context.getString(R.string.renminbi)+list.get(position).get("money").toString()+".00");
                    }
                    if (select){
                        holder.preview_iocn_transfer.setImageResource(R.mipmap.iconshouqian);
                        holder.preview_wxzz_tv.setText(R.string.yilingqu);
                    }else{
                        String n=list.get(position).get("hname").toString();
                        holder.preview_iocn_transfer.setImageResource(R.mipmap.icon_zhuangzhan);
                        holder.preview_wxzz_tv.setText(context.getString(R.string.zhuanzhanggei)+n);
                    }

                }else if(sendtype.equals("h")){
                    //left
                    holder.preview_left_txt_tag.setVisibility(View.GONE);
                    holder.preview_left_txt.setVisibility(View.GONE);
                    holder.preview_left_photo.setVisibility(View.GONE);
                    holder.preview_left_voice_layout.setVisibility(View.GONE);
                    holder.preview_left_redpacket_layout.setVisibility(View.GONE);
                    holder.preview_left_transfer_layout.setVisibility(View.VISIBLE);
                    holder.preview_left_times.setVisibility(View.GONE);
                    holder.left_redpacket_tag_layout.setVisibility(View.GONE);

                    String money=list.get(position).get("money").toString();
                    holder.preview_left_reansfer_money.setText(context.getString(R.string.renminbi)+StaticMethod.keepTwoDecimalNo(money));

                    if (select){
                        holder.preview_left_iocn_transfer.setImageResource(R.mipmap.iconshouqian);
                        holder.preview_left_wxzz_tv.setText(context.getString(R.string.yilingqu));
                    }else{
                        holder.preview_left_iocn_transfer.setImageResource(R.mipmap.icon_zhuangzhan);
                        holder.preview_left_wxzz_tv.setText(R.string.zhuanzhanggeini);
                    }
                }

                break;
        }





        return view;
    }



    class ViewHolder{
        private TextView preview_time;
        //right
        private LinearLayout right_preview_layout;
        private ImageView preview_txt_tag;
        private RelativeLayout preview_context_layout;
        private TextView preview_txt;
        private BubbleImageView preview_photo;
        private RelativeLayout preview_voice_layout;
        private TextView preview_voice_tv;
        private RelativeLayout preview_redpacket_layout;
        private RelativeLayout preview_transfer_layout;
        private TextView preview_reansfer_money;
        private ImageView preview_txt_icon;
        private TextView preview_right_times;
        private LinearLayout right_redpacket_tag_layout;
        private TextView right_text_select_tag;
        private TextView preview_wxhb_tv;
        private TextView preview_wxhb_sub_tv;
        private ImageView preview_iocn_transfer;
        private TextView preview_wxzz_tv;
        //left
        private LinearLayout left_preview_layout;
        private ImageView preview_left_txt_tag;
        private RelativeLayout preview_left_context_layout;
        private TextView preview_left_txt;
        private BubbleImageView preview_left_photo;
        private RelativeLayout preview_left_voice_layout;
        private TextView preview_left_voice_tv;
        private RelativeLayout preview_left_redpacket_layout;
        private RelativeLayout preview_left_transfer_layout;
        private TextView preview_left_reansfer_money;
        private ImageView preview_left_txt_icon;
        private TextView preview_left_times;
        private LinearLayout left_redpacket_tag_layout;
        private TextView left_text_select_tag;
        private TextView preview_left_wxhb_tv;
        private TextView preview_left_wxhb_sub_tv;
        private ImageView preview_left_iocn_transfer;
        private TextView preview_left_wxzz_tv;


        private RelativeLayout talk_preview_layout;
        public ViewHolder(View view){
            //right
            preview_time=(TextView)view.findViewById(R.id.preview_time);
            right_preview_layout=(LinearLayout)view.findViewById(R.id.right_preview_layout);
            preview_txt_tag=(ImageView)view.findViewById(R.id.preview_txt_tag);
            preview_context_layout=(RelativeLayout)view.findViewById(R.id.preview_context_layout);
            preview_txt=(TextView)view.findViewById(R.id.preview_txt);
            preview_photo=(BubbleImageView)view.findViewById(R.id.preview_photo);
            preview_voice_layout=(RelativeLayout)view.findViewById(R.id.preview_voice_layout);
            preview_voice_tv=(TextView)view.findViewById(R.id.preview_voice_tv);
            preview_redpacket_layout=(RelativeLayout)view.findViewById(R.id.preview_redpacket_layout);
            preview_transfer_layout=(RelativeLayout)view.findViewById(R.id.preview_transfer_layout);
            preview_reansfer_money=(TextView)view.findViewById(R.id.preview_reansfer_money);
            preview_txt_icon=(ImageView)view.findViewById(R.id.preview_txt_icon);
            preview_right_times=(TextView) view.findViewById(R.id.preview_right_times);
            right_redpacket_tag_layout=(LinearLayout)view.findViewById(R.id.right_redpacket_tag_layout);
            right_text_select_tag=(TextView)view.findViewById(R.id.right_text_select_tag);
            preview_wxhb_tv=(TextView)view.findViewById(R.id.preview_wxhb_tv);
            preview_wxhb_sub_tv=(TextView)view.findViewById(R.id.preview_wxhb_sub_tv);
            preview_iocn_transfer=(ImageView) view.findViewById(R.id.preview_iocn_transfer);
            preview_wxzz_tv=(TextView)view.findViewById(R.id.preview_wxzz_tv);
            //left
            left_preview_layout=(LinearLayout)view.findViewById(R.id.left_preview_layout);
            preview_left_txt_tag=(ImageView)view.findViewById(R.id.preview_left_txt_tag);
            preview_left_context_layout=(RelativeLayout)view.findViewById(R.id.preview_left_context_layout);
            preview_left_txt=(TextView)view.findViewById(R.id.preview_left_txt);
            preview_left_photo=(BubbleImageView)view.findViewById(R.id.preview_left_photo);
            preview_left_voice_layout=(RelativeLayout)view.findViewById(R.id.preview_left_voice_layout);
            preview_left_voice_tv=(TextView)view.findViewById(R.id.preview_left_voice_tv);
            preview_left_redpacket_layout=(RelativeLayout)view.findViewById(R.id.preview_left_redpacket_layout);
            preview_left_transfer_layout=(RelativeLayout)view.findViewById(R.id.preview_left_transfer_layout);
            preview_left_reansfer_money=(TextView)view.findViewById(R.id.preview_left_reansfer_money);
            preview_left_txt_icon=(ImageView)view.findViewById(R.id.preview_left_txt_icon);
            preview_left_times=(TextView)view.findViewById(R.id.preview_left_times);
            left_redpacket_tag_layout=(LinearLayout)view.findViewById(R.id.left_redpacket_tag_layout);
            left_text_select_tag=(TextView)view.findViewById(R.id.left_text_select_tag);
            preview_left_wxhb_tv=(TextView)view.findViewById(R.id.preview_left_wxhb_tv);
            preview_left_wxhb_sub_tv=(TextView)view.findViewById(R.id.preview_left_wxhb_sub_tv);
            preview_left_iocn_transfer=(ImageView)view.findViewById(R.id.preview_left_iocn_transfer);
            preview_left_wxzz_tv=(TextView)view.findViewById(R.id.preview_left_wxzz_tv);

            talk_preview_layout=(RelativeLayout)view.findViewById(R.id.talk_preview_layout);
        }
    }
}
