package com.zhushou.weichat.auxiliary.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.auxiliary.sp.HbSaveDataUtils;
import com.zhushou.weichat.auxiliary.ui.adapter.RecordListAdapter;

/**
 * Created by Administrator on 2017/4/17.
 */

public class HBRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_auxiliary_hb_record);
        hbSaveDataUtils = new HbSaveDataUtils(this);
        recordListAdapter = new RecordListAdapter(this);
    }

    private ListView lv_hb_record;
    private SwipeRefreshLayout srl_refresh_layout;
    private RecordListAdapter recordListAdapter;
    private HbSaveDataUtils hbSaveDataUtils;
    @Override
    public void initView() {
        srl_refresh_layout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh_layout);
        lv_hb_record = (ListView) findViewById(R.id.lv_hb_record);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void initData() {
        recordListAdapter.stAdapterDataList(hbSaveDataUtils.getHbRecord());
        lv_hb_record.setAdapter(recordListAdapter);
        recordListAdapter.notifyDataSetChanged();

        srl_refresh_layout.setColorSchemeResources(R.color.colorAccent,R.color.red_packets_color2,R.color.red_packets_color_scroll_text);
        srl_refresh_layout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        recordListAdapter.stAdapterDataList(hbSaveDataUtils.getHbRecord());
//        lv_hb_record.setAdapter(recordListAdapter);
        recordListAdapter.notifyDataSetChanged();
        srl_refresh_layout.setRefreshing(false);
    }

    @Override
    protected void viewOnClick(View view) {
        super.viewOnClick(view);
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
