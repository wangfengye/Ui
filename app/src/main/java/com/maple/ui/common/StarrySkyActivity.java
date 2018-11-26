package com.maple.ui.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.maple.ui.R;
import com.moxun.tagcloudlib.view.TagCloudView;

import java.util.ArrayList;
import java.util.List;

public class StarrySkyActivity extends AppCompatActivity {
    public static final String TITLE = "常用自定义控件:星空";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starry_sky);
        TagCloudView tcvMain = findViewById(R.id.tcv_main);
        List<String> strs = new ArrayList<>();
        strs.add("a");
        strs.add("b");
        strs.add("c");
        strs.add("d");
        for (int i = 0; i < 20; i++) {
            strs.add("asd"+i);
        }
        SkyTagAdapter adapter = new SkyTagAdapter(strs);
        tcvMain.setAdapter(adapter);
    }
}
