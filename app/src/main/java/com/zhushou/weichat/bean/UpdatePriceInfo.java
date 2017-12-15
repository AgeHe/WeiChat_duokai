package com.zhushou.weichat.bean;

/**
 * Created by Administrator on 2017/1/19.
 */

public class UpdatePriceInfo {

//    "versionCode": "100",
//    "kfqq": "2659808848",
//    "sysj": "20",
//    "scfy": "8.8",
//    "zcfy": "10"
    private String versionCode;  // 版本号
    private String appId; // 公众号支付id
    private int ad_length;//广告秒数
    private boolean tuia_adv_isbrowser = true;//推啊 广告是否使用内部打开
    private String kfqq;  // 客服QQ
    private String kfwx; // 客服微信
    private String kfdh;//客服电话
    private String top_tip;//主界面醒目提示内容
    private String sysj; // 免费使用時間 /分钟
    private String scfy; // 首冲费用 /元
    private String zcfy; // 正常费用 /元
    private String shareaddress; //分享下载地址
    private String shareTime; //分享后增加时长(小时)
    private String mfSj; // 免费试用时长 （本地计算保存）
    private String fxcs;//免费试用 加时次数
    private String discount;//支付宝折扣
    private String left_button_url;//左上按钮链接

    private String allAppsPrice;//所有应用多开价格

    private ScreenShotPreview screenShotPreview; // 微商功能效果图预览

    private String weichat_hb_price; // 微商功能  自动抢红包功能费用
    private String weichat_xphf_price; // 微商功能 息屏回复 价格
    private String weichat_hfnr_price; // 微商功能 设置回复内容 价格
    private String weichat_yshb_price; // 微商功能 零延时抢红包 价格
    private String weichat_xphb_price; // 微商功能 息屏自动抢红包 价格

    private String weichat_hf_all_price;//自动回复所有功能费用
    private String weichat_hf_dg_price;//自动回复单个功能费用
    private String weichat_jt_all_price;//微商截图所有功能费用
    private String weichat_jt_dg_price;//微商截图功能单个费用
    private String weichat_jf_dg_price;//微信加粉单个功能费用
    private String weichat_jf_all_price;//微信加粉全部功能费用
    private String weichat_qgg_price;//启动页去广告功能费用
    private String weichat_ffh_price;//微信多开防封号功能费用
    private String weichat_svip_price;//微信微商功能SVIP开启所有功能费用

    private String weichat_version_code;//辅助功能支持的微信版本
    private String id_hb;//微信红包View ID
    private String id_chb;//微信拆红包按钮 View ID
    private String id_hf_input;//微信聊天输入框 View ID
    private String id_hf_sendbtn;//微信发送信息　View ID
    private String id_hb_btn_ly; // 微信红包详情界面 留言按钮 ID
    private String id_hb_input_ly; // 微信红包详情界面，留言输入框 ID
    private String id_hb_send_ly; // 微信红包详情界面，发送留言按钮 ID
    private String id_hb_detail_back; // 微信红包详情界面，点击返回聊天页面按钮 ID
    private String id_hb_grab_money; // 微信红包详情界面， 抢到金额View ID
    private String id_hb_boss_name;// 微信红包详情界面， 发红包的用户名 ID
    private String id_chat_item_id;//微信聊天界面 item View ID

    private boolean pay_status_wx = true; // 微信支付开关
    private boolean pay_status_ali= true; //支付宝支付开关
    private boolean pay_status_qq = true;//QQ支付开关

    //UI隐藏控制
    private boolean ffh;//防封号View 显示隐藏
    private boolean wxqhb;//微信抢红包View 显示隐藏
    private boolean shouqi;//View 显示隐藏
    private boolean dabao;//提高抢大包概率View 显示隐藏
    private boolean jiasudu;//抢红包加速度View 显示隐藏
    private boolean guanrao;//干扰竞争者View 显示隐藏
    private boolean xiaobao;//最高概率躲避小包 View 隐藏显示
    private boolean qgg;//去广告 View 显示隐藏
    private boolean jhy;//批量自动加好友 View 显示隐藏
    private boolean zdhf;//自动回复 View 显示隐藏

