package com.maple.common.slid;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.maple.common.R;

/**
 * Created by maple on 2019/8/4 14:18
 */
@CoordinatorLayout.DefaultBehavior(SlidingCardBehavior.class)
public class SlidingCardLayout extends FrameLayout {//子View实现NestedScrollingChild2
    private TextView header;
    private int mHeaderHeight = 144;

    public SlidingCardLayout(@NonNull Context context) {
        this(context, null);
    }

    public SlidingCardLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    SimpleAdapter adapter;

    public SlidingCardLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_slid, this);
        header = findViewById(R.id.tv_header);
        RecyclerView list = findViewById(R.id.list);
        adapter = new SimpleAdapter();
        list.setAdapter(adapter);
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingCardLayout);
        header.setText(a.getText(R.styleable.SlidingCardLayout_text));
        header.setBackgroundColor(a.getColor(R.styleable.SlidingCardLayout_colorBackground, Color.WHITE));
        a.recycle();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            mHeaderHeight = findViewById(R.id.tv_header).getMeasuredHeight();
            Log.i("slid", "onSizeChanged: " + mHeaderHeight + "----" + header.getText().toString());
        }
    }


    public int getHeaderHeight() {
        Log.i("slid", "getHeaderHeight: " + mHeaderHeight + "----" + header.getText().toString());
        return mHeaderHeight;
    }

}
