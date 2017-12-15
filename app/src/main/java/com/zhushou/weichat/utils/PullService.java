package com.zhushou.weichat.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;

import com.zhushou.weichat.bean.WxSdkQureyOrderInfo;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Administrator on 2017/5/21.
 */

public class PullService {

    private Context context;
    public PullService(Context context){
        this.context = context;
    }

    public WxSdkQureyOrderInfo pull(String str){
        //Context上下文，保存当前应用程序，系统资源和配置etc
        AssetManager am=context.getAssets();
        WxSdkQureyOrderInfo info=new WxSdkQureyOrderInfo();
        try {
//            InputStream is=am.open("weather.xml");
            InputStream is = new ByteArrayInputStream(str.getBytes("UTF-8"));
            //1，创建xml解析器
            XmlPullParser parser= Xml.newPullParser();
            //2,初始化解析器，设置要解析的流数据，并设置编码方式
            parser.setInput(is,"utf-8");
            //3,循环解析
            int type=parser.getEventType();

            while(type!=XmlPullParser.END_DOCUMENT){
                //如果是开始标签
                if(type==XmlPullParser.START_TAG){
                    if("device_info".equals(parser.getName())){
                        String device_info=parser.nextText();//获取文本数据
                        info.setDevice_info(device_info);
                    }else if("openid".equals(parser.getName())){
                        info.setOpenid(parser.nextText());
                    }else if("trade_type".equals(parser.getName())){
                        info.setTrade_type(parser.nextText());
                    }else if("trade_state".equals(parser.getName())){
                        info.setTrade_state(parser.nextText());
                    }else if("total_fee".equals(parser.getName())){
                        info.setTotal_fee(parser.nextText());
                    }else if("out_trade_no".equals(parser.getName())){
                        info.setOut_trade_no(parser.nextText());
                    }else if("time_end".equals(parser.getName())){
                        info.setTime_end(parser.nextText());
                    }
                }
                //让解析器移动到下一个
                type=parser.next();
                //关闭流
                is.close();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info;
    }

}
