package com.zhushou.weichat.ui.models;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lody.virtual.client.core.InstallStrategy;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.os.VUserHandle;
import com.lody.virtual.remote.InstalledAppInfo;
import com.zhushou.weichat.abs.ui.VUiKit;
import com.zhushou.weichat.ui.ListAppContract;
import com.zhushou.weichat.uinew.interfac.HomeAppContract;

import org.jdeferred.Promise;

import java.io.File;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


/**
 * @author Lody
 */
public class AppRepository implements AppDataSource {

	private static final Collator COLLATOR = Collator.getInstance(Locale.CHINA);
	private static final String TAG = "AppRepository";
	private static List<String> sdCardScanPaths = new ArrayList<>();

	static {
		String sdCardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		sdCardScanPaths.add(sdCardPath);
		sdCardScanPaths.add(sdCardPath + File.separator + "wandoujia" + File.separator + "app");
		sdCardScanPaths
				.add(sdCardPath + File.separator + "tencent" + File.separator + "tassistant" + File.separator + "apk");
		sdCardScanPaths.add(sdCardPath + File.separator + "BaiduAsa9103056");
		sdCardScanPaths.add(sdCardPath + File.separator + "360Download");
		sdCardScanPaths.add(sdCardPath + File.separator + "pp/downloader");
		sdCardScanPaths.add(sdCardPath + File.separator + "pp/downloader/apk");
		sdCardScanPaths.add(sdCardPath + File.separator + "pp/downloader/silent/apk");
	}

	private Context mContext;
	private ListAppContract.StartCheck checkCallback;
	HomeAppContract.AppPresenter listAppPresenter ;

	public AppRepository(Context context) {
		mContext = context;
	}

