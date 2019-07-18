package com.maple.common.water;

import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.maple.common.R;


public class WaterView extends FrameLayout {
    private TextView textView;
    private PointF initPoint;//文本初始坐标
    private float mRadius = 50;
    private Paint paint;
    private float range = 500;

    public WaterView(@NonNull Context context) {
        super(context);
        init();
    }

    public WaterView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaterView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        textView = new TextView(getContext());
        textView.setPadding(20, 20, 20, 20);
        textView.setText("66");
        textView.setTextColor(Color.WHITE);
        textView.setBackgroundResource(R.drawable.round_red);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        this.addView(textView);
        initPoint = new PointF(500, 500);
        movePoint = new PointF(500, 500);
        paint = new Paint();
        paint.setColor(0xFFFF0404);
        paint.setStyle(Paint.Style.FILL);
    }

    //绘制本身及内部
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    //绘制内部
    @Override
    protected void dispatchDraw(Canvas canvas) {
        //保存canvas
        canvas.save();
        if (isClicked) {
            if (!out) {
                float initRadius = (float) (mRadius - distance() / 30);
                canvas.drawCircle(initPoint.x, initPoint.y, initRadius, paint);
                canvas.drawCircle(movePoint.x, movePoint.y, mRadius, paint);
                drawPath(canvas, initRadius);
            }

            textView.setX(movePoint.x - textView.getWidth() / 2);
            textView.setY(movePoint.y - textView.getHeight() / 2);
        } else {
            textView.setX(initPoint.x - textView.getWidth() / 2);
            textView.setY(initPoint.x - textView.getHeight() / 2);
        }
        canvas.restore();
        super.dispatchDraw(canvas);
    }

    private Path path = new Path();

    private void drawPath(Canvas canvas, float initRadius) {
        float disX = movePoint.x - initPoint.x;
        float disY = movePoint.y - initPoint.y;
        double atan = Math.atan(disY / disX);
        float offsetX = (float) (mRadius * Math.sin(atan));
        float offsetY = (float) (mRadius * Math.cos(atan));
        float offsetXInit = (float) (initRadius * Math.sin(atan));
        float offsetYInit = (float) (initRadius * Math.cos(atan));
        float aX = initPoint.x + offsetXInit;
        float aY = initPoint.y - offsetYInit;
        float bX = movePoint.x + offsetX;
        float bY = movePoint.y - offsetY;
        float cX = movePoint.x - offsetX;
        float cY = movePoint.y + offsetY;
        float dX = initPoint.x - offsetXInit;
        float dY = initPoint.y + offsetYInit;
        float cubicX = (initPoint.x + movePoint.x) / 2;
        float cubicY = (initPoint.y + movePoint.y) / 2;
        path.reset();
        path.moveTo(aX, aY);
        path.quadTo(cubicX, cubicY, bX, bY);
        path.lineTo(cX, cY);
        path.quadTo(cubicX, cubicY, dX, dY);
        path.lineTo(aX, aY);
        canvas.drawPath(path, paint);
    }

    private boolean isClicked;
    private boolean out;//超出拖拽范围
    private PointF movePoint;

    //rowX 相对于父布局坐标, X绝对坐标
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 是否在可点击区域内
                Rect rect = new Rect();
                int[] location = new int[2];
                textView.getLocationOnScreen(location);//获取控件在屏幕中的坐标(左上角)
                rect.left = location[0];
                rect.top = location[1];
                rect.right = location[0] + textView.getWidth();
                rect.bottom = location[1] + textView.getHeight();
                if (rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    isClicked = true;
                    movePoint.set(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (distance() > range) out = true;
                else out = false;
                movePoint.set(event.getX(), event.getY());
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:

                if (!isClicked) return true;
                isClicked = false;
                if (distance() > range) {
                    this.removeView(textView);
                    destroyAnim(100);

                } else {
                    postInvalidate();
                }

                break;
        }
        return true;
    }

    private double distance() {
        float disX = movePoint.x - initPoint.x;
        float disY = movePoint.y - initPoint.y;
        return Math.sqrt(Math.pow(disX, 2) + Math.pow(disY, 2));
    }

    private void destroyAnim(int width) {
        ImageView image = new ImageView(getContext());
        image.setImageResource(R.drawable.anim_heart);
        LayoutParams params = new LayoutParams(width, width);
        image.setLayoutParams(params);
        image.setX(movePoint.x-width/2);
        image.setY(movePoint.y-width/2);
        this.addView(image);
        ((AnimationDrawable) (image.getDrawable())).start();
        Handler handler = new Handler();
        handler.postDelayed(()->this.removeView(image),1200);
    }
}
