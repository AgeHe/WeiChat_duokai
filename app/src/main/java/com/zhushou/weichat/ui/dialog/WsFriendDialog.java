package com.zhushou.weichat.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;
import com.squareup.picasso.Picasso;
import com.zhushou.weichat.R;
import com.zhushou.weichat.ui.view.ZoomImageVIew;
import com.zhushou.weichat.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/21.
 */

public class WsFriendDialog extends Dialog implements View.OnClickListener,EasyVideoCallback {

    public static final int IMAGE_ARRAY = 0x11;
    public static final int VIDEO_PATH = 0x12;
    private int ovalSize = 0;
    private int screenHeight = 0;
    private int screenWidth = 0;



    String[] imgArray;
    String videoPath = "";
    int preViewType = VIDEO_PATH;

    public Context context;


    private List<ImageView> mDataList;
    private View v_redpoint;
    private int initialIndex = 0;



    public WsFriendDialog(@NonNull Context context, Object urlObj, int urlType,int initialIndex) {
        super(context, R.style.ShareOrPayDialog);
        this.preViewType = urlType;
        screenHeight = DisplayUtil.getScreenHeight(context);
        screenWidth = DisplayUtil.getScreenWidth(context);
        if (preViewType == VIDEO_PATH) {
            videoPath = (String) urlObj;
        } else {
            imgArray = (String[]) urlObj;
        }
        ovalSize = DisplayUtil.dip2px(context,10);
        this.initialIndex = initialIndex;
        initData(context);
        initView();
    }

    private void initData(Context context) {
        this.context = context;

    }
    LinearLayout ll_points;
    ViewPager vp_photo;
    EasyVideoPlayer evp_player;
    RelativeLayout rl_bottom;
    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.ws_friend_dialog, null);
        vp_photo = (ViewPager) view.findViewById(R.id.vp_photo);
        evp_player = (EasyVideoPlayer) view.findViewById(R.id.evp_player);
        rl_bottom = (RelativeLayout) view.findViewById(R.id.rl_bottom);
        ll_points = (LinearLayout) view.findViewById(R.id.ll_points);
        v_redpoint = view.findViewById(R.id.v_redpoint);

        if (preViewType == VIDEO_PATH) {
            rl_bottom.setVisibility(View.GONE);
            vp_photo.setVisibility(View.GONE);

            evp_player.setCallback(this);
            evp_player.setAutoPlay(true);
            evp_player.setAutoFullscreen(false);
            evp_player.setOnClickListener(this);
            evp_player.setClickable(false);
//            evp_player.hideControls();
            evp_player.disableControls();
            evp_player.setScaleX( DisplayUtil.getScaleX(context));
            evp_player.setSource(Uri.parse(videoPath));

        }else{
            evp_player.setVisibility(View.GONE);
            if (imgArray==null)
                return;
            initPagerView();
            vp_photo.setAdapter(new ImagePagerAdapter());
            //设置每次加载时第一页在MAX_VALUE / 2 - Extra 页，造成用户无限轮播的错觉
//            int startPage = Integer.MAX_VALUE / 2;
//            int extra = startPage % mDataList.size();
//            startPage = startPage - extra;
            vp_photo.setCurrentItem(initialIndex);

            initEvent();

        }


        this.setCanceledOnTouchOutside(true);             //点击外部关闭窗口

        Window win = this.getWindow();
        win.setGravity(Gravity.CENTER);                       //从中间弹出
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;   //宽度填满
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;  //高度自适应
        win.setAttributes(lp);

        super.setContentView(view);

    }

    private void initPagerView() {
        if (mDataList!=null&&mDataList.size()>0){
            mDataList.clear();
            mDataList = null;
        }

        mDataList = new ArrayList<>();
        for (int i = 0; i < imgArray.length; i++) {
            ZoomImageVIew img = new ZoomImageVIew(context);
            ViewGroup.LayoutParams imageViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            img.setLayoutParams(imageViewParams);
//            img.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            img.set(imgArray[i]);
            Picasso.with(context)
                    .load(imgArray[i])
//                    .resize(screenWidth,screenHeight)
//                    .centerInside()
                    .config(Bitmap.Config.RGB_565)
                    .tag("imagedialog")
                    .into(img);
            mDataList.add(img);

            //添加底部灰点
            View v = new View(context);
            v.setBackgroundResource(R.drawable.ws_friend_oval_f);
            //指定其大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ovalSize, ovalSize);
            if (i != 0)
                params.leftMargin = ovalSize;
            v.setLayoutParams(params);
            ll_points.addView(v);
        }

    }

    private int diatance = 0;
    private void initEvent() {
        /**
         * 当底部红色小圆点加载完成时测出两个小灰点的距离，便于计算后面小红点动态移动的距离
         */
        v_redpoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (ll_points.getChildCount()>1)
                    diatance =  ll_points.getChildAt(1).getLeft() - ll_points.getChildAt(0).getLeft();
                Log.d("两点间距",diatance + "测出来了");
            }
        });

        vp_photo.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //测出页面滚动时小红点移动的距离，并通过setLayoutParams(params)不断更新其位置
//                position = position % mDataList.size();
                float leftMargin = diatance * (position + positionOffset);
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) v_redpoint.getLayoutParams();
                params.leftMargin = Math.round(leftMargin);
                v_redpoint.setLayoutParams(params);
                Log.d("红点在这",leftMargin + "");
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onStarted(EasyVideoPlayer player) {

    }

    @Override
    public void onPaused(EasyVideoPlayer player) {

    }

    @Override
    public void onPreparing(EasyVideoPlayer player) {

    }

    @Override
    public void onPrepared(EasyVideoPlayer player) {
        if(!player.isPlaying()){
            player.start();
        }
    }

    @Override
    public void onBuffering(int percent) {

    }

    @Override
    public void onError(EasyVideoPlayer player, Exception e) {

    }

    @Override
    public void onCompletion(EasyVideoPlayer player) {

        if (!player.isPlaying()){
            dismiss();
        }
    }

    @Override
    public void onRetry(EasyVideoPlayer player, Uri source) {

    }

    @Override
    public void onSubmit(EasyVideoPlayer player, Uri source) {

    }

    /**
     * ViewPager的容器
     */

    public class ImagePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
           //告诉容器我们的数据长度为Integer.MAX_VALUE，这样就可以一直滚动
            return mDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //若position超过mDataList.size()，会发生越界异常，所以这里每次超过size又从0开始计算位置
//            position = position % mDataList.size();

            ImageView img = mDataList.get(position);
            container.addView(img);

            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
//            position = position % mDataList.size();
            container.removeView((View)object);
        }
    }


    @Override
    public void show() {
        super.show();
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        /////////获取屏幕宽度
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        /////////设置高宽
        lp.width = screenWidth; // 宽度
        lp.height = dm.heightPixels;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void dismiss() {
//        if (preViewType==IMAGE_ARRAY){
//            if (mDataList.size()>0){
//                for (int i=0;i<mDataList.size();i++){
//                releaseImageViewResouce(mDataList.get(i));
//                }
//            }
//        }
        if (evp_player!=null){
            evp_player.release();
        }
        super.dismiss();


    }

    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null) return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

}
