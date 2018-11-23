package com.maple.common.bezier;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author maple on 2018/11/21.
 * @version v1.0
 * @see 1040441325@qq.com
 * desc 网格画布,用于调试贝塞尔曲线;必须通过setPoints()设置相对于坐标轴的点位
 */
public abstract class GridComputingView extends View {

    public static final String TAG = "GridComputingView";
    private Point mCoo;
    private Picture mCooPicture;//坐标系canvas元件
    private Picture mGridPicture;//网格canvas元件
    private Paint mHelpPint;
    protected int mRadius;//半径
    private HashMap<Integer, Point> mPoints;
    private Paint mHelpPintAlpha;

    private static float mTouchRadius = 30f;
    private boolean mEnableHelpPos = true;
    private boolean mEnableHelpPosDesc = true;
    private boolean mInit = false;

    /**
     * 初始化点位 实现该方法使用addPoint()添加点位
     */
    protected abstract void setPoints();

    /**
     * 实现 贝塞尔曲线的绘制
     */
    protected abstract void onGridComputingDraw(Canvas canvas);

    /**
     * 添加点位
     *
     * @param index 标签,使用点位时,根据index获取对应点位
     * @param x     相对于坐标轴的X坐标
     * @param y     相对于坐标轴的Y坐标
     * @return 是否添加成功
     */
    protected boolean addPoint(int index, int x, int y) {
        if (mPoints.containsKey(index)) return false;
        mPoints.put(index, new Point(x + mRadius, y + mRadius));
        return true;
    }

    /**
     * 获取是加上半径 ,变为真实坐标
     *
     * @param index 添加点位时设置的index
     * @return 相对于画布的x, y坐标
     */
    protected Point getPoint(int index) {
        return mPoints.get(index);
    }

    /**
     * 更新点位坐标
     *
     * @param index 标签,必须是已添加的点位
     * @param x     相对于坐标轴的X坐标
     * @param y     相对于坐标轴的Y坐标
     * @return 是否更新成功
     */
    public boolean updatePoint(int index, int x, int y) {
        Point p = mPoints.get(index);
        if (p == null) return false;
        p.x = x + mRadius;
        p.y = y + mRadius;
        postInvalidate();
        return true;
    }

    /**
     * 是否绘制辅助点
     */
    public void enableHelpPos(boolean enable) {
        mEnableHelpPos = enable;
    }

    /**
     * 是否绘制辅助点坐标
     */
    public void enableHelpPosDesc(boolean enable) {
        mEnableHelpPosDesc = enable;
    }

    /**
     * 必须在setPoints()中及以后调用
     *
     * @return 返回画布半径
     */
    protected int getRadius() {
        return mRadius;
    }

    /**
     * 将辅助点相连
     *
     * @param canvas 画布
     * @param poes   需要连接的点位(demo:输入点位 p0,p1,p2:则连接线段p0p1,p1p2)
     */
    protected void drawHelpPosLine(Canvas canvas, int... poes) {
        Point pre = mPoints.get(poes[0]);
        Point cur;
        mHelpPint.setStrokeWidth(2);
        for (int i = 1; i < poes.length; i++) {
            cur = mPoints.get(poes[i]);
            canvas.drawLine(pre.x, pre.y, cur.x, cur.y, mHelpPint);
            pre = cur;
        }
        mHelpPint.setStrokeWidth(8);
    }


    public GridComputingView(Context context) {
        this(context, null);
    }

