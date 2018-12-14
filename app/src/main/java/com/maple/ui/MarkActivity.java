package com.maple.ui;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import com.maple.wangfeng.markview.MarkView;

public class MarkActivity extends AppCompatActivity {
    public static final String TITLE = "圆角图标";
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark);
        final MarkView markView = findViewById(R.id.mark);
        final SeekBar bar1 = findViewById(R.id.bar);
        getMetaData(getApplicationContext(),"w");
        bar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.i(TITLE, "onProgressChanged: "+ progress);
                if (fromUser){
                    stop();
                    markView.setRoundPer(progress);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                int speed =1;
                while (!Thread.currentThread().isInterrupted()){

                    i+= speed;
                    if (i>=100)speed =-1;
                    if (i<=0)speed =1;
                    final int ic = i;
                    try {
                        Thread.sleep(1000/24);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            markView.setRoundPer(ic);
                            bar1.setProgress(ic);
                        }
                    });
                }
            }
        });

    }

    private void stop() {
        if (mThread!=null)mThread.interrupt();
    }
    private int  getMetaData(Context context,String key){
        int result = 0;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (info.metaData.containsKey(key)){
                result = info.metaData.getInt(key);
            }else {
                throw  new RuntimeException("未設置"+ key);
            }

            Log.i("TAG", "getInfo: "+ result);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }finally {
            return result;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
    }
}
