package com.zhushou.weichat.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.ui.adapter.HistoryPayRecyclerViewAdapter;
import com.zhushou.weichat.utils.HistoryPayShareUtil;

/**
 * Created by Administrator on 2017/1/6.
 */

public class PayHistoryActivity extends BaseMainActivity {

    private static final String TAG = "PayHistoryActivity";
    RecyclerView lv_pay_history;
    HistoryPayRecyclerViewAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_history);
        findView();
    }
    private void findView() {
        lv_pay_history = (RecyclerView) findViewById(R.id.lv_pay_history);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        lv_pay_history.setLayoutManager(llm);
        dataAdapter = new HistoryPayRecyclerViewAdapter(this);
        lv_pay_history.setAdapter(dataAdapter);
        dataAdapter.setAdapterData(HistoryPayShareUtil.getHistoryPayList(PayHistoryActivity.this));
//        dataAdapter.setAdapterData(APPUsersShareUtil.getBaoyueMessage(PayHistoryActivity.this));

        findViewById(R.id.iv_back).setOnClickListener(view -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}

