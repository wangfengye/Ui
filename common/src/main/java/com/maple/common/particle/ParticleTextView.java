package com.maple.common.particle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.icu.util.Measure;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * 粒子化文字
 */
public class ParticleTextView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private String mText ="abcd";
    private int color;
    private Paint textPaint;
    private ParticleRun run;
    private SurfaceHolder mHolder;
    private boolean mRunning;

    public ParticleTextView(Context context) {
        this(context, null);
    }

    public ParticleTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ParticleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#3399ff"));
        textPaint.setAntiAlias(false);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        run = new ParticleRun(new PointF(0, 0));
        run.setEndPosition(1000, 1000, 0);

    }

    private void setParticles(Canvas canvas) {
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        canvas.drawText(mText, centerX, centerY, textPaint);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRunning = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mRunning = false;
    }

    @Override
    public void run() {
        while (mRunning) {
            draw();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void draw() {
        Canvas canvas = mHolder.lockCanvas();
        PointF f = run.getAxis2D();
        if (canvas != null && f != null) {
            try {
                // 重绘背景
                canvas.drawRGB(255, 255, 255);
                textPaint.setTextSize(getWidth()/2);
                canvas.drawText(mText,200,200,textPaint);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mHolder.unlockCanvasAndPost(canvas);
            }

        }

    }
}
