package com.maple.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.maple.common.wave.WaveProgressView;
import com.maple.ui.R;

public class WaveActivity extends AppCompatActivity {
    public static final String TITLE = "浪花加载";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wave);
        WaveProgressView view =findViewById(R.id.wave);
        view.setCurrent(50,"50%");
    }
}
