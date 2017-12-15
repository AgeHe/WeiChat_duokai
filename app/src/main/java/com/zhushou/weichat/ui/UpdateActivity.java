package com.zhushou.weichat.ui;

import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.lody.virtual.client.ipc.VActivityManager;
import com.zhushou.weichat.GDTLauncherActivity;
import com.zhushou.weichat.auxiliary.base.BaseActivity;
import com.zhushou.weichat.interfac.UpdateInterface;
import com.zhushou.weichat.utils.UpdateControllerImpl;

import java.net.URISyntaxException;

/**
 * Created by Administrator on 2017/12/1.
 */

public class UpdateActivity extends BaseActivity implements UpdateInterface {
    private UpdateControllerImpl viewController;
    @Override
    public void initView() {

    }

    @Override
    public void initData() {
        viewController = new UpdateControllerImpl(this,this);
        viewController.checkVersionUpdate();

    }


   private void formatIntent(Intent intent){
       int userId = intent.getIntExtra("_VA_|_user_id_", 0);
        String splashUri = intent.getStringExtra("_VA_|_splash_");
        String targetUri = intent.getStringExtra("_VA_|_uri_");
        Intent splashIntent = null;
        Intent targetIntent = null;
        if (splashUri != null) {
            try {
                splashIntent = Intent.parseUri(splashUri, 0);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (targetUri != null) {
            try {
                targetIntent = Intent.parseUri(targetUri, 0);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        if (targetIntent == null) {
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            targetIntent.setSelector(null);
        }

        if (splashIntent == null) {
            try {
                VActivityManager.get().startActivity(targetIntent, userId);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            splashIntent.putExtra(Intent.EXTRA_INTENT, targetIntent);
            splashIntent.putExtra(Intent.EXTRA_CC, userId);
            startActivity(splashIntent);
        }
   }

    @Override
    public void checkNeedUpdate(boolean isNeed) {
        if(isNeed){
            Toast.makeText(this,"软件版本需要升级",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, GDTLauncherActivity.class));
        }else {
            formatIntent(getIntent());
        }
        finish();
        System.exit(0);
    }
}
