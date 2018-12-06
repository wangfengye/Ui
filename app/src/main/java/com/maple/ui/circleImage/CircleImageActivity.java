package com.maple.ui.circleImage;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import com.maple.common.circle.CircleImageView;
import com.maple.ui.R;

public class CircleImageActivity extends AppCompatActivity {
    public static final String TITLE = "带角标的圆角图片";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_image);
        CircleImageView cimg = findViewById(R.id.cimg);
        SeekBar bar = findViewById(R.id.bar);
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float max =1f;
                cimg.setBorderWidth(max*progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        Switch sw = findViewById(R.id.sw);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cimg.setSrcType(isChecked?CircleImageView.TYPE_NO_COLOR:CircleImageView.TYPE_NORMAL);
            }
        });
    }

}
