package com.maple.common.Paint;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.UpdateAppearance;

/**
 * @author maple on 2019/1/29 11:48.
 * @version v1.0
 * @see 1040441325@qq.com
 * 文字横向渐变色
 */
public class HorizonColorfulSpan extends CharacterStyle implements UpdateAppearance {
    private int startColor;
    private int endColor;
    private String text;
    private int index;//字符起始点
    private int end;// 字符数量

    /**
     * 修饰渐变文字
     *
     * @param text        文字内容
     * @param centerColor 中心颜色
     * @param edgeColor   边缘颜色
     * @return spannable
     */
    public static SpannableStringBuilder getColorFulWithCenterEdge(String text, @ColorInt int centerColor, @ColorInt int edgeColor) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
        HorizonColorfulSpan span1 = new HorizonColorfulSpan(edgeColor, centerColor, text, 0, text.length() / 2);
        HorizonColorfulSpan span2 = new HorizonColorfulSpan(centerColor, edgeColor, text, text.length() / 2, text.length());
        stringBuilder.setSpan(span1, 0, text.length() / 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(span2, text.length() / 2, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return stringBuilder;
    }

    /**
     * 修饰渐变文字
     *
     * @param text        文字内容
     * @param centerColor 中心颜色
     * @param edgeColor   边缘颜色
     * @param   centerCount  中心不变色文字的数量
     * @return spannable
     */
    public static SpannableStringBuilder getColorFulWithCenterEdge(String text, @ColorInt int centerColor, @ColorInt int edgeColor, int centerCount) {
        if (centerCount >= text.length() / 2) {
            throw new RuntimeException("HorizonColorfulStyle:getColorFulWithCenterEdge 设置的中心文字数量超上限:" + centerCount);
        }
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text);
        HorizonColorfulSpan span1 = new HorizonColorfulSpan(edgeColor, centerColor, text, 0, text.length() / 2 - centerCount);
        HorizonColorfulSpan span2 = new HorizonColorfulSpan(centerColor, edgeColor, text, text.length() / 2 + centerCount, text.length());
        ForegroundColorSpan spanCenter = new ForegroundColorSpan(Color.RED);
        stringBuilder.setSpan(span1, 0, text.length() / 2 - centerCount, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(spanCenter, text.length() / 2 - centerCount, text.length() / 2 + centerCount, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        stringBuilder.setSpan(span2, text.length() / 2 + centerCount, text.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return stringBuilder;
    }

    public HorizonColorfulSpan(@ColorInt int startColor, @ColorInt int endColor, String text) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.text = text;
        this.index = 0;
        this.end = text.length();

    }

    /**
     * @param startColor 起点颜色
     * @param endColor   中点颜色
     * @param text       字符内容
     * @param index      要渲染的字符起始点
     * @param end        要渲染的字符终点(不包含此字符)
     */
    public HorizonColorfulSpan(@ColorInt int startColor, @ColorInt int endColor, String text, int index, int end) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.text = text;
        this.index = index;
        this.end = end;
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        float[] lens1 = new float[text.length()];
        tp.getTextWidths(text, 0, index, lens1);
        int len1 = 0;
        for (float l :
                lens1) {
            len1 += l;
        }
        float[] lens = new float[text.length()];
        tp.getTextWidths(text, index, end, lens);
        int len2 = 0;
        for (float l :
                lens) {
            len2 += l;
        }
        LinearGradient lg = new LinearGradient(len1, 0, len1 + len2, 0,
                startColor, endColor, Shader.TileMode.CLAMP);

        tp.setShader(lg);
    }
}