package com.maple.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.maple.ui.R;

public class SlidActivity extends AppCompatActivity {
    public static final String TITLE = "关联滚动(Behavior)";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slid);
    }
}
