package com.maple.common.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import com.maple.common.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by fengye on 2018/4/23.
 * email 1040441325@qq.com
 */

public class CircleImageView extends android.support.v7.widget.AppCompatImageView {
    public static final String TAG = CircleImageView.class.getSimpleName();
    // 四角位置
    public static final int LEFT_TOP = 0;
    public static final int RIGHT_TOP = 1;
    public static final int RIGHT_BOTTOM = 2;
    public static final int LEFT_BOTTOM = 3;

    public static final int TYPE_NORMAL = 0;//有背景,有色图;
    public static final int TYPE_PURE = 1;//有背景,纯色图;默认白色
    public static final int TYPE_NO_COLOR = 2;//有背景,无色图

    private static final int DEFAULT_COLOR_BG = Color.parseColor("#9a9a9a");
    // 为避免出现小数,比例采用倒数
    private static final int RADIUS_ICON = 3;// 小图标半径比例的倒数
    private static final int RADIUS_POINT = 5;//点半径比例的倒数
    private static final int BLANK = 20; //空白距离比例
    private float mBorderWidth = 4.0f;


    @IntDef({TYPE_NORMAL, TYPE_PURE, TYPE_NO_COLOR})
    @Retention(RetentionPolicy.SOURCE)
    @interface ImgType {

    }

    // 图片过滤色;决定通过过滤矩阵后剩余的颜色
    private int mImgColor = Color.parseColor("#ffffff");

    //背景颜色
    private int mBg = DEFAULT_COLOR_BG;
    private int mRadius;
    private int mRadiusIcon;
    private int mRadiusPoint;
    private int mBlank;
    private int mSrcType = 0;// 主图片绘制类型
    private int mWidth;
    private int mHeight;
    private Paint mImgPaint;// 绘制bitmap

    private Paint mBgPaint;//背景色
    private Paint mBlankPaint;//绘制空白
    private Paint mBorderPaint;//边框画笔

    private Paint mCornerPaint;//绘制点;
    private Paint mCornerBgPaint;//绘制空白
    private HashMap<Integer, Corner> mCornerMap = new HashMap<>(4);
    private int mBorderColor = Color.parseColor("#00bcd4");
    private boolean mDrawBorder = false;
    private boolean mDrawTriangle = false;

    //绘制顶部三角
    public void setDrawTriangle(boolean mDrawTriangle) {
        this.mDrawTriangle = mDrawTriangle;
    }

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
        if (ta.hasValue(R.styleable.CircleImageView_drawBorder)) {
            mDrawBorder = ta.getBoolean(R.styleable.CircleImageView_drawBorder, false);
        }
        if (ta.hasValue(R.styleable.CircleImageView_border_color)) {
            mBorderColor = ta.getColor(R.styleable.CircleImageView_border_color, Color.WHITE);
        }
        if (ta.hasValue(R.styleable.CircleImageView_borderStokeWidth)){
            mBorderWidth = ta.getFloat(R.styleable.CircleImageView_borderStokeWidth,4.0f);
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

    public void drawBorder(boolean drawBorder) {
        this.mDrawBorder = drawBorder;
    }

    public void setBg(int color) {
        this.mBg = color;
        postInvalidate();
    }

    public void setBorderColor(int color) {
        this.mBorderColor = color;
        postInvalidate();
    }

    public float getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.mBorderWidth = borderWidth;
        mBorderPaint.setStrokeWidth(mBorderWidth);
        postInvalidate();
    }

    public void setCorner(int location, Corner corner) {
        mCornerMap.put(location, corner);
        postInvalidate();
    }