	private static boolean isSystemApplication(PackageInfo packageInfo) {
		return (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
	}

	@Override
	public Promise<List<AppModel>, Throwable, Void> getVirtualApps(ListAppContract.StartCheck checkCallback) {
		this.checkCallback = checkCallback;
//		this.listAppPresenter = appPresenter;
		return VUiKit.defer().when(() -> {
			List<InstalledAppInfo> infos = VirtualCore.get().getInstalledApps(0);
			List<AppModel> models = new ArrayList<AppModel>();

			for (InstalledAppInfo info : infos) {
				if (VirtualCore.get().getLaunchIntent(info.packageName, VUserHandle.USER_OWNER) != null) {
					models.add(new AppModel(mContext, info));
				}
			}
			Collections.sort(models, (lhs, rhs) -> COLLATOR.compare(lhs.name, rhs.name));
			return models;
		});
	}

	@Override
	public List<AppModel> getVirtuaAppList() {
		List<InstalledAppInfo> infos = VirtualCore.get().getInstalledApps(0);
		List<AppModel> models = new ArrayList<AppModel>();
		for (InstalledAppInfo info : infos) {
			if (VirtualCore.get().getLaunchIntent(info.packageName, VUserHandle.USER_OWNER) != null) {
				models.add(new AppModel(mContext, info));
			}
		}
		Collections.sort(models, (lhs, rhs) -> COLLATOR.compare(lhs.name, rhs.name));
		return models;
	}



	@Override
	public Promise<List<AppModel>, Throwable, Void> getInstalledApps(Context context) {
		return VUiKit.defer().when(() -> {
			return pkgInfosToAppModels(context, context.getPackageManager().getInstalledPackages(0), true);
		});
	}

	@Override
	public Promise<List<AppModel>, Throwable, Void> getSdCardApps(Context context) {
		return VUiKit.defer().when(() -> {
			return pkgInfosToAppModels(context, findAndParseAPKs(context, sdCardScanPaths), false);
		});
	}

	@Override
	public void chackApps(List<AppModel> listApmodel) {
		Log.i("MainActivity", "chackApps: ");
		if (listApmodel.size()<1){
			checkCallback.isNot();
		}else{
			checkCallback.isOk(listApmodel);
		}
	}


	@Override
	public void installedWXAppList(List<AppModel> models) {
		for (AppModel model : models) {
			if (model.packageName.equals("com.tencent.mm")) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						installedApp(model);
					}
				}).start();
				break;
			}
		}

	}

	/**
	 * @param model
	 */
	private void installedApp(AppModel model) {
		int flags = InstallStrategy.COMPARE_VERSION;
		if (model.fastOpen) {
			flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
		}
		VirtualCore.get().installPackage(model.path, flags);

		InstalledAppInfo info = VirtualCore.get().getInstalledAppInfo(model.packageName, 0);

		if (info != null) {
			model.context = mContext;
			VUiKit.defer().when(() -> {
				try {
					model.loadData(info.getApplicationInfo(VUserHandle.USER_OWNER));
					VirtualCore.get().preOpt(info.packageName);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).done((res) -> {
				myHandler.sendEmptyMessage(0);
			});
		}
	}
	Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 0:
					if (listAppPresenter!=null)
						listAppPresenter.resetModel();
					break;
			}
		}
	};

	public void insTallStrategy(){
		AppModel appModelInfo = null;
		int flags = InstallStrategy.COMPARE_VERSION;
		if (appModelInfo.fastOpen) {
			flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
		}
		VirtualCore.get().installPackage(appModelInfo.path, flags);
	}

	@Override
	public List<AppModel> getApps(Context context) {
		return pkgInfosToAppModels(context, context.getPackageManager().getInstalledPackages(0), true);
	}

	private List<PackageInfo> findAndParseAPKs(Context context, List<String> pathes) {
		List<PackageInfo> pkgs = new ArrayList<>();
		if (pathes == null)
			return pkgs;
		for (String path : pathes) {
			File dir = new File(path);
			if (!dir.exists() || !dir.isDirectory())
				continue;
			for (File f : dir.listFiles()) {
				if (!f.getName().toLowerCase().endsWith(".apk"))
					continue;
				PackageInfo pkgInfo = null;
				try {
					pkgInfo = context.getPackageManager().getPackageArchiveInfo(f.getAbsolutePath(), 0);
					pkgInfo.applicationInfo.sourceDir = f.getAbsolutePath();
					pkgInfo.applicationInfo.publicSourceDir = f.getAbsolutePath();
				} catch (Exception e) {
					// Ignore
				}
				if (pkgInfo != null)
					pkgs.add(pkgInfo);
			}
		}
		return pkgs;
	}

	private List<AppModel> pkgInfosToAppModels(Context context, List<PackageInfo> pkgList, boolean fastOpen) {
		List<AppModel> models = new ArrayList<>(pkgList.size());
		String hostPkg = VirtualCore.get().getHostPkg();
		for (PackageInfo pkg : pkgList) {
			if (hostPkg.equals(pkg.packageName)) {
				continue;
			}
			if (isSystemApplication(pkg)) {
				continue;
			}
			if (VirtualCore.get().isAppInstalled(pkg.packageName)){
				continue;
			}
			AppModel model = new AppModel(context, pkg);
			model.fastOpen = fastOpen;
			models.add(model);
		}
		Collections.sort(models, (lhs, rhs) -> COLLATOR.compare(lhs.name, rhs.name));
		return models;
	}

	/**
	 * 获取微信包名以及版本号
	 * @param pkgList
	 * @return
	 */
	public Promise<AppModel, Throwable, Void> wxCodeUpdateToAppModel(List<PackageInfo> pkgList) {
		AppModel model = new AppModel();
		String hostPkg = VirtualCore.get().getHostPkg();
		for (PackageInfo pkg : pkgList) {
			if (!"com.tencent.mm".equals(pkg.packageName)){
				continue;
			}
			if (hostPkg.equals(pkg.packageName)) {
				continue;
			}
			if (isSystemApplication(pkg)) {
				continue;
			}
			model.versionCode = pkg.versionCode;
			model.packageName = pkg.packageName;
		}
		Log.i(TAG, "wxCodeUpdateToAppModel: ");
		 return VUiKit.defer().when(() -> {return model;});
	}

	@Override
	public void clearWxAppModel() {
		VirtualCore.get().uninstallPackage("com.tencent.mm",0);
	}

	@Override
	public void addVirtualApp(AppModel app) throws Throwable {
//		int flags = InstallStrategy.COMPARE_VERSION;
//		if (app.fastOpen) {
//			flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
//		}
//		VirtualCore.get().installPackage(app.path, flags);

		int flags = InstallStrategy.COMPARE_VERSION;
		if (app.fastOpen) {
			flags |= InstallStrategy.DEPEND_SYSTEM_IF_EXIST;
		}
		VirtualCore.get().installPackage(app.path, flags);
	}

	@Override
	public void removeVirtualApp(AppModel app) throws Throwable {
		VirtualCore.get().uninstallPackage(app.packageName,0);
	}

	@Override
	public void deleteApp(AppModel appModel) {
		VirtualCore.get().uninstallPackage(appModel.packageName,0);
	}

}
