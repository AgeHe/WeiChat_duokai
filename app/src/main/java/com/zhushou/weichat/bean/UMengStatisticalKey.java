package com.zhushou.weichat.bean;

import com.zhushou.weichat.auxiliary.sp.AuxiliaryConstans;

/**
 * Created by Administrator on 2017/4/18.
 */

public class UMengStatisticalKey {
    //友盟自定义事件统计

    public static final String pay_s_svip = "pay_s_svip"; //SVIP开启全部功能支付成功

    public static final String pay_s_add_fans = "pay_s_add_fans"; //批量自动加好友支付成功

    public static final String pay_s_chat_reply = "pay_s_chat_reply";//微信自动回复支付成功

    public static final String pay_s_ffh = "pay_s_ffh";//微信多开防封号支付成功

    public static final String pay_s_qgg = "pay_s_qgg";//启动页去广告支付成功

    public static final String pay_s_jt_all = "pay_s_jt_all";//所有截图功能支付成功
    public static final String pay_s_jt_dongtai = "pay_s_jt_dongtai";//微信动态图片生成器支付成功
    public static final String pay_s_jt_duihua = "pay_s_jt_duihua";//微信对话图片生成器支付成功
    public static final String pay_s_jt_hongbao = "pay_s_jt_hongbao";//微信红包图片生成器支付成功
    public static final String pay_s_jt_lingqian = "pay_s_jt_lingqian";//微信零钱图片生成器支付成功
    public static final String pay_s_jt_tixian = "pay_s_jt_tixian";//微信提现图片生成器支付成功
    public static final String pay_s_jt_zz = "pay_s_jt_zz";//微信转账生成器支付成功
    public static final String pay_s_jt_qunliao = "pay_s_jt_qunliao";//微信群聊生成器支付成功
    public static final String pay_s_jt_qqhb = "pay_s_jt_qqhb";//QQ红包生成器支付成功
    public static final String pay_s_jt_qqqb = "pay_s_jt_qqqb";//QQ钱包生成器支付成功
    public static final String pay_s_jt_qqye = "pay_s_jt_qqye";//QQ余额生成器支付成功
    public static final String pay_s_jt_qqzz = "pay_s_jt_qqzz";//QQ转账生成器支付成功
    public static final String pay_s_jt_zfbhb = "pay_s_jt_zfbhb";//支付宝红包生成器支付成功
    public static final String pay_s_jt_zfbtx = "pay_s_jt_zfbtx";//支付宝提现生成器支付成功
    public static final String pay_s_jt_zfbye = "pay_s_jt_zfbye";//支付宝余额生成器支付成功
    public static final String pay_s_jt_zfbzz = "pay_s_jt_zfbzz";//支付宝转账生成器支付成功
    public static final String adv_click = "adv_click";// 广告点击
    public static final String adv_download = "adv_download";// 广告下载点击
    public static final String adv_open_failed = "adv_open_failed";//开屏广告打开失败直接跳转
    public static final String adv_send_request = "adv_send_request";//开屏启动次数
    public static final String ws_friend_click = "ws_friend_click";//微商朋友圈点击
    public static final String friend_item_lcick = "friend_item_lcick";//朋友圈浏览点击
    public static final String friend_residence_time = "friend_residence_time";//朋友圈界面停留时长

    public static final String reward_success = "reward_success";//打赏成功
    public static final String reward_click_random = "reward_click_random";//打赏点击随机金额
    public static final String reward_exdit = "reward_exdit";//打赏编辑金额点击
    public static final String reward_gif_click = "reward_gif_click";//打赏点击
    public static final String reward_pay_click = "reward_pay_click";//打赏支付点击


    public static final String pay_service_check_f = "pay_service_check_f";//后台查询失败次数
    public static final String pay_service_check_giveup = "pay_service_check_giveup";//后台查询放弃
    public static final String pay_service_check_s = "pay_service_check_s";//后台查询成功次数
    public static final String pay_service_show = "pay_service_show";//后台查询弹出次数


    public static final String pay_s_weichat_dg = "pay_s_weichat_dg";//微信单个分身支付成功
    public static final String pay_s_weichat_zs = "pay_s_weichat_zs";//微信终身分身支付成功


    public static final String clear_app_data = "clear_app_data"; //清理应用分身执行 计数


    public static final String pay_s_grab_lucky_money = "pay_s_grab_lucky_money";//微信抢红包全部功能支付成功
    public static final String pay_s_grab_hb_db = "pay_s_grab_hb_db"; //提高抢大包概率支付成功
    public static final String pay_s_grab_hb_dx = "pay_s_grab_hb_dx"; //抢红包自动答谢支付成功
    public static final String pay_s_grab_hb_gr = "pay_s_grab_hb_gr"; //智能干扰竞争者支付成功
    public static final String pay_s_grab_hb_jsd = "pay_s_grab_hb_jsd"; //抢红包加速度支付成功
    public static final String pay_s_grab_hb_sq = "pay_s_grab_hb_sq"; //提高手气最佳概率支付成功
    public static final String pay_s_grab_hb_xb = "pay_s_grab_hb_xb"; //最高概率躲避小包支付成功
    public static final String pay_s_grab_hb_xphb = "pay_s_grab_hb_xphb"; //息屏自动抢红包支付成功


