package com.maple.ui.overlapping;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
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
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
        //super.onDraw(canvas);
        //canvas.drawARGB(255, 139, 197, 186);
      /*  int layerId = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        mPaint1.setColor(Color.parseColor("#FFFFCC44"));
        canvas.drawCircle(mWidth/3,mWidth/3,mWidth/3,mPaint1);
        mPaint1.setXfermode(new PorterDuffXfermode(mMode));
        mPaint1.setColor(Color.parseColor("#FF66AAFF"));
        canvas.drawRect(mWidth/3,mWidth/3,mWidth,mWidth,mPaint1);
        canvas.restoreToCount(layerId);
        mPaint1.setXfermode(null);*/
        Bitmap src = makeSrc(getWidth(), getHeight());
        Bitmap dst = makeDst(getWidth(), getHeight());
        canvas.drawBitmap(dst,0,0,mPaint1);
        mPaint1.setXfermode(new PorterDuffXfermode(mMode));
        canvas.drawBitmap(src,0,0,mPaint1);
        mPaint1.setXfermode(null);


    }
    public Bitmap makeDst(int w, int h){
        Bitmap bm = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCC44);
        c.drawOval(new RectF(0,0,w*3/4,h*3/4),p);
        return bm;
    }
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xFF66AAFF);
        c.drawRect(w/3, h/3, w*19/20, h*19/20, p);
        return bm;
    }
}
