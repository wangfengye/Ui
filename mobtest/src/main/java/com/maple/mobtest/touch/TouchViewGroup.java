package com.maple.mobtest.touch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class TouchViewGroup extends ViewGroup {


    public static final String TAG = TouchViewGroup.class.getSimpleName();

    public TouchViewGroup(Context context) {
        super(context);
    }

    public TouchViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0, size = getChildCount(); i < size; i++) {
            View view = getChildAt(i);
            int mar = (r - l - view.getMeasuredWidth()) / 2;
            view.layout(mar, 0, r - mar, view.getMeasuredHeight());
        }

    }


    private boolean mOnIntercept;
    private boolean mOnIouch;



    public void setOnIntercept(boolean onIntercept) {
        this.mOnIntercept = onIntercept;
    }

    public void setOnIouch(boolean onIouch) {
        this.mOnIouch = onIouch;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "dispatchTouchEvent: " + Util.getActionName(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.i(TAG, "onInterceptTouchEvent: " + Util.getActionName(ev.getAction()));
        return mOnIntercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: " + Util.getActionName(event.getAction()));
        return mOnIouch;
    }
}
