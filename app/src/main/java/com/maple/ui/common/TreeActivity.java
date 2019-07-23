package com.maple.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.maple.ui.R;

public class TreeActivity extends AppCompatActivity {
    public static final String TITLE = "树生长动画";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
    }
}
