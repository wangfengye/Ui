package com.maple.common.loss;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * Created by maple on 2019/7/29 10:04
 */
public class LossView extends View {
    private Paint mPaint;
    private int mPointColor = Color.BLUE;
    private int mPointRadius = 30;
    private int mLineColor = Color.BLACK;
    private int mLineLen;
    private int mLossLen = 120; //震荡范围
    private int mHigh; //跳动高度
    private boolean mRuning; //
    private int pointMax;//球高度
    private float mTouchy;
    private int mWidth;
    private int mHeight;
    private int cx;
    private int cy;
    private int strokeWidth = 2;
    private int dy;//小球移动点
    private int ey;//贝塞尔控制点
    public LossView(Context context) {
        this(context, null);
    }
    public LossView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LossView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mLineLen = mWidth - 2 * mPointRadius;
        cx = mWidth / 2;
        cy = mHeight / 2;
        dy = cy;
        ey = cy;
        mHeight = cy / 2;
        pointMax = cy;
        mLossLen = 4 * mPointRadius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制两侧点
        canvas.drawARGB(99, 20, 255, 20);
        mPaint.setColor(mPointColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx - mLineLen / 2, cy, mPointRadius, mPaint);
        canvas.drawCircle(cx + mLineLen / 2, cy, mPointRadius, mPaint);

        canvas.drawCircle(cx, ey - mPointRadius - strokeWidth / 2, mPointRadius, mPaint);

        Path path = new Path();
        path.moveTo(cx - mLineLen / 2, cy);
        path.quadTo(cx, dy, cx + mLineLen / 2, cy);
        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, mPaint);
    }

    public void start() {
        ValueAnimator pointAnim = ValueAnimator.ofFloat(0, 1);
        pointAnim.addUpdateListener(anim -> {
            float t = anim.getAnimatedFraction();
            ey = (int) (cy + 2 * pointMax * t * t - 2 * pointMax * t);//模拟自由落体
            postInvalidate();
        });
        pointAnim.setInterpolator(new LinearInterpolator());
        pointAnim.setDuration(1000);
        ValueAnimator lineAnim = ValueAnimator.ofFloat(0, 1);
        lineAnim.addUpdateListener(anim -> {
            ey = (int) (cy + mHeight * anim.getAnimatedFraction());
            dy = 2 * ey - cy;
            postInvalidate();
        });
        lineAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        lineAnim.setDuration(1000);

        ValueAnimator lineAnim2 = ValueAnimator.ofFloat(1, 0);
        lineAnim2.addUpdateListener(anim -> {
            float t = 1 - anim.getAnimatedFraction();
            ey = (int) (cy + mHeight * t);
            dy = 2 * ey - cy;    // 曲线定点,计算贝塞尔控制点  

            postInvalidate();
        });
        lineAnim2.setInterpolator(new AccelerateInterpolator());
        lineAnim2.setDuration(1000);

        ValueAnimator lineAnim3 = ValueAnimator.ofFloat(0, 1);
        lineAnim3.addUpdateListener(animation -> {
            dy = (int) (cy - mLossLen + animation.getAnimatedFraction() * mLossLen);
            postInvalidate();
        });
        lineAnim3.setDuration(1000);
        lineAnim3.setInterpolator(new LossInterpolator());
        AnimatorSet set = new AnimatorSet();
        // set.play(lineAnim).before(lineAnim2);
        set.play(lineAnim2).before(pointAnim);
        set.play(pointAnim).with(lineAnim3);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mRuning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRuning = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mRuning) return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchy = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int y = (int) (event.getRawY() - mTouchy);
                if (y < 0) return true;
                if (y > cy) y = cy;//最大不超过控件半径
                ey = cy + y;
                dy = 2 * ey - cy;
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:
                int upY = (int) (event.getRawY() - mTouchy);
                if (upY <= 0) return true;
                if (upY > cy) upY = cy;//最大不超过控件半径
                mHeight = upY;
                pointMax = cy * mHeight / cy;
                mLossLen = mPointRadius * 6 * mHeight / cy;
                ey = cy + upY;
                dy = 2 * ey - cy;

                postInvalidate();
                start();
                break;
        }
        return true;
    }

    private static class LossInterpolator extends BounceInterpolator {
        public float getInterpolation(float t) {
            return (float) (1 - Math.exp(-3 * t) * Math.cos(10 * t));
        }
    }
}
