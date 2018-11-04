package com.maple.common;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import java.lang.reflect.Method;

public class DelEditText extends AppCompatEditText implements View.OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable;
    private boolean mHasFocus;
    private boolean mShowShake = false;

    public DelEditText(Context context) {
        this(context, null);
    }

    public DelEditText(Context context, AttributeSet attrs) {
        //editTextStyle  默認的屬性
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public DelEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * set clear data function
     *
     * @param enable enable
     */
    public void enableClearIcon(boolean enable) {
        if (!isEnabled()) enable = false;
        Drawable right = enable ? mClearDrawable : null;
        // reset around image
        setCompoundDrawables(getCompoundDrawables()[0],
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * 刪除數據時的動畫
     *
     * @param anim 是否開啟
     */
    public void enableAnimal(boolean anim) {
        this.mShowShake = anim;
    }

    /**
     * this text can copy
     *
     * @param enable 是否開啟
     */
    public void setEnable(boolean enable) {
        super.setEnabled(enable);
        if (!enable) enableClearIcon(false);
    }

    private void init() {
        if (isInEditMode()) return;
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            throw new NullPointerException("you must add drawableRight attribute int xml");
        }
        int space = dp2px(getContext(), 18);
        mClearDrawable.setBounds(0, 0, space, space);
        enableClearIcon(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        clearIsClick(event);
        return super.onTouchEvent(event);
    }

    @SuppressWarnings("all")
    private int dp2px(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.mHasFocus = hasFocus;
        if (hasFocus) enableClearIcon(getText().length() > 0);
        else enableClearIcon(false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (mHasFocus) enableClearIcon(s.length() > 0);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * shacke animation
     *
     * @param counts count between 1 second
     * @return anim
     */
    @SuppressWarnings("all")
    private void shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(1000);
        this.setAnimation(translateAnimation);
    }

    /**
     * 禁止Edittext弹出软键盘，光标依然正常显示。
     */
    @SuppressWarnings("all")
    public static void disableShowSoftInput(EditText editText) {
        editText.setTextIsSelectable(false);
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method method;
            try {
                method = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
                e.getStackTrace();
            }

            try {
                method = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
                method.setAccessible(true);
                method.invoke(editText, false);
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    @SuppressWarnings("all")
    private boolean clearIsClick(MotionEvent event) {
        boolean res = false;
        if (isEnabled() && event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                        && event.getX() < (getWidth() - getPaddingRight());
                if (touchable) {
                    this.setText("");
                    res = true;
                    if (mShowShake) shakeAnimation(5);
                }
            }
        }
        return res;
    }
}
