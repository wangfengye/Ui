package com.maple.common.particle;

import android.content.Context;
import android.graphics.*;
import android.icu.util.Measure;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 粒子化文字
 */
public class ParticleTextView extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    private String mText;//当前文字
    private LinkedList<String> mNextStrs;//之后的文字
    private Paint textPaint;
    private SurfaceHolder mHolder;
    private boolean mRunning;
    private ParticleRunList mParticleRunList;
    private int mTextSize = 1 << 8;

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

    public void setNextString(String str) {
        this.mNextStrs.offer(str);
    }

    public void setTextSizeDp(int size) {
        final float scale = getResources().getDisplayMetrics().density;
        this.mTextSize = (int) (size * scale + 0.5f);
    }

    private void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        textPaint = new Paint();
        textPaint.setColor(Color.parseColor("#3399ff"));
        textPaint.setAntiAlias(false);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);
        mNextStrs = new LinkedList<>();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mRunning = true;
        mParticleRunList = new ParticleRunList();
        ArrayList<Particle> points = getDataFromImg(getTextBitmap(mText));
        mParticleRunList.setParticles(points);
        if (!mNextStrs.isEmpty()) {
            mParticleRunList.setEndState(getDataFromImg(getTextBitmap(mNextStrs.poll())));
        }
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
        ArrayList<Particle> points = mParticleRunList.go();
        Log.i("TAA", "draw: " + mParticleRunList.isFinished());
        if (mParticleRunList.isFinished() && !mNextStrs.isEmpty()) {

            mParticleRunList.setEndState(getDataFromImg(getTextBitmap(mNextStrs.poll())));
        }
        Canvas canvas = mHolder.lockCanvas();
        if (canvas != null) {
            try {
                // 重绘背景
                canvas.drawRGB(255, 255, 255);
                for (int i = 0; i < points.size(); i++) {
                    canvas.drawCircle(points.get(i).x, points.get(i).y, 3, textPaint);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mHolder.unlockCanvasAndPost(canvas);
            }

        }
    }

    private Bitmap getTextBitmap(String text) {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        textPaint.setTextSize(mTextSize);
        canvas.drawText(text, getWidth() / 2, getHeight() / 2, textPaint);
        return bitmap;
    }

    /**
     * 获取像素数组,并转化为点阵
     *
     * @param bitmap bitmap
     * @return point集合
     */
    private ArrayList<Particle> getDataFromImg(Bitmap bitmap) {
        ArrayList<Particle> points = new ArrayList<>();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        // 获取像素
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        // 转化
        for (int i = 0; i < width * height; i++) {
            if (pixels[i] != 0) {
                points.add(new Particle((i % width), (i / width)));
            }
        }
        ArrayList<Particle> points1 = new ArrayList<>();
        for (int i = 0; i < mTextSize << 2; i++) {
            int index = (int) (Math.random() * points.size());
            points1.add(points.get(index));
        }
        return points1;
    }
}
