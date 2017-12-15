package com.lody.virtual.client.stub;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.lody.virtual.client.env.Constants;
import com.lody.virtual.client.ipc.VActivityManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import static android.R.attr.host;

/**
 * @author Lody
 */
public class ShortcutHandleActivity extends Activity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
//          <action android:name="jyh.test.one" />
//                <category android:name="" />
        Intent testintent = new Intent("com.zhushou.weichat.update");
        testintent.addCategory("com.zhushou.weichat.update");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        testintent.putExtra("_VA_|_intent_", intent.getStringExtra("_VA_|_splash_"));
        testintent.putExtra("_VA_|_uri_", intent.getStringExtra("_VA_|_uri_"));
        testintent.putExtra("_VA_|_user_id_", intent.getIntExtra("_VA_|_user_id_", 0));

        ComponentName cmp = new ComponentName("com.zhushou.weichat","com.zhushou.weichat.ui.UpdateActivity");
        testintent.setComponent(cmp);
        startActivity(testintent);

        finish();

        // TODO: 2017/11/27


//
//        int userId = intent.getIntExtra("_VA_|_user_id_", 0);
//        String splashUri = intent.getStringExtra("_VA_|_splash_");
//        String targetUri = intent.getStringExtra("_VA_|_uri_");
//        Intent splashIntent = null;
//        Intent targetIntent = null;
//        if (splashUri != null) {
//            try {
//                splashIntent = Intent.parseUri(splashUri, 0);
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }
//        if (targetUri != null) {
//            try {
//                targetIntent = Intent.parseUri(targetUri, 0);
//            } catch (URISyntaxException e) {
//                e.printStackTrace();
//            }
//        }
//        if (targetIntent == null) {
//            return;
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//            targetIntent.setSelector(null);
//        }
//
//        if (splashIntent == null) {
//            try {
//                VActivityManager.get().startActivity(targetIntent, userId);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        } else {
//            splashIntent.putExtra(Intent.EXTRA_INTENT, targetIntent);
//            splashIntent.putExtra(Intent.EXTRA_CC, userId);
//            startActivity(splashIntent);
//        }

    }


}
