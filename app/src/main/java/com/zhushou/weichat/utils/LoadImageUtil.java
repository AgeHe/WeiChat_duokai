package com.zhushou.weichat.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

/**
 * Created by Administrator on 2017/9/13.
 */

public class LoadImageUtil {


    public static void loadImage(Context context, String loadUrl, ImageView loadImageView){
        Glide.with(context).load(loadUrl).into(loadImageView);
    }
    public static void loadImage(Context context,String loadUrl,int loadinImage, ImageView loadImageView){
        Glide.with(context).load(loadUrl).placeholder(loadinImage).into(loadImageView);
    }

    public static void loadImage(RequestManager requestManager,String loadUrl,ImageView loadImageView){
        requestManager.load(loadUrl).into(loadImageView);
    }

    public static void loadImageResoures(Context context,int res,ImageView loadImageView){
        Glide.with(context).load(res).into(loadImageView);
    }

    public static void loadCircleImage(Activity mActivity,String imageUrl,ImageView imageView){
        Glide.with(mActivity).load(imageUrl).transform(new GlideCircleTransform(mActivity)).into(imageView);
    }

    public static void loadImageGif(Context context,String imageUrl,ImageView imageView){
        try{
            Glide.with(context).load(imageUrl).asGif().into(imageView);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
