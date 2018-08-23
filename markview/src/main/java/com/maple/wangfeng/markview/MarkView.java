package com.maple.wangfeng.markview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class MarkView extends android.support.v7.widget.AppCompatImageView {
    Paint mBgPaint;
    private int mBgColor = Color.parseColor("#00ff00");
    private int mRoundPer = 0;
    Paint mBorderPaint;
    private int mBorderColor = Color.parseColor("#0000ff");
    private boolean mClicked;

    public int getRoundPer() {
        return mRoundPer;
    }
    public void setClicked(boolean clicked){
        mClicked = clicked;
        postInvalidate();
    }

    public void setBorderColor(int color) {
        if (mBorderColor != color) {
            mBorderColor = color;
            mBorderPaint.setColor(color);
            postInvalidate();
        }
    }

    /**
     * 设置圆角百分比
     *
     * @param mRoundPer
     */
    public void setRoundPer(int mRoundPer) {
        this.mRoundPer = mRoundPer;
        postInvalidate();
    }

    public MarkView(Context context) {
        this(context, null);
    }

    public MarkView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MarkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mBgPaint = new Paint();
        mBgPaint.setColor(mBgColor);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBorderPaint = new Paint();
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(4f);
        mBorderPaint.setAntiAlias(true);
        this.setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        RectF rectF = new RectF();
        rectF.left = 2f;
        rectF.top = 2f;
        rectF.right = getWidth() - 2f;
        rectF.bottom = getHeight() - 2f;
        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(makeDst(getWidth(), getHeight()), 0, 0, mBgPaint);
        mBgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(drawableToBitmap(getDrawable()), null, rectF, mBgPaint);
        mBgPaint.setXfermode(null);
        canvas.restoreToCount(sc);
        canvas.drawRoundRect(rectF, mRoundPer * getWidth() / 200, mRoundPer * getWidth() / 200, mBorderPaint);
        if (mClicked){
            mBgPaint.setColor(Color.parseColor("#66eeeeee"));
            canvas.drawRoundRect(rectF, mRoundPer * getWidth() / 200, mRoundPer * getWidth() / 200, mBgPaint);
            mBgPaint.setColor(Color.BLUE);
        }
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = null;
        try {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(0xFFFFCC44);
        RectF rectF = new RectF();
        rectF.left = 2f;
        rectF.top = 2f;
        rectF.right = getWidth() - 2f;
        rectF.bottom = getHeight() - 2f;
        c.drawRoundRect(rectF, mRoundPer * getWidth() / 200, mRoundPer * getWidth() / 200, mBgPaint);
        return bm;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("tag", "onTouchEvent: " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setClicked(true);
                break;
            case MotionEvent.ACTION_UP:
                setClicked(false);
                break;
        }
        return super.onTouchEvent(event);

    }
}