    public GridComputingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridComputingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPoints = new LinkedHashMap<>(16);
        //初始化辅助
        mHelpPint = getHelpPint(Color.RED);
        mHelpPintAlpha = getHelpPint(Color.parseColor("#77ff0000"));
        mHelpPintAlpha.setStrokeWidth(mTouchRadius);
    }

    public static Paint getHelpPint(int color) {
        //初始化网格画笔
        Paint paint = new Paint();
        paint.setStrokeWidth(8);
        paint.setColor(color);
        paint.setTextSize(16);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mGridPicture.draw(canvas);
        mCooPicture.draw(canvas);
        onGridComputingDraw(canvas);
        if (mEnableHelpPos) drawHelpPos(canvas);
        if (mEnableHelpPosDesc) drawDesc(canvas);
    }

    /**
     * 在View左上角绘制所有点位的坐标
     */
    private void drawDesc(Canvas canvas) {
        mHelpPint.setStrokeWidth(1);
        mHelpPint.setAntiAlias(true);
        StringBuilder builder = new StringBuilder();
        int i = 1;
        for (Map.Entry<Integer, Point> entry : mPoints.entrySet()) {
            builder.append(entry.getKey()).append(" : (")
                    .append(entry.getValue().x - mRadius).append(',').append(entry.getValue().y - mRadius).append(')');
            canvas.drawText(builder.toString(), 20, 32 * i, mHelpPint);
            builder.setLength(0);
            i++;
        }
        mHelpPint.setStrokeWidth(8);
    }

    /**
     * 为了绘制在
     *
     * @param canvas
     */
    private void drawHelpPos(Canvas canvas) {
        for (Map.Entry<Integer, Point> entry : mPoints.entrySet()) {
            Point p = entry.getValue();
            canvas.drawPoint(p.x, p.y, mHelpPint);
            canvas.drawPoint(p.x, p.y, mHelpPintAlpha);

        }
    }

    Point mTouchedPoint;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchedPoint = getTouchedPoint(event.getX(), event.getY());
                Log.i(TAG, "onTouchEvent: " + String.valueOf(mTouchedPoint != null));
                Log.i(TAG, "onTouchEvent: " + event.getX() + "~~~" + event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if (mTouchedPoint != null) {
                    Log.i(TAG, "onTouchEvent: " + mTouchedPoint.x + "~~~" + mTouchedPoint.y);
                    Log.i(TAG, "onTouchEvent: " + event.getX() + "~~~" + event.getY());
                    mTouchedPoint.x = (int) event.getX();
                    mTouchedPoint.y = (int) event.getY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mTouchedPoint = null;
                break;
        }
        return true;
    }

    /**
     * 获取当前点击区的Point
     */
    private Point getTouchedPoint(float x, float y) {
        for (Map.Entry<Integer, Point> entry : mPoints.entrySet()) {
            Point p = entry.getValue();
            if (Math.abs(p.x - x) <= mTouchRadius && Math.abs(p.y - y) <= mTouchRadius) {
                return p;
            }
        }
        return null;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(w, h);
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.UNSPECIFIED);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.UNSPECIFIED);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        // 测量完成,初始化点位,绘制坐标轴等
        if (mInit) {
            mRadius = size / 2;
            if (mCoo == null) mCoo = new Point(mRadius, mRadius);
            else {
                mCoo.x = mRadius;
                mCoo.y = mRadius;
            }
            setPoints();
            mCooPicture = getCoo(mCoo, size, size);
            mGridPicture = getGrid(mCoo, size, size);
            mInit = true;
        }else {//重新测绘
            int lastRadius = mRadius;
            mRadius = size / 2;
            if (mCoo == null) mCoo = new Point(mRadius, mRadius);
            else {
                mCoo.x = mRadius;
                mCoo.y = mRadius;
            }
            for (Map.Entry<Integer, Point> entry:mPoints.entrySet()){
                entry.getValue().x=entry.getValue().x-lastRadius+mRadius;
                entry.getValue().y=entry.getValue().y-lastRadius+mRadius;
            }
            mCooPicture = getCoo(mCoo, size, size);
            mGridPicture = getGrid(mCoo, size, size);
            mInit = true;
        }
    }

    /**
     * 绘制坐标轴
     *
     * @param coo 中心点
     * @param x   x轴
     * @param y   轴
     */
    private Picture getCoo(Point coo, int x, int y) {
        Picture picture = new Picture();
        Canvas recording = picture.beginRecording(x, y);
        //初始化网格画笔
        Paint paint = new Paint();
        paint.setStrokeWidth(4);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(null);

        //绘制直线
        recording.drawPath(cooPath(coo, new Point(x, y)), paint);
        //右箭头
        recording.drawLine(x, coo.y, x - 40, coo.y - 20, paint);
        recording.drawLine(x, coo.y, x - 40, coo.y + 20, paint);
        //下箭头
        recording.drawLine(coo.x, y, coo.x - 20, y - 40, paint);
        recording.drawLine(coo.x, y, coo.x + 20, y - 40, paint);
        //为坐标系绘制文字
        drawText4Coo(recording, coo, new Point(x, y), paint);
        return picture;
    }

    /**
     * 坐标系路径
     *
     * @param coo     坐标点
     * @param winSize 屏幕尺寸
     * @return 坐标系路径
     */
    public static Path cooPath(Point coo, Point winSize) {
        Path path = new Path();
        //x正半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(winSize.x, coo.y);
        //x负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x - winSize.x, coo.y);
        //y负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x, coo.y - winSize.y);
        //y负半轴线
        path.moveTo(coo.x, coo.y);
        path.lineTo(coo.x, winSize.y);
        return path;
    }

    /**
     * 为坐标系绘制文字
     *
     * @param canvas  画布
     * @param coo     坐标系原点
     * @param winSize 屏幕尺寸
     * @param paint   画笔
     */
    private static void drawText4Coo(Canvas canvas, Point coo, Point winSize, Paint paint) {
        //绘制文字
        paint.setTextSize(50);
        canvas.drawText("x", winSize.x - 60, coo.y - 40, paint);
        canvas.drawText("y", coo.x - 40, winSize.y - 60, paint);
        paint.setTextSize(25);
        //X正轴文字
        for (int i = 1; i < (winSize.x - coo.x) / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(100 * i + "", coo.x - 20 + 100 * i, coo.y + 40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x + 100 * i, coo.y, coo.x + 100 * i, coo.y - 10, paint);
        }

        //X负轴文字
        for (int i = 1; i < coo.x / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-100 * i + "", coo.x - 20 - 100 * i, coo.y + 40, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x - 100 * i, coo.y, coo.x - 100 * i, coo.y - 10, paint);
        }

        //y正轴文字
        for (int i = 1; i < (winSize.y - coo.y) / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(100 * i + "", coo.x + 20, coo.y + 10 + 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y + 100 * i, coo.x + 10, coo.y + 100 * i, paint);
        }

        //y负轴文字
        for (int i = 1; i < coo.y / 50; i++) {
            paint.setStrokeWidth(2);
            canvas.drawText(-100 * i + "", coo.x + 20, coo.y + 10 - 100 * i, paint);
            paint.setStrokeWidth(5);
            canvas.drawLine(coo.x, coo.y - 100 * i, coo.x + 10, coo.y - 100 * i, paint);
        }
    }

    /**
     * 绘制网格
     */
    private static Picture getGrid(Point coo, int x, int y) {

        Picture picture = new Picture();
        Canvas recording = picture.beginRecording(x, y);
        //初始化网格画笔
        Paint paint = new Paint();
        paint.setStrokeWidth(2);
        paint.setColor(Color.GRAY);
        paint.setTextSize(50);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStyle(Paint.Style.STROKE);
        //设置虚线效果new float[]{可见长度, 不可见长度},偏移值
        paint.setPathEffect(new DashPathEffect(new float[]{10, 5}, 0));
        recording.drawPath(gridPath(coo, 50, x, y), paint);
        return picture;

    }

    /**
     * 绘制网格:注意只有用path才能绘制虚线
     *
     * @param step 小正方形边长
     */
    public static Path gridPath(Point coo, int step, int x, int y) {

        Path path = new Path();

        for (int i = coo.y; i < y; i += step) {
            path.moveTo(0, i);
            path.lineTo(x, i);
        }
        for (int i = coo.y; i > 0; i -= step) {
            path.moveTo(0, i);
            path.lineTo(x, i);
        }

        for (int i = coo.x; i < x; i += step) {
            path.moveTo(i, 0);
            path.lineTo(i, y);
        }
        for (int i = coo.x; i > 0; i -= step) {
            path.moveTo(i, 0);
            path.lineTo(i, y);
        }
        return path;
    }

}
