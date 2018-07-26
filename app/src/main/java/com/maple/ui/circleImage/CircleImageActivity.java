package com.maple.ui.circleImage;

import android.graphics.Color;
import android.os.Bundle;

import com.maple.ui.R;
import com.maple.ui.sizeAdapter.BaseActivity;

public class CircleImageActivity extends BaseActivity {
    public static final String TITLE = "带角标的圆角图片";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_image);
        CircleImageView cimgA = findViewById(R.id.cimg_a);
        cimgA.setSrcType(CircleImageView.TYPE_PURE, Color.parseColor("#00ff00"));
        cimgA.setCorner(CircleImageView.RIGHT_TOP,Corner.newIcon(getDrawable(R.mipmap.ic_pad), getResources().getColor(R.color.colorAccent)));
        cimgA.setCorner(CircleImageView.LEFT_BOTTOM,Corner.newIcon(getDrawable(R.mipmap.ic_pad), Color.parseColor("#0000ff")));
        cimgA.setBg(getResources().getColor(R.color.colorAccent));
        CircleImageView cimgB = findViewById(R.id.cimg_b);
        cimgB.setSrcType(4, Color.parseColor("#00ff00"));
        cimgB.setCorner(CircleImageView.RIGHT_TOP,Corner.newIcon(getDrawable(R.mipmap.ic_pad), getResources().getColor(R.color.colorAccent)));
        cimgB.setCorner(CircleImageView.LEFT_BOTTOM,Corner.newIcon(getDrawable(R.mipmap.ic_pad), Color.parseColor("#0000ff")));
        cimgB.setBg(getResources().getColor(R.color.colorAccent));
    }

}
