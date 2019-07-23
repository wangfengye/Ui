package com.maple.common.tree;

import android.graphics.*;
import android.util.Log;

import java.util.LinkedList;

/**
 * Created by maple on 2019/7/23 09:52
 * todo:相较于自己计算,系统的PtahMeasure,按路径百分比取点,点分布,前密后松,
 */
public class Branch {
    public static final int COLOR = 0xff775533;
    public static final float SCALE_RADIUS = .99f;//每次绘制树干变细比例
    public  String TAG = this.hashCode()+"";
    private Point[] cp = new Point[3];
    private float radius;
    private float maxLength;
    private int currentLength;
    private float part;//等分份数.
    private float growX, growY;
    LinkedList<Branch> childList;

    public Branch(int[] data) {
        cp[0] = new Point(data[2], data[3]);
        cp[1] = new Point(data[4], data[5]);
        cp[2] = new Point(data[6], data[7]);
        radius = data[8];
        maxLength = data[9]*2;//*2绘制更细腻,
        part = 1f / maxLength;
    }

    public boolean grow(Canvas canvas, Paint paint, float scaleFactory) {
        if (currentLength < maxLength) {
            //计算绘制位置
           bezierByMath(currentLength * part);
            //bezierByAndroid(currentLength * part);
            //绘制
            draw(canvas, paint, scaleFactory);
            radius *= SCALE_RADIUS;
            currentLength++;
            return true;
        } else {
            return false;
        }
    }

    private void draw(Canvas canvas, Paint paint, float scaleFactory) {
        paint.setColor(COLOR);
        canvas.save();
        canvas.scale(scaleFactory, scaleFactory);
        canvas.translate(growX+canvas.getWidth()/2/scaleFactory-200, growY+canvas.getHeight()/2/scaleFactory-250);
        canvas.drawCircle(0, 0, radius, paint);
        canvas.restore();
    }

    /**
     * 数学方式计算贝塞尔曲线
     *
     * @param t 百分比
     */
    private void bezierByMath(float t) {
        float c0 = (1 - t) * (1 - t);
        float c1 = 2 * t * (1 - t);
        float c2 = t * t;

        growX = c0 * cp[0].x + c1 * cp[1].x + c2 * cp[2].x;
        growY = c0 * cp[0].y + c1 * cp[1].y + c2 * cp[2].y;
        Log.i(TAG, "bezierByMath: " + growX + "---" + growY);
    }

    /**
     * 利用androidApi
     *
     * @param t 白分比
     */
    private void bezierByAndroid(float t) {
        Path path = new Path();
        path.moveTo(cp[0].x, cp[0].y);
        path.quadTo(cp[1].x, cp[1].y, cp[2].x, cp[2].y);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        float tmp[] = new float[2];
        pathMeasure.getPosTan(t * pathMeasure.getLength(), tmp, null);

        growX = tmp[0];
        growY = tmp[1];
        Log.i(TAG, "bezierByAndroid: " + growX + "---" + growY);
    }

    public void addChild(Branch branch) {
        if (childList == null) childList = new LinkedList<>();
        childList.add(branch);
    }

    // id,parentId,bezierControlPoints(6) maxRadius, length,
    //图形面积400*500
    public static final int[][] DEFAULT_TREE = new int[][]{
            {0, -1, 217, 490, 252, 60, 182, 10, 30, 100},
            {1, 0, 222, 310, 137, 227, 22, 210, 13, 100},
            {2, 1, 132, 245, 116, 240, 76, 205, 2, 40},
            {3, 0, 231, 255, 282, 166, 362, 155, 12, 100},
            {4, 3, 260, 210, 330, 219, 343, 236, 3, 80},
            {5, 0, 217, 91, 219, 58, 216, 27, 3, 40},
            {6, 0, 228, 207, 95, 57, 10, 54, 9, 80},
            {7, 6, 109, 96, 65, 63, 53, 15, 2, 40},
            {8, 6, 180, 155, 117, 125, 77, 140, 4, 60},
            {9, 0, 228, 167, 290, 62, 360, 31, 6, 100}
    };

    public static Branch getDefault() {
        Branch root = null;
        Branch[] data = new Branch[DEFAULT_TREE.length];
        for (int i = 0; i < DEFAULT_TREE.length; i++) {
            data[i] = new Branch(DEFAULT_TREE[i]);
            int parentId = DEFAULT_TREE[i][1];
            if (parentId != -1) {//要求,子节点必须在父节点之后
                data[parentId].addChild(data[i]);
            } else {
                root = data[i];
            }
        }
        return root;
    }
}
