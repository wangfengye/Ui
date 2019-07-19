package com.maple.common.explosion;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.ArrayList;

public class ExplosionField extends View {
    public ExplosionField(Context context) {
        this(context, null);
    }

    public ExplosionField(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExplosionField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public static ExplosionField with(Context context) {
        return new ExplosionField(context);
    }

    public ExplosionField factory(ParticleFactory factory) {
        this.factory = factory;
        return this;
    }

    public ExplosionField listener(Callback listener) {
        this.callback = listener;
        return this;
    }

    private ArrayList<ExplosionAnimator> explosionAnimators;
    private ParticleFactory factory = new BoomParticleFactory();
    private Callback callback;
    private OnClickListener onClickListener;

    private void init() {
        explosionAnimators = new ArrayList<>();

        attach2Activity((Activity) getContext());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (ExplosionAnimator explosionAnimator : explosionAnimators) {
            explosionAnimator.draw(canvas);
        }
    }

    /**
     * @param view 传入groupview,会传递到子View
     */
    public void into(View view) {
        if (view instanceof ViewGroup) {
            int count = ((ViewGroup) view).getChildCount();
            for (int i = 0; i < count; i++) {
                into(((ViewGroup) view).getChildAt(i));
            }
        } else {
            view.setClickable(true);
            view.setOnClickListener(getOnClickListener());
        }
    }

    private OnClickListener getOnClickListener() {
        if (null == onClickListener) {
            this.onClickListener = v -> {
                ExplosionField.this.explode(v);
                if (ExplosionField.this.callback != null) {
                    ExplosionField.this.callback.onClick(v);
                }
            };
        }
        return onClickListener;
    }

    //开启入口
    private void explode(View v) {
        // 确定v位置
        Rect rect = new Rect();
        v.getGlobalVisibleRect(rect);
        int contentTop = ((ViewGroup) this.getParent()).getTop();//actionBar高度
        Rect frame = new Rect();
        ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;//状态栏高度
        rect.offset(0, -contentTop - statusBarHeight);//位置对齐
        //震动
        ValueAnimator animator = new ValueAnimator().ofFloat(0, 1).setDuration(150);
        animator.addUpdateListener(anim -> {
                    v.setTranslationX((Util.RANDOM.nextFloat() - .5f) * getRootView().getWidth() * .05f);
                    v.setTranslationY((Util.RANDOM.nextFloat() - .5f) * getRootView().getHeight() * .05f);
                }
        );
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                v.setTranslationX(0);
                v.setTranslationY(0);
                explode2(v, rect);
            }
        });
        animator.start();
    }

    private void explode2(View v, Rect rect) {
        ExplosionAnimator animator = new ExplosionAnimator(this, Util.createBitmapFromView(v), rect, factory);
        explosionAnimators.add(animator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                v.setClickable(false);
                v.animate().setDuration(150L).scaleX(0).scaleY(0).alpha(0).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.animate().scaleX(1).scaleY(1).alpha(1).start();
                v.setClickable(true);
                explosionAnimators.remove(animation);
            }
        });
        animator.start();
    }

    /**
     * 添加到根布局
     *
     * @param activity activity
     */
    private void attach2Activity(Activity activity) {
        ViewGroup rootView = activity.findViewById(Window.ID_ANDROID_CONTENT);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rootView.addView(this, lp);
    }

    public interface Callback {
        void onClick(View view);
    }

}
