package com.maple.common.dreame;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
 * @author maple on 2018/12/8 14:43.
 * @version v1.0
 * @see 1040441325@qq.com
 */
public class DreamLayout extends ViewGroup implements Runnable {
    private float[] data;
    private int mRadiusMax;
    private Paint mLayoutPaint;
    private int centerColor = Color.parseColor("#00ffffff");
    private int edgeColor = Color.parseColor("#99ffffff");
    private Paint mPaint;
    Handler mHandler;
    BaseAdapter mAdapter;

    public DreamLayout(Context context) {
        super(context);

    }

    @Override
    public void removeView(View view) {
        super.removeView(view);
        for (int i = 0; i <lens.length -1; i++) {
            lens[i]=lens[i+1];
        }

    }

    public DreamLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DreamLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }

    public void addChild(View v) {
        addView(v);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int contentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int contentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        int dimensionX = contentWidth;
        int dimensionY = contentHeight;
        setMeasuredDimension(dimensionX, dimensionY);
  /*      int size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);


        }*/
        int len = Math.min(contentWidth, contentHeight) / 4;
        // measureChildren(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        measureChildren(MeasureSpec.makeMeasureSpec(len, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(len, MeasureSpec.EXACTLY));
    }

    int[] lens = new int[165];

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() <= 0) throw new RuntimeException("must set an center child view");
        // layout center view
        View viewA = getChildAt(0);
        viewA.layout(getMeasuredWidth() / 2 - viewA.getMeasuredWidth() / 2, getMeasuredHeight() / 2 - viewA.getMeasuredHeight() / 2,
                getMeasuredWidth() / 2 + viewA.getMeasuredWidth() / 2, getMeasuredHeight() / 2 + viewA.getMeasuredHeight() / 2);
        // layout around view

        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int rad = Math.max(view.getMeasuredWidth(), view.getMeasuredHeight()) / 2;
            int len;
            if (lens[i] == 0) {
                lens[i] = (int) (mRadiusMax / 3 + rad * 3 / 2 + Math.random() * (getMeasuredWidth() / 2 - rad - mRadiusMax / 3 - rad * 3 / 2));
            }
            len = lens[i];
            int x = (int) (len * Math.sin(i * Math.PI / 4));
            int y = (int) (len * Math.cos(i * Math.PI / 4));

            x = getMeasuredWidth() / 2 + x;
            y = getMeasuredHeight() / 2 + y;
            view.layout(x - rad, y - rad, x + rad, y + rad);
        }
    }

    protected void init() {
        mRadiusMax = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mLayoutPaint = new Paint();
        mLayoutPaint.setAntiAlias(true);
        RadialGradient gradient =
                new RadialGradient(width / 2, height / 2, mRadiusMax,
                        new int[]{centerColor, centerColor, edgeColor}, new float[]{0f,.8f,1.0f}, Shader.TileMode.MIRROR);
        mLayoutPaint.setShader(gradient);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        data = new float[3];
        data[0] = 1 / (float) 16;
        data[1] = 1 / (float) 8;
        data[2] = 1 / (float) 4;
        mHandler = new Handler();
        mHandler.post(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        for (float scale : data) {
            canvas.save();
            canvas.scale(scale, scale, width / 2, height / 2);
            canvas.drawCircle(width / 2, height / 2, mRadiusMax, mLayoutPaint);
            canvas.restore();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeCallbacks(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            data[i] = (float) (data[i] * 1.02);
            if (data[i] >= 2) data[i] = 1 / (float) 8;
        }
        invalidate();
        mHandler.postDelayed(this, 32);
    }
}
