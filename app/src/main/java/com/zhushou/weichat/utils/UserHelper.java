package com.zhushou.weichat.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhanglinkai on 2016/11/19.
 * 功能:
 */

public class UserHelper {
    private SharedPreferences sharedPreferences;
    private Context context;

    public UserHelper(Context context){
        this.context=context;
        sharedPreferences = context.getSharedPreferences("DuoKai",Context.MODE_PRIVATE);
    }

    //获取统计版本
    public String getTjVersion(){
        return sharedPreferences.getString("TjVersion","");
    }
    //设置统计版本
    public void setTjVersion(String TjVersion){
        SharedPreferences.Editor ed=sharedPreferences.edit();
        ed.putString("TjVersion",TjVersion);
        ed.commit();
    }

    //获取下载的文件的long
    public Long getDownloadId(){
        return sharedPreferences.getLong("DownloadId",0);
    }
    //设置下载文件的long
    public void setDownloadId(Long Id) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putLong("DownloadId", Id);
        ed.commit();

    }

    //获取当前版本的安装时间
    public Long getInstallTime(){
        return sharedPreferences.getLong("InstallTime",0);
    }
    //设置下载文件的long
    public void setInstallTime(Long time) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putLong("InstallTime", time);
        ed.commit();

    }


    /**
     * 记录kfqq
     * @param kfqq
     */
    public  void setKfqq(String kfqq){
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putString("kfqq", kfqq);
        ed.commit();
    }

    /**
     * 获取kfqq
     * @return
     */
    public  String getKfqq(){
        return sharedPreferences.getString("kfqq","");

    }


    //获取当前版本的安装时间
    public Long getLoginDate (){
        return sharedPreferences.getLong("LoginDate",0);
    }
    //设置下载文件的long
    public void setLoginDate(Long time) {
        SharedPreferences.Editor ed = sharedPreferences.edit();
        ed.putLong("LoginDate", time);
        ed.commit();

    }
}
