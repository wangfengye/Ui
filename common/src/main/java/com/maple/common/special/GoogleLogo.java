package com.maple.common.special;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by maple on 2019/7/22 11:36
 * ## 不规则图形的点击事件
 * function 1: ImageView 组合
 * 1.将复杂图片的按点击区域切分未不同png图片,重叠到一个ViewGroup中
 * 2. ViewGroup 重写点击事件 `getBackground`获取完整bitmpap, 取点击点像素颜色,分发点击事件到不同图片
 * <p>
 * function2: canvas绘制,Region类判断点击事件
 * 该类使用function2实现该功能.
 */
public class GoogleLogo extends View {
    public static final String TAG = "GoogleLogo";
    public static final int BLUE = 0xff4285f4;//android中16进制颜色必须包含透明度.
    public static final int RED = 0xffea4335;
    public static final int YELLOW = 0xfffbbc05;
    public static final int GREEN = 0xff34a853;
    public ArrayList<Region> mRegions;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private int mLRadius;
    private int mMRadius;
    private int mSRadius;
    private Region clip;
    private String[] strs = new String[]{"蓝", "红", "绿", "黄"};
    private Bitmap mTmpBitmap;
    private OnClickListener mListener;

    public GoogleLogo(Context context) {
        this(context, null);
    }

    public GoogleLogo(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GoogleLogo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mWidth = 0;
        mHeight = 0;
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mRegions = new ArrayList<>();
        clip = new Region();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mLRadius = mWidth < mHeight ? mWidth / 2 : mHeight / 2;
        mMRadius = mLRadius / 2;
        mSRadius = mLRadius / 3;
        clip.set(0, 0, mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mTmpBitmap = drawOut();
        canvas.drawBitmap(mTmpBitmap, 0, 0, mPaint);
    }

    private Bitmap drawOut() {
        mRegions.clear();//清空上次绘制缓存
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);//缓存bitmap;
        Canvas canvas = new Canvas(bitmap);
        // 绘制中心圆
        mPaint.setColor(BLUE);
        mPaint.setStrokeWidth(4f);
        //canvas.drawCircle(mWidth / 2, mHeight / 2, mSRadius, mPaint);
        Region regionCenter = new Region();
        Path pathCenter = new Path();
        pathCenter.addCircle(mWidth / 2, mHeight / 2, mSRadius, Path.Direction.CCW);
        canvas.drawPath(pathCenter, mPaint);
        regionCenter.setPath(pathCenter, clip);
        mRegions.add(regionCenter);

        // 绘制外边
        double angle = 30 * 2 * Math.PI / 360;
        double angleC = 180 * 2 * Math.PI / 360;
        float ax = (float) (mWidth / 2 + Math.cos(angle) * mLRadius);
        float ay = (float) (mHeight / 2 - Math.sin(angle) * mLRadius);

        float cx = (float) (mWidth / 2 + Math.cos(angleC) * mMRadius);
        float cy = (float) (mHeight / 2 - Math.sin(angleC) * mMRadius);
        Path path = new Path();
        path.moveTo(ax, ay);
        RectF rect = new RectF(mWidth / 2 - mLRadius, mHeight / 2 - mLRadius, mWidth / 2 + mLRadius, mHeight / 2 + mLRadius);
        path.arcTo(rect, -30, -120);
        path.lineTo(cx, cy);
        RectF rectM = new RectF(mWidth / 2 - mMRadius, mHeight / 2 - mMRadius, mWidth / 2 + mMRadius, mHeight / 2 + mMRadius);
        path.arcTo(rectM, -180, 120);
        path.lineTo(ax, ay);
        path.close();
        Region region = new Region();
        region.setPath(path, clip);
        mRegions.add(region);
        mPaint.setColor(RED);
        canvas.drawPath(path, mPaint);

        // 构造旋转矩阵
        Matrix matrix = new Matrix();
        matrix.setRotate(-120, mWidth / 2, mHeight / 2);
        //旋转path

        path.transform(matrix);
        Region region1 = new Region();
        region1.setPath(path, clip);
        mRegions.add(region1);
        mPaint.setColor(GREEN);
        canvas.drawPath(path, mPaint);

        path.transform(matrix);
        Region region2 = new Region();
        region2.setPath(path, clip);
        mRegions.add(region2);
        mPaint.setColor(YELLOW);
        canvas.drawPath(path, mPaint);
        return bitmap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action != MotionEvent.ACTION_DOWN && action != MotionEvent.ACTION_UP) return super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //judgeByRegion((int) event.getX(), (int) event.getY());
           judgeByColor(mTmpBitmap, (int) event.getX(), (int) event.getY());
        }
        return true;
    }

    /**
     * region类判断点击区域
     *
     * @param x 相对x
     * @param y 相对y.
     */
    private void judgeByRegion(int x, int y) {
        for (int i = 0; i < mRegions.size(); i++) {
            Region region = mRegions.get(i);
            if (region.contains(x, y)) {
                Log.i(TAG, "onTouchEvent: " + strs[i]);
                if (mListener != null) mListener.onClick(this, strs[i]);
                return;
            }
        }
        Log.i(TAG, "judgeByRegion: 点击空白区域");
    }

    private void judgeByColor(Bitmap bitmap, int x, int y) {
        if (x<0||y<0||bitmap==null){
            Log.i(TAG, "judgeByColor: 入参异常");
            return;
        }
        int color = bitmap.getPixel(x * bitmap.getWidth() / mWidth, y * bitmap.getHeight() / mHeight);
        switch (color) {
            case BLUE:
                if (mListener != null) mListener.onClick(this, strs[0]);
                break;
            case RED:
                if (mListener != null) mListener.onClick(this, strs[1]);
                break;
            case GREEN:
                if (mListener != null) mListener.onClick(this, strs[2]);
                break;
            case YELLOW:
                if (mListener != null) mListener.onClick(this, strs[3]);
                break;
            default:
                Log.i(TAG, "judgeByColor: 点击空白区域");
                break;
        }

    }

    public void setListener(OnClickListener listener) {
        this.mListener = listener;
    }

    public interface OnClickListener {
        void onClick(View view, String data);
    }
}
