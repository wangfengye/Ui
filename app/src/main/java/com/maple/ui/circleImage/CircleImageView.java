package com.maple.ui.circleImage;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.maple.ui.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by fengye on 2018/4/23.
 * email 1040441325@qq.com
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {
    public static final String TAG = CircleImageView.class.getSimpleName();
    private static final int DEFAULT_COLOR_BG = Color.parseColor("#9a9a9a");
    // 为避免出现小数,比例采用倒数
    private static final int RADIUS_ICON = 3;// 小图标半径比例的倒数
    private static final int RADIUS_POINT = 5;//点半径比例的倒数
    private static final int BLANK = 20; //空白距离比例

    // 四角位置
    public static final int LEFT_TOP = 0;
    public static final int RIGHT_TOP = 1;
    public static final int RIGHT_BOTTOM = 2;
    public static final int LEFT_BOTTOM = 3;

    public static final int TYPE_NORMAL = 0;//有背景,有色图;
    public static final int TYPE_PURE = 1;//有背景,纯色图;默认白色
    // 图片过滤色;决定通过过滤矩阵后剩余的颜色
    private int mImgColor = Color.parseColor("#ffffff");
    //背景颜色
    private int mBg = DEFAULT_COLOR_BG;
    private int mRadius;
    private int mRadiusIcon;
    private int mRadiusPoint;
    private int mBlank;
    private Drawable mIcon;//图标
    private boolean mShowOnline;//是否绘制点
    private boolean mOnline;//是否在线
    private int mSrcType = 0;// 主图片绘制类型
    private int mWidth;
    private int mHeight;
    private Paint mImgPaint;// 绘制bitmap

    private Paint mBgPaint;//背景色
    private Paint mBlankPaint;//绘制空白


    private Paint mCornerPaint;//绘制点;
    private Paint mCornerBgPaint;//绘制空白
    private HashMap<Integer, Corner> mCornerMap = new HashMap<>();

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyleAttr, 0);


        if (ta.hasValue(R.styleable.CircleImageView_bg)) {
            mBg = ta.getColor(R.styleable.CircleImageView_bg, 0);
        }
        if (ta.hasValue(R.styleable.CircleImageView_srcType)) {
            mSrcType = ta.getInt(R.styleable.CircleImageView_srcType, 0);
        }
        // 不再使用typedArray,释放资源,以便系统复用;
        ta.recycle();

    }

    // 设置图片
    public void setImage(Drawable drawable) {
        super.setImageDrawable(drawable);
        postInvalidate();
    }

    public void setImage(@DrawableRes int id) {
        setImage(ContextCompat.getDrawable(getContext(), id));
    }


    public void setBg(int color) {
        this.mBg = color;
        postInvalidate();
    }

    public void setCorner(int location, Corner corner) {
        mCornerMap.put(location, corner);
    }

    /**
     * 设置显示模式
     *
     * @param type  模式:原图, 纯色图
     * @param color 纯色图指定的颜色
     */
    public void setSrcType(int type, int color) {
        this.mSrcType = type;
        this.mImgColor = color;
        postInvalidate();
    }

    public void setSrcType(int type) {
        setSrcType(type, Color.parseColor("#ffffff"));
    }

    private void init() {
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mRadius = (mWidth > mHeight ? mHeight / 2 : mWidth / 2) - 2;
        mRadiusIcon = mRadius / RADIUS_ICON;
        mRadiusPoint = mRadius / RADIUS_POINT;
        mBlank = mRadius / BLANK;
        initPaint();
    }

    private void initPaint() {
        // 主图画笔
        mImgPaint = new Paint();
        mImgPaint.setAntiAlias(true);

        mCornerBgPaint = new Paint();
        mImgPaint.setAntiAlias(true);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setColor(mBg);

        mBlankPaint = new Paint();
        mBlankPaint.setAntiAlias(true);
        mBgPaint.setStrokeWidth(2.0f);
        mBlankPaint.setColor(Color.WHITE);
        mBlankPaint.setStyle(Paint.Style.FILL);

        mCornerPaint = new Paint();
        mCornerPaint.setAntiAlias(true);
        mCornerPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawMain(canvas);
        for (Map.Entry<Integer, Corner> entry : mCornerMap.entrySet()) {
            switch (entry.getValue().getType()) {
                case Corner.ICON:
                    drawIcon(canvas, entry.getKey());
                    break;
                case Corner.POINT:
                    drawPoint(canvas, entry.getKey());
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 取短边长度
        int result = MeasureSpec.getSize(widthMeasureSpec) > MeasureSpec.getSize(heightMeasureSpec) ?
                heightMeasureSpec : widthMeasureSpec;
        super.onMeasure(result, result);
        init();
    }

    /**
     * 绘制圆角图,主图
     *
     * @param canvas
     */
    private void drawMain(Canvas canvas) {
        Bitmap bitmap = null;
        Drawable drawable = getDrawable();
        if (drawable != null) {
            bitmap = drawableToBitmap(drawable);
            // 缩放
            switch (mSrcType) {
                case TYPE_NORMAL:
                    drawCircleWithBg(bitmap, canvas);
                    break;
                case TYPE_PURE:
                    // paint 改为纯白色
                    int red = (mImgColor & 0xff0000) >> 16;
                    int green = (mImgColor & 0x00ff00) >> 8;
                    int blue = (mImgColor & 0x0000ff);
                    ColorMatrix cm = new ColorMatrix(new float[]{
                            1, 0, 0, 0, red,
                            0, 1, 0, 0, green,
                            0, 0, 1, 0, blue,
                            0, 0, 0, 1, 0,
                    });
                    ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
                    mImgPaint.setColorFilter(f);
                    drawCircleWithBg(bitmap, canvas);
                    break;
                default:
                    drawSquare(bitmap, canvas);
                    break;
            }
            // recycle后其他图片加载异常
            // bitmap.recycle();
        }
    }

    private void drawSquare(Bitmap bitmap,Canvas canvas) {
        float scale = (mRadius * 2 / 4) * 2.0f / Math.min(bitmap.getHeight(), bitmap.getWidth());


        int left = (int) (mWidth / 2 - scale * bitmap.getWidth() / 2);
        int top = (int) (mHeight / 2 - scale * bitmap.getHeight() / 2);
        int right = (int) (mWidth / 2 + scale * bitmap.getWidth() / 2);
        int bottom = (int) (mHeight / 2 + scale * bitmap.getHeight() / 2);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(bitmap, null, rect, mImgPaint);
    }

    private void drawCircleWithBg(Bitmap bitmap, Canvas canvas) {
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = (mRadius * 2 / 4) * 2.0f / Math.min(bitmap.getHeight(), bitmap.getWidth());

        // 绘制主圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBgPaint);
        int left = (int) (mWidth / 2 - scale * bitmap.getWidth() / 2);
        int top = (int) (mHeight / 2 - scale * bitmap.getHeight() / 2);
        int right = (int) (mWidth / 2 + scale * bitmap.getWidth() / 2);
        int bottom = (int) (mHeight / 2 + scale * bitmap.getHeight() / 2);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(bitmap, null, rect, mImgPaint);
    }

    private void drawFullCircle(Bitmap bitmap, Canvas canvas) {
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = mRadius * 2.0f / Math.min(bitmap.getHeight(), bitmap.getWidth());
        Matrix matrix = new Matrix();
        matrix.setTranslate(mBlank, mBlank);
        matrix.preScale(scale, scale);
        shader.setLocalMatrix(matrix);
        mImgPaint.setShader(shader);
        // 绘制主圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mImgPaint);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;
        if (drawable instanceof BitmapDrawable)
            return ((BitmapDrawable) drawable).getBitmap();
        Bitmap bitmap = null;
        try {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void drawIcon(Canvas canvas, int location) {
        PointF center = getCenter(location, mRadiusIcon);
        Bitmap bitmap = null;
        if (mCornerMap.get(location).getIcon() == null) {
            Log.e(TAG, "drawIcon: " + "角标图片资源异常");
            return;
        }
        bitmap = drawableToBitmap(mCornerMap.get(location).getIcon());
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = (mRadiusIcon - 3 * mBlank) * 2.0f / Math.min(bitmap.getHeight(), bitmap.getWidth());

        int color = mCornerMap.get(location).getColor();
        int red = (color & 0xff0000) >> 16;
        int green = (color & 0x00ff00) >> 8;
        int blue = (color & 0x0000ff);

        ColorMatrix cm = new ColorMatrix(new float[]{
                1, 0, 0, 0, red,
                0, 1, 0, 0, green,
                0, 0, 1, 0, blue,
                0, 0, 0, 1, 0,
        });
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        mCornerBgPaint.setColor(mCornerMap.get(location).getBg());
        canvas.drawCircle(center.x, center.y, mRadiusIcon, mBlankPaint);
        canvas.drawCircle(center.x, center.y, mRadiusIcon - mBlank, mCornerBgPaint);
        mCornerPaint.setColorFilter(f);
        float actulRadius = mRadiusIcon - mBlank - mBlank;
        RectF rect = new RectF(center.x - actulRadius, center.y - actulRadius, center.x + actulRadius,
                center.y + actulRadius);
        canvas.drawBitmap(bitmap, null, rect, mCornerPaint);
        // bitmap.recycle();

    }

    private void drawPoint(Canvas canvas, int location) {
        PointF center = getCenter(location, mRadiusPoint, mBlank);
        mCornerBgPaint.setColor(mCornerMap.get(location).getColor());
        canvas.drawCircle(center.x, center.y, mRadiusPoint, mBlankPaint);
        canvas.drawCircle(center.x, center.y, mRadiusPoint - mBlank, mCornerBgPaint);
    }

    /**
     * 获取角标绘制的圆心
     *
     * @param location 位置
     * @param radius   角标半径
     * @param blank    留白
     * @return
     */
    private PointF getCenter(int location, int radius, float blank) {
        float centerX = 0f;
        float centerY = 0f;
        switch (location) {
            case LEFT_TOP:
                centerX = blank + radius;
                centerY = blank + radius;
                break;
            case RIGHT_TOP:
                centerX = mWidth - blank - radius;
                centerY = blank + radius;

                break;
            case RIGHT_BOTTOM:
                centerX = mWidth - blank - radius;
                centerY = mWidth - blank - radius;
                break;
            case LEFT_BOTTOM:
                centerX = blank + radius;
                centerY = mWidth - blank - radius;
                break;
        }
        return new PointF(centerX, centerY);
    }

    private PointF getCenter(int location, int radius) {
        return getCenter(location, radius, 0);
    }

    @Deprecated
    // 无用方法,保留是为了记录矩阵的bitmap变换
    @SuppressWarnings("all")
    private void drawBitmap(Bitmap bitmap, Canvas canvas) {
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = (mRadius * 2 / 4) * 2.0f / Math.min(bitmap.getHeight(), bitmap.getWidth());
        // 矩阵移动缩放图片
        Matrix matrix = new Matrix();
        matrix.setTranslate(mRadius / 4, mRadius / 4);
        matrix.preScale(scale, scale);
        shader.setLocalMatrix(matrix);
        mImgPaint.setShader(shader);
        // 绘制主圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mImgPaint);
    }
}
