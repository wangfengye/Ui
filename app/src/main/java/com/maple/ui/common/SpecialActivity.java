package com.maple.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.maple.common.special.GoogleLogo;
import com.maple.ui.R;

public class SpecialActivity extends AppCompatActivity {
    public static final String TITLE = "不规则图形的点击<Region>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        GoogleLogo logo = findViewById(R.id.logo);
        logo.setListener((v, data) ->
                Toast.makeText(this, data, Toast.LENGTH_SHORT).show());
    }
}
