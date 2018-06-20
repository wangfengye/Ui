package com.maple.ui;

import java.lang.reflect.Field;

/**
 * Created by fengye on 2018/6/20.
 * email 1040441325@qq.com
 */

public class ActivityItem {
    public String title;
    public Class activity;

    public ActivityItem(Class activity) {
        this.activity = activity;
        try {
            Field field = activity.getField("TITLE");
            title = (String) field.get(activity);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
