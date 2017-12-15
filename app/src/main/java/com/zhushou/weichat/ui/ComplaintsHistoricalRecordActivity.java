package com.zhushou.weichat.ui;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.bean.ComplaintsArrayInfo;
import com.zhushou.weichat.bean.LoginMsgInfo;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.ui.adapter.ComplaintsHisarrayAdapter;
import com.zhushou.weichat.utils.LoginSP;
import com.zhushou.weichat.utils.MyToast;
import com.zhushou.weichat.utils.RandomGUID;
import com.zhushou.weichat.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/9/29.
 */

public class ComplaintsHistoricalRecordActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "ComplaintsHistoricalRecordActivity";

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        setContentView(R.layout.activity_complaints_historicalarray);
    }

    RecyclerView rlv_complaints_hisarray;
    ComplaintsHisarrayAdapter complaintsAdapter;
    SwipeRefreshLayout srl_refresh;

    @Override
    public void initView() {
        rlv_complaints_hisarray = (RecyclerView) findViewById(R.id.rlv_complaints_hisarray);
        srl_refresh = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        LinearLayoutManager lm = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false);
        rlv_complaints_hisarray.setLayoutManager(lm);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        srl_refresh.setColorSchemeResources(R.color.colorPrimary);
        srl_refresh.setOnRefreshListener(this);

    }

    RandomGUID randomGUID;
    ProgressDialog progressDialog;
    LoginMsgInfo loginMsgInfo = null;
    @Override
    public void initData() {

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setMessage("请稍等...");

        complaintsAdapter = new ComplaintsHisarrayAdapter(this);
        randomGUID = new RandomGUID(2,mHandler);
        rlv_complaints_hisarray.setAdapter(complaintsAdapter);

        try {
            loginMsgInfo = LoginSP.getLoginMsg(mContext);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private Handler mHandler = new Handler(){
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    OkHttpResult okHttpResult = (OkHttpResult) msg.obj;
                    if (okHttpResult.isSuccess){
                        List<ComplaintsArrayInfo> listArray = new ArrayList<>();
                        try {
                            JSONArray ja = new JSONArray(okHttpResult.msg);
                            for (int i=0;i<ja.length();i++){
                                ComplaintsArrayInfo info = new ComplaintsArrayInfo();
                               JSONObject jo =  ja.getJSONObject(i);
                                info.setDate(jo.getString("date"));
                                info.setId(jo.getString("id"));
                                info.setResponse(jo.getString("reply"));
                                info.setStatus(jo.getString("stats"));
                                listArray.add(info);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        complaintsAdapter.setArrayData(listArray);
                    }else{
                        MyToast.mCenterToast("获取列表失败！",mContext);
                    }
                    if (progressDialog!=null&&progressDialog.isShowing())
                        progressDialog.dismiss();
                    if (srl_refresh.isRefreshing())
                        srl_refresh.setRefreshing(false);
                    break;
                case 2:

                    if (loginMsgInfo==null){
                        ToastUtil.centerToast(mContext,"请先登录");
                        return;
                    }
                    progressDialog.show();
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("userid", loginMsgInfo.getId());
                    ComplaintsActivity.sendComplaintsMessage(hashMap,"DB_user_querycomplain",0,mHandler);

                    break;
            }
        }
    };

    @Override
    public void onRefresh() {
        if (loginMsgInfo==null){
            ToastUtil.centerToast(mContext,"请先登录");
            return;
        }
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("userid", loginMsgInfo.getId());
        ComplaintsActivity.sendComplaintsMessage(hashMap,"DB_user_querycomplain",0,mHandler);
    }

}
