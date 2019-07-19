package com.maple.common.explosion;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Particle {
    protected float cx;
    protected float cy;
    protected int color;

    public Particle(float cx, float cy, int color) {
        this.cx = cx;
        this.cy = cy;
        this.color = color;
    }
    // 位移
    public void  advance(Canvas canvas, Paint paint,float factor){
        // 计算下一刻位置
        calculate(factor);
        draw(canvas,paint);
    }

    protected abstract void draw(Canvas canvas, Paint paint);

    protected abstract void calculate(float factor);
}
