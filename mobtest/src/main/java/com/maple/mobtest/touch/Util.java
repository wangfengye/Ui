package com.maple.mobtest.touch;

import android.view.MotionEvent;

public class Util {
    public static final int ACTION_DOWN             = 0;

    public static final int ACTION_UP               = 1;

    public static final int ACTION_MOVE             = 2;

    public static final int ACTION_CANCEL           = 3;

    public static final int ACTION_OUTSIDE          = 4;

    public static final int ACTION_POINTER_DOWN     = 5;

    public static final int ACTION_POINTER_UP       = 6;

    public static final int ACTION_HOVER_MOVE       = 7;
    public static final int ACTION_SCROLL           = 8;


    public static final int ACTION_HOVER_ENTER      = 9;

    public static final int ACTION_HOVER_EXIT       = 10;


    public static final int ACTION_BUTTON_PRESS   = 11;

    public static final int ACTION_BUTTON_RELEASE  = 12;
    public static String getActionName(int i){
        String act = String.valueOf(i);
        switch (i){
            case MotionEvent.ACTION_DOWN:
                act = "ACTION_DOWN";
                break;
            case MotionEvent.ACTION_MOVE:
                act = "ACTION_MOVE";
                break;
            case MotionEvent.ACTION_UP:
                act = "ACTION_UP";
                break;
            case MotionEvent.ACTION_CANCEL:
                act = "ACTION_CANCEL";
                break;
        }
        return act;
    }
}
