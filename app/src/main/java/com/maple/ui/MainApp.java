package com.maple.ui;

import android.app.Application;


public class MainApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
       // CustomDensity.init(720,1080,this);
        //registerActivityLifecycleCallbacks(new DensityActivityLifecycleCallbacks());
    }
}
