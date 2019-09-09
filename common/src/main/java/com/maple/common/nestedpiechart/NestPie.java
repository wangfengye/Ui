package com.maple.common.nestedpiechart;


import com.maple.common.R;

/**
 * @author: hexiao
 * @Date: 2019/9/6 13:11.
 * @Description:
 */
public class NestPie {
    /**
     * 标题
     */
    private String title;
    /**
     * 数量
     */
    private int number;
    /**
     * 对应的颜色
     */
    private int color=R.color.color1;
    /**
     * 总数量
     */
    private int totalNumber;
    /**
     * 占用百分比
     */
    private float percentage;
    /**
     * 角度
     */
    private float angle;
    /**
     * 文字颜色
     */
    private int textColor= R.color.color9;
    /**
     * 数字颜色
     */
    private int numberColor=R.color.color10;
    /**
     * 线的颜色
     */
    private int lineColor=R.color.color8;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getNumberColor() {
        return numberColor;
    }

    public void setNumberColor(int numberColor) {
        this.numberColor = numberColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }
}
