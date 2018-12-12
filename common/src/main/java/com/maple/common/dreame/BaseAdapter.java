package com.maple.common.dreame;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * @author maple on 2018/12/12 17:32.
 * @version v1.0
 * @see 1040441325@qq.com
 */
public abstract class BaseAdapter<T, C> {
    private C center;
    private ArrayList<T> list;

    public BaseAdapter(C center, ArrayList<T> list) {
        this.center = center;
        this.list = list;
    }
    public void addChild(T t){
        // 添加View;
        list.add(t);
    }
    protected abstract View onCreateCenter(ViewGroup parent);

    protected abstract View onCreateChild(ViewGroup parent);


}
