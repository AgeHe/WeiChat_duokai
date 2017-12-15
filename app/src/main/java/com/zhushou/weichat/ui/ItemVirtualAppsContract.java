package com.zhushou.weichat.ui;

import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.ui.models.AppModel;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class ItemVirtualAppsContract  {


    interface ActivityContract{
        void loadingDataS(List<ApkItemInfo> dataArray,AppModel appModel);
        void hideLoading(boolean isDialog);
        void displayLoading(boolean isDialog);
        void addVirtualApp(ApkItemInfo apkItemInfo);
    }

    interface  ItemAppsImpl{
        void startLoading();

        void addVirtualApp(AppModel appModel);

        void addDeskTop(ApkItemInfo apkItemInfo);
    }

}
