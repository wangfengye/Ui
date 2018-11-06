package com.maple.ui.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.maple.ui.R;

public class DelEditTextActivity extends AppCompatActivity {
    public static final String TITLE = "常用自定义控件:可清空的EditText";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_del_edit_text);
    }
}
