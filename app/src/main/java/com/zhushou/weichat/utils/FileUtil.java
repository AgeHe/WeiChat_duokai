package com.zhushou.weichat.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2017/5/12.
 */

public class FileUtil {
    //获取根目录
    public static String getRootPath(){
        return Environment.getExternalStorageDirectory().getPath();
    }

    //下载具体操作
    public static boolean download(String fileUrl,String fileName) {
        try {
            URL url = new URL(fileUrl);
            //打开连接
            URLConnection conn = url.openConnection();
            //打开输入流
            InputStream is = conn.getInputStream();
            //获得长度
            int contentLength = conn.getContentLength();
            Log.e("jyh", "contentLength = " + contentLength);
            //下载后的文件名
            File file1 = new File(getRootPath()+"/" +fileName);
            if (file1.exists()) {
                file1.delete();
            }
            //创建字节流
            byte[] bs = new byte[1024];
            int len;
            OutputStream os = new FileOutputStream(file1.getPath());
            //写数据
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            //完成后关闭流
            os.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
