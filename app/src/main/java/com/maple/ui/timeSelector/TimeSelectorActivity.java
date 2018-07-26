package com.maple.ui.timeSelector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.maple.ui.R;

public class TimeSelectorActivity extends AppCompatActivity {
    public static final String TITLE = "时间选择";
    TimeRangeSelector mSelector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_selector);


        Button button = findViewById(R.id.cl);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelect();
            }
        });
    }

    public void showSelect() {
        if (mSelector == null) {
            final View rootview = LayoutInflater.from(TimeSelectorActivity.this)
                    .inflate(R.layout.activity_time_selector, null);
            mSelector = new TimeRangeSelector.Builder(rootview)
                    .setTitle("TIME")
                    .setPositiveListener(new TimeRangeSelector.OnClickListener() {
                        @Override
                        public void onClick(PopupWindow popupWindow, int sHour, int sMinute, int eHour, int eMinute) {
                            String timeRange = String.format("%02d:%02d -- %02d:%02d", sHour, sMinute, eHour, eMinute);
                            Toast.makeText(TimeSelectorActivity.this, timeRange, Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeListener(new TimeRangeSelector.OnClickListener() {
                        @Override
                        public void onClick(PopupWindow popupWindow, int sHour, int sMinute, int eHour, int eMinute) {
                            popupWindow.dismiss();
                        }
                    }).build();
        }
        mSelector.show();
    }
}
