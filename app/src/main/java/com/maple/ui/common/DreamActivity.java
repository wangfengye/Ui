package com.maple.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.maple.common.dreame.DreameLayout;
import com.maple.ui.R;

public class DreamActivity extends AppCompatActivity {
    public static final String TITLE = "蜗牛梦话圈";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dream);
        DreameLayout dreameLayout = findViewById(R.id.dream);
        View view = LayoutInflater.from(this).inflate(R.layout.item_dream,dreameLayout,false);
        TextView tv =view.findViewById(R.id.tv);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(v.getContext()).inflate(R.layout.item_dream,dreameLayout,false);
                dreameLayout.addChild(view);
            }
        });
        tv.setText("first");
        dreameLayout.addChild(view);
    }
}