    /**
     * 设置显示模式
     *
     * @param type  模式:原图, 纯色图
     * @param color 纯色图指定的颜色
     */
    public void setSrcType(@ImgType int type, @ColorInt int color) {
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
        mCornerBgPaint.setAntiAlias(true);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);

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
        mBgPaint.setColor(mBg);
        mBorderPaint.setColor(mBorderColor);
        drawMain(canvas);
        for (Map.Entry<Integer, Corner> entry : mCornerMap.entrySet()) {
            if (entry.getValue() == null) continue;
            switch (entry.getValue().getType()) {
                case Corner.ICON:
                    drawIcon(canvas, entry.getKey());
                    break;
                case Corner.POINT:
                    drawPoint(canvas, entry.getKey());
            }
        }
        if (mDrawTriangle) drawTriangle(canvas);
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
            ColorMatrix cm;
            ColorMatrixColorFilter f;
            switch (mSrcType) {
                case TYPE_NORMAL:
                    mImgPaint.setColorFilter(null);
                    drawCircleWithBg(bitmap, canvas, 2.0f);
                    break;
                case TYPE_PURE:
                    // paint 改为纯色
                    /*int red = (mImgColor & 0xff0000) >> 16;
                    int green = (mImgColor & 0x00ff00) >> 8;
                    int blue = (mImgColor & 0x0000ff);*/
                    int red = Color.red(mImgColor);
                    int green = Color.green(mImgColor);
                    int blue = Color.blue(mImgColor);
                    cm = new ColorMatrix(new float[]{
                            0, 0, 0, 0, red,
                            0, 0, 0, 0, green,
                            0, 0, 0, 0, blue,
                            0, 0, 0, 1, 0,
                    });
                    f = new ColorMatrixColorFilter(cm);
                    mImgPaint.setColorFilter(f);
                    drawCircleWithBg(bitmap, canvas, 1.41f);
                    break;
                case TYPE_NO_COLOR:
                    cm = new ColorMatrix();
                    cm.setSaturation(0);
                    f = new ColorMatrixColorFilter(cm);
                    mImgPaint.setColorFilter(f);
                    drawCircleWithBg(bitmap, canvas, 2.0f);
                    break;
            }
            // recycle后其他图片加载异常
            // bitmap.recycle();
        }
    }

    private void drawSquare(Bitmap bitmap, Canvas canvas) {
        float scale = mRadius * 1.0f / Math.min(bitmap.getHeight(), bitmap.getWidth());


        int left = (int) (mWidth / 2 - scale * bitmap.getWidth() / 2);
        int top = (int) (mHeight / 2 - scale * bitmap.getHeight() / 2);
        int right = (int) (mWidth / 2 + scale * bitmap.getWidth() / 2);
        int bottom = (int) (mHeight / 2 + scale * bitmap.getHeight() / 2);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawBitmap(bitmap, null, rect, mImgPaint);
    }

    /**
     * @param bitmap 主图
     * @param canvas
     * @param per    绘制的百分比 1.41保证图形真好被包裹,
     */
    private void drawCircleWithBg(Bitmap bitmap, Canvas canvas, float per) {
        // 1.41 是开根号2的值,保证图形不超过外层圆
        float scale = mRadius * per / Math.min(bitmap.getHeight(), bitmap.getWidth());

        // 绘制主圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBgPaint);
        int layerId = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius, mBgPaint);
        int left = (int) (mWidth / 2 - scale * bitmap.getWidth() / 2);
        int top = (int) (mHeight / 2 - scale * bitmap.getHeight() / 2);
        int right = (int) (mWidth / 2 + scale * bitmap.getWidth() / 2);
        int bottom = (int) (mHeight / 2 + scale * bitmap.getHeight() / 2);
        Rect rect = new Rect(left, top, right, bottom);
        mImgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, mImgPaint);
        canvas.restoreToCount(layerId);
        mImgPaint.setXfermode(null);
        //绘制边框

        if (mSrcType!= TYPE_NO_COLOR){
            LinearGradient lg = new LinearGradient(mWidth / 2, 0, mWidth / 2, mHeight,
                    Color.parseColor("#00BCD4"), Color.parseColor("#B400CA"),
                    Shader.TileMode.MIRROR);
            mBorderPaint.setShader(lg);
        }
        if (mDrawBorder) canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius-mBorderWidth/2, mBorderPaint);
        mBorderPaint.setShader(null);
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
     * 頂部繪製三角
     *
     * @param canvas
     */
    private void drawTriangle(Canvas canvas) {
        Path path = new Path();
        path.moveTo(mRadius - mRadius / 3, -mRadius / 3);
        path.lineTo(mRadius + mRadius / 3, -mRadius / 3);
        path.lineTo(mRadius, 0);
        path.close();
        canvas.drawPath(path, mBgPaint);
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
