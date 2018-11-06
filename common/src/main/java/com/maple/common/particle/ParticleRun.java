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
    public Particle center;
    public boolean finished;
    private static final double SPRING = .01;//弹性系数
    private static final double FRICTION = .9;//摩擦系数
    private static final int FOCUS_POSITION = 1200;//默认z轴高度

    public ParticleRun(Particle p) {
        center = p;
        x = 0;
        y = 0;
        z = 0;
        vx = 0;
        vy = 0;
        vz = 0;
        color = p.getColor();
        finished = true;
    }

    public void setEndPosition(Particle end) {
        if (finished) {
            this.ex = end.x - center.x;
            this.ey = end.y - center.y;
            this.ez = end.z - center.z;
            this.color = end.getColor();
            finished = false;
        } else {
            throw new RuntimeException("上一个动作未完成,无法设置下一个动作");
        }

    }

    private void step() {
        // 弹力模型,距离目标越远速度越快
        vx += (float) ((ex - x) * SPRING);
        vy += (float) ((ey - y) * SPRING);
        vz += (float) ((ez - z) * SPRING);
        // 阻力模型: 让粒子可以趋向稳定
        vx *= FRICTION;
        vy *= FRICTION;
        vz *= FRICTION;

        x += vx;
        y += vy;
        z += vz;
    }

    public Particle getAxis2D() {
        step();
        //3D 坐标下的 2D 偏移，暂且只考虑位置，不考虑大小变化
        float scale = FOCUS_POSITION / (FOCUS_POSITION + this.z);
        if (Math.abs(x - ex)<1 && Math.abs(y - ey)<1) finished = true;
        return new Particle(this.center.x + this.x * scale, this.center.y + y * scale);

    }
}
