package com.maple.ui.GithubView;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.maple.ui.R;

public class GithubViewActivity extends AppCompatActivity {
    public static final String TITLE = "GitHub贡献图";
    GithubActivityView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_github_view);
        mView = findViewById(R.id.gt);
        Integer[][] data = new Integer[7][];
        for (int i = 0; i < 7; i++) {
            Integer[] column = new Integer[24];
            for (int j = 0; j < 24; j++) {
                column[j] = (int) (Math.random() * 60);
            }
            data[i] = column;
        }

        mView.setData(data);
    }
}
