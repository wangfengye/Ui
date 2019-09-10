package com.maple.common.nestedpiechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: hexiao
 * @Date: 2019/9/6 13:05.
 * @Description:
 */
public class NestedPieChart extends View {
    public static final String TAG = "NestedPieChart";
    private List<NestPie> innerList = new ArrayList<>();
    private List<NestPie> outerList = new ArrayList<>();
    private Paint paintText;
    private Paint paintCircle;
    private int width, height;
    private RectF innerRectF, outerRectF;
    private float centerX, centerY;
    private Path pathLine;


    public void setData(List<NestPie> innerList, List<NestPie> outerList) {
        this.innerList = innerList;
        this.outerList = outerList;
        invalidate();
    }


    public NestedPieChart(Context context) {
        this(context, null);
    }

    public NestedPieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedPieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setTextSize(30);
        paintText.setStyle(Paint.Style.STROKE);
        paintText.setTextAlign(Paint.Align.CENTER);

        innerRectF = new RectF();
        outerRectF = new RectF();

        paintCircle = new Paint();
        paintCircle.setAntiAlias(true);
        paintCircle.setStyle(Paint.Style.FILL);

        pathLine = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        centerX = (float) width / 2;
        centerY = (float) height / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画布移到中心点
        canvas.translate(centerX, centerY);
        // 最小的半径
        float minRadius = (float) Math.min(width, height) / 2;
        // 内圆半径
        float innerRadius = minRadius - 150;
        // 外圆半径
        float outerRadius = minRadius - 50;
        innerRectF.set(-innerRadius, -innerRadius, innerRadius, innerRadius);
        outerRectF.set(-outerRadius, -outerRadius, outerRadius, outerRadius);

        drawOuterCircle(canvas, outerRadius);

        drawInnerCycle(canvas, innerRadius);

        // openAnimate(true);
    }


    private void drawOuterCircle(Canvas canvas, float outerRadius) {
        int totalNumber = 0;
        float currentStartAngle = -90;
        if (!outerList.isEmpty()) {
            // 求总数
            for (NestPie nestPie : outerList) {
                int eachNumber = nestPie.getNumber();
                totalNumber += eachNumber;
            }
            // 设置百分比，角度
            for (NestPie nestPie : outerList) {
                int number = nestPie.getNumber();
               /* float percentage = ((float) number) / ((float) totalNumber);
                float angle = percentage * 360;*/
               // 先乘再除减少精度损失.
               float angle = ((float) number)*360 / ((float) totalNumber);
                paintCircle.setColor(getResources().getColor(nestPie.getColor()));
                // 绘制扇形
                Log.i(TAG, "drawOuterCircle: "+currentStartAngle +"---"+angle);
                canvas.drawArc(outerRectF, currentStartAngle, angle, true, paintCircle);
                float textAngle = currentStartAngle + angle / 2;
                paintCircle.setColor(getResources().getColor(nestPie.getLineColor()));
                float x = (float) (outerRadius * 4 / 5 * Math.cos(textAngle * Math.PI / 180));
                float y = (float) (outerRadius * 4 / 5 * Math.sin(textAngle * Math.PI / 180));
                canvas.drawCircle(x, y, 10, paintCircle);
                // 画线和信息
                drawLine(x, y, canvas, nestPie);
                // 改变起始角度
                currentStartAngle += angle;
            }
        }
    }

    private void drawLine(float x, float y, Canvas canvas, NestPie nestPie) {
        Log.i("***********", "中心点（" + outerRectF.centerX() + "," + outerRectF.centerY() + ")" + "--坐标点（" + x + "," + y + ")");
        String title= nestPie.getTitle();
        String number=String.valueOf(nestPie.getNumber());
        outerRectF.centerX();
        pathLine.reset();
        pathLine.moveTo(x, y);
        if (x < 0 && y < 0) {
            float currentY = y - 50;
            float lastX = x - 150;
            pathLine.lineTo(x - 50, currentY);
            pathLine.lineTo(lastX, currentY);
            paintText.setColor(getResources().getColor(nestPie.getNumberColor()));
            canvas.drawText(number, lastX - 50, currentY, paintText);
            paintText.setColor(getResources().getColor(nestPie.getTextColor()));
            canvas.drawText(title, lastX - 50, currentY + 50, paintText);
        } else if (x < 0 && y > 0) {
            float currentY = y + 50;
            float lastX = x - 150;
            pathLine.lineTo(x - 50, currentY);
            pathLine.lineTo(lastX, currentY);
            paintText.setColor(getResources().getColor(nestPie.getNumberColor()));
            canvas.drawText(number, lastX - 50, currentY, paintText);
            paintText.setColor(getResources().getColor(nestPie.getTextColor()));
            canvas.drawText(title, lastX - 50, currentY + 50, paintText);
        } else if (x > 0 && y < 0) {
            float currentY = y - 50;
            float lastX = x + 150;
            pathLine.lineTo(x + 50, currentY);
            pathLine.lineTo(lastX, currentY);
            paintText.setColor(getResources().getColor(nestPie.getNumberColor()));
            canvas.drawText(number, lastX + 50, currentY, paintText);
            paintText.setColor(getResources().getColor(nestPie.getTextColor()));
            canvas.drawText(title, lastX + 50, currentY + 50, paintText);
        } else if (x > 0 && y > 0) {
            float currentY = y + 50;
            float lastX = x + 150;
            pathLine.lineTo(x + 50, currentY);
            pathLine.lineTo(lastX, currentY);
            paintText.setColor(getResources().getColor(nestPie.getNumberColor()));
            canvas.drawText(number, lastX + 50, currentY, paintText);
            paintText.setColor(getResources().getColor(nestPie.getTextColor()));
            canvas.drawText(title, lastX + 50, currentY + 50, paintText);
        }
        paintText.setColor(getResources().getColor(nestPie.getLineColor()));
        canvas.drawPath(pathLine, paintText);
    }

    private void drawInnerCycle(Canvas canvas, float innerRadius) {
        int totalNumber = 0;
        float currentStartAngle = -90;
        if (!innerList.isEmpty()) {
            // 求总数
            for (NestPie nestPie : innerList) {
                int eachNumber = nestPie.getNumber();
                totalNumber += eachNumber;
            }
            // 设置百分比，角度
            for (NestPie nestPie : innerList) {
                int number = nestPie.getNumber();
                float percentage = ((float) number) / ((float) totalNumber);
                float angle = percentage * 360;
                paintCircle.setColor(getResources().getColor(nestPie.getColor()));
                // 绘制扇形
                canvas.drawArc(innerRectF, currentStartAngle, angle, true, paintCircle);
                // 绘制扇形上文字 计算文字位置角度
                float textAngle = currentStartAngle + angle / 2;
                paintText.setColor(Color.WHITE);
                float x = (float) (innerRadius / 2 * Math.cos(textAngle * Math.PI / 180));
                float y = (float) (innerRadius / 2 * Math.sin(textAngle * Math.PI / 180));
                canvas.drawText(nestPie.getTitle(), x, y, paintText);
                // 改变起始角度
                currentStartAngle += angle;
            }
        }
    }



}