    //抢红包功能点价格
    private String  pc_svip;//Svip价格
    private String pc_shouqi; // 提高抢红包手气价格
    private String pc_dabao;//提高抢大包概率价格
    private String pc_xiaobao;//搞概率躲避最小包
    private String pc_jiasudu;//抢红包加速度价格
    private String pc_ganrao;//干扰竞争者价格
    private String pc_xpqhb;//息屏抢红包价格
    private String pc_zddx;//自动答谢价格



    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getKfqq() {
        return kfqq;
    }

    public void setKfqq(String kfqq) {
        this.kfqq = kfqq;
    }

    public String getSysj(){
        return sysj;
    }

    public void setSysj(String sysj) {
        this.sysj = sysj;
    }

    public String getScfy() {
        return scfy;
    }

    public void setScfy(String scfy) {
        this.scfy = scfy;
    }

    public String getZcfy() {
        return zcfy;
    }

    public void setZcfy(String zcfy) {
        this.zcfy = zcfy;
    }

    public String getShareaddress() {
        return shareaddress;
    }

    public void setShareaddress(String shareaddress) {
        this.shareaddress = shareaddress;
    }

    public String getShareTime() {
        return shareTime;
    }

    public void setShareTime(String shareTime) {
        this.shareTime = shareTime;
    }

    public String getMfSj() {
        return mfSj;
    }

    public void setMfSj(String mfSj) {
        this.mfSj = mfSj;
    }

    public String getWeichat_hb_price() {
        return weichat_hb_price;
    }

    public void setWeichat_hb_price(String weichat_hb_price) {
        this.weichat_hb_price = weichat_hb_price;
    }

    public String getWeichat_xphf_price() {
        return weichat_xphf_price;
    }

    public void setWeichat_xphf_price(String weichat_xphf_price) {
        this.weichat_xphf_price = weichat_xphf_price;
    }

    public String getWeichat_hfnr_price() {
        return weichat_hfnr_price;
    }

    public void setWeichat_hfnr_price(String weichat_hfnr_price) {
        this.weichat_hfnr_price = weichat_hfnr_price;
    }

    public String getWeichat_yshb_price() {
        return weichat_yshb_price;
    }

    public void setWeichat_yshb_price(String weichat_yshb_price) {
        this.weichat_yshb_price = weichat_yshb_price;
    }

    public String getWeichat_xphb_price() {
        return weichat_xphb_price;
    }

    public void setWeichat_xphb_price(String weichat_xphb_price) {
        this.weichat_xphb_price = weichat_xphb_price;
    }

    public String getWeichat_hf_all_price() {
        return weichat_hf_all_price;
    }

    public void setWeichat_hf_all_price(String weichat_hf_all_price) {
        this.weichat_hf_all_price = weichat_hf_all_price;
    }

    public String getWeichat_hf_dg_price() {
        return weichat_hf_dg_price;
    }

    public void setWeichat_hf_dg_price(String weichat_hf_dg_price) {
        this.weichat_hf_dg_price = weichat_hf_dg_price;
    }

    public String getWeichat_jt_all_price() {
        return weichat_jt_all_price;
    }

    public void setWeichat_jt_all_price(String weichat_jt_all_price) {
        this.weichat_jt_all_price = weichat_jt_all_price;
    }

    public String getWeichat_jt_dg_price() {
        return weichat_jt_dg_price;
    }

    public void setWeichat_jt_dg_price(String weichat_jt_dg_price) {
        this.weichat_jt_dg_price = weichat_jt_dg_price;
    }

    public String getWeichat_jf_dg_price() {
        return weichat_jf_dg_price;
    }

    public void setWeichat_jf_dg_price(String weichat_jf_dg_price) {
        this.weichat_jf_dg_price = weichat_jf_dg_price;
    }

    public String getWeichat_jf_all_price() {
        return weichat_jf_all_price;
    }

    public void setWeichat_jf_all_price(String weichat_jf_all_price) {
        this.weichat_jf_all_price = weichat_jf_all_price;
    }

