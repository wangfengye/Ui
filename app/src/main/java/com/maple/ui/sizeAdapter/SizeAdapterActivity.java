package com.maple.ui.sizeAdapter;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.maple.ui.R;

public class SizeAdapterActivity extends AppCompatActivity {
    public static final String TITLE = "UI适配";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomDensity.auto(true,1200,this);
        setContentView(R.layout.activity_size_adapter);
        TextView tvUi = findViewById(R.id.tv_ui);
        tvUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SizeAdapterActivity.this,Size2Activity.class));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        CustomDensity.cancelAuto(this);
    }
}
