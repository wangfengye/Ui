package com.maple.common.particle;

import android.graphics.PointF;

import java.util.ArrayList;

public class ParticleRunList {
    ArrayList<ParticleRun> particles;
    private boolean finished;


    public ParticleRunList() {
        this.particles = new ArrayList<>();
    }

    public void setParticles(ArrayList<Particle> points) {
        for (Particle point : points) {
            particles.add(new ParticleRun(point));
        }
        finished =true;
    }

    public void setEndState(ArrayList<Particle> points) {
        if (!finished)throw new RuntimeException("上一个动作未完成,无法设置下一个动作");
        for (int i = 0; i < particles.size(); i++) {
            particles.get(i).setEndPosition(points.get(i));
        }
        finished =false;
    }

    public ArrayList<Particle> go() {
        ArrayList<Particle> points = new ArrayList<>();
        boolean finish = true;
        for (int i = 0; i < particles.size(); i++) {
            Particle pointF = particles.get(i).getAxis2D();
            finish = finish&& particles.get(i).finished;
            points.add(pointF);
        }
        finished= finish;
        return points;
    }

    public boolean isFinished() {
        return finished;
    }

}
