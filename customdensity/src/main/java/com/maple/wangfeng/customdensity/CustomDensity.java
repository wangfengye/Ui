package com.maple.wangfeng.customdensity;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Created by fengye on 2018/6/20.
 * email 1040441325@qq.com
 * 自定义屏幕密度,实现适配
 */

public class CustomDensity {

    public static final int WIDTH = 0x1;
    public static final int HEIGHT = 0x2;

    private static final String TAG = CustomDensity.class.getSimpleName();
    // 屏幕宽度
    private static int mWidth = 720;
    private static int mHeight = 1080;
    private static final int DPI = 160;
    // 默认密度
    private static float sDefaultDensity;
    // 默认字体放大密度
    private static float sDefaultScaledDensity;

    /**
     * 初始化默认分辨率 并保存真实分辨率，用于取消适配
     *
     * @param width   设计稿宽度px
     * @param height  设计稿高度px
     * @param context 上下文
     */
    public static void init(int width, int height, Context context) {
        sDefaultDensity = context.getResources().getDisplayMetrics().density;
        sDefaultScaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        mWidth = width;
        mHeight = height;
    }

    /**
     * 取消适配
     *
     * @param context 上下文
     */
    public static void cancelAuto(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        metrics.density = sDefaultDensity;
        metrics.densityDpi = (int) (DPI * sDefaultDensity);
    }

    /**
     * 以默认宽度适配
     *
     * @param context
     */
    public static void autoBaseOnWidth(Context context) {
        auto(WIDTH, mWidth, context);
    }

    /**
     * 以默认高度适配
     *
     * @param context
     */
    public static void autoBaseOnHeight(Context context) {
        auto(WIDTH, mHeight, context);
    }

    /**
     * 适配
     *
     * @param length  设计稿的宽或高
     * @param context
     */
    public static void auto(@AutoMode int direction, float length, Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        DisplayMetrics activityMetrics = context.getResources().getDisplayMetrics();
        auto(direction, length, context, metrics, activityMetrics);
        metrics = getMetricsOnMiui(context.getApplicationContext().getResources());
        activityMetrics = getMetricsOnMiui(context.getResources());
        if (metrics != null && activityMetrics != null) {
            auto(direction, length, context, metrics, activityMetrics);
        }

    }

    private static void auto(@AutoMode int direction, float length, Context context, DisplayMetrics metrics, DisplayMetrics activityMetrics) {
        boolean isWidth = direction == WIDTH;
        int actLength = 0;
        if (isWidth) {
            actLength = context.getResources().getDisplayMetrics().widthPixels;
        } else {
            actLength = context.getResources().getDisplayMetrics().heightPixels
                    - getStatusBarHeight(context);
        }
        // 设置密度
        float density = actLength / length;
        // 设置dpi
        int targetDensityDpi = (int) (DPI * density);
        metrics.density = density;
        metrics.densityDpi = targetDensityDpi;
        activityMetrics.density = density;
        activityMetrics.densityDpi = targetDensityDpi;
        Log.i("TAG", "auto: " + actLength);
    }

    @Deprecated
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

    /**
     * 解决 MIUI 更改框架导致的 MIUI7 + Android5.1.1 上出现的失效问题 (以及极少数基于这部分 MIUI 去掉 ART 然后置入 XPosed 的手机)
     * 来源于: https://github.com/Firedamp/Rudeness/blob/master/rudeness-sdk/src/main/java/com/bulong/rudeness/RudenessScreenHelper.java#L61:5
     *
     * @param resources {@link Resources}
     * @return {@link DisplayMetrics}, 可能为 {@code null}
     */
    private static DisplayMetrics getMetricsOnMiui(Resources resources) {
        if ("MiuiResources".equals(resources.getClass().getSimpleName()) || "XResources".equals(resources.getClass().getSimpleName())) {
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

}
