package com.maple.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.maple.ui.R;

public class DragImageGroupActivity extends AppCompatActivity {
    public static final String TITLE = "移形换位";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_image_group);
    }

    public void click(View v) {
        String data = "未获取";
        if (v instanceof TextView) {
            data = String.valueOf(v.getId());
        }
        Toast.makeText(DragImageGroupActivity.this, data, Toast.LENGTH_SHORT).show();
    }

}
