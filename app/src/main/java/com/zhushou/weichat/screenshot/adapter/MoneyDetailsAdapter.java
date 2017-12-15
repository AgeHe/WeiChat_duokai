package com.zhushou.weichat.screenshot.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

/**
 * Created by zhanglinkai on 2017/4/1.
 * 功能:
 */

public class MoneyDetailsAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> list;
    String tag="";
    public MoneyDetailsAdapter(Context context,List<Map<String,String>> list,String tag){
        this.context=context;
        this.list=list;
        this.tag=tag;
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
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view==null){
            view= LayoutInflater.from(context).inflate(R.layout.layout_money_details_list_item,parent,false);
            holder=new ViewHolder(view);
            view.setTag(holder);
        }else{

            holder=(ViewHolder)view.getTag();
        }

        String type=list.get(position).get("type");
        String money=list.get(position).get("money");
        holder.money_details_list_item_context_tv.setText(list.get(position).get("description"));
        holder.money_details_list_item_time.setText(list.get(position).get("time"));
        if (tag.equals("preview")){
            holder.money_details_list_item_context_tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        if (type.equals("disburse")){
            //支出
            holder.money_details_list_item_money.setText("-"+ StaticMethod.keepTwoDecimalNo(money));
            holder.money_details_list_item_money.setTextColor(context.getResources().getColor(R.color.color_txtblack));
        }else if(type.equals("income")){
            //收入
            holder.money_details_list_item_money.setText("+"+StaticMethod.keepTwoDecimalNo(money));
            holder.money_details_list_item_money.setTextColor(context.getResources().getColor(R.color.color_green));
        }
        return view;
    }
    class ViewHolder{
        private TextView money_details_list_item_context_tv;
        private TextView money_details_list_item_time;
        private TextView money_details_list_item_money;
        public ViewHolder(View view){
            money_details_list_item_context_tv=(TextView)view.findViewById(R.id.money_details_list_item_context_tv);
            money_details_list_item_time=(TextView)view.findViewById(R.id.money_details_list_item_time);
            money_details_list_item_money=(TextView)view.findViewById(R.id.money_details_list_item_money);
        }
    }
}
