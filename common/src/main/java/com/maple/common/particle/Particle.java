package com.maple.common.particle;

import android.graphics.Point;
import android.graphics.PointF;

import java.security.Policy;

public class Particle {
    private float radius;
    public float x;
    public float y;
    public float z;
    private int color;

    public Particle(float x, float y) {
        this.radius = 4;
        this.x = x;
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
