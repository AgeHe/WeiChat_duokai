package com.zhushou.weichat.auxiliary.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.info.HbRecordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/17.
 */

public class RecordListAdapter extends BaseAdapter {

    private List<HbRecordInfo> listData = new ArrayList<>();
    private Context mContext;
    private LayoutInflater inflater;

    public RecordListAdapter(Context mContext){
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);
    }

    public void stAdapterDataList(List<HbRecordInfo> listData){
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public HbRecordInfo getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Holder holder = null;
        if (view==null){
            holder =new Holder();
            view = inflater.inflate(R.layout.item_hb_record_list,null);
            holder.tv_date_time = (TextView) view.findViewById(R.id.tv_date_time);
            holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
            holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
            view.setTag(holder);
        }else{
            holder = (Holder) view.getTag();
        }
        holder.tv_name.setText(listData.get(i).getBoss());
        holder.tv_date_time.setText(listData.get(i).getHbTime());
        holder.tv_money.setText(listData.get(i).getMoney());
        return view;
    }


     class Holder{
        TextView tv_name;
        TextView tv_money;
        TextView tv_date_time;
    }
}
