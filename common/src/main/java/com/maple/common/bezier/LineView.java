package com.maple.common.bezier;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @author maple on 2018/11/22.
 * @version v1.0
 * @see 1040441325@qq.com
 */
public final class LineView extends GridComputingView {
    private Paint mPaint;

    public LineView(Context context) {
        super(context);
        init();
    }

    public LineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#409EFF"));
        mPaint.setStrokeWidth(4f);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void setPoints() {
        addPoint(1, -200, -200);
        addPoint(2, 0, 0);
        addPoint(3, 100, -300);
        addPoint(4, 300, 100);
    }

    @Override
    protected void onGridComputingDraw(Canvas canvas) {
        Path mPath = new Path();
        Point p0 = getPoint(1);
        Point p1 = getPoint(2);
        Point p2 = getPoint(3);
        Point p3 = getPoint(4);
        mPath.moveTo(p0.x, p0.y);
        mPath.cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
        canvas.drawPath(mPath, mPaint);
        drawHelpPosLine(canvas,1,2);
        drawHelpPosLine(canvas,3,4);
    }
}
