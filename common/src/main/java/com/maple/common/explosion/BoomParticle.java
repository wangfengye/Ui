package com.maple.common.explosion;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

public class BoomParticle extends Particle {
    static Random random = new Random();
    float radius = BoomParticleFactory.PART_WH;
    float alpha;
    Rect mBound;

    public BoomParticle(float cx, float cy, int color, Rect rect) {
        super(cx, cy, color);
        mBound = rect;
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        paint.setAlpha((int) (Color.alpha(color) * alpha));
        canvas.drawCircle(cx, cy, radius, paint);
    }

    @Override
    protected void calculate(float factor) {
        cx = cx + factor * random.nextInt(mBound.width()) * (random.nextFloat() - .5f);
        cy = cy + factor * random.nextInt(mBound.height()) * (random.nextFloat() - .5f);
        radius = radius - factor * random.nextInt(2);
        alpha = (1f - factor) * (1 + random.nextFloat());
    }
}
