package com.maple.ui.circleImage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;

/**
 * Created by fengye on 2018/7/21.
 * email 1040441325@qq.com
 * 角标
 */

public class Corner {
    public static final int ICON = 1;
    public static final int POINT = 2;
    private int mType;
    private int mBg = Color.parseColor("#e9392a");//背景颜色
    private Drawable mIcon;//图标内容
    private int mColor = Color.parseColor("#ffffff");//内容颜色

    public Corner(int type) {
        mType = type;

    }

    public Corner(int type, int color) {
        mType = type;
        mColor = color;
    }

    public static Corner newIcon(Drawable icon,int bg) {
        Corner corner = new Corner(ICON);
        corner.mIcon = icon;
        corner.mBg = bg;
        return corner;
    }
    public static Corner newIcon(Context context,@DrawableRes int icon, @ColorRes int bg){
        Corner corner = new Corner(ICON);
        corner.mIcon = context.getDrawable(icon);
        corner.mBg = ContextCompat.getColor(context,bg);
        return corner;
    }
    public static Corner newIcon(Drawable icon,int bg,int color) {
        Corner corner = new Corner(ICON);
        corner.mBg = bg;
        corner.mColor = color;
        return corner;
    }

    public static Corner newPoint(int color) {
        return new Corner(POINT, color);
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getBg() {
        return mBg;
    }

    public void setBg(int bg) {
        mBg = bg;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        mColor = color;
    }
}
