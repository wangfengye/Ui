package com.maple.ui.sizeAdapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.maple.ui.R;
import com.maple.wangfeng.customdensity.CustomDensity;

public class SizeAdapterActivity extends AppCompatActivity {
    public static final String TITLE = "UI适配";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDensity.auto(CustomDensity.HEIGHT,720,this);
        setContentView(R.layout.activity_size_adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomDensity.cancelAuto(this);
    }
}
