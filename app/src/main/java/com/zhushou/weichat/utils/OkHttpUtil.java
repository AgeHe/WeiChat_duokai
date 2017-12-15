package com.zhushou.weichat.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import com.zhushou.weichat.bean.OkHttpResult;
import com.zhushou.weichat.bean.WxSdkQureyOrderInfo;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016-11-03.
 */

public class OkHttpUtil {
    private static OkHttpUtil util;
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");//mdiatype 这个需要和服务端保持一致
    private static final String TAG = "OkHttpUtil";
    private OkHttpClient client;
//    private static String BASE_URL = MyApplication.URL;

    public OkHttpUtil() {
        //初始化OkHttpClient
        client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                .build();
    }

    public static OkHttpUtil getInstance() {
        if (util == null) {
            util = new OkHttpUtil();
        }
        return util;
    }

    /**
     * okHttp post同步请求 需要另开线程使用
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     */
    public OkHttpResult requestPostBySyn(String url, String actionUrl, HashMap<String, String> paramsMap) {
        try {
            //处理参数
            StringBuilder tempParams = new StringBuilder();
            int pos = 0;
            if (paramsMap != null) {//当有参数
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }
            }
            //补全请求地址
            String requestUrl = String.format("%s/%s", url, actionUrl);
            //生成参数
            String params = tempParams.toString();
            //创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, params);
            //创建一个请求
            final Request request = addHeaders().url(requestUrl).post(body).build();
            //创建一个Call
            final Call call = client.newCall(request);
            //执行请求
            Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful()) {
                //获取返回数据 可以是String，bytes ,byteStream
//                return new OkHttpResult(true, response.body().string());
                return new OkHttpResult(true, response.body().string());
            } else {
                return new OkHttpResult(false, "请求执行失败");
            }
        } catch (Exception e) {
            return new OkHttpResult(false, "请求执行失败");
        }
    }


    /**
     * okHttp get同步请求
     *
     * @param actionUrl 接口地址
     * @param paramsMap 请求参数
     */
    public OkHttpResult requestGetBySyn(String url, String actionUrl, HashMap<String, String> paramsMap) {
        StringBuilder tempParams = new StringBuilder();
        try {
            //处理参数
            int pos = 0;
            if (paramsMap != null) {//当有参数
                for (String key : paramsMap.keySet()) {
                    if (pos > 0) {
                        tempParams.append("&");
                    }
                    //对参数进行URLEncoder
                    tempParams.append(String.format("%s=%s", key, URLEncoder.encode(paramsMap.get(key), "utf-8")));
                    pos++;
                }
            }
            //补全请求地址
            String format = "%s";
            if(actionUrl!=null&&actionUrl.length()!=0){
                  format +="/%s";
            }
            if(tempParams!=null&&tempParams.length()!=0){
                format +="?%s";
            }
            String requestUrl = String.format(format, url, actionUrl, tempParams.toString());
            Log.i(TAG, "requestGetBySyn: "+requestUrl);
            //创建一个请求
            Request request = addHeaders().url(requestUrl).build();
            Log.e("jyh",requestUrl);
            //创建一个Call
            final Call call = client.newCall(request);
            //执行请求
            final Response response = call.execute();
            //请求执行成功
            if (response.isSuccessful()) {
                //获取返回数据 可以是String，bytes ,byteStream
                return new OkHttpResult(true, response.body().string());
            } else {
                return new OkHttpResult(false, "请求执行失败");
            }
        } catch (Exception e) {
            return new OkHttpResult(false, "请求执行失败");
        }
    }

    /**
     * 统一为请求添加头信息
     *
     * @return
     */
    private Request.Builder addHeaders() {
        Request.Builder builder = new Request.Builder()
                .addHeader("Connection", "keep-alive")
                .addHeader("platform", "2")
                .addHeader("phoneModel", Build.MODEL)
                .addHeader("systemVersion", Build.VERSION.RELEASE)
                .addHeader("appVersion", "3.2.0");
        return builder;
    }


    /**
    * 检测当的网络（WLAN、3G/2G）状态
    * @param context Context
    * @return true 表示网络可用
    */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }


    public WxSdkQureyOrderInfo wxpayquery(String str_appid,
                                          String str_partnerId,
                                          String str_orderNumber,
                                          String str_prepayId,
                                          String queryOrderSign,
                                          Context context){
        WxSdkQureyOrderInfo info = new WxSdkQureyOrderInfo();
         String queryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";
        //组建xml数据
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>");
        xml.append("<appid>"+str_appid+"</appid>");
        xml.append("<mch_id>"+str_partnerId+"</mch_id>");
        xml.append("<nonce_str>"+str_prepayId+"</nonce_str>");
        xml.append("<out_trade_no>"+str_orderNumber+"</out_trade_no>");
        xml.append("<sign>"+queryOrderSign+"</sign>");
        xml.append("</xml>");

        try {
            byte[] xmlbyte = xml.toString().getBytes("UTF-8");

            System.out.println(xml);

            URL url = new URL(queryUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setDoOutput(true);// 允许输出
            conn.setDoInput(true);
            conn.setUseCaches(false);// 不使用缓存
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Length",
                    String.valueOf(xmlbyte.length));
            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            conn.setRequestProperty("X-ClientType", "2");//发送自定义的头信息

            conn.getOutputStream().write(xmlbyte);
            conn.getOutputStream().flush();
            conn.getOutputStream().close();


            if (conn.getResponseCode() != 200)
                throw new RuntimeException("请求url失败");

            InputStream is = conn.getInputStream();// 获取返回数据

            // 使用输出流来输出字符(可选)
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            String string = out.toString("UTF-8");
            System.out.println(string);
            out.close();
            PullService pullService = new PullService(context);

            info = pullService.pull(string);

//            // xml解析
//            String version = null;
//            String seqID = null;
//            XmlPullParser parser = Xml.newPullParser();
//            try {
//                parser.setInput(new ByteArrayInputStream(string.substring(1)
//                        .getBytes("UTF-8")), "UTF-8");
//                parser.setInput(is, "UTF-8");
//                int eventType = parser.getEventType();
//                while (eventType != XmlPullParser.END_DOCUMENT) {
//                    if (eventType == XmlPullParser.START_TAG) {
//                        if ("SSOMessage".equals(parser.getName())) {
//                            version = parser.getAttributeValue(0);
//                        } else if ("SeqID".equals(parser.getName())) {
//                            seqID = parser.nextText();
//                        } else if ("ResultCode".equals(parser.getName())) {
////                                resultCode = parser.nextText();
//                        }
//                    }
//                    eventType = parser.next();
//                }
//            } catch (XmlPullParserException e) {
//                e.printStackTrace();
//                System.out.println(e);
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println(e);
//            }
//            System.out.println("version = " + version);
//            System.out.println("seqID = " + seqID);
////                System.out.println("resultCode = " + resultCode);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e);
        }
        return info;
    }

}
