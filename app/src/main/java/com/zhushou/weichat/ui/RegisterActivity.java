package com.zhushou.weichat.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhushou.weichat.R;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.utils.OkHttpUtil;

import java.util.HashMap;
import java.util.regex.Pattern;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Administrator on 2017/11/28.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    private Context mContext;
    private TextView  register, login;
    private EditText sjh, mm,cfmm;
    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void initView() {
        mContext = this;
        back = (ImageView) findViewById(R.id.iv_back);
        register = (TextView) findViewById(R.id.register);
        login = (TextView) findViewById(R.id.login);
        sjh = (EditText) findViewById(R.id.sjh);
        mm = (EditText) findViewById(R.id.mm);
        cfmm = (EditText) findViewById(R.id.cfmm);
        back.setOnClickListener(this);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.login:
                finish();
                break;
            case R.id.register:
               check();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static boolean isMobile(String mobile) {
        return Pattern.matches("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$", mobile);
    }

    private void check(){
        String sjhStr = sjh.getText().toString().trim();
        if(!isMobile(sjhStr)){
            Toast.makeText(mContext,"请输入正确的手机号码!",Toast.LENGTH_LONG).show();
        }else{
            String mmStr = mm.getText().toString().trim();
            if(mmStr.length()<6){
                Toast.makeText(mContext,"密码不能小于6位!",Toast.LENGTH_LONG).show();
            }else{
                String mmagainStr = cfmm.getText().toString().trim();
                if(mmagainStr.length()<6){
                    Toast.makeText(mContext,"重复密码不能小于6位!",Toast.LENGTH_LONG).show();
                }else{
                    if(!mmStr.equals(mmagainStr)){
                        Toast.makeText(mContext,"两次输入的密码不一致",Toast.LENGTH_LONG).show();
                    }else{//注册
                        register(sjhStr,mmStr);
                    }
                }
            }
        }

    }


    private void register(String sjh,String password) {
        //数据源
        Flowable flowable = Flowable.create(new FlowableOnSubscribe<OkHttpResult>() {
            @Override
            public void subscribe(FlowableEmitter<OkHttpResult> e) throws Exception {
                OkHttpUtil okHttpUtil = OkHttpUtil.getInstance();
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("",sjh);
                map.put("",password);
                OkHttpResult result = okHttpUtil.requestGetBySyn("", null, null);
                e.onNext(result);
                e.onComplete();
            }
        }, BackpressureStrategy.DROP).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()); //指定背压处理策略
        //观察者
        ResourceSubscriber resultSubscriber = new ResourceSubscriber<OkHttpResult>() {
            @Override
            public void onNext(OkHttpResult result) {
            }

            @Override
            public void onError(Throwable t) {
                this.dispose();
            }

            @Override
            public void onComplete() {
                this.dispose();
            }
        };
        //订阅注册
        flowable.subscribe(resultSubscriber);
    }


}



