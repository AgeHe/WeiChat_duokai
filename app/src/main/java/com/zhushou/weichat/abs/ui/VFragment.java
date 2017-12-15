package com.zhushou.weichat.abs.ui;

import android.app.Activity;
import android.support.v4.app.Fragment;


import com.zhushou.weichat.abs.BasePresenter;

import org.jdeferred.android.AndroidDeferredManager;


/**
 * @author Lody
 */
public class VFragment<T extends BasePresenter> extends Fragment {

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
		Activity activity = getActivity();
		if (activity != null) {
			activity.finish();
		}
	}

	public void destroy() {
		finishActivity();
	}
}
