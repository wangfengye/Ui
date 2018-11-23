package com.maple.ui.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.maple.ui.R;

public class BezierActivity extends AppCompatActivity {
    public static final String TITLE = "贝塞尔曲线";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bezier);
    }
}
