package com.zhushou.weichat.uinew.interfac;

import com.zhushou.weichat.abs.BasePresenter;
import com.zhushou.weichat.abs.BaseView;
import com.zhushou.weichat.bean.ApkItemInfo;
import com.zhushou.weichat.bean.ScreenShotPreview;
import com.zhushou.weichat.bean.UpdateInfo;
import com.zhushou.weichat.ui.ListAppContract;
import com.zhushou.weichat.ui.models.AppModel;
import com.zhushou.weichat.uinew.info.HomeItemInfo;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class HomeAppContract {

    public interface AppView extends BaseView<ListAppContract.ListAppPresenter> {
        void startLoading();
        void loadFinish(List<AppModel> models);
        void loadVirtualApps(List<AppModel> models);
        void loadInstallAllApps(List<AppModel> models);
        void mHideLoadin();
        void addItem(HomeItemInfo info);
        void startUpdateApp(UpdateInfo updateInfo);
        void netWorkRequestFailure();
        void bdHandling(String bdCode);
    }



    public interface AppPresenter extends BasePresenter {
        void addWeiChatApp(String appName,boolean isFreeTrial);
        void deleteItemApp(ApkItemInfo apkItemInfo);
        void deleteVirtualApp(AppModel appModel);
        void addItemToDeskTop(ApkItemInfo apkItemInfo,int position);
        void resetModel();
        void resetVirtualApps();
        void onDestroy();
        void showPreviewDialog(List<ScreenShotPreview> listData, String functionKey);
        void checkWxVersionCode();
    }
    public interface StartCheck{
        void isOk(List<AppModel> models);
        void isNot();

    }

}
