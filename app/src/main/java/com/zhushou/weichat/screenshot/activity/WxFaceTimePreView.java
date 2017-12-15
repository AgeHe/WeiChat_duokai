package com.zhushou.weichat.screenshot.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import com.zhushou.weichat.R;
import com.zhushou.weichat.screenshot.Utils.StaticMethod;

/**
 * Created by zhanglinkai on 2017/4/1.
 * 功能:
 */

public class WxFaceTimePreView extends Activity {
    //未接通
    private RelativeLayout facetimr_preview_norcall_layout;
    private ImageView facetime_call_btn_not;
    private ImageView facetime_call_btn_yes;
    private ImageView facetime_call_voice_iv;
    private ImageView wx_facetime_preview_icon;
    private TextView wx_facetime_preview_name;
    //接通
    private RelativeLayout facetimr_preview_calling_layout;
    private ImageView facetimr_preview_calling_layout_bg;
    private ImageView facetimr_preview_calling_shrink;
    private ImageView facetimr_preview_calling_mybg;
    private ImageView facetime_preview_calling_btn_transfervoice;
    private ImageView facetime_preview_calling_btn_over;
    private ImageView facetime_preview_calling_btn_transfercamera;
    private TextView facetime_preview_calling_time_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wx_facetime_preview);
        iniUI();
    }

    private void iniUI() {
        float index=getSharedPreferences("index", Context.MODE_PRIVATE).getFloat("index",0);
        facetimr_preview_norcall_layout=(RelativeLayout)findViewById(R.id.facetimr_preview_norcall_layout);//未接通layout
        facetime_call_btn_not=(ImageView)findViewById(R.id.facetime_call_btn_not);
        facetime_call_btn_yes=(ImageView)findViewById(R.id.facetime_call_btn_yes);
        facetime_call_voice_iv=(ImageView)findViewById(R.id.facetime_call_voice_iv);
        wx_facetime_preview_icon=(ImageView)findViewById(R.id.wx_facetime_preview_icon);
        wx_facetime_preview_name=(TextView)findViewById(R.id.wx_facetime_preview_name);

        facetimr_preview_calling_layout=(RelativeLayout)findViewById(R.id.facetimr_preview_calling_layout);//接通layout
        facetimr_preview_calling_layout_bg=(ImageView)findViewById(R.id.facetimr_preview_calling_layout_bg);
        facetimr_preview_calling_shrink=(ImageView)findViewById(R.id.facetimr_preview_calling_shrink);
        facetimr_preview_calling_mybg=(ImageView)findViewById(R.id.facetimr_preview_calling_mybg);
        facetime_preview_calling_btn_transfervoice=(ImageView)findViewById(R.id.facetime_preview_calling_btn_transfervoice);
        facetime_preview_calling_btn_over=(ImageView)findViewById(R.id.facetime_preview_calling_btn_over);
        facetime_preview_calling_btn_transfercamera=(ImageView)findViewById(R.id.facetime_preview_calling_btn_transfercamera);
        facetime_preview_calling_time_tv=(TextView)findViewById(R.id.facetime_preview_calling_time_tv);

        StaticMethod.adaptiveView(facetime_call_btn_not,200,200,index);
        StaticMethod.adaptiveView(facetime_call_btn_yes,200,200,index);
        StaticMethod.adaptiveView(facetime_call_voice_iv,100,100,index);
        StaticMethod.adaptiveView(wx_facetime_preview_icon,250,250,index);

        StaticMethod.adaptiveView(facetimr_preview_calling_shrink,105,75,index);
        StaticMethod.adaptiveView(facetimr_preview_calling_mybg,200,350,index);
        StaticMethod.adaptiveView(facetime_preview_calling_btn_transfervoice,200,200,index);
        StaticMethod.adaptiveView(facetime_preview_calling_btn_over,200,200,index);
        StaticMethod.adaptiveView(facetime_preview_calling_btn_transfercamera,200,200,index);


        String type=getIntent().getStringExtra("type");
        if (type.equals("nocall")){
            facetimr_preview_norcall_layout.setVisibility(View.VISIBLE);
            facetimr_preview_calling_layout.setVisibility(View.GONE);

            String name=getIntent().getStringExtra("name");
            String image=getIntent().getStringExtra("image");

            wx_facetime_preview_name.setText(name);
            Picasso.with(this).load(new File(image)).resize(100,100).into(wx_facetime_preview_icon);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                facetimr_preview_norcall_layout.setBackgroundDrawable(new BitmapDrawable(blurBitmap(BitmapFactory.decodeFile(image))));
            }

        }else if (type.equals("calling")){
            facetimr_preview_norcall_layout.setVisibility(View.GONE);
            facetimr_preview_calling_layout.setVisibility(View.VISIBLE);

            String time=getIntent().getStringExtra("time");
            String mImage=getIntent().getStringExtra("myphotopath");
            String hImage=getIntent().getStringExtra("herphotopath");

            facetime_preview_calling_time_tv.setText(time);

            Picasso.with(this).load(new File(hImage)).resize(300,300).into(facetimr_preview_calling_layout_bg);
            Picasso.with(this).load(new File(mImage)).resize(100,100).into(facetimr_preview_calling_mybg);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap blurBitmap(Bitmap bitmap){

        //Let's create an empty bitmap with the same size of the bitmap we want to blur
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        //Instantiate a new Renderscript
        RenderScript rs = RenderScript.create(getApplicationContext());

        //Create an Intrinsic Blur Script using the Renderscript
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //Create the Allocations (in/out) with the Renderscript and the in/out bitmaps
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);

        //Set the radius of the blur
        blurScript.setRadius(25.f);

        //Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);

        //Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);

        //recycle the original bitmap
        bitmap.recycle();

        //After finishing everything, we destroy the Renderscript.
        rs.destroy();

        return outBitmap;


    }
}
