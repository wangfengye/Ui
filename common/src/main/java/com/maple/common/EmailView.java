package com.maple.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by maple on 2019/8/23 16:08
 */
public class EmailView extends FrameLayout {
    private Paint mPaint;
    private Path mPath;
    private int backGroundColor = Color.WHITE;

    public EmailView(Context context) {
        this(context, null);
    }

    public EmailView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EmailView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setWillNotDraw(false);//系统在ViewGroup会根据背景等属性判断是否调用自己的ondraw,该方法强制进行调用
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(backGroundColor);
        mPath = new Path();

    }

    @Override
    protected void onDraw(Canvas canvas) {        super.onDraw(canvas);
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.O_MR1){// 8.1以下关闭硬件加速
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        int w = getWidth();
        int h = getHeight();
        mPath.moveTo(0, 0 + 10);
        mPath.quadTo(w / 2, h / 4, w, 0 + 10);
        mPath.lineTo(w, h);
        mPath.lineTo(0, h);
        mPath.close();
        mPaint.setShadowLayer(10f, 0, -7f, 0xaa888888);
        canvas.drawPath(mPath, mPaint);
    }
}
