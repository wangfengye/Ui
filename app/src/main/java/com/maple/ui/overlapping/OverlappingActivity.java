package com.maple.ui.overlapping;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.maple.ui.R;

public class OverlappingActivity extends AppCompatActivity {
    public static final String TITLE = "图形重叠";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overlapping);
        RecyclerView rv = findViewById(R.id.rv);
        rv.setAdapter(new OverlappingAdapter());
        rv.setLayoutManager( new GridLayoutManager(this,4));
    }
}
