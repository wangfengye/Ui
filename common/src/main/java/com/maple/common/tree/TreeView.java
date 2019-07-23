package com.maple.common.tree;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by maple on 2019/7/23 11:02
 */
public class TreeView extends View {
    private Paint paint;
    boolean isEnd;
    private SnapShot snapShot;
    private LinkedList<Branch> growingBanches;
    private float scale =1f;

    public TreeView(Context context) {
        this(context, null);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        growingBanches = new LinkedList<>();
        growingBanches.add(Branch.getDefault());


    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        snapShot = new SnapShot(Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888));
    }

    boolean first=true;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (first){//绘制整体背景用于,调试树在整个布局中的位置
            snapShot.canvas.save();
            snapShot.canvas.scale(scale, scale);
           snapShot.canvas.translate((getWidth()/2/scale-200), (getHeight()/2/scale-250));
            paint.setColor(0x7a00ff00);
            snapShot.canvas.drawRect(new Rect(0, 0, 400, 500), paint);
            snapShot.canvas.restore();first=false;
        }

        drawBranches();
        canvas.drawBitmap(snapShot.bitmap, 0, 0, paint);
        if (!isEnd) invalidate();
    }

    private void drawBranches() {

        if (!growingBanches.isEmpty()) {
            LinkedList<Branch> tmp = null;
            snapShot.canvas.save();
            Iterator<Branch> it = growingBanches.iterator();
            while (it.hasNext()) {
                Branch branch = it.next();
                if (!branch.grow(snapShot.canvas, paint, scale)) {
                    //绘制完成
                    it.remove();
                    if (branch.childList != null) {
                        if (tmp == null) {
                            tmp = branch.childList;
                        } else {
                            tmp.addAll(branch.childList);
                        }
                    }
                }
            }
            snapShot.canvas.restore();
            if (tmp != null) {
                growingBanches.addAll(tmp);
            }
            if (growingBanches.isEmpty()) {
                //全部完成
                isEnd = true;
            }
        }
    }
}
