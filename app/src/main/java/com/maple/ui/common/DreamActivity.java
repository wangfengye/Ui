package com.maple.ui.common;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import com.maple.common.dreame.DreamLayout;
import com.maple.ui.R;

public class DreamActivity extends AppCompatActivity {
    public static final String TITLE = "蜗牛梦话圈";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream);
        DreamLayout dreamLayout = findViewById(R.id.dream);
        View view = LayoutInflater.from(this).inflate(R.layout.item_dream, dreamLayout, false);
        TextView tv = view.findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.item_dream, dreamLayout, false);


                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
                drawable.setCornerRadius(1080);
                view.findViewById(R.id.tv).setBackground(drawable);
                dreamLayout.addChild(view);
                anim(view);
            }
        });
        tv.setText("first");
        dreamLayout.addChild(view);
    }

    private void anim(View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        anim.setDuration(1000);
        anim.start();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        anim1.setInterpolator(new AccelerateDecelerateInterpolator());
        anim1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup group = (ViewGroup) view.getParent();
                group.removeView(view);
            }
        });
        anim1.setDuration(3000);
        AnimatorSet set = new AnimatorSet();
        set.play(anim1).after(3000).after(anim);
        set.start();
    }
}