    public static final String selectUmenfunctionPayHBKey(String functionKey){
        String uMengPayKey = "";
        if (functionKey.equals(AuxiliaryConstans.C_HB_SVIP)){
            uMengPayKey = pay_s_grab_lucky_money;
        }
        if (functionKey.equals(AuxiliaryConstans.C_HB_DB)){
            uMengPayKey = pay_s_grab_hb_db;
        }
        if (functionKey.equals(AuxiliaryConstans.C_DX)){
            uMengPayKey = pay_s_grab_hb_dx;
        }
        if (functionKey.equals(AuxiliaryConstans.C_HB_GR)){
            uMengPayKey = pay_s_grab_hb_gr;
        }
        if (functionKey.equals(AuxiliaryConstans.C_HB_JSD)){
            uMengPayKey = pay_s_grab_hb_jsd;
        }
        if (functionKey.equals(AuxiliaryConstans.C_HB_SQ)){
            uMengPayKey = pay_s_grab_hb_sq;
        }
        if (functionKey.equals(AuxiliaryConstans.C_HB_XB)){
            uMengPayKey = pay_s_grab_hb_xb;
        }
        if (functionKey.equals(AuxiliaryConstans.C_XPHB)){
            uMengPayKey = pay_s_grab_hb_xphb;
        }

        return uMengPayKey;
    }



    public static final String selectUMentFunctionPaykey(String functionKey){
        String uMengPayKey = "";
        if (functionKey.equals(AuxiliaryConstans.C_SVIP)){
            uMengPayKey = pay_s_svip;
        }
        if (functionKey.equals(AuxiliaryConstans.C_ALLHF)){
            uMengPayKey = pay_s_chat_reply;
        }
        if (functionKey.equals(AuxiliaryConstans.C_JHYALL)){
            uMengPayKey = pay_s_add_fans;
        }
        if (functionKey.equals(AuxiliaryConstans.C_QGG)){
            uMengPayKey = pay_s_qgg;
        }
        if (functionKey.equals(AuxiliaryConstans.C_FFH)){
            uMengPayKey = pay_s_ffh;
        }
        if (functionKey.equals(AuxiliaryConstans.C_TIXIAN)){
            uMengPayKey = pay_s_jt_tixian;
        }
        if (functionKey.equals(AuxiliaryConstans.C_LINGQIAN)){
            uMengPayKey = pay_s_jt_lingqian;
        }
        if (functionKey.equals(AuxiliaryConstans.C_WX_ZZ)){
            uMengPayKey = pay_s_jt_zz;
        }
        if (functionKey.equals(AuxiliaryConstans.C_HONGBAO)){
            uMengPayKey = pay_s_jt_hongbao;
        }
        if (functionKey.equals(AuxiliaryConstans.C_PY_QUAN)){
            uMengPayKey = pay_s_jt_dongtai;
        }
        if (functionKey.equals(AuxiliaryConstans.C_DUIHUA)){
            uMengPayKey = pay_s_jt_duihua;
        }
        if (functionKey.equals(AuxiliaryConstans.C_QUNLIAO)){
            uMengPayKey = pay_s_jt_qunliao;
        }
        if (functionKey.equals(AuxiliaryConstans.C_ZFB_HB)){
            uMengPayKey = pay_s_jt_zfbhb;
        }
        if (functionKey.equals(AuxiliaryConstans.C_ZFB_TX)){
            uMengPayKey = pay_s_jt_zfbtx;
        }
        if (functionKey.equals(AuxiliaryConstans.C_ZFB_YE)){
            uMengPayKey = pay_s_jt_zfbye;
        }
        if (functionKey.equals(AuxiliaryConstans.C_ZFB_ZZ)){
            uMengPayKey = pay_s_jt_zfbzz;
        }
        if (functionKey.equals(AuxiliaryConstans.C_QQ_HB)){
            uMengPayKey = pay_s_jt_qqhb;
        }
        if (functionKey.equals(AuxiliaryConstans.C_QQ_YE)){
            uMengPayKey = pay_s_jt_qqye;
        }
        if (functionKey.equals(AuxiliaryConstans.C_QQ_QB)){
            uMengPayKey = pay_s_jt_qqqb;
        }
        if (functionKey.equals(AuxiliaryConstans.C_QQ_ZZ)){
            uMengPayKey = pay_s_jt_qqzz;
        }

        return uMengPayKey;
    }

}
