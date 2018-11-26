package com.maple.mobtest;

import android.app.Application;
import android.content.Context;

/**
 * @author maple on 2018/11/23 15:27.
 * @version v1.0
 * @see 1040441325@qq.com
 */
public class MainApp extends Application {
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
