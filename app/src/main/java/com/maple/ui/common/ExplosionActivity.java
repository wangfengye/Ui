package com.maple.ui.common;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.maple.common.explosion.ExplosionField;
import com.maple.ui.R;

public class ExplosionActivity extends AppCompatActivity {
    public static final String TITLE = "第三方控件:粒子爆炸";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explosion);

        ExplosionField.with(this)
                .listener(view ->Toast.makeText(this, "我要炸了", Toast.LENGTH_SHORT).show())
                .into(findViewById(R.id.imageView));
        ExplosionField.with(this).into(findViewById(R.id.textView));
    }
}
