package com.zhushou.weichat.screenshot.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhanglinkai on 2017/3/19.
 * 功能:
 */

public class StaticMethod {


    /**
     *功能:输出表情
     *参数:
     */
    public static void changeStrWithEmoji(Context context,String input,TextView tv) {
        //1.匹配字符串input中“[xxx]”
        Pattern pattern = Pattern.compile("\\[[^\\[\\]]*\\]");
        //2.获取匹配器
        Matcher matcher = pattern.matcher(input);
        //3.创建SP
        SpannableString sp = new SpannableString(input);
        //4.依次找到字符串input匹配到的子字符串
        while (matcher.find()) {
            //5.获取匹配到的子字符串[emoxxx]
            String group = matcher.group();
            //6.在该字符串开始的位置 与结束的位置
            int start = matcher.start();
            int end = matcher.end();
            Log.d("ttt", group);
            //7.获取 R.mipmap.class 实例
            //Class<R.mipmap> mipmapClass = R.mipmap.class;

            Bitmap bitmap=getImageFromAssetsFile(context,faceNameList(context).get(Integer.valueOf(group.substring(1, group.length() - 1))));
            ImageSpan imageSpan = new ImageSpan(context,bitmap);
            sp.setSpan(imageSpan, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        //12.显示出来
        tv.setText(sp);
    }
    /**
    *功能:表情名
    *参数:
    */
    public static List<String> faceNameList(Context context){
        List<String> list=new ArrayList<>();
        //获取AssetManager
        AssetManager assets = context.getResources().getAssets();
        try {
            //获取assets目录下的所有文件及目录名
            String[] pictures = assets.list("");
            for (int i = 0; i < pictures.length; i++) {
                if (pictures[i].indexOf("gif_expression")!=-1){
                    list.add(pictures[i]);
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        return list;
    }
    /**
    *功能:获取表情
    *参数:
    */
    public static Bitmap getImageFromAssetsFile(Context context,String fileName)
    {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try
        {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return image;

    }
    public static void adaptiveView(View view, int w, int h , float index) {

        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if(w!= 0){
            lp.width = (int) (w * index) ;
        }
        if (h!=0){

            lp.height = (int) (h * index) ;
        }
        view.setLayoutParams(lp);
    }
    /**
    *功能:String 保留两位小数，三位数出现逗号
    *参数:
    */
    public static String keepTwoDecimal(String s){
        float m=Float.valueOf(s);
        DecimalFormat fnum  =  new  DecimalFormat("#,###0.00");
        return  String.format("%s",fnum.format(m).toString());
    }
    /**
    *功能:String 保留两位小数，三位数出现逗号
    *参数:
    */
    public static String keepTwoDecimalNo(String s){
        float m=Float.valueOf(s);
        DecimalFormat fnum  =  new  DecimalFormat("###0.00");
        return  String.format("%s",fnum.format(m).toString());
    }

    /**
    *功能:生成随机数
    *参数:
    */
    public static String getRandomString(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; ++i) {
            int number = random.nextInt(10);// [0,62)
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
    /**
    *功能:时间格式转化
    *参数:
    */
    public static String StringPattern(String date, String oldPattern, String newPattern) {
        if (date == null || oldPattern == null || newPattern == null)
            return "";
        SimpleDateFormat sdf1 = new SimpleDateFormat(oldPattern) ;
        SimpleDateFormat sdf2 = new SimpleDateFormat(newPattern) ;
        Date d = null ;
        try{
            d = sdf1.parse(date) ;
        }catch(Exception e){
            e.printStackTrace() ;
        }
        return sdf2.format(d);
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

}
