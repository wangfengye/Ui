package com.maple.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.maple.common.loss.LossView;
import com.maple.ui.R;

public class LossActivity extends AppCompatActivity {
    public static final String TITLE = "震荡动画";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loss);
        LossView lossView = findViewById(R.id.loss);
        lossView.setOnClickListener(v -> lossView.start());
    }
}
