package com.zhushou.weichat.uinew;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhushou.weichat.R;
import com.zhushou.weichat.base.BaseActivity;
import com.zhushou.weichat.uinew.adapter.ExceptionOrderRVAdapter;
import com.zhushou.weichat.utils.database.OperationDB;


/**
 * Created by Administrator on 2017/11/2.
 */

public class ExceptionOrdersActivity extends BaseActivity implements View.OnClickListener {


    ExceptionOrderRVAdapter dataAdapter;
    OperationDB operationDB;
    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_exception_order);
    }

    private RecyclerView rv_excaption_history;
    @Override
    public void initView() {
        rv_excaption_history = (RecyclerView) findViewById(R.id.rv_excaption_history);
    }

    @Override
    public void initData() {
        operationDB = new OperationDB(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_excaption_history.setLayoutManager(llm);
        dataAdapter = new ExceptionOrderRVAdapter(this);
        rv_excaption_history.setAdapter(dataAdapter);
        dataAdapter.setAdapterData(operationDB.selectOrderTable());
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.tv_back:
                finish();
                break;
            case R.id.rv_excaption_history:

                break;

        }

    }
}
