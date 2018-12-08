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
public class DreameLayout extends ViewGroup implements Runnable {
    private float[] data;
    private int mRadiusMax;
    private Paint mLayoutPaint;
    private int centerColor = Color.parseColor("#00ffffff");
    private int edgeColor = Color.parseColor("#99ffffff");
    private Paint mPaint;
    Handler mHandler;

    public DreameLayout(Context context) {
        super(context);

    }

    public DreameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DreameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }
    public void addChild(View v){
        addView(v);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int contentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int contentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        int dimensionX = contentWidth ;
        int dimensionY =  contentHeight ;
        setMeasuredDimension(dimensionX, dimensionY);
        measureChildren(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View viewA = getChildAt(0);
        viewA.layout(getMeasuredWidth() / 2 - mRadiusMax / 4, getMeasuredHeight() / 2 - mRadiusMax / 4,
                getMeasuredWidth() / 2 + mRadiusMax / 4, getMeasuredHeight() / 2 + mRadiusMax / 4);

        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int rad = Math.max(view.getMeasuredWidth(),view.getMeasuredHeight())/2;

            int x = (int) ((mRadiusMax/3+rad)*Math.sin(i*Math.PI/4));
            int y = (int) ((mRadiusMax/3+rad)*Math.cos(i*Math.PI/4));
            x= getMeasuredWidth()/2+x;
            y = getMeasuredHeight()/2+y;
            view.layout(x-rad,y-rad,x+rad,y+rad);
        }
    }

    protected void init() {
        mRadiusMax = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        mLayoutPaint = new Paint();
        mLayoutPaint.setAntiAlias(true);
        RadialGradient gradient = new RadialGradient(width / 2, height / 2, mRadiusMax, new int[]{centerColor,centerColor,edgeColor},null , Shader.TileMode.MIRROR);
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
