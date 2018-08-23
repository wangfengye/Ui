package com.maple.ui.sizeAdapter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maple.ui.R;

public class Size2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDensity.auto(true,360,this);
        setContentView(R.layout.activity_size2);
    }
}
