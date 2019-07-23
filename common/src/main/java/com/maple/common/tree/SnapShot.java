package com.maple.common.tree;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by maple on 2019/7/23 11:10
 */
public class SnapShot {
    Canvas canvas;
    Bitmap bitmap;
    SnapShot(Bitmap bitmap){
        this.bitmap = bitmap;
        canvas= new Canvas(bitmap);
    }
}
