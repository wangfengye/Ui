package com.maple.common.particle;

import android.graphics.Point;
import android.graphics.PointF;

import java.security.Policy;

public class Particle {
    private float radius;
    private double x;
    private double y;

    public Particle(float radius, double x, double y) {
        this.radius = radius;
        this.x = x;
        this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
