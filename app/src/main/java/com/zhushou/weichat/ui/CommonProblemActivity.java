package com.zhushou.weichat.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.bean.CommonProblemInfo;
import com.zhushou.weichat.ui.adapter.CommonProblemRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/6.
 * 常见问题界面
 */
public class CommonProblemActivity extends BaseMainActivity {

    RecyclerView rcv_problem;
    List<CommonProblemInfo> listData;
    CommonProblemRecyclerViewAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_problem);
        findView();
    }
    private void findView() {
        listData = new ArrayList<>();
        listData.clear();
        setListData();
        rcv_problem = (RecyclerView) findViewById(R.id.rcv_problem);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rcv_problem.setLayoutManager(llm);
        mAdapter = new CommonProblemRecyclerViewAdapter(this);
        rcv_problem.setAdapter(mAdapter);
        findViewById(R.id.iv_back).setOnClickListener(view -> finish());
        mAdapter.setListData(listData);
    }

    private void setListData(){
        for (int i=0;i<21;i++){
            CommonProblemInfo info = new CommonProblemInfo();
            try {
                info.setTitle(getResources().getString(R.string.class.getField("cp_title"+i).getInt(null)));
                info.setContent(getResources().getString(R.string.class.getField("cp_"+i).getInt(null)));
            }catch(IllegalAccessException e){
                e.printStackTrace();
            }catch(NoSuchFieldException e){
                e.printStackTrace();
            }
            info.setOpen(false);
            listData.add(info);
        }
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
