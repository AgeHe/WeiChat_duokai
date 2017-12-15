package com.zhushou.weichat.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;

import com.lody.virtual.helper.utils.VLog;
import com.zhushou.weichat.bean.ApkItemInfo;

import java.util.List;


/**
 * Created by Administrator on 2017/2/14.
 */

public class AddShortcutToDesktop {

    private Context mContext;

    public AddShortcutToDesktop(Context context){
        this.mContext =context;
    }

    public void addShortcutToDesktop(ApkItemInfo info) {

        Intent shortcut = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重建
        shortcut.putExtra("duplicate", false);
        // 设置名字
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,info.getTitle().toString());// 桌面快捷方式名称
        VLog.d("jyh icon",info.getAppModel().icon==null?"null":"not null");
        // 设置图标
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,drawableToBitmap(info.getAppModel().icon) );
        // 设置意图和快捷方式关联程序
        Intent intent = new Intent(mContext, mContext.getClass());
        // 桌面图标和应用绑定，卸载应用后系统会同时自动删除图标
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        // 发送广播
        mContext.sendBroadcast(shortcut);

    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        //canvas.setBitmap(bitmap);

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        drawable.draw(canvas);

        return bitmap;

    }

//    public static Bitmap drawableToBitamp(Drawable drawable) {
//        Bitmap bitmap;
//        int w = drawable.getIntrinsicWidth();
//        int h = drawable.getIntrinsicHeight();
//        VLog.d("jyh Bitmap",w+"=========="+h);
//        Bitmap.Config config =
//                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                        : Bitmap.Config.RGB_565;
//        bitmap = Bitmap.createBitmap(w, h, config);
//        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
//        Canvas canvas = new Canvas(bitmap);
//        drawable.setBounds(0, 0, w, h);
//        drawable.draw(canvas);
//        return bitmap;
//    }

//    public boolean isShortcutInstalled() {
//        boolean isInstallShortcut = false;
//        final ContentResolver cr = mContext.getContentResolver();
//        // 2.2系统是”com.android.launcher2.settings”,网上见其他的为"com.android.launcher.settings"
//        String AUTHORITY = null;
//        /*
//         * 根据版本号设置Uri的AUTHORITY
//         */
//        if (Build.VERSION.SDK_INT >= 8) {
//            AUTHORITY = "com.android.launcher2.settings";
//        } else {
//            AUTHORITY = "com.android.launcher.settings";
//        }
//        Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
//                + "/favorites?notify=true");
//        Cursor c = cr.query(CONTENT_URI,
//                new String[] { "title", "iconResource" }, "title=?",
//                new String[] { mContext.getResources().getString(R.string.app_name) }, null);// 这里得保证app_name与创建
//        //快捷方式名的一致，否则会出现提示“快捷方式已经创建”
//        if (c != null && c.getCount() > 0) {
//            isInstallShortcut = true;
//        }
//        return isInstallShortcut;
//    }

    public  boolean isShortcutInstalled(String name) {
            boolean result = false;
            String AUTHORITY = getAuthorityFromPermission(mContext);
            final Uri CONTENT_URI = Uri.parse(AUTHORITY);
            final Cursor c = mContext.getContentResolver().query(CONTENT_URI,  new String[]{"title", "iconResource"},
                    "title=?",new String[]{name}, null);
//            VLog.d("jyh c",c==null?"null":"not null");
//            VLog.d("jyh c getCount",c.getCount()+"");
//            while(c.moveToNext()) {
//            VLog.d("jyh cursor",c.getString(0)==null?"null":c.getString(0));
//            }
            if (c != null && c.getCount() > 0) {
              result = true;
            }
            return result;
    }

    public static String getAuthorityFromPermission(Context context) {
        // 获取默认
        String authority = getAuthorityFromPermissionDefault(context);
        // 获取特殊第三方
        if (authority == null || authority.trim().equals("")) {
            String packageName = getCurrentLauncherPackageName(context);
            packageName += ".permission.READ_SETTINGS";
            authority = getThirdAuthorityFromPermission(context, packageName);
        }
        // 还是获取不到，直接写死
        if (TextUtils.isEmpty(authority)) {
            int sdkInt = android.os.Build.VERSION.SDK_INT;
            if (sdkInt < 8) { // Android 2.1.x(API 7)以及以下的
                authority = "com.android.launcher.settings";
            } else if (sdkInt < 19) {// Android 4.4以下
                authority = "com.android.launcher2.settings";
            } else {// 4.4以及以上
                authority = "com.android.launcher3.settings";
            }
        }
        authority = "content://" + authority + "/favorites?notify=true";
        return authority;

    }

    public static String getCurrentLauncherPackageName(Context context) {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        ResolveInfo res = context.getPackageManager().resolveActivity(intent, 0);
        if (res == null || res.activityInfo == null) {
            // should not happen. A home is always installed, isn't it?
            return "";
        }
        if (res.activityInfo.packageName.equals("android")) {
            return "";
        } else {
            return res.activityInfo.packageName;
        }
    }


    public static String getAuthorityFromPermissionDefault(Context context) {

        return getThirdAuthorityFromPermission(context, "com.android.launcher.permission.READ_SETTINGS");
    }

    public static String getThirdAuthorityFromPermission(Context context, String permission) {
        if (TextUtils.isEmpty(permission)) {
            return "";
        }

        try {
            List<PackageInfo> packs = context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
            if (packs == null) {
                return "";
            }
            for (PackageInfo pack : packs) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        if (permission.equals(provider.readPermission) || permission.equals(provider.writePermission)) {
                            if (!TextUtils.isEmpty(provider.authority)// 精准匹配launcher.settings，再一次验证
                                    && (provider.authority).contains(".launcher.settings"))
                                return provider.authority;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    private static int getSystemVersion(){
        return android.os.Build.VERSION.SDK_INT;
    }

}