    public String getWeichat_version_code() {
        return weichat_version_code;
    }

    public void setWeichat_version_code(String weichat_version_code) {
        this.weichat_version_code = weichat_version_code;
    }

    public String getId_hb() {
        return id_hb;
    }

    public void setId_hb(String id_hb) {
        this.id_hb = id_hb;
    }

    public String getId_chb() {
        return id_chb;
    }

    public void setId_chb(String id_chb) {
        this.id_chb = id_chb;
    }

    public String getId_hf_input() {
        return id_hf_input;
    }

    public void setId_hf_input(String id_hf_input) {
        this.id_hf_input = id_hf_input;
    }

    public String getId_hf_sendbtn() {
        return id_hf_sendbtn;
    }

    public void setId_hf_sendbtn(String id_hf_sendbtn) {
        this.id_hf_sendbtn = id_hf_sendbtn;
    }

    public boolean isPay_status_wx() {
        return pay_status_wx;
    }

    public void setPay_status_wx(boolean pay_status_wx) {
        this.pay_status_wx = pay_status_wx;
    }

    public boolean isPay_status_ali() {
        return pay_status_ali;
    }

    public void setPay_status_ali(boolean pay_status_ali) {
        this.pay_status_ali = pay_status_ali;
    }

    public String getId_hb_btn_ly() {
        return id_hb_btn_ly;
    }

    public void setId_hb_btn_ly(String id_hb_btn_ly) {
        this.id_hb_btn_ly = id_hb_btn_ly;
    }

    public String getId_hb_input_ly() {
        return id_hb_input_ly;
    }

    public void setId_hb_input_ly(String id_hb_input_ly) {
        this.id_hb_input_ly = id_hb_input_ly;
    }

    public String getId_hb_send_ly() {
        return id_hb_send_ly;
    }

    public void setId_hb_send_ly(String id_hb_send_ly) {
        this.id_hb_send_ly = id_hb_send_ly;
    }

    public String getId_hb_detail_back() {
        return id_hb_detail_back;
    }

    public void setId_hb_detail_back(String id_hb_detail_back) {
        this.id_hb_detail_back = id_hb_detail_back;
    }

    public String getId_hb_grab_money() {
        return id_hb_grab_money;
    }

    public void setId_hb_grab_money(String id_hb_grab_money) {
        this.id_hb_grab_money = id_hb_grab_money;
    }

    public String getId_hb_boss_name() {
        return id_hb_boss_name;
    }

    public void setId_hb_boss_name(String id_hb_boss_name) {
        this.id_hb_boss_name = id_hb_boss_name;
    }

    public String getId_chat_item_id() {
        return id_chat_item_id;
    }

    public void setId_chat_item_id(String id_chat_item_id) {
        this.id_chat_item_id = id_chat_item_id;
    }

    public boolean isFfh() {
        return ffh;
    }

    public void setFfh(boolean ffh) {
        this.ffh = ffh;
    }

    public boolean isWxqhb() {
        return wxqhb;
    }

    public void setWxqhb(boolean wxqhb) {
        this.wxqhb = wxqhb;
    }

    public boolean isShouqi() {
        return shouqi;
    }

    public void setShouqi(boolean shouqi) {
        this.shouqi = shouqi;
    }

    public boolean isDabao() {
        return dabao;
    }

    public void setDabao(boolean dabao) {
        this.dabao = dabao;
    }

    public boolean isJiasudu() {
        return jiasudu;
    }

    public void setJiasudu(boolean jiasudu) {
        this.jiasudu = jiasudu;
    }

    public boolean isGuanrao() {
        return guanrao;
    }

    public void setGuanrao(boolean guanrao) {
        this.guanrao = guanrao;
    }

    public String getPc_svip() {
        return pc_svip;
    }

    public void setPc_svip(String pc_svip) {
        this.pc_svip = pc_svip;
    }

    public String getPc_shouqi() {
        return pc_shouqi;
    }

    public void setPc_shouqi(String pc_shouqi) {
        this.pc_shouqi = pc_shouqi;
    }

    public String getPc_dabao() {
        return pc_dabao;
    }

