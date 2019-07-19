package com.maple.common.explosion;

import android.graphics.Bitmap;
import android.graphics.Rect;

public abstract class ParticleFactory {
    //图片粒子化
    public abstract  Particle[][] generateParticles(Bitmap bitmap, Rect rect);
}
