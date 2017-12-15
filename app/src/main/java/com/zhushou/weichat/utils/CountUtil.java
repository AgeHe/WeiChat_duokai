package com.zhushou.weichat.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import com.lody.virtual.helper.utils.VLog;
import com.okhttplib.HttpInfo;
import com.okhttplib.callback.CallbackOk;
import com.zhushou.weichat.VApp;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * Created by Administrator on 2016/11/7.
 */

public class CountUtil {
    private static final String TAG = "CountUtil";
    private Context mContext;

    public CountUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取Umeng渠道号
     *
     * @return 渠道号
     */
    public String getFrom() {
        String channel = "1001";
        try {
            ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null) {
                Object metaDatperfer = info.metaData.get("CHANNEL");
                if (metaDatperfer != null) {
                    channel = metaDatperfer.toString();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        VLog.d("xx", channel);
        return channel;
    }

    /**
     * //获取应用号
     *
     * @return 获取应用号
     */
    public String getApp() {
        String app = "fssq";
        try {
            ApplicationInfo info = mContext.getPackageManager().getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null) {
                Object metaDatperfer = info.metaData.get("APP");
                if (metaDatperfer != null) {
                        app = metaDatperfer.toString();
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        VLog.d("xx", app);
        return app;
    }
    //获取versioncode
    public String getVersion() {
        try {
            PackageInfo info = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return Integer.toString(info.versionCode);
        } catch (Exception e) {
            return "";
        }
    }
    //获取应用号
    private String getSecurityKey() {
        return VApp.getSecurityKey();
    }

    //获取Android_ID的base64编码后
    public String getOnlyCode() {
        return Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    //获取当前秒数 注意除1000 不然是毫秒
    private String getTime() {
        return Long.toString(System.currentTimeMillis() / 1000);
//        return "1478516238.68024";
    }

    private String getInstallDays() {
        String days ="";
        UserHelper helper = new UserHelper(mContext);
        Long installtime = helper.getInstallTime();
        if(helper.getInstallTime()!=0){
             days = Long.toString((System.currentTimeMillis()/1000-installtime)/60/60/24);
        }
        return days;
    }


    private String getMD5Encode(String type) {
        String code = new StringBuilder(getFrom()).append(getApp()).append(getVersion()).append(type).append(getOnlyCode()).append(getTime()).append(getSecurityKey()).toString();
        VLog.d("MD5code first", code);
        VLog.d("MD5code", stringMD5(code));
        return stringMD5(code);
    }

    public String getFinalUrl(String type, String count) {
        String codeFirst = new StringBuilder()
                .append("&type=").append(type)
                .append("&from=").append(getFrom())
                .append("&app=").append(getApp())
                .append("&version=").append(getVersion())
                .append("&sn=").append(Base64.encodeToString(getOnlyCode().getBytes(), Base64.DEFAULT).trim())
                .append("&code=").append(getMD5Encode(type))
                .append("&time=").append(getTime())
                .append("&count=").append(count)
                .toString();
        try {
            StringBuilder url = new StringBuilder(VApp.TJURL).append(stringToAscii(reverseString(Base64.encodeToString(codeFirst.getBytes(), Base64.DEFAULT).trim())));
            return url.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //status 支付状态 (failed ,success)     orderno 订单号 count支付金额 paytype 支付类型( 微信通, 财付通,支付宝....) tags标签 (用于记录一些想要记录的信息. 如,二次付费,新用户付费,礼物赠送....)
    public String getPayFinalUrl(String status, String orderno, String count, String paytype, String tags) {
        try {
            String codeFirst = new StringBuilder()
                    .append("&type=").append(VApp.TYPE_PAYMENT)
                    .append("&from=").append(getFrom())
                    .append("&app=").append(getApp())
                    .append("&version=").append(getVersion())
                    .append("&sn=").append(Base64.encodeToString(getOnlyCode().getBytes(), Base64.DEFAULT).trim())
                    .append("&code=").append(getMD5Encode(VApp.TYPE_PAYMENT))
                    .append("&time=").append(getTime())
                    .append("&count=").append(Float.parseFloat(count))
                    .append("&installed_days=").append(getInstallDays())
                    .append("&payment_type=").append(URLEncoder.encode(paytype, "UTF-8").toString())
                    .append("&payment_status=").append(URLEncoder.encode(status, "UTF-8").toString())
                    .append("&payment_order=").append(URLEncoder.encode(orderno, "UTF-8").toString())
                    .append("&tags=").append(URLEncoder.encode(tags, "UTF-8").toString())
                    .toString();
            VLog.d("code first", codeFirst);
            StringBuilder url = new StringBuilder(VApp.TJURL).append(stringToAscii(reverseString(Base64.encodeToString(codeFirst.getBytes(), Base64.DEFAULT).trim())));
            return url.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 字符串镜像，对称字符串
     * 比如： I am a student. -->  .tneduts a ma I
     *
     * @param s 参数字符串
     * @return 字符串镜像
     */
    public static String reverseString(String s) {
        if (s == null)
            return null;
        else {
            if (s.length() == 1)
                return s;
            else {
                char[] chars = s.toCharArray();
                int length = chars.length;
                int start = 0;
                int end = length - 1;
                char temp;
                while (start < end) {
                    temp = chars[start];
                    chars[start] = chars[end];
                    chars[end] = temp;
                    start++;
                    end--;
                }
                return String.valueOf(chars);
            }
        }
    }


    //String 转asc码
    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        value = value.replaceAll("\\s+", "");
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == 'Z') {
                sbu.append((char) 97);//a
            } else if (chars[i] == 'z') {
                sbu.append((char) 48);//0
            } else if (chars[i] == '9') {
                sbu.append((char) 65);//A
            } else if (chars[i] == '=') {
                sbu.append('=');
            } else {
                sbu.append((char) (chars[i] + 1));
            }
        }
        return sbu.toString().trim();
    }

    private static String bytesToHexString(byte[] bytes) {
        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        char[] buf = new char[bytes.length * 2];
        int c = 0;
        for (byte b : bytes) {
            buf[c++] = chars[(b >> 4) & 0xf];
            buf[c++] = chars[b & 0xf];
        }
        return new String(buf);
    }

    private String stringMD5(String pw) {
        try {
            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 输入的字符串转换成字节数组
            byte[] inputByteArray = pw.getBytes();
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray, 0, inputByteArray.length);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return bytesToHexString(resultByteArray);
        } catch (Exception e) {
            return "";
        }
    }

    //统计安装启动信息
    public void doInstallOrStartTj(final String type) {
        com.okhttplib.OkHttpUtil.getDefault().doGetAsync(HttpInfo.Builder().setUrl(getFinalUrl(type, "1")).build(),
                new CallbackOk() {
                    @Override
                    public void onResponse(final HttpInfo info) throws IOException {
                        if (info.isSuccessful() && VApp.TYPE_INSTALL.equals(type)) {//如果是安装而成功
                            PackageInfo packageInfo = null;
                            try {
                                doInstallOrStartTj(VApp.TYPE_STARTUP);//统计启动
                                packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                                UserHelper helper = new UserHelper(mContext);
                                helper.setTjVersion(packageInfo.versionName);
                                helper.setInstallTime(System.currentTimeMillis()/1000);
                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    //统计支付信息
    public void doPayNumTj(boolean status, String orderno, String num, String paytype, String tags) {
        try{
            com.okhttplib.OkHttpUtil.getDefault().doGetAsync(HttpInfo.Builder().setUrl(getPayFinalUrl(status ? "success" : "failed", orderno, num, paytype, tags)).build(),
                    new CallbackOk() {
                        @Override
                        public void onResponse(final HttpInfo info) throws IOException {
                        }
                    });
        }catch (Exception e){
            Log.i(TAG, "doPayNumTj: "+e.getMessage());
        }
    }

}