    public void setPc_dabao(String pc_dabao) {
        this.pc_dabao = pc_dabao;
    }

    public String getPc_jiasudu() {
        return pc_jiasudu;
    }

    public void setPc_jiasudu(String pc_jiasudu) {
        this.pc_jiasudu = pc_jiasudu;
    }

    public String getPc_ganrao() {
        return pc_ganrao;
    }

    public void setPc_ganrao(String pc_ganrao) {
        this.pc_ganrao = pc_ganrao;
    }

    public String getPc_xpqhb() {
        return pc_xpqhb;
    }

    public void setPc_xpqhb(String pc_xpqhb) {
        this.pc_xpqhb = pc_xpqhb;
    }

    public String getPc_zddx() {
        return pc_zddx;
    }

    public void setPc_zddx(String pc_zddx) {
        this.pc_zddx = pc_zddx;
    }
    public String getWeichat_qgg_price() {
        return weichat_qgg_price;
    }
    public void setWeichat_qgg_price(String weichat_qgg_price) {
        this.weichat_qgg_price = weichat_qgg_price;
    }
    public String getWeichat_ffh_price() {
        return weichat_ffh_price;
    }
    public void setWeichat_ffh_price(String weichat_ffh_price) {
        this.weichat_ffh_price = weichat_ffh_price;
    }

    public String getWeichat_svip_price() {
        return weichat_svip_price;
    }

    public void setWeichat_svip_price(String weichat_svip_price) {
        this.weichat_svip_price = weichat_svip_price;
    }

    public String getFxcs() {
        return fxcs;
    }

    public void setFxcs(String fxcs) {
        this.fxcs = fxcs;
    }

    public ScreenShotPreview getScreenShotPreview() {
        return screenShotPreview;
    }

    public void setScreenShotPreview(ScreenShotPreview screenShotPreview) {
        this.screenShotPreview = screenShotPreview;
    }

    public boolean isXiaobao() {
        return xiaobao;
    }

    public void setXiaobao(boolean xiaobao) {
        this.xiaobao = xiaobao;
    }


    public String getPc_xiaobao() {
        return pc_xiaobao;
    }

    public void setPc_xiaobao(String pc_xiaobao) {
        this.pc_xiaobao = pc_xiaobao;
    }

    public boolean isQgg() {
        return qgg;
    }

    public void setQgg(boolean qgg) {
        this.qgg = qgg;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getAd_length() {
        return ad_length;
    }

    public void setAd_length(int ad_length) {
        this.ad_length = ad_length;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public boolean isJhy() {
        return jhy;
    }

    public void setJhy(boolean jhy) {
        this.jhy = jhy;
    }

    public boolean isZdhf() {
        return zdhf;
    }

    public void setZdhf(boolean zdhf) {
        this.zdhf = zdhf;
    }

    public String getKfdh() {
        return kfdh;
    }

    public void setKfdh(String kfdh) {
        this.kfdh = kfdh;
    }

    public String getKfwx() {
        return kfwx;
    }

    public void setKfwx(String kfwx) {
        this.kfwx = kfwx;
    }

    public String getAllAppsPrice() {
        return allAppsPrice;
    }

    public void setAllAppsPrice(String allAppsPrice) {
        this.allAppsPrice = allAppsPrice;
    }

    public boolean isTuia_adv_isbrowser() {
        return tuia_adv_isbrowser;
    }

    public void setTuia_adv_isbrowser(boolean tuia_adv_isbrowser) {
        this.tuia_adv_isbrowser = tuia_adv_isbrowser;
    }

    public String getTop_tip() {
        return top_tip;
    }

    public void setTop_tip(String top_tip) {
        this.top_tip = top_tip;
    }

    public boolean isPay_status_qq() {
        return pay_status_qq;
    }

    public void setPay_status_qq(boolean pay_status_qq) {
        this.pay_status_qq = pay_status_qq;
    }

    public String getLeft_button_url() {
        return left_button_url;
    }

    public void setLeft_button_url(String left_button_url) {
        this.left_button_url = left_button_url;
    }
}
