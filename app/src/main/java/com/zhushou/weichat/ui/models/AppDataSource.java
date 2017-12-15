package com.zhushou.weichat.ui.models;

import android.content.Context;
import android.content.pm.PackageInfo;

import com.zhushou.weichat.ui.ListAppContract;

import org.jdeferred.Promise;

import java.util.List;

/**
 * @author Lody
 * @version 1.0
 */
public interface AppDataSource {

	/**
	 * @return All the Applications we Virtual.
	 */
//	Promise<List<AppModel>, Throwable, Void> getVirtualApps(ListAppContract.StartCheck checkCallback,ListAppContract.ListAppPresenter appPresenter);

    Promise<List<AppModel>, Throwable, Void> getVirtualApps(ListAppContract.StartCheck checkCallback);

	/**
	 * @param context
	 *            Context
	 * @return All the Applications we Installed.
	 */
	Promise<List<AppModel>, Throwable, Void> getInstalledApps(Context context);

	Promise<List<AppModel>, Throwable, Void> getSdCardApps(Context context);

	List<AppModel> getApps(Context context);
	List<AppModel> getVirtuaAppList();
	void deleteApp(AppModel appModel);

	void chackApps(List<AppModel> listApmodel);

	void installedWXAppList(List<AppModel> models);

	void addVirtualApp(AppModel app) throws Throwable;

	void removeVirtualApp(AppModel app) throws Throwable;

	Promise<AppModel, Throwable ,Void> wxCodeUpdateToAppModel(List<PackageInfo> pkgList);

	void clearWxAppModel();

}
