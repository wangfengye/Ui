package com.maple.ui.sizeAdapter;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.TreeMap;

/**
 * Created by fengye on 2018/6/20.
 * email 1040441325@qq.com
 * 自定义屏幕密度,实现适配
 */

public class CustomDensity {
    private static final String TAG = CustomDensity.class.getSimpleName();
    // 屏幕宽度
    static int mWidth = 720;
    static int mHeight = 1080;
    static final int DPI = 160;
    // 默认密度
    private static float sDefaultDensity;
    // 默认字体放大密度
    private static float sDefaultScaledDensity;
    private static boolean mIsBaseOnWidth = true;

    /**
     * 初始化默认分辨率 并保存真实分辨率，用于取消适配
     *
     * @param width
     * @param height
     */
    public static void init(int width, int height,Context context) {
        sDefaultDensity = context.getResources().getDisplayMetrics().density;
        sDefaultScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        mWidth = width;
        mHeight = height;
    }
    public static void cancelAuto(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        metrics.density = sDefaultDensity;
        metrics.densityDpi = (int) (DPI * sDefaultDensity);
    }
    public static void autoBaseOnWidth(Context context) {
        auto(true, mWidth, context);
    }

    public static void autoBaseOnHeight(Context context) {
        auto(false, mHeight, context);
    }

    public static void auto(boolean isWidth, int length, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int actLength = 0;
        if (isWidth) {
            actLength = context.getResources().getDisplayMetrics().widthPixels;
        } else {
            actLength = context.getResources().getDisplayMetrics().heightPixels
                    + getNavigationBarHeight(context);
        }
        // 设置密度
        float density = actLength / length;
        // 设置dpi
        int targetDensityDpi = (int) (DPI * density);
        metrics.density = density;
        metrics.densityDpi = targetDensityDpi;
        Log.i("TAG", "auto: " + actLength);

    }

    public static void setCustomDensity(Activity activity, final Application application) {
        if (sDefaultDensity == 0) {
            sDefaultDensity = activity.getResources().getDisplayMetrics().density;
            sDefaultScaledDensity = activity.getResources().getDisplayMetrics().scaledDensity;
            // 监听系统配置改变,当字体大小调整时修改字体大小
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration configuration) {
                    if (configuration != null && configuration.fontScale > 0) {
                        sDefaultScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        int stand = 0;
        // 橫屏切換時保證獲取整個屏幕的寬度
      /*  if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 横屏
            stand = activity.getResources().getDisplayMetrics().widthPixels
                    + getNavigationBarHeight(activity)
                    + getStatusBarHeight(activity);
        } else if (activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            // 竖屏
            stand = activity.getResources().getDisplayMetrics().widthPixels;
        }*/
        stand = activity.getResources().getDisplayMetrics().widthPixels;
        setCustomDensity(activity.getResources().getDisplayMetrics(), stand);

    }

    private static void setCustomDensity(DisplayMetrics metrics, int stand) {
        // 设置密度
        float density = stand / mWidth;
        // 设置dpi
        int targetDensityDpi = (int) (DPI * density);
        metrics.density = density;
        metrics.scaledDensity = density * (sDefaultScaledDensity / sDefaultDensity);
        metrics.densityDpi = targetDensityDpi;
    }

    public static int getNavigationBarHeight(Context activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


}
