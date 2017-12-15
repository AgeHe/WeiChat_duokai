package com.zhushou.weichat.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zhushou.weichat.ui.dialog.WsFriendDialog;
import com.zhushou.weichat.utils.DisplayUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Administrator on 2017/6/21.
 */

public class WsFriendViewGroup extends ViewGroup implements View.OnClickListener {
    private Context context;
    private String[] imageArray;
    private int paddingLift = 0;
    private int screenWidth = 0;
    private int silpWidth = 0;
    public WsFriendViewGroup(Context context) {
        super(context);
        init(context);
    }

    public WsFriendViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private int groupWidth;
    private int imageWidth;
    private void init(Context context){
        this.context = context;
        paddingLift = DisplayUtil.dip2px(context,79);
        screenWidth = DisplayUtil.getScreenWidth(context);
        silpWidth = DisplayUtil.dip2px(context,3);
        groupWidth = screenWidth-paddingLift;
        imageWidth = (groupWidth-2*silpWidth)/3;
    }
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3){
        int count = getChildCount();
        if (imageArray==null)
            return;
        if (imageArray.length==1&&count>0){
            getChildAt(0).layout(i,0,i2,i3);
        }else if (count>1){
            for (int c=0;c<count;c++){
                View childAt = getChildAt(c);
                int line = 1;
                if (c<=2){
                    line = 0;
                    if (c==2){
                        childAt.layout(2*imageWidth+2*silpWidth,0,3*imageWidth+2*silpWidth,imageWidth);
                    }else if (c==1){
                        childAt.layout(imageWidth+silpWidth,0,2*imageWidth+silpWidth,imageWidth);
                    }else{
                        childAt.layout(0,0,imageWidth,imageWidth);
                    }
                }else if (c>2&&c<=5){
                    line = 1;
                    if (c==5){
                        childAt.layout(2*imageWidth+2*silpWidth,imageWidth+silpWidth,3*imageWidth+2*silpWidth,2*imageWidth+silpWidth);
                    }else if (c==4){
                        childAt.layout(imageWidth+silpWidth,imageWidth+silpWidth,2*imageWidth+silpWidth,2*imageWidth+silpWidth);
                    }else{
                        childAt.layout(0,imageWidth+silpWidth,imageWidth,2*imageWidth+silpWidth);
                    }
                }else if (c>5){
                    line = 2;
                    if (c==8){
                        childAt.layout(2*imageWidth+2*silpWidth,2*imageWidth+2*silpWidth,3*imageWidth+2*silpWidth,3*imageWidth+2*silpWidth);
                    }else if (c==7){
                        childAt.layout(imageWidth+silpWidth,2*imageWidth+2*silpWidth,2*imageWidth+silpWidth,3*imageWidth+2*silpWidth);
                    }else{
                        childAt.layout(0,2*imageWidth+2*silpWidth,imageWidth,3*imageWidth+2*silpWidth);
                    }
                }
            }

        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = groupWidth;
        int height;
        if (imageArray.length==1){
                setMeasuredDimension(imageBitmapWidth,imageBitmapHeight);
        }else{
            int arraylength = imageArray.length;
            int line = arraylength/3f>1?arraylength%3>0?arraylength/3+1:arraylength/3:1;
            height = (line>3?3:line)*(imageWidth+silpWidth);
            setMeasuredDimension(width, height);
        }

        int count = getChildCount();
        for (int i = 0; i < count; i++){
            View child = getChildAt(i);
            if (child.getVisibility()!=View.GONE){
                if (count==1)
                    child.measure(firstImageView.getMeasuredWidth(), firstImageView.getMeasuredHeight());
                else
                    child.measure(imageWidth, imageWidth);
            }
        }
    }
    ImageView firstImageView = null;
    public void setArrayImageData(String[] arrayImage,String id){
        imageArray = null;
        if (arrayImage.length==1){
//            ViewGroup.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
            firstImageView = new ImageView(context);
//            firstImageView.setScaleType(ImageView.ScaleType.MATRIX);
//            firstImageView.setLayoutParams(params);
            Picasso.with(context)
                    .load(arrayImage[0])
                    .config(Bitmap.Config.RGB_565)
                    .noPlaceholder()
                    .into(firstImageTarget);
            addView(firstImageView);
            firstImageView.setTag(0);
            firstImageView.setOnClickListener(this);
        }else{
            for (int i=0;i<arrayImage.length;i++){
                LayoutParams params1 = new LayoutParams(imageWidth,imageWidth);
                ImageView imageView1 = new ImageView(context);
                imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView1.setLayoutParams(params1);
                Picasso.with(context)
                        .load(arrayImage[i])
                        .resize(imageWidth,imageWidth)
                        .centerCrop()
                        .config(Bitmap.Config.ALPHA_8)
                        .into(imageView1);
                addView(imageView1);
                imageView1.setTag(i);
                imageView1.setOnClickListener(this);
                if (i>=8)
                    break;
            }
        }
        imageArray = arrayImage;

        invalidate();
    }

    @Override
    public void onClick(View view) {

        if (view.getTag()!=null&&(int)view.getTag()>=0){
            try {
                new WsFriendDialog(context,imageArray,WsFriendDialog.IMAGE_ARRAY,(int)view.getTag()).show();
            }catch (Exception e){

            }
        }
    }

    @Override
    public void removeAllViews() {
//        if (getChildCount()>0){
//            for (int i=0;i<getChildCount();i++){
////                releaseImageViewResouce((ImageView) getChildAt(i));
//            }
//        }
        super.removeAllViews();
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

    public Target firstImageTarget = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from){
            if (firstImageView!=null&&bitmap!=null){
                int[] imageSizeArray = sumImageViewSize(bitmap);
                imageBitmapWidth = imageSizeArray[0];
                imageBitmapHeight = imageSizeArray[1];
                LayoutParams params = new LayoutParams(imageBitmapWidth,imageBitmapHeight);
                firstImageView.setScaleType(ImageView.ScaleType.FIT_START);
                firstImageView.setLayoutParams(params);
                firstImageView.setImageBitmap(bitmap);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };
    int imageBitmapWidth = 0;
    int imageBitmapHeight = 0;
    private int[] sumImageViewSize(Bitmap imageBitmap){

        int[] imageSizeArray = new int[2];
        int imageBitmapWidth = 0;
        int imageBitmapHeight = 0;
        int maxlength = 0;
        float sizef = 0f;
        float  samllSizef =0f;

        if (imageBitmap!=null&&groupWidth>0){
            imageBitmapWidth = imageBitmap.getWidth();
            imageBitmapHeight = imageBitmap.getHeight();
            maxlength = Math.max(imageBitmapWidth,imageBitmapHeight);
            if (maxlength>groupWidth){
                sizef = (float) groupWidth/maxlength;
                if (imageBitmapWidth>imageBitmapHeight){
                    imageSizeArray[0] = groupWidth;
                    imageSizeArray[1] = (int) (imageBitmapHeight*sizef);
                }else{
                    imageSizeArray[0] = (int) (imageBitmapWidth*sizef);
                    imageSizeArray[1] = groupWidth;
                }
            }else{
                if (maxlength<groupWidth/2){
                    samllSizef = (float) (groupWidth/2)/maxlength;
                    if (imageBitmapWidth>imageBitmapHeight){
                        imageSizeArray[0] = (int) (maxlength*samllSizef);
                        imageSizeArray[1] = (int) (imageBitmapHeight*samllSizef);
                    }else{
                        imageSizeArray[0] = (int) (imageBitmapWidth*samllSizef);
                        imageSizeArray[1] = (int) (maxlength*samllSizef);
                    }
                }else{
                    imageSizeArray[0] = imageBitmap.getWidth();
                    imageSizeArray[1] = imageBitmap.getHeight();
                }
            }

        }else{
            imageSizeArray[0] = groupWidth;
            imageSizeArray[1] = groupWidth;
        }

        return imageSizeArray;
    }


}
