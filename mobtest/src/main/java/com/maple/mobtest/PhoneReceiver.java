package com.maple.mobtest;



import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.Intent;

import android.os.Handler;

import android.util.Log;
import android.view.*;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.yhao.floatwindow.FloatWindow;
import com.yhao.floatwindow.Screen;


/**
 * @author maple on 2018/11/23 14:22.
 * @version v1.0
 * @see 1040441325@qq.com
 */
public class PhoneReceiver extends BroadcastReceiver{
    public static final String TAG = "PhoneReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.i(TAG, "onReceive: " + phoneNumber);

            View view = LayoutInflater.from(context).inflate(R.layout.win_aa,null);
            TextView a = view.findViewById(R.id.tv_a);
            a.setText(phoneNumber);
            FloatWindow
                    .with(MainApp.context)
                    .setView(view)
                    .setWidth(100)                               //设置控件宽高
                    .setHeight(Screen.width,0.2f)
                    .setX(100)                                   //设置控件初始位置
                    .setY(Screen.height,0.3f)
                    .setDesktopShow(true)                        //桌面显示
                   // .setViewStateListener()    //监听悬浮控件状态改变
                 //   .setPermissionListener()  //监听权限申请结果
                    .build();
            Intent intentThree = new Intent(context, RecordingService.class);
            context.startService(intentThree);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Rxbus.send();
                }
            },3000);
        }
    }
}
