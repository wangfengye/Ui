package com.maple.mobtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import com.maple.mobtest.touch.TouchView;
import com.maple.mobtest.touch.TouchViewGroup;
import com.maple.mobtest.touch.Util;

public class TouchActivity extends AppCompatActivity {

    public static final String TAG = TouchActivity.class.getSimpleName();
    TouchViewGroup mVg;
    TouchView mView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
        mVg = findViewById(R.id.vg);
        mView = findViewById(R.id.view);

        ((SwitchCompat) findViewById(R.id.rb_viewGroup_onInterceptTouchEvent)).setOnCheckedChangeListener((buttonView, isChecked) -> mVg.setOnIntercept(isChecked));
        ((SwitchCompat) findViewById(R.id.rb_viewGroup_onTouchEvent)).setOnCheckedChangeListener((buttonView, isChecked) -> mVg.setOnIouch(isChecked));

        ((SwitchCompat) findViewById(R.id.rb_view_onTouchEvent)).setOnCheckedChangeListener((buttonView, isChecked) -> mView.setTouch(isChecked));

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent: " + Util.getActionName(event.getAction()));
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.i(TAG, "dispatchTouchEvent: " + Util.getActionName(ev.getAction()));
        return super.dispatchTouchEvent(ev);
    }
}
