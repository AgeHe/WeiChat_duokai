package com.zhushou.weichat.uinew.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhushou.weichat.R;
import com.zhushou.weichat.base.BaseFragment;

/**
 * Created by Administrator on 2017/11/27.
 */

public class NewsFragment extends BaseFragment {
    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    protected View init(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news,container,false);
    }
}
