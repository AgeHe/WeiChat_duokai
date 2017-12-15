package com.zhushou.weichat.abs.ui;

import android.app.Activity;

import com.zhushou.weichat.abs.BasePresenter;

import org.jdeferred.android.AndroidDeferredManager;

/**
 * Created by Administrator on 2017/1/10.
 */

public class VMyActivity<T extends BasePresenter> extends Activity {

    protected T mPresenter;

    public T getPresenter() {
        return mPresenter;
    }

    public void setPresenter(T presenter) {
        this.mPresenter = presenter;
    }

    protected AndroidDeferredManager defer() {
        return VUiKit.defer();
    }

    public void finishActivity() {
        Activity activity = this;
        if (activity != null) {
            activity.finish();
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

    public void destroy() {
        finishActivity();
    }



}
