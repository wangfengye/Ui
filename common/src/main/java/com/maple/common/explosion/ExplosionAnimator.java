package com.maple.common.explosion;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class ExplosionAnimator extends ValueAnimator {
    public static final int DEFAULT_DURATION = 1500;
    private int duration = DEFAULT_DURATION;
    private Particle[][] mParticles;
    private Paint mPaint;
    private View mContainer;
    private ParticleFactory mFactory;

    public ExplosionAnimator(ExplosionField view, Bitmap bitmap, Rect rect, ParticleFactory factory) {
        this.mContainer = view;
        this.mFactory = factory;
        this.mPaint = new Paint();
        this.setFloatValues(new float[]{0.0f, 1.0f});
        this.setDuration(duration);
        this.mParticles = this.mFactory.generateParticles(bitmap, rect);
        bitmap.recycle();
    }
    public void draw(Canvas canvas){
        if (!isStarted()){//动画结束
            return;
        }
        for (Particle[] mParticle : mParticles) {
            for (Particle particle : mParticle) {
                particle.advance(canvas,mPaint, (Float) getAnimatedValue());
            }
        }
        mContainer.invalidate();
    }
    @Override
    public void start() {
        super.start();
        mContainer.invalidate();
    }

}
