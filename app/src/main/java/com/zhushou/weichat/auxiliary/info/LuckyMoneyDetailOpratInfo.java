package com.zhushou.weichat.auxiliary.info;

/**
 * Created by Administrator on 2017/4/14.
 * 红包详情页面操作
 */

public class LuckyMoneyDetailOpratInfo {

    private boolean dxOprat = false; //红包详情页面自动答谢
    private boolean backOprat = false; //红包详情页面返回聊天页



    public boolean isDxOprat() {
        return dxOprat;
    }

    public void setDxOprat(boolean dxOprat) {
        this.dxOprat = dxOprat;
    }

    public boolean isBackOprat() {
        return backOprat;
    }

    public void setBackOprat(boolean backOprat) {
        this.backOprat = backOprat;
    }
}
