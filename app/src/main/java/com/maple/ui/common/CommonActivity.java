package com.maple.ui.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.maple.common.DelEditText;
import com.maple.common.particle.ParticleTextView;
import com.maple.ui.R;

public class CommonActivity extends AppCompatActivity {
    public static final String TITLE = "常用自定义控件:粒子文字特效";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);
        ParticleTextView mView = findViewById(R.id.ptv);
        mView.setText("10");
        mView.setNextString("9");
        mView.setNextString("8");
        EditText text = findViewById(R.id.edit);

        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理事件
                    if (text.getText().toString().isEmpty()){
                        Toast.makeText(CommonActivity.this, "内容不可为空", Toast.LENGTH_SHORT).show();
                    }else {
                        mView.setNextString(text.getText().toString());
                        text.setText("");
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
