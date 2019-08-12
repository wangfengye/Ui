package com.maple.common;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by maple on 2019/8/11 16:23
 * todo margin的实现
 * GestureDetectorCompat 手势识别
 * ViewDragHelper 拖拽帮助类
 */
public class DragImageGroup extends LinearLayout {

    public static final String TAG = "DragImageGroup";
    public static final String DRAGGING_VIEW = "draggingView";
    private ViewDragHelper mDragger;
    private static final int INTERCEPT_TIME_SLOP = 200;//onClick事件时间阈值
    private View mDragView;
    private boolean dragging;

    public DragImageGroup(Context context) {
        this(context, null);
    }

    public DragImageGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragImageGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //参数 关联的父布局,灵敏度,回调实例
        mDragger = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
                if (changedView==mDragView){
                    int x = (changedView.getLeft()+changedView.getRight())/2;
                    int y = (changedView.getTop()+changedView.getBottom())/2;
                    int id = getAtViewId(x,y);
                    Log.i(TAG, "onTouchEvent: "+views.size());
                    if (id>=0&&mDragView!=null){
                        int cur = views.indexOf(mDragView);
                        views.set(cur,views.get(id));
                        views.set(id,mDragView);
                        requestLayout();
                        invalidate();
                    }
                }

            }

            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                // 判断是否对此子View拖拽事件进行捕获.
                if (mDragView!=null)return false;
                mDragView = child;
                mDragView.setTag(DRAGGING_VIEW);
                bringChildToFront(mDragView);
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {

                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {

                return top;
            }

            //手指释放回调
            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                mDragView.setTag("");
                mDragView=null;
                dragging=false;
                requestLayout();
                invalidate();
            }

            // 开启边缘滑动后,触发边缘滑动.
            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                //  mDragger.captureChildView();
            }
        });
        mDragger.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);

    }

    private long time;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (time > 0 && System.currentTimeMillis() - time > INTERCEPT_TIME_SLOP) return true;
        mDragger.shouldInterceptTouchEvent(ev);//mDragger必须捕获down事件否则后续事件无法触发.
        int action = ev.getActionMasked();
        if (action == MotionEvent.ACTION_DOWN) {
            mDragger.processTouchEvent(ev);
        }
        return false;
    }

    ArrayList<View> views = null;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int marginLeft = 0;
        if (views == null) {
            views=new ArrayList<>();
            for (int i = 0; i < getChildCount(); i++) {
                views.add(getChildAt(i));
            }
        }
        for (int i = 0; i < getChildCount(); i++) {
            int margin = 2 * 3;
            View child = views.get(i);
            if (dragging&&child.getTag() != null && child.getTag().equals(DRAGGING_VIEW)) {
                child.layout(margin + marginLeft + child.getMeasuredWidth() / 4,
                        margin + child.getMeasuredHeight() / 4,
                        margin + child.getMeasuredWidth() + marginLeft - child.getMeasuredWidth() / 4,
                        margin + child.getMeasuredHeight() - child.getMeasuredHeight() / 4);
            } else {
                child.layout(margin + marginLeft, margin, margin + child.getMeasuredWidth() + marginLeft, margin + child.getMeasuredHeight());
            }
            marginLeft += margin + child.getMeasuredWidth();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragger.processTouchEvent(event);
        return true;
    }

    private int getAtViewId(float x, float y) {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = views.get(i);
            if (child instanceof TextView)
            Log.i(TAG, "getAtViewId: "+((TextView) child).getText().toString()+child.getLeft()+"---"+child.getRight()+"---"+x);
            if (child.getTag() != null && child.getTag().equals(DRAGGING_VIEW)) continue;
            if (child.getLeft() < x && x < child.getRight()
                    && child.getTop() < y && y < child.getBottom()) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "dispatchTouchEvent: ");
        boolean handle;
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            time = System.currentTimeMillis();
            handle = super.dispatchTouchEvent(ev);
             //  bringChildToFront(getChildAt(1));
            getParent().requestDisallowInterceptTouchEvent(false);
            if (handle) return true;
        }
        if (onInterceptTouchEvent(ev)) {
            if (!dragging){//判断进入group拖拽事件
                dragging =true;
                requestLayout();
                invalidate();
                Log.i(TAG, "dispatchTouchEvent: group move start");
            }
            onTouchEvent(ev);
            return true;
        } else {
            return super.dispatchTouchEvent(ev);
        }

    }


}
