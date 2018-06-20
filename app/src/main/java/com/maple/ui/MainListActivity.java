package com.maple.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.maple.ui.sizeAdapter.SizeAdapterActivity;

import java.util.ArrayList;

public class MainListActivity extends AppCompatActivity {
    public ArrayList<ActivityItem> mItems;
    private ActivityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list);
        RecyclerView rv = findViewById(R.id.list);
        mItems = getItems();
        mAdapter = new ActivityAdapter(mItems);
        mAdapter.setListener(new OnClickListener<ActivityItem>() {
            @Override
            public void onClick(ActivityItem item) {
                startActivity(new Intent(MainListActivity.this, item.activity));
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
    }

    public ArrayList<ActivityItem> getItems() {
        mItems = new ArrayList<>();
        mItems.add(new ActivityItem(SizeAdapterActivity.class));
        return mItems;

    }
}
