package com.maple.common.slid;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;

/**
 * Created by maple on 2019/8/4 14:17
 */

public class SlidingCardBehavior extends CoordinatorLayout.Behavior<SlidingCardLayout> {
    public static final String TAG = "SlidingCardBehavior";
    private int mInitOffset;

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, SlidingCardLayout child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        //当前控件高度,父容器高度-下面各个头部的高度.
        int offset = getChildMeasureOffset(parent, child);
        int h = View.MeasureSpec.getSize(parentHeightMeasureSpec) - offset;
        child.measure(parentWidthMeasureSpec, View.MeasureSpec.makeMeasureSpec(h, View.MeasureSpec.getMode(parentHeightMeasureSpec)));
        return true;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, SlidingCardLayout child, int layoutDirection) {
        Log.i(TAG, "onLayoutChild: " + parent.indexOfChild(child));
        parent.onLayoutChild(child, layoutDirection);
        offsetY(parent, child);
        mInitOffset = child.getTop();
        return true;
    }

    private int getChildMeasureOffset(CoordinatorLayout parent, SlidingCardLayout child) {
        int offset = 0;
        for (int i = 0; i < parent.getChildCount(); i++) {
            View view = parent.getChildAt(i);
            if (view != child && view instanceof SlidingCardLayout) {
                offset += ((SlidingCardLayout) view).getHeaderHeight();
            }
        }
        return offset;
    }

    // 获取偏移值
    private void offsetY(CoordinatorLayout parent, SlidingCardLayout child) {
        int offset = 0;
        int index = parent.indexOfChild(child);
        int h = child.getHeaderHeight();
        offset = h * index;
        child.offsetTopAndBottom(offset);

    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SlidingCardLayout child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean vertical = (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
        return vertical && child == directTargetChild;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SlidingCardLayout child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        Log.i(TAG, "onNestedScroll: " + coordinatorLayout.indexOfChild(child));
        consumed[1] = scroll(child, dy, mInitOffset, mInitOffset + child.getHeight() - child.getHeaderHeight());
        shiftSlidings(consumed[1], coordinatorLayout, child);

    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SlidingCardLayout child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
     //   Log.i(TAG, "onNestedScroll: " + coordinatorLayout.indexOfChild(child));
        //控制自己的移动 offset>0-fu,height-fu
        //int shift = scroll(child, dyConsumed, mInitOffset, mInitOffset + child.getHeight() - child.getHeaderHeight());
        //控制兄弟节点的移动
       // shiftSlidings(shift, coordinatorLayout, child);


    }

    private void shiftSlidings(int shift, CoordinatorLayout coordinatorLayout, SlidingCardLayout child) {
        if (shift == 0) return;
        if (shift > 0) {//往上推
            SlidingCardLayout current = child;
            SlidingCardLayout perv = getPrev(coordinatorLayout, child);
            while (perv != null) {
                int offset = getOverlap(perv, current);
                if (offset > 0)
                    perv.offsetTopAndBottom(-offset);
                current = perv;
                perv = getPrev(coordinatorLayout, perv);
            }
        } else {//往下推
            SlidingCardLayout current = child;
            SlidingCardLayout next = getNext(coordinatorLayout, child);
            while (next != null) {
                int offset = getOverlap(current, next);
                if (offset > 0)
                    next.offsetTopAndBottom(offset);
                current = next;
                next = getNext(coordinatorLayout, next);
            }

        }
    }

    private int getOverlap(SlidingCardLayout above, SlidingCardLayout below) {
        return above.getTop() + above.getHeaderHeight() - below.getTop();
    }

    private SlidingCardLayout getNext(CoordinatorLayout parent, SlidingCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex + 1; i < parent.getChildCount(); i++) {
            View v = parent.getChildAt(i);
            if (v instanceof SlidingCardLayout) {
                return (SlidingCardLayout) v;
            }
        }
        return null;
    }

    private SlidingCardLayout getPrev(CoordinatorLayout parent, SlidingCardLayout child) {
        int cardIndex = parent.indexOfChild(child);
        for (int i = cardIndex - 1; i >= 0; i--) {
            View v = parent.getChildAt(i);
            if (v instanceof SlidingCardLayout) {
                return (SlidingCardLayout) v;
            }
        }
        return null;
    }

    private int scroll(SlidingCardLayout child, int dy, int minOffset, int maxOffset) {
        int init = child.getTop();
        int offset = clamp(init - dy, minOffset, maxOffset) - init;
        child.offsetTopAndBottom(offset);
        return -offset;
    }

    private int clamp(int i, int minOffset, int maxOffset) {
        if (i > maxOffset) return maxOffset;
        if (i < minOffset) return minOffset;
        return i;
    }
}
