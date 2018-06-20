package com.maple.ui.GithubView;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by fengye on 2018/5/20.
 * email 1040441325@qq.com
 */

public class GithubActivityView extends View {
    public static final String TAG = GithubActivityView.class.getSimpleName();
    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private GestureDetector mDetector;
    private int mWidth;
    private Integer[][] data;
    private Paint mPaint;
    private float mColumnTitleHeight;
    private float mRowTitleWidth;
    private int mSpeed = 12;
    private float mSpaceWidth = 5f;

    private int mLastX = 0;
    private int mStartX = 0;
    public OnLoadListener mListener;
    float moveX = 0;
    private float moveY;

    public GithubActivityView(Context context) {
        this(context, null);
    }

    public GithubActivityView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GithubActivityView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDetector = new GestureDetector(context, new Gesture());
        init();
    }

    public void setData(Integer[][] data) {
        this.data = data;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        creatImage(canvas);
    }

    private void creatImage(Canvas canvas) {
        if (data == null) return;
        int column = data[0].length;
        float radius = (mWidth - mSpaceWidth * column) / (column + 1.6f);
        int row = data.length;
        mColumnTitleHeight = radius + mSpaceWidth;
        mRowTitleWidth = (radius * 1.6f + mSpaceWidth) * 1;
        mPaint.setTextSize(radius * .8f);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(Color.parseColor("#606266"));
        for (int i = 0; i < column; i++) {
            if (i % 4 == 0) {
                canvas.drawText(String.valueOf(i) + ":00", mRowTitleWidth + i * (radius + mSpaceWidth), radius, mPaint);
                canvas.save();
            }
        }
        mPaint.setTextAlign(Paint.Align.RIGHT);
        for (int i = 0; i < row; i++) {
            // y坐标多减 一个mSpaceWidth 为了text视觉效果
            canvas.drawText(getWeek(i), mRowTitleWidth - mSpaceWidth, i * (radius + mSpaceWidth) + radius + mColumnTitleHeight - mSpaceWidth, mPaint);
            canvas.save();
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                Path path = new Path();
                path.moveTo(moveX + mRowTitleWidth + j * (radius + mSpaceWidth), mColumnTitleHeight + i * (radius + mSpaceWidth) + moveY);
                path.lineTo(moveX + mRowTitleWidth + j * (radius + mSpaceWidth), mColumnTitleHeight + i * (radius + mSpaceWidth) + radius + moveY);
                path.lineTo(moveX + mRowTitleWidth + j * (radius + mSpaceWidth) + radius, mColumnTitleHeight + i * (radius + mSpaceWidth) + radius + moveY);
                path.lineTo(moveX + mRowTitleWidth + j * (radius + mSpaceWidth) + radius, mColumnTitleHeight + i * (radius + mSpaceWidth) + moveY);
                mPaint.setColor(getColor(data[i][j]));
                canvas.drawPath(path, mPaint);
                canvas.save();
            }
        }
        // 绘制标注
        mPaint.setTextAlign(Paint.Align.RIGHT);
        mPaint.setColor(getColor(0));
        canvas.drawText("最少", mRowTitleWidth - mSpaceWidth, 8 * (radius + mSpaceWidth) - mSpaceWidth + radius + mColumnTitleHeight, mPaint);
        for (int i = 0; i < 5; i++) {
            Path path = new Path();
            path.moveTo(mRowTitleWidth + i * (radius + mSpaceWidth), mColumnTitleHeight + 8 * (radius + mSpaceWidth));
            path.lineTo(mRowTitleWidth + i * (radius + mSpaceWidth), mColumnTitleHeight + 8 * (radius + mSpaceWidth) + radius);
            path.lineTo(mRowTitleWidth + i * (radius + mSpaceWidth) + radius, mColumnTitleHeight + 8 * (radius + mSpaceWidth) + radius);
            path.lineTo(mRowTitleWidth + i * (radius + mSpaceWidth) + radius, mColumnTitleHeight + 8 * (radius + mSpaceWidth));
            mPaint.setColor(getColor(i * mSpeed));
            canvas.drawPath(path, mPaint);
        }
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setColor(getColor(4 * mSpeed));
        canvas.drawText("最多", mRowTitleWidth + 5 * (radius + mSpaceWidth), 8 * (radius + mSpaceWidth) + radius - mSpaceWidth + mColumnTitleHeight, mPaint);
    }

    public int getColor(Integer i) {
        if (i == null) i = 0;
        i = i / mSpeed;
        switch (i) {
            case 0:
                return Color.parseColor("#eeeeee");
            case 1:
                return Color.parseColor("#c6e48b");
            case 2:
                return Color.parseColor("#7bc96f");
            case 3:
                return Color.parseColor("#239a3b");
            case 4:
                return Color.parseColor("#196127");
            default:
                return Color.parseColor("#196127");
        }
    }

    public String getWeek(int day) {
        day += 1;
        day = day % 7;
        switch (day) {
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            case 0:
                return "周日";
        }
        return "未知";
    }

    public interface OnLoadListener {
        void load(int i);
    }

    private class Gesture implements GestureDetector.OnGestureListener {


        @Override
        public boolean onDown(MotionEvent event) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent event) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent event, MotionEvent event1, float v, float v1) {
            moveX -= v;
            moveY -= v1;
            invalidate();
            return false;
        }

        @Override
        public void onLongPress(MotionEvent event) {
            Log.i(TAG, "onLongPress: " + event.toString());
        }

        @Override
        public boolean onFling(MotionEvent event, MotionEvent event1, float v, float v1) {
            return false;
        }
    }
}
