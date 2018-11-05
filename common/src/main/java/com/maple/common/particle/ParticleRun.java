package com.maple.common.particle;

import android.graphics.PointF;

/**
 * 3D 粒子运动模拟器
 */
public class ParticleRun {
    private float x, y, z;
    private float vx, vy, vz;
    private float ex, ey, ez;
    private int color;
    private PointF center;
    private double SPRING = .01;//弹性系数
    private double FRICTION = .9;//摩擦系数
    private int FOCUS_POSITION = 1200;//默认z轴过度

    public ParticleRun(PointF p) {
        center = p;
        x = 0;
        y = 0;
        z = 0;
        vx = 0;
        vy = 0;
        vz = 0;
    }

    public void setEndPosition(int x, int y, int z) {
        this.ex = x;
        this.ey = y;
        this.ez = z;
        this.color = 0;
    }

    private void step() {
        // 弹力模型,距离目标越远速度越快
        vx = (float) ((ex - x) * SPRING);
        vy = (float) ((ey - y) * SPRING);
        vz = (float) ((ez - z) * SPRING);
        // 阻力模型: 让粒子可以趋向稳定
        vx *= FRICTION;
        vy *= FRICTION;
        vz *= FRICTION;

        x += vx;
        y += vy;
        z += vz;
    }

    public PointF getAxis2D() {
        step();
        //3D 坐标下的 2D 偏移，暂且只考虑位置，不考虑大小变化
        float scale = FOCUS_POSITION / (FOCUS_POSITION + this.z);
        if (x != ex && y != ey)
            return new PointF(this.center.x + this.x * scale, this.center.y + y * scale);
        return null;
    }
}
