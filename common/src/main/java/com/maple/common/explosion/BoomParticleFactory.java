package com.maple.common.explosion;

import android.graphics.Bitmap;
import android.graphics.Rect;

public class BoomParticleFactory extends ParticleFactory {
    public static final int PART_WH = 8;

    @Override
    public Particle[][] generateParticles(Bitmap bitmap, Rect rect) {
        int w = rect.width();
        int h = rect.height();
        int wCount = w / PART_WH;
        int hCount = h / PART_WH;
        Particle[][] particles = new Particle[hCount][wCount];
        int bitmap_part_w = bitmap.getWidth() / wCount;
        int bitmap_part_h = bitmap.getHeight() / hCount;
        for (int i = 0; i < hCount; i++) {
            for (int j = 0; j < wCount; j++) {
                int color = bitmap.getPixel(j * bitmap_part_w, i * bitmap_part_h);
                float x = rect.left + PART_WH * j;
                float y = rect.top + PART_WH * i;
                particles[i][j] = new BoomParticle(x, y, color, rect);
            }
        }
        return particles;
    }
}
