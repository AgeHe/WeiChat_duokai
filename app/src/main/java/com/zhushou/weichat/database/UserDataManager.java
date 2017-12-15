package com.zhushou.weichat.database;

import com.lody.virtual.os.VUserInfo;
import com.lody.virtual.os.VUserManager;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.ui.adapter.MakeApkRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/9.
 */

public class UserDataManager {


    public void createUser(String userName,int type){
        VUserManager.get().createUser(type+"_"+userName, VUserInfo.FLAG_ADMIN);
    }

    public List<VUserInfo> getUsers(){
        List<VUserInfo> listUserInfo = new ArrayList<>();
        List<VUserInfo> userinfos = VUserManager.get().getUsers(false);
        for (VUserInfo info:userinfos){
            String[] strArray = info.name.split("_");
            if (strArray[0].equals(MakeApkRecyclerViewAdapter.QQ_TYPE)){
                ApkItemInfo itemInfo = new ApkItemInfo();
                itemInfo.setTitle(strArray[1]);
                itemInfo.setCreateDate("");
//                itemInfo.setIcon(info.);
            }
        }
        return userinfos;
    }

}
