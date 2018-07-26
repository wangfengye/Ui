package com.maple.ui.timeSelector;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.maple.ui.R;

/**
 * Created by fengye on 2018/7/26.
 * email 1040441325@qq.com
 * 时间范围选择器;
 */

public class TimeRangeSelector {
    private int mSHour;
    private int mSMinute;
    private int mEHour;
    private int mEMinute;
    private PopupWindow mPopupWindow;
    private Builder mBuild;

    private TimeRangeSelector(Builder build) {
        mBuild = build;
    }

    public void create() {
        final PopupWindow popupWindow = new PopupWindow(mBuild.mView.getContext());
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        View rootView = LayoutInflater.from(mBuild.mView.getContext()).inflate(R.layout.window_time_range, null);
        TextView mTvTitle = rootView.findViewById(R.id.tv_title);
        TextView mTvCancel = rootView.findViewById(R.id.tv_cancel);
        if (mBuild.mNegativeListener != null)
            mTvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBuild.mNegativeListener.onClick(popupWindow, mSHour, mSMinute, mEHour, mEMinute);
                }
            });
        TextView mTvSure = rootView.findViewById(R.id.tv_sure);
        if (mBuild.mPositiveListener != null)
            mTvSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mBuild.mPositiveListener.onClick(popupWindow, mSHour, mSMinute, mEHour, mEMinute);
                }
            });
        TimePicker tpStart = rootView.findViewById(R.id.start);
        tpStart.setIs24HourView(true);
        tpStart.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker picker, int i, int i1) {
                mSHour = i;
                mSMinute = i1;
            }
        });
        mSHour = tpStart.getCurrentHour();
        mSMinute = tpStart.getCurrentMinute();
        TimePicker tpEnd = rootView.findViewById(R.id.end);
        tpEnd.setIs24HourView(true);
        tpEnd.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker picker, int i, int i1) {
                mEHour = i;
                mEMinute = i1;
            }
        });
        mEHour = tpEnd.getCurrentHour();
        mEMinute = tpEnd.getCurrentMinute();
        mTvTitle.setText(mBuild.mTitle);
        popupWindow.setContentView(rootView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        mPopupWindow = popupWindow;
    }

    public void show() {
        if (mPopupWindow == null) create();
        mPopupWindow.showAtLocation(mBuild.mView, Gravity.BOTTOM, 0, 0);
    }

    public interface OnClickListener {
        /**
         * 确认键返回事件
         *
         * @param popupWindow 控件本身
         * @param sHour       开始小时
         * @param sMinute     开始分钟
         * @param eHour       结束小时
         * @param eMinute     结束分钟
         */
        void onClick(PopupWindow popupWindow, int sHour, int sMinute, int eHour, int eMinute);
    }


    public static class Builder{
        String mTitle = "标题";
        OnClickListener mNegativeListener;
        OnClickListener mPositiveListener;
        View mView;

        public Builder(View view) {
            mView = view;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setNegativeListener(OnClickListener listener) {
            mNegativeListener = listener;
            return this;
        }

        public Builder setPositiveListener(OnClickListener listener) {
            mPositiveListener = listener;
            return this;
        }

        public TimeRangeSelector build() {
            return new TimeRangeSelector(this);
        }

    }
}
