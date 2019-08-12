package com.maple.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.maple.ui.GithubView.GithubViewActivity;
import com.maple.ui.circleImage.CircleImageActivity;
import com.maple.ui.common.*;
import com.maple.ui.overlapping.OverlappingActivity;
import com.maple.ui.sizeAdapter.SizeAdapterActivity;
import com.maple.ui.timeSelector.TimeSelectorActivity;

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
        mAdapter.setListener(item -> startActivity(new Intent(MainListActivity.this, item.activity)));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapter);
        rv.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        /* NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.bjt)
                .setContentTitle("ssss")
                .setContentText("ss")
                .setProgress(100,33,false);
        manager.notify(1,builder.build());*/
    }

    public ArrayList<ActivityItem> getItems() {
        mItems = new ArrayList<>();
        mItems.add(new ActivityItem(DreamActivity.class));
        mItems.add(new ActivityItem(SizeAdapterActivity.class));
        mItems.add(new ActivityItem(GithubViewActivity.class));
        mItems.add(new ActivityItem(CircleImageActivity.class));
        mItems.add(new ActivityItem(TimeSelectorActivity.class));
        mItems.add(new ActivityItem(OverlappingActivity.class));
        mItems.add(new ActivityItem(MarkActivity.class));
        mItems.add(new ActivityItem(CommonActivity.class));
        mItems.add(new ActivityItem(DelEditTextActivity.class));
        mItems.add(new ActivityItem(StarrySkyActivity.class));
        mItems.add(new ActivityItem(BezierActivity.class));
        mItems.add(new ActivityItem(HandlerWriteActivity.class));
        mItems.add(new ActivityItem(WaterActivity.class));
        mItems.add(new ActivityItem(ExplosionActivity.class));
        mItems.add(new ActivityItem(SpecialActivity.class));
        mItems.add(new ActivityItem(TreeActivity.class));
        mItems.add(new ActivityItem(LossActivity.class));
        mItems.add(new ActivityItem(SlidActivity.class));
        mItems.add(new ActivityItem(WaveActivity.class));
        mItems.add(new ActivityItem(DragImageGroupActivity.class));
        return mItems;
    }
}
