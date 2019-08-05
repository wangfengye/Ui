package com.maple.common.wave;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import com.maple.common.R;

/**
 * Created by fengye on 2018/5/18.
 * email 1040441325@qq.com
 * 水浪加载图
 */

public class WaveProgressView extends View {
    private int mWidth;
    private int mHeight;
    private Bitmap mBackgroundBitmap;

    private Path mPath;
    private Paint mPathPaint;

    private float mWaveHeight = 60f;
    private int mWaveNumber = 1;//可见浪花个数
    private int mWaveColor = Color.parseColor("#5be4ef");
    private int mWaveSpeed = 10;// 浪花频率
    private int mHeightSpeed = 5;
    private Paint mTextPaint;
    private String mCurrentText = "";
    private int mTextColor = Color.WHITE;
    private int mTextSize = 40;

    private int mMaxProgress = 100;//最大进度
    private int mCurrentProgress = 0;
    private float mCurY;// 当前高度
    private int mSeconds = 10; // 高度变化时间

    private float mDistance = 0;//浪花右移距离

    public void setAllowProgressInBothDirections(boolean allowProgressInBothDirections) {
        mAllowProgressInBothDirections = allowProgressInBothDirections;
    }

    private boolean mAllowProgressInBothDirections = true;

    public void reSet() {
        this.mCurY = mHeight;
    }

    public WaveProgressView(Context context) {
        this(context, null, 0);
    }

    public WaveProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        Init();
    }

    public void setCurrent(int currentProgress, String currentText) {
        this.mCurrentProgress = currentProgress;
        this.mCurrentText = currentText;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }


    public void setText(@ColorInt int textColor, int mTextSize) {
        this.mTextColor = textColor;
        this.mTextSize = mTextSize;
    }

    public void setWave(float mWaveHight, int number) {
        this.mWaveHeight = mWaveHight;
        this.mWaveNumber = number;
    }


    public void setWaveColor(@ColorInt int i) {
        this.mWaveColor = i;
    }

    public void setWaveSpeed(int mWaveSpeed) {
        this.mWaveSpeed = mWaveSpeed;
    }

    public void allowProgressInBothDirections(boolean allow) {
        this.mAllowProgressInBothDirections = allow;
    }

    private void Init() {
        if (null == getBackground()) {
            //throw new IllegalArgumentException(String.format("background is null."));
            mBackgroundBitmap=getBitmapFromDrawable(getResources().getDrawable(R.drawable.wave_white));
        } else {
            mBackgroundBitmap = getBitmapFromDrawable(getBackground());
        }

        mPath = new Path();
        mPathPaint = new Paint();
        mPathPaint.setAntiAlias(true);
        mPathPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mCurY = mHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (mBackgroundBitmap != null) {
            canvas.drawBitmap(createImage(), 0, 0, null);
        }

        // 屏幕下一次绘制时调用onDraw()l
        postInvalidate();
    }
    private void drawRec(){

    }
    private Bitmap createImage() {
        mPathPaint.setColor(mWaveColor);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        Bitmap finalBmp = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(finalBmp);

        float curMidY = mHeight * (mMaxProgress - mCurrentProgress) / mMaxProgress - mWaveHeight;

        float a = (mCurY - curMidY) / (60*mSeconds);
        if (mAllowProgressInBothDirections && mCurY > curMidY) {
            mCurY = mCurY - a;
        } else {
            mCurY = curMidY;
        }
        mPath.reset();
        mPath.moveTo(-mWidth + mDistance, mCurY);

        int waveNum = mWaveNumber * 2;
        int waveWidth = mWidth / mWaveNumber / 4;// 可见宽度/可见个数/4
        int multiplier = 0;

        for (int i = 0; i < waveNum; i++) {
            mPath.quadTo(waveWidth * (multiplier + 1) + mDistance - mWidth, mCurY - mWaveHeight, waveWidth * (multiplier + 2) + mDistance - mWidth, mCurY);
            mPath.quadTo(waveWidth * (multiplier + 3) + mDistance - mWidth, mCurY + mWaveHeight, waveWidth * (multiplier + 4) + mDistance - mWidth, mCurY);
            multiplier += 4;
        }
        mDistance = mDistance >= mWidth ? 0 : mDistance + mWaveSpeed;

        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPathPaint);

        int min = Math.min(mWidth, mHeight);
        mBackgroundBitmap = Bitmap.createScaledBitmap(mBackgroundBitmap, min, min, false);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        canvas.drawBitmap(mBackgroundBitmap, 0, 0, paint);

        drawPoint(canvas);

        canvas.drawText(mCurrentText, mWidth / 2, mHeight / 2, mTextPaint);
        return finalBmp;
    }

    private void drawPoint(Canvas canvas) {
        int locX= mWidth/2;
        Region region = new Region();
        Region clip = new Region(locX-1,0,locX,mHeight);
        region.setPath(mPath,clip);

        mPathPaint.setColor(Color.RED);
        canvas.drawCircle(locX,region.getBounds().top-50,50,mPathPaint);
        mPathPaint.setColor(mWaveColor);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {

        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

}