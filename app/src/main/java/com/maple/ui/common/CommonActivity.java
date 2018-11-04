package com.maple.ui.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maple.common.DelEditText;
import com.maple.ui.R;

public class CommonActivity extends AppCompatActivity {
    public static final String TITLE = "常用自定義控件";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        DelEditText text = findViewById(R.id.ed);
        text.enableAnimal(true);
    }
}
