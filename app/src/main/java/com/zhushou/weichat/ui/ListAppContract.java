package com.zhushou.weichat.ui;


import com.zhushou.weichat.abs.BasePresenter;
import com.zhushou.weichat.abs.BaseView;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.ScreenShotPreview;
import com.zhushou.weichat.bean.UpdateInfo;
import com.zhushou.weichat.ui.models.AppModel;

import java.util.List;


/**
 * @author Lody
 * @version 1.0
 */
public class ListAppContract {
	public static final int SELECT_APP_FROM_SYSTEM = 0;
	public static final int SELECT_APP_FROM_SD_CARD = 1;
	public static final int SET_WEICHAT = 0x1;
	public static final int SET_ALL = 0x2;

	interface ListAppView extends BaseView<ListAppPresenter> {
		void startLoading();
		void loadFinish(List<AppModel> models);
        void loadVirtualApps(List<AppModel> models);
		void loadInstallAllApps(List<AppModel> models);
		void mHideLoadin();
		void addItem(ApkItemInfo info,int changeType);
		void appsinsertVirtuaFail();
		void installedAppList(List<AppModel> models);
		void deleteItem(ApkItemInfo apkItemInfo);
		void startUpdateApp(UpdateInfo updateInfo);
		void netWorkRequestFailure();
		void shareCallBack(String ErrCode);
		void bdHandling(String bdCode);
	}

	public interface ListAppPresenter extends BasePresenter {
		void selectApp(AppModel model);
		void addWeiChatApp(String appName,String price,String date,boolean isFreeTrial);
		void addQQApp(String appName,String price,String date,boolean isFreeTrial);
		void changeItemAppName(AppModel model);
		void deleteItemApp(ApkItemInfo apkItemInfo);
		void deleteVirtualApp(AppModel appModel);
		void addItemToDeskTop(ApkItemInfo apkItemInfo,int position);
		void resetModel();
		void resetVirtualApps();
		void onDestroy();
		void showPreviewDialog(List<ScreenShotPreview> listData,String functionKey);
		void checkWxVersionCode();
		void clearWxModel();
	}
	public interface StartCheck{
		void isOk(List<AppModel> models);
		void isNot();
	}
}
