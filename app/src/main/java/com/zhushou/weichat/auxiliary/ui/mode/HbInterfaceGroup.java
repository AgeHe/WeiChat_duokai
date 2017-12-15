package com.zhushou.weichat.auxiliary.ui.mode;

/**
 * Created by Administrator on 2017/4/11.
 */

public class HbInterfaceGroup {

    public interface ViewContacts{

        void setRedPacketsNumberAndMoney(float number,float money); //设置红包数量和金额

        void setGrabRedPacketsStatus(boolean status); // 设置抢红包状态

        void loadingStatus(boolean status); // 加载状态

        void updateOpeningFunctionData(String UsersData);

        void setUserNumber(String UserNumber);

        void setGrabRedPacketsSwichStatus(boolean status);//设置抢红包开关
    }

    public interface ImplContacts{

        void initViewStatus(); // 创建视图

        void startViewData(); // 开始加载View数据

        void updateRedPacktsNumberAndMoney(); // 设置已抢红包数量和已抢金额

        void updateOpeningFunctionData(String userid,String function);

        String getUserId();

        void hbAnimationClick(boolean isHbSwich); // 红包界面动画点击处理

    }

}
