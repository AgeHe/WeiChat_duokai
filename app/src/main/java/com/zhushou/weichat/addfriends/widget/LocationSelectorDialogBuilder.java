package com.zhushou.weichat.addfriends.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.zhushou.weichat.R;
import com.zhushou.weichat.addfriends.animation.Effectstype;
import com.zhushou.weichat.addfriends.utils.DateUtils;


public class LocationSelectorDialogBuilder extends NiftyDialogBuilder implements
		View.OnClickListener {

	private Context context;
	private RelativeLayout rlCustomLayout;
	private AreasWheel areasWheel;
	private OnSaveLocationLister saveLocationLister;
	private static LocationSelectorDialogBuilder instance;
	private static  int mOrientation=1;

	public interface OnSaveLocationLister {
		abstract void onSaveLocation(String location, String provinceId, String cityId);
	}

	public LocationSelectorDialogBuilder(Context context, int theme) {
		super(context);
		this.context = context;
		initDialog();
	}

	public LocationSelectorDialogBuilder(Context context) {
		super(context);
		this.context = context;
		initDialog();
	}

	public static LocationSelectorDialogBuilder getInstance(Context context){

        int ort=context.getResources().getConfiguration().orientation;
        if (mOrientation!=ort){
            mOrientation=ort;
            instance=null;
        }

        if (instance == null||((Activity) context).isFinishing()) {
            synchronized (LocationSelectorDialogBuilder.class) {
                if (instance == null) {
                    instance = new LocationSelectorDialogBuilder(context,R.style.dialog_untran);
                }
            }
        }
        return instance;

    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setBackgroundDrawableResource(R.drawable.edit_dialog_coner);
	}
	
	private void initDialog() {
		rlCustomLayout = (RelativeLayout) LayoutInflater.from(context).inflate(
				R.layout.location_selector_dialog_layout, null);
		areasWheel = (AreasWheel) rlCustomLayout.findViewById(R.id.aw_location_selector_wheel);
		setDialogProperties();
	}

	private void setDialogProperties() {
		int width = DateUtils.getScreenWidth(context) * 3 / 4;
		this.withDialogWindows(width, LayoutParams.WRAP_CONTENT)
				.withTitleColor("#000000").withTitle("选择地区")
				.setDialogClick(this).withEffect(Effectstype.Slit)
				.withPreviousText("取消").withPreviousTextColor("#17c782")
				.withDuration(200).setPreviousLayoutClick(this)
				.withNextText("保存").withNextTextColor("#17c782")
				.withMessageMiss(View.GONE).withNextTextColor("#17c782")
				.setNextLayoutClick(this)
				.setCustomView(rlCustomLayout, context);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fl_dialog_title_previous:
			dismiss();
			break;
		case R.id.fl_dialog_title_next:
			if (null != saveLocationLister) {
				saveLocationLister.onSaveLocation(areasWheel.getArea(), areasWheel.getProvinceId(), areasWheel.getCityId());
			}
			dismiss();
			break;
		}
	}
	/**
	 * 设置点击保存的监听
	 * @param saveLocationLister
	 */
	public void setOnSaveLocationLister(OnSaveLocationLister saveLocationLister) {
		this.saveLocationLister = saveLocationLister;
	}
	@Override
	public void dismiss() {
		super.dismiss();
		instance = null;
	}
}
