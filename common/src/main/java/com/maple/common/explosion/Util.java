package com.maple.common.explosion;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.util.Random;

/**
 * 从View获取Bitmap
 */
public class Util {
    public static final Random RANDOM = new Random(System.currentTimeMillis());
    public static final Canvas CANVAS = new Canvas();

    public static Bitmap createBitmapFromView(View view) {
        view.clearFocus();
        Bitmap bitmap = createBitmapSafely(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888, 1);
        if (bitmap != null) {
            synchronized (CANVAS) {
                CANVAS.setBitmap(bitmap);
                view.draw(CANVAS);
                CANVAS.setBitmap(null);
            }
        }
        return bitmap;
    }

    private static Bitmap createBitmapSafely(int width, int height, Bitmap.Config config, int i) {
        try {
            return Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            if (i > 0) {
                System.gc();
                return createBitmapSafely(width, height, config, i - 1);
            }
            return null;
        }
    }
}
