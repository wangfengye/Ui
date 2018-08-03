package com.maple.ui.overlapping;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fengye on 2018/8/3.
 * email 1040441325@qq.com
 */

public class OverlappingTestView extends View{
    private Paint mPaint1;
    private Paint mPaint2;
    private int mWidth;
    private PorterDuff.Mode mMode = PorterDuff.Mode.DST;

    public PorterDuff.Mode getMode() {
        return mMode;
    }

    public void setMode(PorterDuff.Mode mode) {
        mMode = mode;
        postInvalidate();
    }

    public OverlappingTestView(Context context) {
        this(context,null);
    }

    public OverlappingTestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public OverlappingTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr,0);
    }

    public OverlappingTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    private void initPaint() {
        mPaint1= new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setColor(Color.parseColor("#FFFFCC44"));
        mPaint2= new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setColor(Color.parseColor("#FF66AAFF"));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(255, 139, 197, 186);
        int layerId = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(mWidth/3,mWidth/3,mWidth/3,mPaint1);
        canvas.save();
        mPaint2.setXfermode(new PorterDuffXfermode(mMode));


        canvas.drawRect(mWidth/3,mWidth/3,mWidth,mWidth,mPaint2);
        canvas.restoreToCount(layerId);
    }
}
