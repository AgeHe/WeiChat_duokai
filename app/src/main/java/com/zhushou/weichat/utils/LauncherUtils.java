package com.zhushou.weichat.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/4/19.
 */

public class LauncherUtils {

    public static final String LAUNCHER_ADDRESS = "launcher.address";
    public static final String LAUNCHER_TYPE = "launcher.type";

    private Context mContext;
    public LauncherUtils(Context mContext){
        this.mContext = mContext;
    }


    public boolean isStartAdver(){

        return false;
    }

    public String[] getlauncheraddress(){
//        String[] strArray;
//        String spAddress = (String) SharedPreferencesUtils.getParam(mContext,LAUNCHER_ADDRESS,"");
        return null;
    }

    /**
     * 保存主界面数据展示类型
     * @param context
     * @param launcherType
     */
    public static void saveLauncherType(Context context,int launcherType){
        SharedPreferencesUtils.setParam(context,LAUNCHER_TYPE,launcherType);
    }

    /**
     * 获取主界面数据展示类型
     * @param context
     * @param launcherType
     * @return
     */
    public static boolean checkLauncherType(Context context,int launcherType){
        int param = (int) SharedPreferencesUtils.getParam(context,LAUNCHER_TYPE,0);
        if (param!=0&&param==launcherType){
            return true;
        }else{
            return false;
        }
    }


}
