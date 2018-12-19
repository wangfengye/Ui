package com.maple.common.dreame;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * @author maple on 2018/12/19 10:42.
 * @version v1.0
 * @see 1040441325@qq.com
 * 添加增删View的动画
 */
public class AnimDreamLayout extends DreamLayout {
    public AnimDreamLayout(Context context) {
        super(context);
    }

    public AnimDreamLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimDreamLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addChild(View v) {
        super.addChild(v);
        // 添加后执行的动画
        ObjectAnimator anim = ObjectAnimator.ofFloat(v, "alpha", 0f, 1f);
        anim.setDuration(1000);
        anim.start();
    }

    @Override
    protected void removeChild(int i) {
        // 添加删除动画,监听动画完成后删除view,及记录view位置的点
        View view = getChildAt(i + 1);
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        anim1.setInterpolator(new AccelerateDecelerateInterpolator());
        anim1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                AnimDreamLayout.this.removeChild(i);
            }
        });
        anim1.setDuration(3000);
        anim1.start();
    }
}
