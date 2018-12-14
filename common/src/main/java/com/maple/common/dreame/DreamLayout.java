package com.maple.common.dreame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.Random;


/**
 * @author maple on 2018/12/8 14:43.
 * @version v1.0
 * @see 1040441325@qq.com
 */
public class DreamLayout extends ViewGroup implements Runnable, BaseAdapter.AdapterDataObserver {
    public static final String DREAM_CENTER = "dream_center";
    public static final String TAG = DreamLayout.class.getSimpleName();
    private Paint mLayoutPaint;
    protected int centerColor = Color.argb(0, 255, 255, 255);//初始背景色
    protected int edgeColor = Color.argb(77, 255, 255, 255);//扩散背景色
    protected Handler mHandler;
    private int mRadiusMax; //整个布局的半径

    BaseAdapter mAdapter;
    private float mBlankPer = .1f;// 圆之间的最小空隙(百分比值,取圆的直径的百分比)
    ArrayList<Point> mChildrenPoints = new ArrayList<>();
    private float[] data;
    private int mCenterRadius;

    public DreamLayout(Context context) {
        super(context);
    }

    public DreamLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DreamLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        init();
    }


    public void addChild(View v) {
        addView(v);
        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        anim.setDuration(1000);
        anim.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int contentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int contentHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int dimensionX = contentWidth;
        int dimensionY = contentHeight;
        setMeasuredDimension(dimensionX, dimensionY);
        int len = Math.min(contentWidth, contentHeight);
        measureChildren(MeasureSpec.makeMeasureSpec(len, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(len, MeasureSpec.AT_MOST));
    }


    public void setCenterView(View view) {
        if (getChildAt(0) != null && getChildAt(0).getTag().equals(DREAM_CENTER)) {
            removeView(getChildAt(0));
        }
        int radius = (int) (Math.max(view.getMeasuredWidth(), view.getMeasuredHeight()) * 1.2 / 2);

        view.setTag(DREAM_CENTER);
        addView(view, 0);
        Log.i(TAG, "setCenterView: ");
    }


    public void setAdapter(BaseAdapter baseAdapter) {
        mAdapter = baseAdapter;
        setCenterView(baseAdapter.onCreateCenter(this));

        for (int i = 0; i < baseAdapter.getList().size(); i++) {
            View childView = mAdapter.onCreateChild(this, mAdapter.getList().get(i));
            addChild(childView);

        }
        mAdapter.registerAdapterDataObserver(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (getChildCount() <= 0) return;
        // layout center view
        View viewC = getChildAt(0);
        mCenterRadius = Math.max(viewC.getMeasuredHeight(), viewC.getMeasuredWidth()) / 2;
        viewC.layout(getMeasuredWidth() / 2 - viewC.getMeasuredWidth() / 2, getMeasuredHeight() / 2 - viewC.getMeasuredHeight() / 2,
                getMeasuredWidth() / 2 + viewC.getMeasuredWidth() / 2, getMeasuredHeight() / 2 + viewC.getMeasuredHeight() / 2);
        // layout around view
        for (int i = 1; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int rad = Math.max(view.getMeasuredWidth(), view.getMeasuredHeight()) / 2;
            // 新增的控件

            Point position;
            if (i > mChildrenPoints.size()) {
                position = getRandomPoint(rad, mRadiusMax, getMeasuredWidth() / 2, getMeasuredHeight() / 2);
                mChildrenPoints.add(position);
            } else {
                position = mChildrenPoints.get(i - 1);
            }
            int x = position.x;
            int y = position.y;
            view.layout(x - rad, y - rad, x + rad, y + rad);

        }
    }

    protected void init() {
        mRadiusMax = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        // 初始化一个环形渐变色的画笔
        mLayoutPaint = new Paint();
        mLayoutPaint.setAntiAlias(true);
        RadialGradient gradient =
                new RadialGradient(width / 2, height / 2, mRadiusMax,
                        new int[]{centerColor, centerColor, edgeColor}, new float[]{0f, .8f, 1.0f}, Shader.TileMode.MIRROR);
        mLayoutPaint.setShader(gradient);
        //初始化三个大小不同的圆,data存储圆直径占布局半径的比例;
        data = new float[3];
        data[0] = 1 / (float) 16;
        data[1] = 1 / (float) 8;
        data[2] = 1 / (float) 4;

        mHandler = new Handler();
        mHandler.post(this);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        for (float scale : data) {
            canvas.save();
            canvas.scale(scale, scale, width / 2, height / 2);
            canvas.drawCircle(width / 2, height / 2, mRadiusMax, mLayoutPaint);
            canvas.restore();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeCallbacks(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            data[i] = (float) (data[i] * 1.02);
            if (data[i] >= 2) data[i] = 1 / (float) 8;
        }
        invalidate();
        mHandler.postDelayed(this, 32);
    }

    /**
     * 获取随机点,根据父子空间半径判断随机点是否有足够空间可用
     *
     * @return 返回随机结果
     */
    private Point getRandomPoint(int rad, int radMax, int centerX, int centerY) {
        int x = 0;
        int y = 0;
        int counter = 0;
        boolean conflict;
        Random random = new Random();
        while (counter < 100000) {
            counter++;
            x = (int) (getMeasuredWidth() / 2 - radMax + rad + random.nextInt( 2 * radMax - 2 * rad));
            y = (int) (getMeasuredHeight() / 2 - radMax + rad + random.nextInt( 2 * radMax - 2 * rad));
            conflict = false;
            for (Point p : mChildrenPoints) {
                int dis = (int) Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2));
                if (dis < 2 * rad * (1 + mBlankPer)) {
                    conflict = true;
                    break;
                }
            }
            if (!conflict) {
                int dis = (int) Math.sqrt(Math.pow(centerX - x, 2) + Math.pow(centerY - y, 2));
                if (dis < (rad + mCenterRadius) * ((1 + mBlankPer))) conflict = true;
            }
            if (!conflict) return new Point(x, y);
        }
        Log.e(TAG, "getRandomPoint: 无剩余空间");
        return new Point(0, 0);
    }


    @Override
    public void notifyDataAdded(int i) {
        addChild(mAdapter.onCreateChild(this, mAdapter.getItem(i)));
    }

    @Override
    public void notifyDataRemoved(int i) {
        removeChild(i);
    }

    private void removeChild(int i) {
        View view = getChildAt(i + 1);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        anim1.setInterpolator(new AccelerateDecelerateInterpolator());
        anim1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mChildrenPoints.remove(i);
                removeView(view);
            }
        });
        anim1.setDuration(3000);
        anim1.start();
    }
}
