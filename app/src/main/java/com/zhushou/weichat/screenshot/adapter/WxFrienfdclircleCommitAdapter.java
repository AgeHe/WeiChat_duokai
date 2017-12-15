package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;

import static com.zhushou.weichat.screenshot.Utils.StaticMethod.isNumeric;

/**
 * Created by zhanglinkai on 2017/4/12.
 * 功能:
 */

public class WxFrienfdclircleCommitAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String,String>> list;
    private commitClear click;
    public WxFrienfdclircleCommitAdapter(Context context,List<Map<String,String>> list,commitClear click){
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
            view= LayoutInflater.from(context).inflate(R.layout.layout_wxfriendcircle_preview_commit_item,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{
            holder=(ViewHolder)view.getTag();
        }
        String type=list.get(position).get("type");
        String from=list.get(position).get("from");
        String to=list.get(position).get("to");
        String con=list.get(position).get("con");

        if (type.equals("from")){
            holder.wxfriendcircle_preview_commit_item_tv.setText(from+context.getString(R.string.maohao)+con);
        }else if (type.equals("to")){
            holder.wxfriendcircle_preview_commit_item_tv.setText(from+context.getString(R.string.huifu)+to+context.getString(R.string.maohao)+con);
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(holder.wxfriendcircle_preview_commit_item_tv.getText().toString());
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(context.getResources().getColor(R.color.wxblue));
        ForegroundColorSpan blueSpan2 = new ForegroundColorSpan(context.getResources().getColor(R.color.wxblue));
        ForegroundColorSpan blueSpan3 = new ForegroundColorSpan(context.getResources().getColor(R.color.wxpyqcommit));
        if (type.equals("from")){
            builder.setSpan(blueSpan, 0, from.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (isNumeric(con)&&con.length()>7){
                Log.e("ttt", "toString: "+holder.wxfriendcircle_preview_commit_item_tv.getText().toString() );
                Log.e("ttt", "length: "+holder.wxfriendcircle_preview_commit_item_tv.getText().length() );
                Log.e("ttt", "from.length(): "+from.length());
                Log.e("ttt", "con.length(): "+con.length());

                builder.setSpan(blueSpan3, from.length()+1, holder.wxfriendcircle_preview_commit_item_tv.getText().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }else if (type.equals("to")){
            String t=from+context.getString(R.string.huifu);
            builder.setSpan(blueSpan, 0, from.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(blueSpan2, t.length(), t.length()+to.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (isNumeric(con)&&con.length()>7){
                builder.setSpan(blueSpan3, t.length()+to.length()+1, holder.wxfriendcircle_preview_commit_item_tv.getText().length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
        }

        holder.wxfriendcircle_preview_commit_item_tv.setText(builder);

        holder.wxfriendcircle_preview_commit_item_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.itemclick(position);
            }
        });

        return view;
    }
    class ViewHolder{
        private TextView wxfriendcircle_preview_commit_item_tv;
        public ViewHolder(View view){
            wxfriendcircle_preview_commit_item_tv=(TextView)view.findViewById(R.id.wxfriendcircle_preview_commit_item_tv);
        }
    }
    public interface commitClear{
        void itemclick(int p);
    }
}
