package com.zhushou.weichat.screenshot.roleIndex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhushou.weichat.R;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/5/24.
 */

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String,String>> list;
    private ViewHolder viewHolder;
    private editItem click;

    public ListViewAdapter(Context context,editItem click) {
        this.context = context;
        this.click=click;
    }

    public void setList(List<Map<String,String>> list){
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
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        if (list.get(position).get("name").length() == 1)// 如果是字母索引
            return false;// 表示不能点击
        return super.isEnabled(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String item = list.get(position).get("name");
        if (convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_indexable_index_item,
                    null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }

        if (!list.get(position).get("image").isEmpty()) {
            viewHolder.itemTv.setVisibility(View.GONE);
            viewHolder.txl_layout.setVisibility(View.VISIBLE);
            viewHolder.indexTv.setText(list.get(position).get("name"));
            Picasso.with(context).load(new File(list.get(position).get("image"))).resize(100,100).into(viewHolder.indexable_iv);
            //viewHolder.indexable_iv.setImageBitmap(BitmapFactory.decodeFile(list.get(position).get("image")));
        } else {
            viewHolder.itemTv.setVisibility(View.VISIBLE);
            viewHolder.txl_layout.setVisibility(View.GONE);
            viewHolder.itemTv.setText(list.get(position).get("name"));
        }
        viewHolder.txl_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int j=0;
                for (int i=0;i<position;i++){
                    if (list.get(i).get("name").length()<2&&list.get(i).get("image").isEmpty()){
                        j++;
                    }
                }
                click.selectitem(position,position-j);
            }
        });
        viewHolder.txl_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                int j=0;
                for (int i=0;i<position;i++){
                    if (list.get(i).get("name").length()<2&&list.get(i).get("image").isEmpty()){
                        j++;
                    }
                }
                click.edititem(position,position-j);

                return false;
            }
        });

        return convertView;
    }

    private class ViewHolder {
        private TextView indexTv;
        private TextView itemTv;
        private ImageView indexable_iv;
        private LinearLayout txl_layout;

        public ViewHolder(View view){
            indexTv = (TextView) view.findViewById(R.id.indexTv);
            itemTv = (TextView) view.findViewById(R.id.itemTv);
            indexable_iv=(ImageView)view.findViewById(R.id.indexable_iv);
            txl_layout=(LinearLayout)view.findViewById(R.id.txl_layout);
        }
    }
    public interface editItem{
        void edititem(int position,int locationp);
        void selectitem(int position,int locationp);
    }

}

