package com.maple.common.bezier;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;


/**
 * @author maple
 * @version v1.0
 * created on 2018/11/21.
 * @see 1040441325@qq.com
 */
public class CircleView extends GridComputingView {
    private int mRadius;
    private Paint mPaint;

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void setPoints() {
        mRadius = 300;
        addPoint(1, 0, mRadius);
        addPoint(11, -mRadius / 2, mRadius);
        addPoint(12, mRadius / 2, mRadius);
        addPoint(2, mRadius, 0);
        addPoint(21, mRadius, mRadius / 2);
        addPoint(22, mRadius, -mRadius / 2);
        addPoint(3, 0, -mRadius);
        addPoint(31, mRadius / 2, -mRadius);
        addPoint(32, -mRadius / 2, -mRadius);
        addPoint(4, -mRadius, 0);
        addPoint(41, -mRadius, -mRadius / 2);
        addPoint(42, -mRadius, mRadius / 2);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#409EFF"));
        mPaint.setStrokeWidth(4f);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onGridComputingDraw(Canvas canvas) {
        Path mPath = new Path();
        canvas.drawPath(mPath, mPaint);
        Point pre = getPoint(4);
        Point preB = getPoint(42);
        Point cur;
        Point curA;
        Point curB;
        mPath.moveTo(pre.x, pre.y);
        for (int i =1; i <5; i++) {
            cur = getPoint(i);
            curA = getPoint(i*10+1);
            curB = getPoint(i*10+2);
            mPath.cubicTo(preB.x, preB.y, curA.x, curA.y, cur.x, cur.y);
            drawHelpPosLine(canvas,i*10+1,i,i*10+2);
            preB =curB;
        }
        canvas.drawPath(mPath, mPaint);
    }


}
