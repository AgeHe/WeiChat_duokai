package com.zhushou.weichat.ui.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.ui.models.AppModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class InstallAppsAdapter extends PagerAdapter<AppModel> {


    public InstallAppsAdapter(Context context) {
        super(context,  new ArrayList<AppModel>(6));
    }

    public void setModels(List<AppModel> models){
        this.mList = models;
    }

    @Override
    public int getItemLayoutId(int position, AppModel appModel) {
        return R.layout.item_launcher_app;
    }

    @Override
    public void onBindView(View view, AppModel appModel) {
        ImageView iconView = (ImageView) view.findViewById(R.id.item_app_icon);
        TextView nameView = (TextView) view.findViewById(R.id.item_app_name);
        iconView.setImageDrawable(appModel.icon);
        nameView.setText(appModel.name);
    }
}
