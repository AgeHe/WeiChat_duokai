package com.zhushou.weichat.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.UMengStatisticalKey;
import com.zhushou.weichat.bean.WxFriendMsgInfo;
import com.zhushou.weichat.ui.adapter.WsFriendRecyclerAdapter;
import com.zhushou.weichat.utils.OkHttpUtil;
import com.zhushou.weichat.utils.UMengTJUtils;
import com.zhushou.weichat.utils.WsFriendJsonDataUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/6/15.
 */

public class WsFriendActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private int dataPage = 1;
    public InputInterfac inputInterfac;
    public boolean isLoadingData = false;


    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_ws_friend);
    }

    private SwipeRefreshLayout srl_refresh;
    private RecyclerView rv_recyclerview;
    private LinearLayout ll_input;
    private EditText et_input;
    private TextView tv_input_send;
    private FloatingActionButton fab_floatingAb;

    @Override
    public void initView() {
        srl_refresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        rv_recyclerview = (RecyclerView) findViewById(R.id.rv_recyclerview);
        ll_input = (LinearLayout) findViewById(R.id.ll_input);
        et_input = (EditText) findViewById(R.id.et_input);
        tv_input_send = (TextView) findViewById(R.id.tv_input_send);
        fab_floatingAb = (FloatingActionButton) findViewById(R.id.fab_floatingAb);
    }

    WsFriendRecyclerAdapter wsFriendRecyclerAdapter;
    int inputIndex = 0;

    @Override
    public void initData() {
        tv_input_send.setOnClickListener(this);
        srl_refresh.setOnRefreshListener(this);
        srl_refresh.setColorSchemeResources(R.color.colorAccent);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_recyclerview.setLayoutManager(llm);
        wsFriendRecyclerAdapter = new WsFriendRecyclerAdapter(new WsFriendRecyclerAdapter.StartInput() {
            @Override
            public void startInput(int listIndex) {
                inputIndex = listIndex;
                ll_input.setVisibility(View.VISIBLE);
                et_input.setFocusable(true);
                et_input.setFocusableInTouchMode(true);
                et_input.requestFocus();
                upInputMethodManager();
            }
        });
        inputInterfac = wsFriendRecyclerAdapter.getInputInterFac();
        rv_recyclerview.setAdapter(wsFriendRecyclerAdapter);
        rv_recyclerview.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int itemcount = recyclerView.getAdapter().getItemCount();
                int i = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (i==itemcount-1&&!isLoadingData){
                    dataPage++;
                    requestHttp(dataPage);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });

        findViewById(R.id.iv_back).setOnClickListener(view -> {
            finish();
        });

        srl_refresh.setRefreshing(true);
        requestHttp(dataPage);
    }

    @Override
    public void onRefresh() {
        dataPage = 1;
        requestHttp(dataPage);
    }

    private Handler requestHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    List<WxFriendMsgInfo> listData = WsFriendJsonDataUtil.analyticWsjson((String) msg.obj);
                    if (listData != null) {
                        wsFriendRecyclerAdapter.setListData(listData);
                    }
                    break;
                case 1:
                    List<WxFriendMsgInfo> addlistData = WsFriendJsonDataUtil.analyticWsjson((String) msg.obj);
                    if (addlistData != null) {
                        wsFriendRecyclerAdapter.addListData(addlistData);
                    }
                    break;
            }
            isLoadingData = false;
            srl_refresh.setRefreshing(false);
        }
    };

    private String httpUrl = "http://api.duokai.mxitie.com/cgi/api.ashx";

    public void requestHttp(int page) {
        isLoadingData = true;
        class WxFriendRunnable implements Runnable {
            @Override
            public void run() {
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                HashMap<String, String> map = new HashMap<>();
                map.put("count", "10");
                map.put("page", String.valueOf(page));
                OkHttpResult result = okHttpUtil.requestGetBySyn(httpUrl,"user/tweet/list.json", map);
                Message msg = new Message();
                msg.obj = result.msg;
                if (page==1)
                    msg.what = 0;
                else
                    msg.what =1;
                requestHandler.sendMessage(msg);
            }
        }
        new Thread(new WxFriendRunnable()).start();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_input_send:
                inputInterfac.getInputData(inputIndex,et_input.getText().toString());
                ll_input.setVisibility(View.GONE);
                et_input.setText("");
                et_input.setFocusable(false);
                et_input.setFocusableInTouchMode(false);
                FocusAndInputMethod(false);
                break;
        }
    }

    public interface InputInterfac {
        void getInputData(int id, String input);
    }

    //隐藏或者显示EditText键盘
    private void FocusAndInputMethod(boolean bon) {
        if (bon) {
            InputMethodManager inputManager = (InputMethodManager) et_input.getContext().getSystemService(INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et_input, 0);
        } else {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    //打开输入法
    private void upInputMethodManager(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                FocusAndInputMethod(true);
            }
        }, 200);
    }

    @Override
    protected void onDestroy() {
        String residenceTime = UMengTJUtils.longTime(System.currentTimeMillis()-startActivityTime);
        UMengTJUtils uMengTJUtils = new UMengTJUtils(mContext);
        uMengTJUtils.onEventValuePay(UMengStatisticalKey.friend_residence_time,uMengTJUtils.friendMap(residenceTime),0);
        super.onDestroy();
    }
}
