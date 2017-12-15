package com.zhushou.weichat.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.ipc.VActivityManager;
import com.lody.virtual.remote.InstalledAppInfo;
import com.zhushou.weichat.R;
import com.zhushou.weichat.abs.ui.VUiKit;
import com.zhushou.weichat.ui.models.AppModel;


/**
 * @author Lody
 */

public class LoadingActivity extends AppCompatActivity {

	private static final String PKG_NAME_ARGUMENT = "MODEL_ARGUMENT";
	private static final String KEY_INTENT = "KEY_INTENT";
	private static final String KEY_USER = "KEY_USER";
	private static final String TAG = "LoadingActivity";
	private AppModel appModel;
	private String appName = "";

	public static boolean launch(Context context, AppModel model, int userId,String appName) {
		Intent intent = VirtualCore.get().getLaunchIntent(model.packageName,userId);
		if (intent != null) {
			Intent loadingPageIntent = new Intent(context, LoadingActivity.class);
			loadingPageIntent.putExtra(PKG_NAME_ARGUMENT, model);
			loadingPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			loadingPageIntent.putExtra(KEY_INTENT, intent);
			loadingPageIntent.putExtra(KEY_USER, userId);
			loadingPageIntent.putExtra("name",appName);
			context.startActivity(loadingPageIntent);
		}else{
			VirtualCore.get().installPackageAsUser(userId, model.packageName);
			intent = VirtualCore.get().getLaunchIntent(model.packageName,userId);
			if (intent != null) {
				Intent loadingPageIntent = new Intent(context, LoadingActivity.class);
				loadingPageIntent.putExtra(PKG_NAME_ARGUMENT, model);
				loadingPageIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				loadingPageIntent.putExtra(KEY_INTENT, intent);
				loadingPageIntent.putExtra(KEY_USER, userId);
				loadingPageIntent.putExtra("name",appName);
				context.startActivity(loadingPageIntent);
			}else{
				Toast.makeText(context,"分身打开异常，请到客服界面点击常见问题找到第21条解决",Toast.LENGTH_SHORT).show();
				return false;
			}
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		appModel = getIntent().getParcelableExtra(PKG_NAME_ARGUMENT);
		this.appName = getIntent().getStringExtra("name");
		int userId = getIntent().getIntExtra(KEY_USER, -1);

		VUiKit.defer().when(() -> {
			InstalledAppInfo info  = VirtualCore.get().getInstalledAppInfo(appModel.packageName,0);
			if (info != null) {
				appModel = new AppModel(this, info);
			}
		}).done((res) -> {
			ImageView iconView = (ImageView) findViewById(R.id.app_icon);
			if (iconView != null) {
				iconView.setImageDrawable(appModel.icon);
			}
		});

		TextView nameView = (TextView) findViewById(R.id.app_name);

//		if (nameView != null) {
//			nameView.setText(appModel.name);
//		}

		if (appName!=null)
			nameView.setText(appName);

		Intent intent = getIntent().getParcelableExtra(KEY_INTENT);
		VirtualCore.get().setLoadingPage(intent, this);
		if (intent != null) {
			VUiKit.defer().when(() -> {
				long startTime = System.currentTimeMillis();
				if (!appModel.fastOpen) {
					try {
						VirtualCore.get().preOpt(appModel.packageName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				long spend = System.currentTimeMillis() - startTime;
				if (spend < 500) {
					try {
						Thread.sleep(500 - spend);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).done((res) ->
					VActivityManager.get().startActivity(intent, userId));
		}else{
			Log.i(TAG, "onCreate: 执行异常");
			Toast.makeText(LoadingActivity.this,"分身打开异常，请到客服界面点击常见问题找到第21条解决",Toast.LENGTH_SHORT).show();
		}
	}

}
